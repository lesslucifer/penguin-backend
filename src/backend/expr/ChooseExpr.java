/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.expr;

import java.util.Map;

/**
 *
 * @author Batero
 */
public class ChooseExpr implements AccumExpression {
    public static final String P_ID = "$red_choose";
    public final Expression key, value, comparator;
    public Object cKey = null, cVal = null;

    public ChooseExpr(Expression key, Expression value,
            Expression comparator) {
        this.key = key;
        this.value = value;
        this.comparator = comparator;
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
            
            Object k = key.eval(subCtx);
            if (isGreater(k))
            {
                cKey = k;
                cVal = value.eval(subCtx);
            }
        }
        
        return cVal;
    }
    
    private boolean isGreater(Object k)
    {
        if (cKey == null)
        {
            return true;
        }
        
        int i = (Integer) comparator.eval((dat) -> {
            if (null != dat)
                switch (dat) {
                case "1":
                    return cKey;
                case "0":
                    return k;
            }
            
            return null;
        });
        
        return i > 0;
    }

    @Override
    public Object getResult() {
        return cVal;
    }
    
    public static class ChooseReducer implements Expression
    {
        private final Expression key, value, comp;

        public ChooseReducer(Expression key, Expression value, Expression comp) {
            this.key = key;
            this.value = value;
            this.comp = comp;
        }
        
        @Override
        public Object eval(ExprContext ctx) {
            return new ChooseExpr(this.key, this.value, this.comp);
        }
    }
    
    static class Parser implements ExprParser
    {
        private Parser() {}
        public static final Parser PARSER = new Parser();
        
        @Override
        public Expression parse(Object js) {
            if (!(js instanceof Map))
                throw new IllegalArgumentException("");
            
            Map<?, ?> m = (Map) js;
            Expression _key = ExprParser.parseExprF(null, m.get("$key"));
            Expression _val = ExprParser.parseExprF(null, m.get("$value"));
            Expression _comp = (ctx) -> {
                Object o1 = ctx.getData("0");
                Object o2 = ctx.getData("1");
                
                return CompareExpr.compare(o1, o2);
            };
            
            return new ChooseReducer(_key, _val, _comp);
        }
    }
}
