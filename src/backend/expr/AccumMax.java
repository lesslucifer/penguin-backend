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
public class AccumMax implements AccumExpression {
    private final Expression extractor;
    private int max = Integer.MIN_VALUE;

    public AccumMax(Expression field) {
        this.extractor = field;
    }

    @Override
    public Object eval(ExprContext ctx) {
        if (!(ctx instanceof ArrayExprContext))
            throw new IllegalArgumentException("Reduce expr require array context");
        
        ArrayExprContext arrCtx = (ArrayExprContext) ctx;
        for (Object subCtxObj : arrCtx) {
            if (!(subCtxObj instanceof ExprContext))
                throw new IllegalArgumentException("Array context must contains Context");
        }
        
        for (Object subCtxObj : arrCtx) {
            ExprContext subCtx = (ExprContext) subCtxObj;
            
            int val = (Integer) this.extractor.eval(subCtx);
            if (val > max)
            {
                max = val;
            }
        }
        
        return max;
    }

    @Override
    public Object getResult() {
        return max;
    }
    
    public static class MaxReducer implements Expression
    {
        private final Expression extractor;

        public MaxReducer(Expression field) {
            this.extractor = field;
        }
        
        @Override
        public Object eval(ExprContext ctx) {
            return new AccumMax(extractor);
        }
    }
    
    static class Parser implements ExprParser
    {
        private Parser() {}
        public static final Parser PARSER = new Parser();
        
        @Override
        public Expression parse(Object js) {
            Expression extractor = ExprParser.parseExpr((js instanceof String)?FieldExpr.P_ID:null, js);
            return new MaxReducer(extractor);
        }
    }
}
