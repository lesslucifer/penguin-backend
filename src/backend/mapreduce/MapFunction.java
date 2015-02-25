/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.mapreduce;

import backend.record.LogRecord;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author Salm
 */
public interface MapFunction<K, V1> {
    List<Pair<K, V1>> map(List<LogRecord> recs);
    default String getName()
    {
        return this.getClass().getSimpleName();
    }
}
