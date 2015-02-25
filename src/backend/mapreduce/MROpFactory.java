/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.mapreduce;

import java.io.File;
import java.util.List;

/**
 *
 * @author Salm
 */
public interface MROpFactory {
    MapReduceOperation<?, ?, ?> createOp(List<File> files);
    default String getName()
    {
        return this.getClass().getSimpleName();
    }
}
