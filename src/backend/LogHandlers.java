/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import backend.handle.Category;
import backend.handle.ChiXuTheoNguonTheoNgay;
import backend.handle.MuaChim;
import backend.handle.NguonThuExpTheoNgay;
import backend.handle.UserNhanEventQuayLai;
import backend.handle.core.LogHandle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Salm
 */
class LogHandlers {
    private static final Map<Category, List<LogHandle>> handlers = new HashMap();
    
    public static void addHandler(Category ctgr, LogHandle handler)
    {
        List<LogHandle> hdlrs = handlers.get(ctgr);
        if (hdlrs == null)
        {
            hdlrs = new ArrayList();
            handlers.put(ctgr, hdlrs);
        }
            
        hdlrs.add(handler);
    }
    
    public static List<LogHandle> getHandlers(Category ctgr)
    {
        List<LogHandle> hdlrs = handlers.get(ctgr);
        if (hdlrs == null)
            return Collections.EMPTY_LIST;
        
        return Collections.unmodifiableList(hdlrs);
    }
    
    static
    {
        addHandler(Category.HANHDONG, new MuaChim());
        addHandler(Category.HANHDONG, new UserNhanEventQuayLai());
        
        addHandler(Category.THU_EXP, new NguonThuExpTheoNgay());
        
        addHandler(Category.CHI_XU, new ChiXuTheoNguonTheoNgay());
    }
}
