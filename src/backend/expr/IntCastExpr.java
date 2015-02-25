/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.expr;

/**
 *
 * @author Salm
 */
public class IntCastExpr implements Expression {
    public static final String P_ID = "$int";
    private final Expression field;

    public IntCastExpr(Expression field) {
        this.field = field;
    }
    
    @Override
    public Object eval(ExprContext ctx) {
        Object o = field.eval(ctx);
        if (o instanceof Number)
            return ((Number) o).intValue();
        
        return Integer.valueOf(String.valueOf(o));
    }
    
    static class Parser implements ExprParser
    {
        private Parser() {}
        public static final Parser PARSER = new Parser();
        
        @Override
        public Expression parse(Object js) {
            String id = null;
            if (js instanceof String)
            {
                id = FieldExpr.P_ID;
            }
            
            Expression field = ExprParser.parseExpr(id, js);
            return new IntCastExpr(field);
        }
    }
}
