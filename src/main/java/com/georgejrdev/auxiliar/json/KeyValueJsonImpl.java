package com.georgejrdev.auxiliar.json;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.InvalidKeyException;
import java.util.ArrayList;


public class KeyValueJsonImpl implements KeyValueJson {

    private String path;
    @SuppressWarnings("unused")
    private boolean fileExist;
    private List<Map<String, Object>> content;

    public KeyValueJsonImpl(String path) {
        this.path = path;
        this.fileExist = false;
        this.content = new ArrayList<>();

        createFile();
        loadContent();  
    }


    @Override
    public void createRegister(Map<String, Object> newRegister) {
        content.add(newRegister);
        saveContent(); 
    }


    @Override
    public List<Map<String, Object>> getContent() {
        return content;
    }


    @Override
    public void updateRegister(int index, Map<String, Object> updatedRegister) {
        if (index >= 0 && index < content.size()) {
            content.set(index, updatedRegister);
            saveContent(); 

        } else {
            try {
                throw new InvalidKeyException("Invalid index for update.");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    
    @Override
    public void deleteRegister(int index) {
        if (index >= 0 && index < content.size()) {
            content.remove(index);
            saveContent();

        } else {
            try {
                throw new InvalidKeyException("Invalid index for delete.");
                
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
        }
    }


    private void createFile() {
        File file = new File(this.path);

        if (!file.exists()) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String emptyJsonContent = gson.toJson(new Object[]{});

            try (FileWriter writer = new FileWriter(this.path)) {
                writer.write(emptyJsonContent);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.fileExist = true;
    }


    private void loadContent() {
        File file = new File(this.path);

        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
                
                this.content = gson.fromJson(reader, listType);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void saveContent() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        try (FileWriter writer = new FileWriter(this.path)) {
            String jsonContent = gson.toJson(this.content);
            writer.write(jsonContent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}