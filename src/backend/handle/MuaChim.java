/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.handle;

import backend.handle.core.LogHandle;
import backend.handle.core.LogHandleOutput;
import backend.handle.core.LogHandleOutputManager;
import backend.record.LogRecord;
import backend.record.ActionRecord;
import backend.record.UserAction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author Salm
 */
public class MuaChim implements LogHandle {

    @Override
    public LogHandleOutput handle(List<LogRecord> logs) {
        Output out = new Output();
        logs.stream().forEach((log) -> {
            if (!(log instanceof ActionRecord))
                return;

            ActionRecord act = (ActionRecord) log;
            if (act.getAction() != UserAction.MUA_CHIM)
                return;
            
            String pKind = act.getParam(0);
            int n = Integer.parseInt(act.getParam(1));
            String payType = act.getParam(2);
            int price = Integer.parseInt(act.getParam(3));

            Data dat = out.get(pKind);
            if (dat == null)
            {
                dat = new Data();
                out.put(pKind, dat);
            }

            if (null != payType)
            switch (payType) {
                case "gold":
                    dat.nBuyGold += n;
                    dat.nGold += price;
                    break;
                case "coin":
                    dat.nBuyCoin += n;
                    dat.nCoin += price;
                    break;
            }
        });
        
        return out;
    }

    @Override
    public LogHandleOutputManager createOutputManager() {
        return new OutputManager();
    }

    @Override
    public String getDescription() {
        return "Phân tích lượt mua chim theo loại - số lượt mua bằng vàng - tổng số vàng - số lượt mua bằng xu - tổng số xu";
    }
    
    private static class Output
        extends HashMap<String, Data> implements LogHandleOutput {}
    
    private static class OutputManager implements LogHandleOutputManager
    {
        private final Map<String, Data> data = new HashMap();
        
        @Override
        public synchronized void accept(LogHandleOutput output) {
            if (!(output instanceof Output))
                return;
            
            Output oData = ((Output) output);
            oData.forEach((k, v) -> {
                data.merge(k, v, Data::sum);
            });
        }

        @Override
        public void renderOutput(StringBuilder sb) {
            data.forEach((k, v) -> {
                sb.append(k).append('\t');
                sb.append(v.nBuyGold).append('\t');
                sb.append(v.nGold).append('\t');
                sb.append(v.nBuyCoin).append('\t');
                sb.append(v.nCoin).append('\n');
            });
        }
        
    }

    private static class Data
    {
        private int nBuyGold = 0, nBuyCoin = 0;
        private int nGold = 0, nCoin = 0;
        
        public static Data sum(Data d1, Data d2)
        {
            Data d = new Data();
            d.nBuyGold = d1.nBuyGold + d2.nBuyGold;
            d.nBuyCoin = d1.nBuyCoin + d2.nBuyCoin;
            d.nGold = d1.nGold + d2.nGold;
            d.nCoin = d1.nCoin + d2.nCoin;
            
            return d;
        }
    }
}
