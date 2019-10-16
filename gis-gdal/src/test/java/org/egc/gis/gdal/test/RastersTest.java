package org.egc.gis.gdal.test;

import com.alibaba.fastjson.JSON;
import org.egc.gis.gdal.raster.RasterUtil;
import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;
import org.gdal.osr.SpatialReference;
import org.junit.Test;

/**
 * TODO
 *
 * @author houzhiwei
 * @date 2017/8/24 22:37
 */
public class RastersTest {

    String tif = "H:\\gisdemo\\in\\dem_TX.tif";
    String tif2 = "H:\\gisdemo\\in\\clay2.tif";
    String tif3 = "H:\\gisdemo\\in\\xcDEM.tif";

    @Test
    public void testRasterGdal() {
        gdal.AllRegister();
        Dataset ds = gdal.Open(tif, gdalconst.GA_ReadOnly);
        print(ds.GetDriver().getLongName());
        print("x " + ds.GetRasterXSize());
        print("y " + ds.GetRasterYSize());
        print("proj " + ds.GetProjection());
        double[] trans = ds.GetGeoTransform();
        print(trans.toString());
        print(ds.getRasterCount());//1
        SpatialReference srs = new SpatialReference(ds.GetProjectionRef());
        print(srs.GetAuthorityName(null));//epsg
        print(srs.GetAuthorityCode(null));//epsg
        print(ds.GetDescription());//H:/grass-demos/demo1/01_DEM.tif
//        print(ds.GetLayerCount());//0
        print(ds.GetGCPs());//[]
        print(ds.GetMetadata_List("IMAGE_STRUCTURE"));//[COMPRESSION=LZW, INTERLEAVE=BAND]
        print(trans[1]);//8.333333333333375E-4
        print(trans[5]);
        Band band_0 = ds.GetRasterBand(1);
        Double[] nodataval = new Double[1];
        band_0.GetNoDataValue(nodataval);
        band_0.ComputeStatistics(false);
        print(nodataval[0]);//no data value if available
        Double[] max = new Double[1];
        band_0.GetMaximum(max);
        print(max[0]);//592
        double[] min = new double[]{0};
        double[] maxd = new double[]{0};
        double[] meand = new double[]{0};
        double[] stddevd = new double[]{0};
        band_0.GetStatistics(false, true, min, maxd, meand, stddevd);
        print(min[0]);//186.0
        print(maxd[0]);//592
        print(meand[0]);//371.21
        print(stddevd[0]);//60.67.

        ds.delete();
    }

    @Test
    public void testBJ54() {

        gdal.AllRegister();
        Dataset ds = gdal.Open(tif3, gdalconst.GA_ReadOnly);
        SpatialReference reference = new SpatialReference(ds.GetProjectionRef());
        System.out.println(reference.ExportToPrettyWkt());
        System.out.println(reference.IsGeographic());
        System.out.println(reference.IsProjected());
        System.out.println(reference.IsGeocentric());
        System.out.println(reference.GetAuthorityCode(null));
        print(reference.GetAttrValue("AUTHORITY",1));
        print(reference.GetAttrValue("PROJCS"));
        print(reference.GetAttrValue("GEOGCS"));
        print(reference.GetLinearUnitsName());
    }

    @Test
    public void testMetagdal() {
        print(RasterUtil.getMetadataByGDAL(tif));
//        GeoTiffUtil.getMetadataByGDAL(tif2);
    }

    private void print(Object o) {
        String json = JSON.toJSONString(o, true);
        System.out.println(json);
    }

    //https://www.linkedin.com/pulse/reprojecting-geotiff-images-using-easy-way-chonghua-yin
    @Test
    public void testReproject() {
        gdal.AllRegister();
        SpatialReference toSR = new SpatialReference();
        toSR.SetWellKnownGeogCS("WGS84");//"WGS84""WGS72" "NAD27" "NAD83" "EPSG:n"
        Dataset ds = gdal.Open(tif, gdalconst.GA_ReadOnly);
//        SpatialReference fromSR = new SpatialReference(ds.GetProjectionRef());
        // 最后参数：插值方法
        // 第二个参数可以为null
        Dataset vrds = gdal.AutoCreateWarpedVRT(ds, null, toSR.ExportToWkt(), gdalconst.GRA_Bilinear);
        String outfile = "H:\\dem_TX_WGS84.tif";
        gdal.GetDriverByName("GTiff").CreateCopy(outfile, vrds);
        ds.delete();
        Dataset newds = gdal.Open(outfile);
        System.out.println(new SpatialReference(newds.GetProjectionRef()).ExportToPrettyWkt());
        newds.GetRasterBand(1).ComputeStatistics(false);//ERROR 1: H:\dem_TX_WGS84.tif, band 1: Failed to compute statistics, no valid pixels found in sampling.
        newds.delete();
    }

    //https://gis.stackexchange.com/questions/224380/how-to-get-a-list-of-supported-gdal-formats-within-python
    @Test
    public void testDriver() {

        //gdalinfo --formats
        int count = gdal.GetDriverCount();//0
        for (int i = 0; i < count; i++) {
            Driver driver = gdal.GetDriver(i);
            System.out.println(driver.getShortName());
            System.out.println(driver.GetDescription());
        }

    }

}
