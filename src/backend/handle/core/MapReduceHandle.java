/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.handle.core;

import backend.record.LogRecord;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import javafx.util.Pair;

/**
 *
 * @author Salm
 * @param <K>
 * @param <V>
 */
public class MapReduceHandle<K, V> implements LogHandle {
    private final MapFunction<K, V> map;
    private final ReduceFunction<K, V> reduce;
    private final Predicate<LogRecord> filter;

    public MapReduceHandle(MapFunction<K, V> map, ReduceFunction<K, V> reduce, Predicate<LogRecord> filter) {
        this.map = map;
        this.reduce = reduce;
        this.filter = filter;
    }

    @Override
    public LogHandleOutput handle(List<LogRecord> logs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LogHandleOutputManager createOutputManager() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static interface MapFunction<K, V>
    {
        Pair<K, V> map(LogRecord rec);
    }
    
    public static interface ReduceFunction<K, V>
    {
        Pair<K, V> reduce(Map<K, List<V>> data);
    }
}
