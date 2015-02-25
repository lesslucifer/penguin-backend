/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.handle;

import backend.handle.core.HashMapOutput;
import backend.handle.core.LogHandle;
import backend.handle.core.LogHandleOutput;
import backend.handle.core.LogHandleOutputManager;
import backend.record.LogRecord;
import backend.record.ActionRecord;
import backend.record.UserAction;
import java.util.List;

/**
 *
 * @author Salm
 */
public class UserNhanEventQuayLai implements LogHandle {

    @Override
    public LogHandleOutput handle(List<LogRecord> logs) {
        Output o = new Output();
        logs.forEach((log) -> {
            if (!(log instanceof ActionRecord))
                return;
            
            ActionRecord r = (ActionRecord) log;
            
            if (!(r.getAction() == UserAction.WAY_BACK))
                return;
            
            int nDay = Integer.parseInt(r.getParam(0));
            Data d = new Data();
            d.K = nDay;
            
            o.put(r.getUid(), d);
        });
        
        return o;
    }

    @Override
    public LogHandleOutputManager createOutputManager() {
        return new Output();
    }
    
    private static class Data
    {
        private int K;
    }
    
    private static class Output
        extends HashMapOutput<String, Data>
        implements LogHandleOutputManager
    {
        @Override
        public void accept(LogHandleOutput output) {
            if (!(output instanceof Output))
                return;
            
            Output o = (Output) output;
            this.putAll(o);
        }

        @Override
        public void renderOutput(StringBuilder sb) {
            this.forEach((k, v) -> {
                sb.append(k).append('\t');
                sb.append(v.K).append('\n');
            });
        }

        @Override
        protected Data makeValue() {
            return new Data();
        }
        
    }
}
