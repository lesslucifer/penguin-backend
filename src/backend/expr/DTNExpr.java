/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.expr;

import backend.record.StdRecord;
import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 *
 * @author Salm
 */
public class DTNExpr implements Expression {
    public static final String P_ID = "$dtn";
    
    private final Expression startDay;
    private final Expression expr;

    public DTNExpr(Expression startDay, Expression expr) {
        this.startDay = startDay;
        this.expr = expr;
    }

    @Override
    public Object eval(ExprContext ctx) {
        DateTime stDay = toDate(this.startDay.eval(ctx));
        DateTime dt = toDate(this.expr.eval(ctx));
        
        return Days.daysBetween(stDay, dt).getDays();
    }
    
    static DateTime toDate(Object dat)
    {
        if (dat instanceof DateTime)
            return (DateTime) dat;
        else if (dat instanceof String)
        {
            String sDat = (String) dat;
            if (sDat.indexOf('\\') >= 0)
                sDat = sDat.replace('\\', '-');
            if (sDat.indexOf('/') >= 0)
                sDat = sDat.replace('/', '-');
            
            return StdRecord.D_FMT.parseDateTime(sDat);
        }
        else
            throw new IllegalStateException("Invalid datetime type");
    }
    
    static class Parser implements ExprParser
    {
        private Parser() {}
        public static final Parser PARSER = new Parser();
        static final DateTime DEFAULT_START = new DateTime(0);
        
        @Override
        public Expression parse(Object js) {
            Object dt, st;
            if (js instanceof Map && (((Map) js).size() > 1))
            {
                Map<?, ?> m = (Map) js;
                dt = m.get("$date");
                st = StdRecord.D_FMT.parseDateTime((String) m.get("$start"));
            }
            else
            {
                dt = js;
                st = DEFAULT_START;
            }
            
            Expression dtExpr = ExprParser.parseExprF(null, dt);
            Expression stExpr = ExprParser.parseExpr(null, st);
            
            return new DTNExpr(stExpr, dtExpr);
        }
    }
}