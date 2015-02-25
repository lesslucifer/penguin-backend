/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.expr;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Salm
 */
class EQ implements Expression {
    private final Map<Expression, Expression> req;

    EQ(Map<Expression, Expression> req) {
        this.req = req;
    }
    
    @Override
    public Object eval(ExprContext ctx) {
        if (req == null || req.isEmpty())
            return true;
        
        return req.entrySet().stream().allMatch((e) ->
                e.getKey().eval(ctx).equals(e.getValue().eval(ctx)));
    }
    
    static class Parser implements ExprParser
    {
        private Parser() {}
        public static final Parser PARSER = new Parser();
        
        @Override
        public Expression parse(Object js) {
            if (!(js instanceof Map))
                throw new IllegalArgumentException("Data for \"$eq\" must be Object");
            
            Map<?, ?> m = (Map) js;
            
            Map<Expression, Expression> data = new LinkedHashMap();
            
            m.forEach((k, v) -> {
                data.put(ExprParser.parseExpr(FieldExpr.P_ID, k),
                        ExprParser.parseExpr(null, v));
            });
            
            return new EQ(data);
        }
    }
}
