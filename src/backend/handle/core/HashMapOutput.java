/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.handle.core;

import java.util.HashMap;

/**
 *
 * @author Salm
 */
public abstract class HashMapOutput<K, V> extends HashMap<K, V> implements LogHandleOutput {
    protected abstract V makeValue();
    
    public V mkGet(K k)
    {
        V v = this.get(k);
        if (v == null)
        {
            v = makeValue();
            this.put(k, v);
        }
        
        return v;
    }
}
