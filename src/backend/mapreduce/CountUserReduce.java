/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.mapreduce;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Salm
 */
public abstract class CountUserReduce<K> implements Reducer<K, String, Integer> {
    private final Map<K, Set<String>> out = new ConcurrentHashMap();

    @Override
    public Map<K, Integer> output() {
        Map<K, Integer> outp = new HashMap(out.size());
        out.forEach((k, v) -> {
            outp.put(k, v.size());
        });

        return outp;
    }

    @Override
    public void renderOutput(StringBuilder sb) {
        output().forEach((k, v) -> {
            renderKey(k, sb);
            sb.append(v).append('\n');
        });
    }
    
    protected abstract void renderKey(K k, StringBuilder sb);

    @Override
    public synchronized void accept(Map<K, List<String>> t) {
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
