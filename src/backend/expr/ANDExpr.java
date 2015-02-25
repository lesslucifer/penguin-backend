/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.expr;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Salm
 */
public class ANDExpr implements Expression {
    private final List<Expression> req;

    ANDExpr(List<Expression> req) {
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
            if (!(js instanceof List))
                throw new IllegalArgumentException("Data for \"$and\" must be List");
            
            List<?> l = (List) js;
            List<Expression> data = new ArrayList(l.size());
            l.forEach((d) -> data.add(ExprParser.parseExpr(null, d)));
            
            return new ANDExpr(data);
        }
    }
}
