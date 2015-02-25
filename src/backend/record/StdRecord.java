/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.record;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author Salm
 */
public class StdRecord extends LogRecord {
    public static final DateTimeFormatter DT_FMT = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss");
    
    private DateTime time;
    private final String uid;
    private final int level;
    private final int gold;
    private final int coin;

    public StdRecord(LogRecord rec) {
        super(rec);
        this.uid = this.getRawData(1);
        this.level = Integer.parseInt(this.getRawData(2));
        this.gold = Integer.parseInt(this.getRawData(3));
        this.coin = Integer.parseInt(this.getRawData(4));
    }

    public DateTime getTime() {
        synchronized (this)
        {
            if (time == null)
            {
                this.time = DT_FMT.parseDateTime(this.getRawData(0));
            }
        }
        
        return time;
    }
    
    public String getDay()
    {
        return this.getRawData(0).substring(0, 10);
    }

    public String getUid() {
        return uid;
    }

    public int getLevel() {
        return level;
    }

    public int getGold() {
        return gold;
    }

    public int getCoin() {
        return coin;
    }
    
    public String getInfo(int index)
    {
        return this.getRawData(index + 5);
    }
    
    public int getInfoLength()
    {
        return this.getRawDataLength() - 5;
    }

    @Override
    public Object getData(String dat) {
        Object ret = super.getData(dat);
        if (ret != null)
            return ret;
        
        if ("uid".equals(dat))
            return uid;
        else if ("level".equals(dat))
            return level;
        else if ("gold".equals(dat))
            return gold;
        else if ("coin".equals(dat))
            return coin;
        else if ("time".equals(dat))
            return this.getRawData(0);
        else if ("date".equals(dat))
            return getDay();
        else if (dat.startsWith("info"))
        {
            return this.getInfo(Integer.parseInt(dat.substring(4)));
        }
        
        return null;
    }
}
