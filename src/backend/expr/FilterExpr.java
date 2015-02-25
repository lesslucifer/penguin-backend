/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.expr;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Salm
 */
public class FilterExpr implements Expression {
    private final Set<Expression> req;

    FilterExpr(Set<Expression> req) {
        this.req = req;
    }
    
    @Override
    public Object eval(ExprContext ctx) {
        if (req == null || req.isEmpty())
            return true;
        
        return req.stream().allMatch((e) ->
        {
            Object o = e.eval(ctx);
            if (!(o instanceof Boolean))
                return false;
            
            return (Boolean) o;
        });
    }
    
    static class Parser implements ExprParser
    {
        private Parser() {}
        public static final Parser PARSER = new Parser();
        
        @Override
        public Expression parse(Object js) {
            if (!(js instanceof Map))
                throw new IllegalArgumentException("Data for \"$filter\" must be Set");
            
            Map<String, ?> l = (Map) js;
            Set<Expression> data = new HashSet(l.size());
            l.forEach((k, v) -> data.add(ExprParser.parseExpr(k, v)));
            
            return new FilterExpr(data);
        }
    }
}
