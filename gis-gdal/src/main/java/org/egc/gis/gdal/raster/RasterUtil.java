package org.egc.gis.gdal.raster;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.egc.commons.util.StringUtil;
import org.egc.gis.commons.GDALDriversEnum;
import org.egc.gis.commons.RasterMetadata;
import org.egc.gis.commons.BoundingBox;
import org.gdal.gdal.*;
import org.gdal.gdalconst.gdalconst;
import org.gdal.gdalconst.gdalconstConstants;
import org.gdal.osr.SpatialReference;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Vector;

/**
 * Description:
 * <pre>
 * 可以参考 https://github.com/lreis2415/PyGeoC
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/9/19 21:35
 */
@Slf4j
public class RasterUtil {
    public static final String NODATA = "-9999";

    public RasterMetadata getRasterMetadata(String rasterFile) throws IOException {

        gdal.AllRegister();
        Dataset ds = gdal.Open(rasterFile, gdalconst.GA_ReadOnly);
        if (ds == null) {
            throw new IOException("Read raster file [ " + rasterFile + " ] failed!");
        }
        Driver driver = ds.GetDriver();
        String driverName = driver.GetDescription();
        //Long name of the driver.
        driver.GetMetadataItem(gdalconst.GDAL_DMD_LONGNAME);
        if (ds.GetProjectionRef() != null) {
            ds.GetProjectionRef();//proj
            String code = new SpatialReference(ds.GetProjectionRef()).GetAuthorityCode(null);
            System.out.println(code);
        }
        int width = ds.GetRasterXSize();
        int height = ds.getRasterYSize();
        double[] gt = ds.GetGeoTransform();
        //minx
        double topLeftX = gt[0];
        double westEastPixelResolution = gt[1];
        //maxy
        double topLeftY = gt[3];
        double northSouthResolution = gt[5];
        double maxX = topLeftX + gt[1] * ds.getRasterXSize();
        double miny = topLeftY + gt[5] * ds.getRasterYSize();

        double[] origin = new double[]{gt[0], gt[3]};
        double[] pixelSize = new double[]{gt[1], gt[5]};
        RasterMetadata metadata = new RasterMetadata();
        //Applies the following computation, converting a (pixel,line) coordinate into a georeferenced (geo_x,geo_y) location.
        gdal.ApplyGeoTransform(gt, 0d, 0d, null, null);

        Band band_0 = ds.GetRasterBand(0);
        band_0.GetNoDataValue(new Double[]{2d}); //?
        ds.delete();
        gdal.GDALDestroyDriverManager();
        //??
        return metadata;
    }

    /**
     * 利用gdal获取栅格数据元数据
     * 主要参考原GeoInfoExtraction类
     *
     * @param tif
     * @return
     */
    public static RasterMetadata getMetadataByGDAL(String tif) {
        StringUtil.isNullOrEmptyPrecondition(tif, "Raster file must exists");
        gdal.AllRegister();
        RasterMetadata metadata = new RasterMetadata();
        Dataset dataset = gdal.Open(tif, gdalconstConstants.GA_ReadOnly);
        Driver driver = dataset.GetDriver();
        metadata.setFormat(driver.getShortName());
        SpatialReference sr = new SpatialReference(dataset.GetProjectionRef());
        metadata.setCrsProj4(sr.ExportToProj4());
        metadata.setCrsWkt(sr.ExportToWkt());
        String authorityCode = sr.GetAuthorityCode(null);

        if (authorityCode != null) {
            Integer srid = Integer.parseInt(authorityCode);
            metadata.setSrid(srid);
        } else {
            String geogcs = sr.GetAttrValue("GEOGCS");
            String projcs = sr.GetAttrValue("PROJCS");
            if (Strings.isNullOrEmpty(projcs)) {
                metadata.setCrs(geogcs);
            } else {
                metadata.setCrs(projcs);
            }
        }
        Band band = dataset.GetRasterBand(1);
        Double[] nodataval = new Double[1];
        band.GetNoDataValue(nodataval);
        if (nodataval[0] != null) {
            metadata.setNodata(nodataval[0]);
        } else {
            metadata.setNodata(-9999d);
        }
        double[] min = new double[1], max = new double[1], stddev = new double[1], mean = new double[1];
        band.GetStatistics(true, true, min, max, mean, stddev);
        metadata.setMinValue(format(min[0]));
        metadata.setMaxValue(format(max[0]));
        metadata.setMeanValue(format(mean[0]));
        metadata.setSdev(format(stddev[0]));

        double[] gt = dataset.GetGeoTransform();
       /*
        adfGeoTransform[0] // top left x
        adfGeoTransform[1] // w-e pixel resolution
        adfGeoTransform[2] // 0
        adfGeoTransform[3] // top left y
        adfGeoTransform[4] // 0
        adfGeoTransform[5] // n-s pixel resolution (negative value)
        */
        metadata.setMinX(gt[0]);
        metadata.setMaxX(gt[0] + gt[1] * dataset.GetRasterXSize());
        metadata.setCenterX(format(gt[0] + gt[1] * (dataset.GetRasterXSize() / 2)));
        metadata.setMaxY(gt[3]);
        metadata.setMinY(format(gt[3] + gt[5] * dataset.GetRasterYSize()));
        metadata.setCenterY(format(gt[3] + gt[5] * (dataset.GetRasterYSize() / 2)));
        //gt[5]
        metadata.setPixelSize(format(gt[1]));
        metadata.setWidth(format(dataset.GetRasterXSize() * gt[1]));
        metadata.setHeight(format(dataset.GetRasterYSize() * gt[5]));
        metadata.setSizeHeight(dataset.GetRasterYSize());
        metadata.setSizeWidth(dataset.GetRasterXSize());

        dataset.delete();
        gdal.GDALDestroyDriverManager();
        return metadata;
    }

