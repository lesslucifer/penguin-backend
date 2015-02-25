/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Salm
 */
public class Config {
    public static int CONCURRENT_LEVEL = 1;
    public static String LAST_DIR = "E:/PLOG/LOG";
    public static String LAST_Q_DIR = "E:/PLOG/LOG/QUERIES";
    
    public static void defaultConfigs()
    {
        CONCURRENT_LEVEL = 1;
        LAST_DIR = "E:/PLOG/LOG";
        LAST_Q_DIR = "E:/PLOG/LOG/QUERIES";
    }
    
    public static void loadConfigs()
    {
        File confFile = new File("conf.ini");
        if (!confFile.exists())
            return;
        
        try
        {
            JSONObject conf = (JSONObject) JSONValue.parse(new FileReader(confFile));
            LAST_DIR = (String) conf.get("last_log_dir");
            LAST_Q_DIR = (String) conf.get("last_query_dir");
            CONCURRENT_LEVEL = ((Number) conf.get("thread")).intValue();
        }
        catch (Exception ex)
        {
            // ignore
            defaultConfigs();
        }
    }
    
    public static void saveConfigs()
    {
        File confFile = new File("conf.ini");
        if (confFile.exists())
            confFile.delete();
        
        try
        {
            JSONObject conf = new JSONObject();
            conf.put("last_log_dir", LAST_DIR);
            conf.put("last_query_dir", LAST_Q_DIR);
            conf.put("thread", CONCURRENT_LEVEL);
            Files.write(confFile.toPath(),
                    JSONValue.toJSONString(conf).getBytes());
        }
        catch (Exception ex)
        {
            // ignore
        }
    }
}
