/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.expr;

import backend.Config;
import backend.expr.ExprLogOperation.EVENT_TYPE;
import backend.handle.Category;
import backend.operation.LogOperation;
import backend.record.LogRecord;
import backend.util.SDEvent;
import backend.util.SwingEventDispatcher;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.simple.JSONValue;

/**
 *
 * @author Salm
 */
public class ExprLogOperation
        extends SwingEventDispatcher<EVENT_TYPE, Object>
        implements LogOperation<EVENT_TYPE, Object> {

    private static final ExecutorService EXECS = (Config.CONCURRENT_LEVEL <= 1)?
            Executors.newSingleThreadExecutor():Executors.newWorkStealingPool(8);
    
    private final Category category;
    private final List<File> files;
    private final Expression filter, map, reducer;
    private boolean run = false;

    public ExprLogOperation(Category ctgr, List<File> file, Map<?, ?> json) {
        this.files = file;
        
        GroupExpr grExpr = (GroupExpr) ExprParser.parseExpr(GroupExpr.P_ID, json);
        filter = grExpr.get("$filter");
        map = grExpr.get("$map");
        reducer = grExpr.get("$reduce");
        this.category = ctgr;
    }

    @Override
    public void run() {
        // map
        Map<Object, List<AccumExpression>> data = new ConcurrentHashMap<>(256);
        final int total = files.size();
        final AtomicInteger i = new AtomicInteger();
        this.run = true;
        files.forEach((f) -> {
            EXECS.execute(() -> {
                if (!run)
                    return;
                
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                    List<LogRecord> recs = new LinkedList();
                    while (reader.ready()) {
                        LogRecord rec;
                        try {
                            rec = LogRecord.parse(this.category, reader.readLine());
                        }
                        catch (Exception ex)
                        {
                            // ignore
                            continue;
                        }
                        
                        if ((Boolean) filter.eval(rec)) {
                            recs.add(rec);
                        }
                    }
                    
                    Map<Object, List<LogRecord>> mappedData = new HashMap(256);
                    recs.forEach((r) -> {
                        Object dat = this.map.eval(r);
                        if (dat == null)
                            throw new IllegalStateException("Cannot get eval for map " + map);
                        List<LogRecord> l;
                        synchronized (mappedData)
                        {
                            l = mappedData.get(dat);
                            if (l == null)
                            {
                                l = new LinkedList();
                                mappedData.put(dat, l);
                            }
                        }
                        
                        l.add(r);
                    });
                    
                    mappedData.forEach((k, logs) -> {
                        ArrayExprContext arrCtxs = ArrayExprContext.make(logs);
                        List<AccumExpression> accums = data.get(k);
                        if (accums == null)
                        {
                            accums = (List) this.reducer.eval(null);
                            data.put(k, accums);
                        }
                        
                        accums.forEach((accum) -> accum.eval(arrCtxs));
                    });

                    this.dispatchEvent(new SDEvent(EVENT_TYPE.TASK_COMPLETED, i.incrementAndGet()));
                    if (i.get() >= total) {
                        Map<Object, Object> output = new HashMap(data.size());
                        data.forEach((k, v) -> {
                            List<Object> l = new LinkedList();
                            v.forEach((accum) -> l.add(accum.getResult()));
                            output.put(k, l);
                        });
                        this.dispatchEvent(new SDEvent(EVENT_TYPE.DONE, output));
                    }
                } catch (Exception ex) {
                        this.dispatchEvent(new SDEvent(EVENT_TYPE.ERROR, ex));
                }
            });
        });
    }

    @Override
    public int getTotalTasks() {
        return files.size();
    }

    @Override
    public void stop() {
        this.run = false;
    }
    
    public static enum EVENT_TYPE {
        TASK_COMPLETED,
        ERROR,
        DONE;
    }
}
