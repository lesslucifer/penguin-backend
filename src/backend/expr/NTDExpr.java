/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.expr;

import static backend.expr.DTNExpr.Parser.DEFAULT_START;
import static backend.expr.DTNExpr.toDate;
import backend.record.StdRecord;
import java.util.Map;
import org.joda.time.DateTime;

/**
 *
 * @author Salm
 */
public class NTDExpr implements Expression {
    public static final String P_ID = "$ntd";
    
    private final Expression startDay;
    private final Expression expr;

    public NTDExpr(Expression startDay, Expression expr) {
        this.startDay = startDay;
        this.expr = expr;
    }

    @Override
    public Object eval(ExprContext ctx) {
        DateTime stDay = toDate(this.startDay.eval(ctx));
        int incDay = ((Number) this.expr.eval(ctx)).intValue();
        
        return StdRecord.DT_FMT.print(stDay.plusDays(incDay + 1));
    }
    
    static class Parser implements ExprParser
    {
        private Parser() {}
        public static final Parser PARSER = new Parser();
        
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
            
            return new NTDExpr(stExpr, dtExpr);
        }
    }
}