/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.mapreduce;

import java.util.function.Predicate;

/**
 *
 * @author Salm
 */
public class ConstantPredicate implements Predicate {
    private final boolean c;
    private ConstantPredicate(boolean logic)
    {
        this.c = logic;
    }
    
    public static final ConstantPredicate TRUE = new ConstantPredicate(true);
    public static final ConstantPredicate FALSE = new ConstantPredicate(false);

    @Override
    public boolean test(Object t) {
        return c;
    }
}
