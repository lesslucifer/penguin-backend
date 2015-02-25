/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.mapreduce.mr;

import backend.handle.Category;
import backend.mapreduce.CountUserReduce;
import backend.mapreduce.MROpFactory;
import backend.mapreduce.MapFunction;
import backend.mapreduce.MapReduceOperation;
import backend.mapreduce.ReduceFactory;
import backend.mapreduce.Reducer;
import backend.record.LogRecord;
import backend.record.ActionRecord;
import backend.record.UserAction;
import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javafx.util.Pair;

/**
 *
 * @author Salm
 */
public class DemSoUserNangCapTramCanh extends MapReduceOperation<Object, Object, Object> {

    public DemSoUserNangCapTramCanh(List<File> file) {
        super(Category.HANHDONG, file, new Mapper(), 
                ReduceFactory.create(ReducerImpl.class));
    }
    
    public static MROpFactory DemSoUserNangCapTramCanh()
    {
        return new MROpFactory() {
            @Override
            public MapReduceOperation<?, ?, ?> createOp(List<File> files) {
                return new DemSoUserNangCapTramCanh(files);
            }

            @Override
            public String getName() {
                return "DemSoUserNangCapTramCanh";
            }
        };
    }
    
    public static class Mapper implements MapFunction<Object, Object>
    {
        @Override
        public List<Pair<Object, Object>> map(List<LogRecord> recs) {
            List<Pair<Object, Object>> out = new LinkedList();
            
            for (LogRecord rec : recs) {
                if (!(rec instanceof ActionRecord))
                    continue;
                
                ActionRecord r = (ActionRecord) rec;
                
                if (r.getAction() != UserAction.NC_TRAMCANH)
                    continue;
                
                Object key = r.getParam(0);
                Object value = r.getUid();
                
                out.add(new Pair(key, value));
            }
            
            return out;
        }
    }
    
    public static class ReducerImpl extends CountUserReduce<Object>
    {
        @Override
        protected void renderKey(Object k, StringBuilder sb) {
            sb.append(k).append('\t');
        }
    }
}
