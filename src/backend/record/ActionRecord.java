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
public class ActionRecord extends StdRecord {
    private final String action;

    public ActionRecord(LogRecord rec) {
        super(rec);
        this.action = this.getInfo(0);
    }

    public UserAction getAction() {
        return UserAction.valueOf(action);
    }
    
    public String getParam(int idx)
    {
        return this.getInfo(idx + 1);
    }
    
    public int getParamLength()
    {
        return this.getInfoLength() - 1;
    }

    @Override
    public Object getData(String dat) {
        Object ret = super.getData(dat);
        if (ret != null)
            return ret;
        
        if ("act".equals(dat) || "action".equals(dat))
            return action;
        
        if (dat.startsWith("par"))
        {
            return this.getInfo(Integer.parseInt(dat.substring(3)));
        }
        else if (dat.startsWith("param"))
        {
            return this.getInfo(Integer.parseInt(dat.substring(5)));
        }
        
        return null;
    }
}
