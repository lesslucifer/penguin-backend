/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.handle.core;

import backend.record.LogRecord;
import java.util.List;

/**
 *
 * @author Salm
 */
public interface LogHandle {
    LogHandleOutput handle(List<LogRecord> logs);
    LogHandleOutputManager createOutputManager();
    
    default String getDescription()
    {
        return this.getClass().getCanonicalName();
    }
    default String getName()
    {
        return this.getClass().getSimpleName();
    }
    
    default LogHandle cloneHandle()
    {
        try {
            return this.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            return null;
        }
    }
}
