package org.egc.gis.ows.test;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.util.List;

public class ColorRampTest {
    @Test
    public void color() throws IOException {
        URL resource = this.getClass().getClassLoader().getResource("color_ramp.txt");
        System.out.println(resource.getFile().toString());
        List<String> strings = FileUtils.readLines(new File(resource.getFile().toString()));
        for (String s : strings) {
            System.out.println(s);
        }
    }
}
