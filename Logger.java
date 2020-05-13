package flashcards;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Logger {

    private String path = "result.txt";
    private ArrayList<String> list = new ArrayList<String>();

    void log(String text){
        list.add(text);
    }

    void logAndPrintLn(String text){
        log(text + "\n");
        System.out.println(text);
    }

    void logAndPrint(String text){
        log(text);
        System.out.print(text);
    }

    void exportToFile(){
        try(FileWriter writer = new FileWriter(path, false)) {
            for (var el : list) writer.write(el);
            writer.flush();
            logAndPrintLn("The log has been saved.");
        } catch(IOException ex){
            logAndPrintLn("File not found.");
        }
    }

    void setPath(String path){
        this.path = path;
    }

}
