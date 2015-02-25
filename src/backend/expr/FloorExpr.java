/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.expr;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 *
 * @author Salm
 */
public class FloorExpr implements Expression {
    private final Expression data;
    private final NavigableMap<Object, Object> indices;

    public FloorExpr(Expression data,
            Map<Object, Object> indices, Comparator<Object> comp) {
        this.data = data;
        this.indices = new TreeMap<>(comp);
        this.indices.putAll(indices);
    }

    @Override
    public Object eval(ExprContext ctx) {
        Object dat = data.eval(ctx);
        return indices.get(indices.floorKey(dat));
    }
    
    static class Parser implements ExprParser
    {
        private Parser() {}
        public static final Parser PARSER = new Parser();
        
        @Override
        public Expression parse(Object js) {
            if (!(js instanceof Map))
                throw new IllegalArgumentException("Data for \"$floor\" must be Map");
            
            Map<?, ?> m = (Map) js;
            if (!m.containsKey("$data") || !m.containsKey("$index"))
                throw new IllegalArgumentException("Data for \"$floor\" must contains $data and $index");
            
            Object data = m.get("$data");
            List<Object> index = (List) m.get("$index");
            
            return null;
        }
    }
}
