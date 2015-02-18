/**
 * Created by Ole-Martin on 18.02.2015.
 */

//Må berre teste :)
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class writer {

    public void writing() {

        try {
            //Whatever the file path is.
            File statText = new File("C:/Users/Ole-Martin/Desktop/statsTest.txt");
            FileOutputStream is = new FileOutputStream(statText);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer w = new BufferedWriter(osw);
            w.write("POTATO!!!\n");
            w.close();
        } catch (IOException e) {
            System.err.println("Problem writing to the file statsTest.txt");
        }
    }

    public static void main(String[]args) {
        writer write = new writer();
        write.writing();
    }



}
