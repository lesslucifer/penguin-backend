/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.mapreduce;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Salm
 */
public class CountValueReducer<K, V> implements Reducer<K, V, Integer> {
    public static final ReduceFactory FACTORY = ReduceFactory.create(CountValueReducer.class);
    
    protected final Map<K, Integer> out = new ConcurrentHashMap<>();
    
    @Override
    public Map<K, Integer> output() {
        return out;
    }

    @Override
    public void renderOutput(StringBuilder sb) {
        out.forEach((k, v) -> {
            renderKey(k, sb);
            sb.append(v).append('\n');
        });
    }
    
    protected void renderKey(K k, StringBuilder sb)
    {
        sb.append(k).append('\t');
    }

    @Override
    public void accept(Map<K, List<V>> t) {
        t.forEach((k, v) -> {
            out.merge(k, v.size(), Integer::sum);
        });
    }
}
