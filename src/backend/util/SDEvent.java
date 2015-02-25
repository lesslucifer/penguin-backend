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
public class SDEvent<T, P> {
    private final T type;
    private final P param;

    public SDEvent(T type, P param) {
        this.type = type;
        this.param = param;
    }

    public T getType() {
        return type;
    }

    public P getParam() {
        return param;
    }
}
