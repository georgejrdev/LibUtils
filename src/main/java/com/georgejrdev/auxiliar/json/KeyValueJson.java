package com.georgejrdev.auxiliar.json;

import java.util.List;
import java.util.Map;


public interface KeyValueJson {
    void createRegister(Map<String, Object> newRegister);
    void updateRegister(int index, Map<String, Object> updatedRegister);
    void deleteRegister(int index);
    List<Map<String, Object>> getContent();
}
