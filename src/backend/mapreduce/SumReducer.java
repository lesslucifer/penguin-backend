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
public class SumReducer<K> implements Reducer<K, Integer, Integer> {
    public static final ReduceFactory FACTORY = ReduceFactory.create(SumReducer.class);
    
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
    public void accept(Map<K, List<Integer>> t) {
        t.forEach((k, v) -> {
            int total = 0;
            total = v.stream().map((v1) -> {
                return v1;
            }).reduce(total, Integer::sum);
            out.merge(k, total, Integer::sum);
        });
    }
}