/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.expr;

import java.util.Map;
import org.joda.time.DateTime;

/**
 *
 * @author Batero
 */
public class CompareExpr implements Expression {
    public static final String P_ID = "$comp";
    private final String op;
    private final Expression e1, e2;

    public CompareExpr(String op, Expression e1, Expression e2) {
        this.op = op;
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public Object eval(ExprContext ctx) {
        if (op == null)
            throw new IllegalArgumentException("Null compare operation");
        
        Object o1 = e1.eval(ctx);
        Object o2 = e2.eval(ctx);
        
        int res = compare(o1, o2);
        switch (op)
        {
            case "=":
                return res == 0;
            case "!=":
                return res != 0;
            case ">":
                return res > 0;
            case ">=":
                return res >= 0;
            case "<":
                return res < 0;
            case "<=":
                return res <= 0;
        }
        
        throw new IllegalArgumentException("Unknown compare op");
    }
    
    public static int compare(Object o1, Object o2)
    {
        if (o1.equals(o2))
            return 0;

        if (o1 instanceof Number && o2 instanceof Number)
        {
            return Double.compare(((Number) o1).doubleValue(),
                    ((Number) o2).doubleValue());
        }
        else if (o1 instanceof DateTime && o2 instanceof DateTime)
        {
            return ((DateTime) o1).compareTo((DateTime) o2);
        }
        else if (o1 instanceof String && o2 instanceof String)
        {
            return ((String) o1).compareTo((String) o2);
        }

        throw new IllegalArgumentException("No type matched for compare");
    }
    
    static class Parser implements ExprParser
    {
        private Parser() {}
        public static final Parser PARSER = new Parser();
        
        @Override
        public Expression parse(Object js) {
            if (!(js instanceof Map))
                throw new IllegalArgumentException("Data for \"$compare\" must be Object");
            
            Map<?, ?> m = (Map) js;
            String op = (String) m.get("$op");
            Expression e1 = ExprParser.parseExprF(null, m.get("$e1"));
            Expression e2 = ExprParser.parseExpr(null, m.get("$e2"));
            
            return new CompareExpr(op, e1, e2);
        }
    }
}
