/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.record;

import backend.expr.ExprContext;
import backend.handle.Category;

/**
 *
 * @author Salm
 */
public class LogRecord implements ExprContext {
    private final Category category;
    private final String[] rawData;

    LogRecord(Category category, String[] data) {
        this.category = category;
        this.rawData = data;
    }

    LogRecord(Category category, String log) {
        this.category = category;
        this.rawData = log.split("\t");
    }

    LogRecord(LogRecord rec) {
        this.category = rec.category;
        this.rawData = rec.rawData;
    }

    public Category getCategory() {
        return category;
    }

    protected String getRawData(int index) {
        return rawData[index];
    }
    
    protected int getRawDataLength()
    {
        return rawData.length;
    }
    
    public static LogRecord parse(Category ct, String log) throws Exception
    {
        LogRecord rec = new LogRecord(ct, log);
        switch (rec.getCategory())
        {
            case HANHDONG:
            case EVENT:
                return new ActionRecord(rec);
            case THU_EXP:
            case THU_XU:
            case CHI_XU:
            case THU_VANG:
            case CHI_VANG:
                return new ThuChiRecord(rec);
        }
        
        return new StdRecord(rec);
    }

    @Override
    public Object getData(String dat) {
        dat = dat.toLowerCase();
        if ("category".equals(dat) || "ct".equals(dat))
        {
            return this.category.toString();
        }
        
        if (dat.startsWith("raw"))
        {
            return this.getRawData(Integer.parseInt(dat.substring(4)));
        }
        
        return null;
    }
}
