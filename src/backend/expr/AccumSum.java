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
public class AccumSum implements AccumExpression {
    private final Expression extractor;
    private int total = 0;

    public AccumSum(Expression field) {
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
            
            total += (Integer) this.extractor.eval(subCtx);
        }
        
        return total;
    }

    @Override
    public Object getResult() {
        return total;
    }
    
    public static class SumReducer implements Expression
    {
        private final Expression extractor;

        public SumReducer(Expression field) {
            this.extractor = field;
        }
        
        @Override
        public Object eval(ExprContext ctx) {
            return new AccumSum(extractor);
        }
    }
    
    static class Parser implements ExprParser
    {
        private Parser() {}
        public static final Parser PARSER = new Parser();
        
        @Override
        public Expression parse(Object js) {
            Expression extractor = ExprParser.parseExpr((js instanceof String)?FieldExpr.P_ID:null, js);
            return new SumReducer(extractor);
        }
    }
}
