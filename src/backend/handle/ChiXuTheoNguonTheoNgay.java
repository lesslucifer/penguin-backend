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
import backend.record.ThuChiRecord;
import backend.record.LogRecord;
import backend.record.UserAction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Salm
 */
public class ChiXuTheoNguonTheoNgay implements LogHandle {

    @Override
    public LogHandleOutput handle(List<LogRecord> logs) {
        Output o = new Output();
        logs.forEach((r) -> {
            if (!(r instanceof ThuChiRecord))
                return;
            
            ThuChiRecord coinRec = (ThuChiRecord) r;
            Data data = o.mkGet(coinRec.getSource());
            data.merge(coinRec.getDay(), coinRec.getInc(),
                    Integer::sum);
        });
        
        return o;
    }

    @Override
    public LogHandleOutputManager createOutputManager() {
        return new Output();
    }

    @Override
    public String getDescription() {
        return "Phân tích lượng thu xu chi theo từng nguồn theo từng ngày";
    }
    
    private static class Data extends HashMap<String, Integer>
    {
        public Data()
        {
            super(16);
        }

        public Data(Map<String, Integer> days) {
            super(days);
        }
        
        public static Data merge(Data d1, Data d2)
        {
            Data d = new Data(d1);
            d2.forEach((k, v) -> {
                d.merge(k, v, Integer::sum);
            });
            
            return d;
        }
    }
    
    private static class Output
        extends HashMapOutput<UserAction, Data> implements LogHandleOutputManager
    {

        @Override
        protected Data makeValue() {
            return new Data();
        }
        
        @Override
        public void accept(LogHandleOutput output) {
            if (!(output instanceof Output))
                return;
            
            Output o = (Output) output;
            o.forEach((k, v) -> {
                this.merge(k, v, Data::merge);
            });
        }

        @Override
        public void renderOutput(StringBuilder sb) {
            this.forEach((k, v) -> {
                v.forEach((d, coin) -> {
                    sb.append(k).append('\t');
                    sb.append(d).append('\t');
                    sb.append(coin).append('\n');
                });
            });
        }
    }
    
}
