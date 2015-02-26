/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.expr;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author Salm
 */
public class AccumCountDistinct implements AccumExpression {
    private final Expression extractor;
    private final Set<Object> set = new LinkedHashSet();

    public AccumCountDistinct(Expression field) {
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
            set.add(this.extractor.eval(subCtx));
        }
        
        return set.size();
    }

    @Override
    public Object getResult() {
        return set.size();
    }
    
    public static class CountDistinctReducer implements Expression
    {
        private final Expression extractor;

        public CountDistinctReducer(Expression field) {
            this.extractor = field;
        }
        
        @Override
        public Object eval(ExprContext ctx) {
            return new AccumCountDistinct(extractor);
        }
    }
    
    static class Parser implements ExprParser
    {
        private Parser() {}
        public static final Parser PARSER = new Parser();
        
        @Override
        public Expression parse(Object js) {
            Expression extractor = ExprParser.parseExpr(
                    (js instanceof String)?FieldExpr.P_ID:null, js);
            return new CountDistinctReducer(extractor);
        }
    }
}
