/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.expr;

/**
 *
 * @author Salm
 */
public interface ExprContext {
    Object getData(String dat);
    default boolean contains(String dat)
    {
        return getData(dat) != null;
    }
}
