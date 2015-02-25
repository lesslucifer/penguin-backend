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
public class AccumCount implements AccumExpression {
    private int count = 0;

    public AccumCount() {
    }
    
    @Override
    public Object getResult() {
        return count;
    }

    @Override
    public Object eval(ExprContext ctx) {
        if (!(ctx instanceof ArrayExprContext))
            throw new IllegalArgumentException("Reduce expr require array context");
        
        count += ((ArrayExprContext) ctx).size();
        
        return getResult();
    }
    
    public static class CountReducer implements Expression
    {
        public CountReducer() {
        }
        
        @Override
        public Object eval(ExprContext ctx) {
            return new AccumCount();
        }
    }
    
    static class Parser implements ExprParser
    {
        private Parser() {}
        public static final Parser PARSER = new Parser();
        
        @Override
        public Expression parse(Object js) {
            return new CountReducer();
        }
    }
}
