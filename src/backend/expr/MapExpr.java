/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.expr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Salm
 */
public class MapExpr implements Expression {
    private final List<Expression> data;

    MapExpr(List<Expression> req) {
        this.data = req;
    }
    
    @Override
    public Object eval(ExprContext ctx) {
        if (data == null || data.isEmpty())
            return Collections.EMPTY_LIST;
        
        List<Object> outp = new ArrayList(this.data.size());
        this.data.forEach((e) -> outp.add(e.eval(ctx)));
        
        return outp;
    }

    @Override
    public String toString() {
        return "MapExpr{" + "data=" + data + '}';
    }
    
    static class Parser implements ExprParser
    {
        private Parser() {}
        public static final Parser PARSER = new Parser();
        
        @Override
        public Expression parse(Object js) {
            if (!(js instanceof List))
                throw new IllegalArgumentException("Data for \"$map\" must be List");
            
            List<?> l = (List) js;
            List<Expression> data = new ArrayList(l.size());
            l.forEach((d) -> data.add(ExprParser.parseExpr((d instanceof String)?FieldExpr.P_ID:null, d)));
            
            return new MapExpr(data);
        }
    }
}
