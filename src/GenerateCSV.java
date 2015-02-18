import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GenerateCSV
{
    ArrayList<String> out;
    FieldStats stats;

    public GenerateCSV() {
        out = new ArrayList<String>();
        out.add("Zombie,Human\n");
        stats = new FieldStats();
    }

    public void savePopulationCount(Field field){
        stats.reset();
        out.add(stats.getPopulationCount(field,Zombie.class)+","+stats.getPopulationCount(field,Human.class)+"\n");
    }

    public void generateCsvFile(String sFileName)
    {
        try
        {
            FileWriter writer = new FileWriter(sFileName);

            for(String line:out){
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