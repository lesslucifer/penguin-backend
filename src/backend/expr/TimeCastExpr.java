/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.expr;

import backend.record.StdRecord;
import org.joda.time.DateTime;

/**
 *
 * @author Batero
 */
public class TimeCastExpr implements Expression {
    public static final String P_ID = "$time";
    private final Expression field;

    public TimeCastExpr(Expression field) {
        this.field = field;
    }
    
    @Override
    public Object eval(ExprContext ctx) {
        Object o = field.eval(ctx);
        if (o instanceof DateTime)
            return o;
        else if (o instanceof Number)
            return new DateTime(((Number) o).longValue());
        
        return StdRecord.DT_FMT.parseDateTime(String.valueOf(o));
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
            return new TimeCastExpr(field);
        }
    }
}
