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
public interface SwingEventHost<T, P> {
    void addEventListener(T type, SDEventListener<T, P> listener);
    void removeEventListener(T type, SDEventListener<T, P> listener);
}
