package com.georgejrdev;

import org.junit.Before;
import org.junit.Test;

import com.georgejrdev.auxiliar.json.KeyValueJson;
import com.georgejrdev.auxiliar.json.KeyValueJsonImpl;

import org.junit.After;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TestKeyValueJson {

    private KeyValueJson jsonImpl;
    private String testFilePath = "test.json";

    @Before
    public void setUp() {
        jsonImpl = new KeyValueJsonImpl(testFilePath);
    }

    @After
    public void tearDown() {
        File file = new File(testFilePath);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testCreateRegister() {
        Map<String, Object> newRegister = new HashMap<>();
        newRegister.put("name", "John Doe");
        newRegister.put("age", 30);
        jsonImpl.createRegister(newRegister);

        List<Map<String, Object>> content = jsonImpl.getContent();
        assertEquals(1, content.size());
        assertEquals("John Doe", content.get(0).get("name"));
        assertEquals(30, content.get(0).get("age"));
    }

    @Test
    public void testUpdateRegister() {
        Map<String, Object> newRegister = new HashMap<>();
        newRegister.put("name", "John Doe");
        newRegister.put("age", 30);
        jsonImpl.createRegister(newRegister);

        Map<String, Object> updatedRegister = new HashMap<>();
        updatedRegister.put("name", "Jane Doe");
        updatedRegister.put("age", 25);
        jsonImpl.updateRegister(0, updatedRegister);

        List<Map<String, Object>> content = jsonImpl.getContent();
        assertEquals(1, content.size());
        assertEquals("Jane Doe", content.get(0).get("name"));
        assertEquals(25, content.get(0).get("age"));
    }

    @Test
    public void testDeleteRegister() {
        Map<String, Object> register1 = new HashMap<>();
        register1.put("name", "John Doe");
        register1.put("age", 30);
        jsonImpl.createRegister(register1);

        Map<String, Object> register2 = new HashMap<>();
        register2.put("name", "Jane Doe");
        register2.put("age", 25);
        jsonImpl.createRegister(register2);

        jsonImpl.deleteRegister(0);

        List<Map<String, Object>> content = jsonImpl.getContent();
        assertEquals(1, content.size());
        assertEquals("Jane Doe", content.get(0).get("name"));
        assertEquals(25, content.get(0).get("age"));
    }

    @Test
    public void testGetContent() {
        Map<String, Object> register1 = new HashMap<>();
        register1.put("name", "John Doe");
        register1.put("age", 30);
        jsonImpl.createRegister(register1);

        Map<String, Object> register2 = new HashMap<>();
        register2.put("name", "Jane Doe");
        register2.put("age", 25);
        jsonImpl.createRegister(register2);

        List<Map<String, Object>> content = jsonImpl.getContent();
        assertEquals(2, content.size());
        assertEquals("John Doe", content.get(0).get("name"));
        assertEquals(30, content.get(0).get("age"));
        assertEquals("Jane Doe", content.get(1).get("name"));
        assertEquals(25, content.get(1).get("age"));
    }
}
