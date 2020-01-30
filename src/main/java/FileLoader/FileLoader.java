package FileLoader;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileLoader {

    private static FileLoader instance;
    private final String projectpath = System.getProperty("user.dir")+File.separator+"Data";

    private FileLoader() {
        System.out.println("Project-Path: "+projectpath);
    }

    public static FileLoader getInstance() {
        if (FileLoader.instance == null) {
            FileLoader.instance = new FileLoader();
        }
        return FileLoader.instance;
    }

    private File loadFile(String fileName){
        File file = new File(projectpath+File.separator+fileName);
        if(file.exists()) return file;
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
            writeFile(fileName,"{}");
            return file;
        }catch (IOException i){i.printStackTrace();}
        return null;
    }

    public String readFile(String fileName){
        File file = loadFile(fileName);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    public void writeFile(String fileName, String content){
        try {
            File file = loadFile(fileName);
            BufferedWriter writter = new BufferedWriter(new FileWriter((file)));
            writter.write(content);
            writter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



