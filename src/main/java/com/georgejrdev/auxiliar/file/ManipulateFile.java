package com.georgejrdev.auxiliar.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ManipulateFile {

    public String getFileContent(String filePath) {
        Path path = Paths.get(filePath);
    
        try {
            return Files.readString(path);
    
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    public void createFile(String filePath, String name, String content) {
        Path path = Paths.get(filePath, name);
    
        try {
            Files.writeString(path, content, StandardOpenOption.CREATE_NEW);
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void writeFile(String filePath, String content) {
        Path path = Paths.get(filePath);
    
        try {
            Files.writeString(path, content, StandardOpenOption.WRITE);
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public boolean fileExist(String filePath) {
        Path path = Paths.get(filePath);
        return Files.exists(path);
    }


    public void deleteFile(String filePath) {
        Path path = Paths.get(filePath);
 
        try {
            Files.delete(path);
 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}