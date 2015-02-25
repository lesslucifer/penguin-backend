/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.handle.core;

import backend.handle.Category;
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
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Salm
 */
public class LogHandleOperation
    extends SwingEventDispatcher<LogHandleOperation.EVENT_TYPE, LogHandleOutputManager>
    implements Runnable, LogOperation<LogHandleOperation.EVENT_TYPE, LogHandleOutputManager> {
    private static final ExecutorService execs = Executors.newFixedThreadPool(2);
    
    private MODE mode = MODE.IDLE;
    private final Category category;
    private final List<File> logFiles;
    private final LogHandle handler;
    private final int totalTasks;
    private final AtomicInteger completedTasks = new AtomicInteger();

    public LogHandleOperation(Category ct, List<File> logFiles, LogHandle handle) {
        this.category = ct;
        this.logFiles = logFiles;
        this.totalTasks = logFiles.size();
        this.handler = handle;
    }

    @Override
    public void run() {
        if (completedTasks.get() > 0 || this.mode != MODE.IDLE)
            throw new IllegalStateException("Operation not clearly, please use clone to create another");
        
        this.mode = MODE.RUNNING;
        LogHandleOutputManager outMan = handler.createOutputManager();
        for (File f : logFiles) {
            execs.execute(() -> {
                if (mode != MODE.RUNNING)
                    return;

                try {
                    BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                    List<LogRecord> recs = new LinkedList();
                    while (r.ready())
                    {
                        try {
                            LogRecord rec = LogRecord.parse(this.category, r.readLine());
                            recs.add(rec);
                        } catch (Exception ex) {
                            // ingore
                        }
                    }

                    LogHandleOutput output = handler.handle(recs);
                    outMan.accept(output);
                    int tsk = completedTasks.incrementAndGet();
                    this.dispatchEvent(new SDEvent(EVENT_TYPE.TASK_COMPLETED, outMan));
                    if (tsk >= this.totalTasks)
                    {
                        this.mode = MODE.STOPPED;
                        this.dispatchEvent(new SDEvent(EVENT_TYPE.DONE, outMan));
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(LogHandleOperation.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(LogHandleOperation.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }
    
    public LogHandleOperation clone2()
    {
        return new LogHandleOperation(this.category, this.logFiles, handler);
    }
    
//    @Override
    public int getTotalTasks()
    {
        return this.totalTasks;
    }
    
//    @Override
    public int getProgress()
    {
        return this.completedTasks.get();
    }
    
//    @Override
    public void stop()
    {
        if (this.mode != MODE.STOPPED)
        {
            this.mode = MODE.STOPPED;
            this.dispatchEvent(new SDEvent(EVENT_TYPE.STOPPED, null));
        }
    }
    
    public static enum MODE
    {
        IDLE,
        RUNNING,
        STOPPED
    }
    
    public static enum EVENT_TYPE
    {
        STOPPED,
        TASK_COMPLETED,
        DONE
    }
}
