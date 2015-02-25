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
public class FieldExpr implements Expression {
    public static final String P_ID = "$f";
    private final String field;

    public FieldExpr(String field) {
        this.field = field;
    }
    
    @Override
    public Object eval(ExprContext ctx) {
        Object ret = ctx.getData(field);
        if (ret == null)
            throw new IllegalStateException("Cannot get field " + field);

        return ret;
    }

    @Override
    public String toString() {
        return "FieldExpr{" + "field=" + field + '}';
    }
    
    static class Parser implements ExprParser
    {
        private Parser() {}
        public static final Parser PARSER = new Parser();
        
        @Override
        public Expression parse(Object js) {
            if (!(js instanceof String))
                throw new IllegalArgumentException("Data for \"$f\" must be String");
            
            return new FieldExpr((String) js);
        }
    }
}
