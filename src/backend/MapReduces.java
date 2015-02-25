/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import backend.mapreduce.MROpFactory;
import backend.mapreduce.MapFunction;
import backend.mapreduce.MapReduceOperation;
import backend.mapreduce.ReduceFactory;
import backend.mapreduce.mr.ChiXuTheoNguon;
import backend.mapreduce.mr.DemSoUserNangCapTramCanh;
import backend.mapreduce.mr.SoLanTraEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Salm
 */
public class MapReduces {
    private static final List<MROpFactory> ops = new ArrayList();
    
    public static MROpFactory getOperation(int idx) {return ops.get(idx);}
    
    public static List<MROpFactory> getAllOperations()
    {
        return Collections.unmodifiableList((List) ops);
    }
    
    static
    {
        ops.add(SoLanTraEvent.DemSoLanTra());
        ops.add(SoLanTraEvent.DemSoUserTra());
        ops.add(DemSoUserNangCapTramCanh.DemSoUserNangCapTramCanh());
        ops.add(ChiXuTheoNguon.getFactory());
    }
}
