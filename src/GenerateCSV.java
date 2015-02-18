import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GenerateCSV
{
    ArrayList<String> out;
    FieldStats stats;

    public GenerateCSV() {
        out = new ArrayList<String>();
        stats = new FieldStats();
    }

    public void savePopulationCount(Field field){
        stats.reset();
//        for(int row = 0; row < field.getDepth(); row++) {
//            for(int col = 0; col < field.getWidth(); col++) {
//                Object humanoid = field.getObjectAt(row, col);
//                if(humanoid != null) {
//                    stats.incrementCount(humanoid.getClass());
//                }
//            }
//        }
//        stats.countFinished();
        out.add(stats.getPopulationDetails(field)+"\n");
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