/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.record;

/**
 *
 * @author Salm
 */
public class ThuChiRecord extends StdRecord {
    private final int chg;
    private final UserAction source;

    public ThuChiRecord(LogRecord rec) {
        super(rec);
        this.chg = Integer.parseInt(this.getInfo(0));
        this.source = UserAction.valueOf(this.getInfo(1));
    }

    public int getChange() {
        return chg;
    }

    public int getInc() {
        return chg;
    }

    public int getDec() {
        return chg;
    }

    public UserAction getSource() {
        return source;
    }

    @Override
    public Object getData(String dat) {
        Object ret = super.getData(dat);
        if (ret != null)
            return ret;
        
        switch (dat)
        {
            case "inc":
            case "dec":
            case "chg":
                return this.chg;
            case "source":
            case "src":
                return this.source.toString();
        }
        
        return null;
    }
}
