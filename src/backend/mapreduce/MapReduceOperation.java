/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.mapreduce;

import backend.handle.Category;
import backend.handle.core.LogHandleOperation;
import backend.mapreduce.MapReduceOperation.EVENT_TYPE;
import backend.operation.LogOperation;
import backend.record.LogRecord;
import backend.util.SDEvent;
import backend.util.SwingEventDispatcher;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

/**
 *
 * @author Salm
 */
public class MapReduceOperation<K, V1, V2>
        extends SwingEventDispatcher<EVENT_TYPE, Object>
        implements LogOperation<EVENT_TYPE, Object> {

    private static final ExecutorService EXECS = Executors.newFixedThreadPool(8);
    protected String name = this.getClass().getSimpleName();
    private final Category category;
    private final List<File> files;
    private final MapFunction<K, V1> map;
    private final ReduceFactory<K, V1, V2> reduceF;
    private final Predicate<LogRecord> filter;

    public MapReduceOperation(Category ct, List<File> file,
            MapFunction<K, V1> map, ReduceFactory<K, V1, V2> reduceF,
            Predicate<LogRecord> filter) {
        this.category = ct;
        this.files = file;
        this.map = map;
        this.reduceF = reduceF;
        this.filter = filter;
    }

    public MapReduceOperation(Category ct, List<File> file,
            MapFunction<K, V1> map, ReduceFactory<K, V1, V2> reduceF) {
        this(ct, file, map, reduceF, ConstantPredicate.TRUE);
    }

    @Override
    public void run() {
        // map
        Reducer<K, V1, V2> reducer = reduceF.createReducer();
        final int total = files.size();
        final AtomicInteger i = new AtomicInteger();
        files.forEach((f) -> {
            EXECS.execute(() -> {
                try {
                    Map<K, List<V1>> reduceInput = new HashMap();

                    BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                    List<LogRecord> recs = new LinkedList();
                    while (r.ready()) {
                        try {
                            LogRecord rec = LogRecord.parse(this.category, r.readLine());
                            if (filter.test(rec)) {
                                recs.add(rec);
                            }
                        } catch (Exception ex) {
                            // ingore
                        }
                    }

                    List<Pair<K, V1>> mappedData = this.map.map(recs);
                    mappedData.forEach((m) -> {
                        List<V1> v = reduceInput.get(m.getKey());
                        if (v == null) {
                            v = new LinkedList();
                            reduceInput.put(m.getKey(), v);
                        }

                        v.add(m.getValue());
                    });

                    reducer.accept(reduceInput);

                    this.dispatchEvent(new SDEvent(EVENT_TYPE.TASK_COMPLETED, i.incrementAndGet()));
                    if (i.get() >= total) {
                        this.dispatchEvent(new SDEvent(EVENT_TYPE.DONE, reducer));
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(LogHandleOperation.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(LogHandleOperation.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        });
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int getTotalTasks() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void stop() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static enum EVENT_TYPE {

        TASK_COMPLETED,
        DONE;
    }
}
