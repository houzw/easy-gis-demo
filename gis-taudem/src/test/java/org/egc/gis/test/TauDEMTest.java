package org.egc.gis.test;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.junit.Test;

import java.io.File;

/**
 * TODO
 *
 * @author houzhiwei
 * @date 2018/9/4 14:36
 */
public class TauDEMTest {
    @Test
    public void filenameTest() {
        String tif = "H:/grass-demos/demo1/01_DEM.tif";
        getFileExtension(tif);
        System.out.println(insert2Filename(tif,"slp"));
    }

    private String getFileExtension(String filename) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(filename), "Filename can not be null or empty");
        File file = new File(filename);
        String name = file.getName();
        String suffix = name.substring(name.lastIndexOf(".") + 1);
        return suffix;
    }


    private String insert2Filename(String filename, String insertStr) {
        StringBuilder sb = new StringBuilder(filename);
        sb.insert(filename.lastIndexOf(".") , "_" + insertStr);
        return sb.toString();
    }
}
