package org.egc.gis.gdal.test;

import org.egc.gis.commons.GDALDriversEnum;
import org.egc.gis.commons.StatusCode;
import org.egc.gis.gdal.raster.RasterUtil;
import org.egc.gis.gdal.raster.format.RasterConversion;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.InfoOptions;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Driver;
import org.gdal.ogr.ogr;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/10/10 14:19
 */
public class FormatConvert {
    String shp = "H:\\gisdemo\\in\\UserWatersheds.shp";
    String json = "H:\\gisdemo\\out\\UserWatersheds.json";
    String kml = "H:\\gisdemo\\out\\UserWatersheds.kml";

    String tif = "H:\\gisdemo\\in\\xcDEM.tif";
    String jpg = "H:\\gisdemo\\out\\xcDEM.jpg";
    File file = new File("src/test/resources/sourcedem");
    String outputDir = "H:/gisdemo/out";
    String inputDir = "H:/gisdemo/in";
    String adf = inputDir + "/adf/hdr.adf";
    String path = file.getAbsolutePath();

    //adf2tiff
    @Test
    public void adf2tiff() {
        StatusCode statusCode = RasterConversion.adf2GeoTIFF(adf, outputDir + "/adf.tif", 4326);
        System.out.println(statusCode);
    }
    @Test
    public void format() throws IOException {
       RasterUtil.formatConvert(adf, outputDir + "/adf.tif");
    }
    @Test
    public void info() {
        gdal.AllRegister();
        Dataset open = gdal.Open(tif, gdalconst.GA_ReadOnly);
        //https://gdal.org/programs/gdalinfo.html#gdalinfo
        //注意，与Python版赋值方式不同
        Vector<String> opVec = new Vector<String>();
        opVec.add("-proj4");//reportProj4 = False in python
        opVec.add("-stats");
        InfoOptions options = new InfoOptions(opVec);
        String s = gdal.GDALInfo(open, options);
        System.out.println(s);
    }

    //asc2tiff
    @Test
    public void asc2tiff() {
        StatusCode statusCode = RasterConversion.asc2GeoTIFF(path + "/asc/twi.asc", path + "/twi.tif", 4326);
        System.out.println(statusCode);
    }

    @Test
    public void toJpg() {
        gdal.AllRegister();
        //设置中文
        gdal.SetConfigOption("gdal_FILENAME_IS_UTF8", "YES");
        Dataset ds = gdal.Open(tif, gdalconst.GA_ReadOnly);
        org.gdal.gdal.Driver driver = ds.GetDriver();
        driver.CreateCopy(jpg, ds);
        ds.delete();
        driver.delete();
        System.out.println("success");
    }

    //shp2geojson
    // 原文：https://blog.csdn.net/wk1134314305/article/details/76696333?utm_source=copy
    @Test
    public void toGeoJSON() {
        // 注册所有的驱动
        ogr.RegisterAll();
        // 为了支持中文路径，请添加下面这句代码
        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");
        // 为了使属性表字段支持中文，请添加下面这句
        gdal.SetConfigOption("SHAPE_ENCODING", "");

        // 打开文件
        DataSource ds = ogr.Open(shp, 0);
        Driver dv = ogr.GetDriverByName(GDALDriversEnum.GeoJSON.name());
        if (dv == null) {
            System.out.println("打开驱动失败！");
            return;
        }
        DataSource out = dv.CopyDataSource(ds, json);
        ds.delete();
        out.delete();
        dv.delete();
        System.out.println("转换成功！");
    }

    @Test
    public void toKML() {
        // 注册所有的驱动
        ogr.RegisterAll();
        // 为了支持中文路径，请添加下面这句代码
        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");
        // 为了使属性表字段支持中文，请添加下面这句
        gdal.SetConfigOption("SHAPE_ENCODING", "");

        // 打开文件
        DataSource ds = ogr.Open(shp, 0);
        Driver dv = ogr.GetDriverByName(GDALDriversEnum.KML.name());
        if (dv == null) {
            System.out.println("打开驱动失败！");
            return;
        }
        DataSource out = dv.CopyDataSource(ds, kml);
        ds.delete();
        out.delete();
        dv.delete();
        System.out.println("转换成功！");
    }

}
