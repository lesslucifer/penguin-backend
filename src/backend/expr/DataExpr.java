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
public class DataExpr implements Expression {
    private final Object data;

    DataExpr(Object data) {
        this.data = data;
    }

    @Override
    public Object eval(ExprContext ctx) {
        return data;
    }
}
