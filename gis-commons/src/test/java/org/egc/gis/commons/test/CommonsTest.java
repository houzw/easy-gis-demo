package org.egc.gis.commons.test;

import org.egc.gis.commons.GDALDriversEnum;
import org.junit.Test;

public class CommonsTest {
    @Test
    public void getname(){
        GDALDriversEnum getEnum = GDALDriversEnum.lookupByExtension("tif");
        System.out.println(getEnum.name());
    }
}
