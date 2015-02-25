/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author Salm
 */
public class FileGroup {
    public static void main(String []args) throws IOException
    {
        DateTimeFormatter templFMT = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTimeFormatter subDirFMT = DateTimeFormat.forPattern("yyMMdd");
        
        JFileChooser fChooser = new JFileChooser("E:\\PLOG\\LOG");
        fChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//        fChooser.setFileFilter(new FileNameExtensionFilter("LOG File",""));
        if (fChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
            return;
        
        File dir = fChooser.getSelectedFile();
        List<File> files = new LinkedList(Arrays.asList(dir.listFiles()));
        
        DateTime d = new DateTime(2014, 11, 20, 0, 0);
        DateTime now = DateTime.now();
        while (d.compareTo(now) <= 0)
        {
            String templ = templFMT.print(d);
            
            List<File> moved = new LinkedList();
            files.stream().filter((file) -> (file.getName().contains(templ))).forEach((file) -> {
                moved.add(file);
            });
            
            files.removeAll(moved);
            if (moved.size() > 0)
            {
                String subDir = dir.getAbsolutePath() + "\\" + subDirFMT.print(d);
                File subDirFile = new File(subDir);
                if (!subDirFile.exists())
                    Files.createDirectory(subDirFile.toPath());
                
                moved.stream().forEach((file) -> {
                    try {
                        File target = new File(subDir + "\\" + file.getName());
                        if (!target.exists())
                        {
                            Files.move(new File(dir.getAbsolutePath()+ "\\" + file.getName()).toPath(),
                                    target.toPath());
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(FileGroup.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
            
            d = d.plusDays(1);
        }
    }
}
