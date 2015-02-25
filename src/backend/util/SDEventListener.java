/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.util;

/**
 *
 * @author Salm
 */
public interface SDEventListener<T, P> {
    public void handleEvent(SDEvent<T, P> event);
}
