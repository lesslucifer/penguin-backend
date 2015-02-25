/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.expr;

import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Salm
 */
public class WarppedExprParser implements ExprParser {
    public static final String P_ID = "$1";
    
    private WarppedExprParser() {}
    public static final WarppedExprParser PARSER = new WarppedExprParser();
        
    @Override
    public Expression parse(Object js) {
        if (!(js instanceof Map))
            throw new IllegalArgumentException("Data for \"$1\" must be Object");

        Map<?, ?> m = (Map) js;
        
        if (m.size() != 1)
            throw new IllegalArgumentException("Data for $1 must has size = 1");
        
        Entry<?, ?> e = m.entrySet().iterator().next();
        
        return ExprParser.parseExpr((String) e.getKey(), e.getValue());
    }
    
}
