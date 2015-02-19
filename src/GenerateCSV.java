import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GenerateCSV
{
    static HashMap<String,ArrayList<String>> files = new HashMap<String,ArrayList<String>>();

    public static void fileAppendBuffer(String line,String sFileName){
        if(files.get(sFileName)==null){
            files.put(sFileName,new ArrayList<String>());
        }
        files.get(sFileName).add(line);
    }

    public static void generateCsvFiles()
    {
        for(String file:files.keySet()){
            try
            {
                FileWriter writer = new FileWriter(file);

                for(String line:files.get(file)){
                    writer.append(line);
                }

                writer.flush();
                writer.close();
                }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}