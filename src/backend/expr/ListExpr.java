/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.expr;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Salm
 */
public class ListExpr implements Expression {
    private final List<Expression> exprs;

    public ListExpr(List<Expression> exprs) {
        this.exprs = exprs;
    }
    
    @Override
    public Object eval(ExprContext ctx) {
        List<Object> ret = new ArrayList(exprs.size());
        this.exprs.forEach((expr) -> ret.add(expr.eval(ctx)));
        return ret;
    }
    
    static class Parser implements ExprParser
    {
        private Parser() {}
        public static final Parser PARSER = new Parser();
        
        @Override
        public Expression parse(Object js) {
            if (!(js instanceof List))
                throw new IllegalArgumentException("Data for \"$arr\" must be List");
            
            List<?> l = (List) js;
            
            List<Expression> data = new LinkedList();
            
            l.forEach((exprData) -> {
                data.add(ExprParser.parseExprF(null, exprData));
            });
            
            return new ListExpr(data);
        }
    }
}
