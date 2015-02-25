/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.SwingUtilities;

/**
 *
 * @author Salm
 */
public class SwingEventDispatcher<T, P> implements SwingEventHost<T, P> {
    private final Map<T, List<SDEventListener<T, P>>> handlers = new ConcurrentHashMap();
    
    @Override
    public synchronized void addEventListener(T type, SDEventListener<T, P> listener)
    {
        List<SDEventListener<T, P>> listeners = handlers.get(type);
        if (listeners == null)
        {
            listeners = new LinkedList();
            handlers.put(type, listeners);
        }
        
        listeners.add(listener);
    }
    
    @Override
    public synchronized void removeEventListener(T type, SDEventListener<T, P> listener)
    {
        List<SDEventListener<T, P>> listeners = handlers.get(type);
        if (listeners == null)
            return;
        
        listeners.remove(listener);
    }
    
    public synchronized void dispatchEvent(SDEvent<T, P> evt)
    {
        java.awt.EventQueue.invokeLater(() -> {
            List<SDEventListener<T, P>> listeners = handlers.get(evt.getType());
            if (listeners == null)
                return;
            
            listeners.stream().forEach((l) -> {
                l.handleEvent(evt);
            });
        });
    }
}
