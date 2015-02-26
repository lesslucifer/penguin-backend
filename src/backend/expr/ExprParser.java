/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.expr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Salm
 */
interface ExprParser {
    Expression parse(Object js);
    
    static Expression parseExpr(String s, Object js)
    {
        if (s != null && s.length() > 0 && __.PARSERS.containsKey(s))
        {
            ExprParser parser = __.PARSERS.get(s);
            return parser.parse(js);
        }
        
        if (js instanceof Map)
        {
            if (((Map) js).size() == 1)
            {
                return WarppedExprParser.PARSER.parse(js);
            }
        }
        
        if (js instanceof List)
        {
            return ListExpr.Parser.PARSER.parse(js);
        }
        
        return new DataExpr(js);
    }
    
    static Expression parseExprF(String s, Object js)
    {
        return  parseExpr((js instanceof String)?FieldExpr.P_ID:s, js);
    }
    
    static class __
    {
        private static final Map<String, ExprParser> PARSERS = new HashMap();
        
        static
        {
            PARSERS.put(FieldExpr.P_ID, FieldExpr.Parser.PARSER);
            PARSERS.put("$eq", EQ.Parser.PARSER);
            PARSERS.put("$and", ANDExpr.Parser.PARSER);
            PARSERS.put("$filter", FilterExpr.Parser.PARSER);
            PARSERS.put("$map", MapExpr.Parser.PARSER);
            PARSERS.put("$reduce", ReduceExpr.Parser.PARSER);
            PARSERS.put("$red_sum", AccumSum.Parser.PARSER);
            PARSERS.put("$red_max", AccumMax.Parser.PARSER);
            PARSERS.put("$red_avg", AccumAvg.Parser.PARSER);
            PARSERS.put("$red_count", AccumCount.Parser.PARSER);
            PARSERS.put("$red_count_dist", AccumCountDistinct.Parser.PARSER);
            PARSERS.put(WarppedExprParser.P_ID, WarppedExprParser.PARSER);
            PARSERS.put(GroupExpr.P_ID, GroupExpr.Parser.PARSER);
            PARSERS.put(IntCastExpr.P_ID, IntCastExpr.Parser.PARSER);
            PARSERS.put(TimeCastExpr.P_ID, TimeCastExpr.Parser.PARSER);
            PARSERS.put(ChooseExpr.P_ID, ChooseExpr.Parser.PARSER);
            PARSERS.put(CompareExpr.P_ID, CompareExpr.Parser.PARSER);
            PARSERS.put(DTNExpr.P_ID, DTNExpr.Parser.PARSER);
            PARSERS.put(NTDExpr.P_ID, NTDExpr.Parser.PARSER);
            PARSERS.put(ExprExpr.P_ID, ExprExpr.Parser.PARSER);
        }
    }
}
