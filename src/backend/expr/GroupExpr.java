/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.expr;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Salm
 */
public class GroupExpr implements Expression {
    public static final String P_ID = "$gr";
    private final Map<String, Expression> exprs;

    public GroupExpr(Map<String, Expression> exprs) {
        this.exprs = exprs;
    }

    @Override
    public Object eval(ExprContext ctx) {
        Map<String, Object> outp = new HashMap(exprs.size());
        exprs.forEach((s, e) -> outp.put(s, e.eval(ctx)));
        
        return outp;
    }
    
    public Expression get(String s)
    {
        return exprs.get(s);
    }
    
    static class Parser implements ExprParser
    {
        private Parser() {}
        public static final Parser PARSER = new Parser();
        
        @Override
        public Expression parse(Object js) {
            if (!(js instanceof Map))
                throw new IllegalArgumentException("Data for \"$gr\" must be Object");
            
            Map<?, ?> m = (Map) js;
            
            Map<String, Expression> data = new LinkedHashMap();
            
            m.forEach((k, v) -> {
                data.put((String) k,
                        ExprParser.parseExpr((String) k, v));
            });
            
            return new GroupExpr(data);
        }
    }
}
