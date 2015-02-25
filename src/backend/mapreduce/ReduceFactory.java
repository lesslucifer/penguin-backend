/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.mapreduce;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Salm
 */
public interface ReduceFactory<K, V1, V2>
{
    Reducer<K, V1, V2> createReducer();
    String getName();
    
    public static <K, V1, V2> ReduceFactory<K, V1, V2> create(Class<? extends Reducer> redClass)
    {
        return new ReduceFactory()
        {
            @Override
            public Reducer createReducer() {
                try {
                    return redClass.newInstance();
                } catch (InstantiationException | IllegalAccessException ex) {
                    Logger.getLogger(ReduceFactory.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                return null;
            }

            @Override
            public String getName() {
                return redClass.getSimpleName();
            }
        };
    }
}
