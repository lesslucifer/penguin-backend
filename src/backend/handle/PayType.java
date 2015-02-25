/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.handle;

/**
 *
 * @author Salm
 */
public enum PayType {
    none,
    gold,
    coin;
    
    public static PayType parse(String p)
    {
        try
        {
            return valueOf(p.toLowerCase());
        }
        catch (Exception ex)
        {
            return none;
        }
    }
}
