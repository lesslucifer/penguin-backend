/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.expr;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Salm
 */
public interface ArrayExprContext extends ExprContext, Iterable<Object> {

    @Override
    public default Object getData(String dat) {
        return get(Integer.parseInt(dat));
    }
    
    Object get(int idx);
    int size();
    
    public static ArrayExprContext make(List<? extends ExprContext> ctxs)
    {
        return new Impl(ctxs);
    }
    
    static class Impl implements ArrayExprContext
    {
        private final List<? extends ExprContext> contexts;
        private Impl(List<? extends ExprContext> ctxs)
        {
            this.contexts = ctxs;
        }

        @Override
        public Object get(int idx) {
            return this.contexts.get(idx);
        }

        @Override
        public int size() {
            return this.contexts.size();
        }

        @Override
        public Iterator<Object> iterator() {
            return (Iterator) this.contexts.iterator();
        }
    }
}
