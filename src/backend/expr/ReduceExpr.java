/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.expr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Salm
 */
public class ReduceExpr implements Expression {
    private final List<Expression> data;

    ReduceExpr(List<Expression> req) {
        this.data = req;
    }
    
    @Override
    public Object eval(ExprContext ctx) {
        if (data == null || data.isEmpty())
            return Collections.EMPTY_MAP;
        
        List<AccumExpression> accums = new ArrayList(data.size());
        data.forEach((e) -> accums.add((AccumExpression) e.eval(null)));
        
        return accums;
    }
    
    static class Parser implements ExprParser
    {
        private Parser() {}
        public static final Parser PARSER = new Parser();
        
        @Override
        public Expression parse(Object js) {
            if (!(js instanceof Collection))
                throw new IllegalArgumentException("Data for \"$reduce\" must be List");
            
            Collection<?> l = (Collection) js;
            List<Expression> data = new ArrayList(l.size());
            l.forEach((d) -> data.add(ExprParser.parseExpr(null, d)));
            
            return new ReduceExpr(data);
        }
    }
}
