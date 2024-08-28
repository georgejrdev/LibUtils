package com.georgejrdev;

import org.junit.Before;
import org.junit.Test;

import com.georgejrdev.lib.reload.*;


public class HotReloadTest {
    
    private HotReload hotReload;

    @Before
    public void setUp(){
        this.hotReload = new HotReload("./src/test/resources/testFile.txt", "./src/test/resources/testFile.txt", 8080, 8081);
    }


    @Test
    public void testStart(){
        hotReload.start();
    }
}