    private static double format(double d) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        // 5 位小数
        nf.setMaximumFractionDigits(5);
        // 四舍五入
        nf.setRoundingMode(RoundingMode.HALF_UP);
        // 是否分组，即每三位加逗号分隔
        nf.setGroupingUsed(false);
        return Double.parseDouble(nf.format(d));
    }

    // Generate mask from  given raster
    public static String generateMask(String tif) {
        return "";
    }

    public static String reclassify(String tif) {
        return "";
    }
    //https://gis.stackexchange.com/questions/45053/gdalwarp-cutline-along-with-shapefile

    public static String clip(String tif, String shp, String shpField) {

        return "";
    }

    /**
     * GDAL 栅格格式转换
     * @param in raster to be coverted
     * @param out output filename, with file extension
     * @throws IOException
     */
    public static void formatConvert(String in, String out) throws IOException {
        gdal.AllRegister();
        Dataset dataset = gdal.Open(in);

        GDALDriversEnum driversEnum = GDALDriversEnum.lookupByExtension(FilenameUtils.getExtension(out));
        Driver driver = gdal.GetDriverByName(driversEnum.name());
        if (driver == null) {
            throw new IOException("Output Format Not Supported");
        }
        Vector<String> vector = new Vector<String>();
        vector.add("-of");
        vector.add(driver.getShortName());
        TranslateOptions options = new TranslateOptions(vector);
        gdal.Translate(out, dataset, options);
        dataset.delete();
        gdal.GDALDestroyDriverManager();
    }

    //https://joeyklee.github.io/broc-cli-geo/guide/XX_raster_cropping_and_clipping.html
    public static void clip(String srcFile, String dstFile, BoundingBox bounds, Double expand) {
        Driver driver = gdal.GetDriverByName(GDALDriversEnum.GTiff.name());
        driver.Register();
        Dataset src = gdal.Open(srcFile);
        log.info("Running with " + gdal.VersionInfo());
        //version >= 2.1
        Vector<String> optionsVector = new Vector<>();
        optionsVector.add("-a_nodata");
        optionsVector.add(NODATA);
        optionsVector.add("-of");
        optionsVector.add(GDALDriversEnum.GTiff.name());
        optionsVector.add("-projwin");
        if (expand == null) {
            optionsVector.add(bounds.toString());
        } else {
            optionsVector.add(bounds.expand(expand).toString());
        }
        TranslateOptions options = new TranslateOptions(optionsVector);
        gdal.Translate(dstFile, src, options);
    }

    //二值化,大于等于阈值的重新赋值为1，其他为0
    public static String binarization(float threshold, String tif) {
        return "";
    }

    //形态学操作？
}
