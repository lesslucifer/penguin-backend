/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.mapreduce.mr;

import backend.handle.Category;
import backend.mapreduce.MROpFactory;
import backend.mapreduce.MapFunction;
import backend.mapreduce.MapReduceOperation;
import backend.mapreduce.SumReducer;
import backend.record.ThuChiRecord;
import backend.record.LogRecord;
import backend.record.UserAction;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author Salm
 */
public class ChiXuTheoNguon extends MapReduceOperation<UserAction, Integer, Integer> {
    
    private ChiXuTheoNguon(List<File> files)
    {
        super(Category.CHI_XU, files,
        (recs) -> {
            List<Pair<UserAction, Integer>> data = new LinkedList();
            recs.forEach((rec) -> {
                if (!(rec instanceof ThuChiRecord))
                    return;
                
                ThuChiRecord r = (ThuChiRecord) rec;
                data.add(new Pair(r.getSource(), r.getChange()));
            });
            
            return data;
        }, SumReducer.FACTORY);
    }
    
    public static MROpFactory getFactory()
    {
        return new MROpFactory() {
            @Override
            public MapReduceOperation<?, ?, ?> createOp(List<File> files) {
                return new ChiXuTheoNguon(files);
            }

            @Override
            public String getName() {
                return "ChiXuTheoNguon";
            }
        };
    }
    
    public static class Mapper implements MapFunction<UserAction, Integer>
    {
        @Override
        public List<Pair<UserAction, Integer>> map(List<LogRecord> recs) {
            List<Pair<UserAction, Integer>> data = new LinkedList();
            recs.forEach((rec) -> {
                if (!(rec instanceof ThuChiRecord))
                    return;
                
                ThuChiRecord r = (ThuChiRecord) rec;
                data.add(new Pair(r.getSource(), r.getInc()));
            });
            
            return data;
        }
    }
}
