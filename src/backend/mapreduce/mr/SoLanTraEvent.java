/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.mapreduce.mr;

import backend.handle.Category;
import backend.mapreduce.ConstantPredicate;
import backend.mapreduce.MROpFactory;
import backend.mapreduce.MapFunction;
import backend.mapreduce.MapReduceOperation;
import backend.mapreduce.ReduceFactory;
import backend.mapreduce.Reducer;
import backend.mapreduce.mr.SoLanTraEvent.Key;
import backend.record.ActionRecord;
import backend.record.LogRecord;
import backend.record.UserAction;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javafx.util.Pair;

/**
 *
 * @author Salm
 */
public class SoLanTraEvent extends MapReduceOperation<Key, String, Integer> {

    private SoLanTraEvent(List<File> file, ReduceFactory<Key, String, Integer> rf) {
        super(Category.EVENT, file,
                new Mapper(), rf,
                ConstantPredicate.TRUE);
    }
    
    public static MROpFactory DemSoLanTra()
    {
        return new MROpFactory() {
            @Override
            public MapReduceOperation<?, ?, ?> createOp(List<File> files) {
                return new SoLanTraEvent(files, 
                        ReduceFactory.<Key, String, Integer>create(CountOnGiftReducer.class));
            }

            @Override
            public String getName() {
                return "DemSoLanTraEvent";
            }
        };
    }
    
    public static MROpFactory DemSoUserTra()
    {
        return new MROpFactory() {
            @Override
            public MapReduceOperation<?, ?, ?> createOp(List<File> files) {
                return new SoLanTraEvent(files, 
                        ReduceFactory.<Key, String, Integer>create(CountUserReducer.class));
            }

            @Override
            public String getName() {
                return "DemSoUserTraEvent";
            }
        };
    }
    
    public static class Mapper implements MapFunction<Key, String>
    {
        @Override
        public List<Pair<Key, String>> map(List<LogRecord> recs) {
            List<Pair<Key, String>> lr = new LinkedList();
            
            recs.forEach((r) -> {
                if (!(r instanceof ActionRecord))
                    return;
                
                ActionRecord er = (ActionRecord) r;
                if (er.getAction()!= UserAction.TRA_EVENT_RE && er.getAction()!= UserAction.TRA_EVENT_TONG_RE)
                    return;
                
                lr.add(new Pair(new Key(er.getAction(), er.getInfo(1)), er.getUid()));
            });
            
            return lr;
        }
    }
    
    public static class CountOnGiftReducer implements Reducer<Key, String, Integer>
    {
        private final Map<Key, Integer> out = new ConcurrentHashMap();
        
        @Override
        public Map<Key, Integer> output() {
            return out;
        }

        @Override
        public void renderOutput(StringBuilder sb) {
            out.forEach((k, v) -> {
                sb.append(k.act).append('\t');
                sb.append(k.itemID).append('\t');
                sb.append(v).append('\n');
            });
        }

        @Override
        public synchronized void accept(Map<Key, List<String>> t) {
            t.forEach((k, v) -> {
                out.merge(k, v.size(), Integer::sum);
            });
        }
    }
    
    public static class CountUserReducer implements Reducer<Key, String, Integer>
    {
        private final Map<Key, Set<String>> out = new ConcurrentHashMap();
        
        @Override
        public Map<Key, Integer> output() {
            Map<Key, Integer> outp = new HashMap(out.size());
            out.forEach((k, v) -> {
                outp.put(k, v.size());
            });
            
            return outp;
        }

        @Override
        public void renderOutput(StringBuilder sb) {
            output().forEach((k, v) -> {
                sb.append(k.act).append('\t');
                sb.append(k.itemID).append('\t');
                sb.append(v).append('\n');
            });
        }

        @Override
        public synchronized void accept(Map<Key, List<String>> t) {
            t.forEach((k, v) -> {
                Set<String> uids = out.get(k);
                if (uids == null)
                {
                    uids = new HashSet();
                    out.put(k, uids);
                }
                
                uids.addAll(v);
            });
        }
    }
    
    public static class Key
    {
        private final UserAction act;
        private final String itemID;

        public Key(UserAction act, String itemID) {
            this.act = act;
            this.itemID = itemID;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 23 * hash + Objects.hashCode(this.act);
            hash = 23 * hash + Objects.hashCode(this.itemID);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Key other = (Key) obj;
            if (this.act != other.act) {
                return false;
            }
            if (!Objects.equals(this.itemID, other.itemID)) {
                return false;
            }
            return true;
        }
    }
}
