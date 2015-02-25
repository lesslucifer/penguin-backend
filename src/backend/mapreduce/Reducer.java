/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.mapreduce;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 *
 * @author Salm
 */
public interface Reducer<K, V1, V2>
    extends Consumer<Map<K, List<V1>>>
{
    Map<K, V2> output();
    void renderOutput(StringBuilder sb);
}
