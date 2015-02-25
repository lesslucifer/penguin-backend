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
public class AccumAvg implements AccumExpression {
    private final Expression extractor;
    private double total = 0;
    private int count = 0;

    public AccumAvg(Expression extractor) {
        this.extractor = extractor;
    }
    
    @Override
    public Object getResult() {
        return total / count;
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
            
            total += ((Number) this.extractor.eval(subCtx)).doubleValue();
            ++count;
        }
        
        return getResult();
    }
    
    public static class AvgReducer implements Expression
    {
        private final Expression extractor;

        public AvgReducer(Expression field) {
            this.extractor = field;
        }
        
        @Override
        public Object eval(ExprContext ctx) {
            return new AccumAvg(extractor);
        }
    }
    
    static class Parser implements ExprParser
    {
        private Parser() {}
        public static final Parser PARSER = new Parser();
        
        @Override
        public Expression parse(Object js) {
            Expression extractor = ExprParser.parseExpr(null, js);
            return new AvgReducer(extractor);
        }
    }
}
