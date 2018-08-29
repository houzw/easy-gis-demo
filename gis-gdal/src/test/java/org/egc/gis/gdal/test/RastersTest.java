package org.egc.gis.gdal.test;

import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.junit.Test;

/**
 * TODO
 *
 * @author houzhiwei
 * @date 2017/8/24 22:37
 */
public class RastersTest {

    String tif = "H:/grass-demos/demo1/01_DEM.tif";

    @Test
    public void testRasterGdal() {
        gdal.AllRegister();
        Dataset ds = gdal.Open(tif);
        System.out.println(ds.GetDriver().getLongName());
        System.out.println("x " + ds.GetRasterXSize());
        System.out.println("y " + ds.GetRasterYSize());
        System.out.println("proj " + ds.GetProjection());
        ds.GetGeoTransform();
        ds.delete();
    }
}
