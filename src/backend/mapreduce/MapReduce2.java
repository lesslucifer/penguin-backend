/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.mapreduce;

import backend.handle.Category;
import backend.record.ThuChiRecord;
import backend.record.LogRecord;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Salm
 */
public class MapReduce2 {
    public static void main(String[] args)
    {
        List<LogRecord> recs = new ArrayList();
        recs.stream()
            .filter((r) -> r.getCategory() == Category.CHI_XU)
            .map((r) -> (ThuChiRecord) r)
            .map((r) -> r.getSource())
            .distinct()
            .forEach((s) -> {
                recs.stream()
                    .filter((r) -> r.getCategory() == Category.CHI_XU)
                    .map((r) -> (ThuChiRecord) r)
                    .filter((r) -> r.getSource() == s)
                    .mapToInt((r) -> r.getInc())
                    .reduce(Integer::sum);
            });
        
        recs.stream()
            .filter((r) -> r.getCategory() == Category.CHI_XU)
            .map((r) -> (ThuChiRecord) r)
            .collect(Collectors.toMap(
                    (r) -> r.getSource(),
                    (r) -> r.getInc(),
                    Integer::sum,
                    HashMap::new));
    }
}
