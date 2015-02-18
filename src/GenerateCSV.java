import java.io.FileWriter;
import java.io.IOException;

public class GenerateCSV
{
    public static void main(String [] args)
    {
        generateCsvFile("c:\\test.csv");
    }

    private static void generateCsvFile(String sFileName)
    {
        try
        {
            FileWriter writer = new FileWriter(sFileName);

            writer.append();
            writer.append(',');
            //writer.append("Age");
            //writer.append('\n');

            writer.flush();
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}