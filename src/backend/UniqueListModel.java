/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractListModel;

/**
 *
 * @author Salm
 */
public class UniqueListModel extends AbstractListModel<String> {
    private final List<String> data = new ArrayList<>();
    
    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public String getElementAt(int index) {
        return data.get(index);
    }
    
    public void add(String elem)
    {
        if (!data.stream().anyMatch((s) -> {return s.equals(elem);}))
        {
            this.data.add(elem);
            fireIntervalAdded(elem, this.data.size() - 1, this.data.size());
        }
    }
    
    public void clear()
    {
        int ll = this.data.size();
        this.data.clear();
        this.fireContentsChanged(this, 0, ll);
    }
    
    public List<String> getAll()
    {
        return Collections.unmodifiableList(this.data);
    }
}
