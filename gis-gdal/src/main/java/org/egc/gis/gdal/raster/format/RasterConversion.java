package org.egc.gis.gdal.raster.format;

import lombok.extern.slf4j.Slf4j;
import org.egc.gis.commons.GDALDriversEnum;
import org.egc.gis.commons.StatusCode;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;
import org.gdal.osr.SpatialReference;

/**
 * 格式转换
 *
 * @author houzhiwei
 * @date 2017 /9/29 20:32
 */
@Slf4j
public class RasterConversion {

    /**
     * TODO test
     * asc (Arc/Info ASCII Grid) to geo tiff
     *
     * @param asc        the asc
     * @param targetEpsg the target epsg code
     * @param tiff       the tiff
     * @return status code
     */
    public static StatusCode asc2GeoTIFF(String asc, String tiff, Integer targetEpsg) {
        return raster2Geotiff(asc, tiff, targetEpsg);
    }

    /**
     * Adf (Arc/Info Binary Grid) to geo tiff .
     *
     * @param adf        the source adf file
     * @param targetEpsg the target epsg code
     * @param tiff       the target tiff file
     * @return the status code
     */
    public static StatusCode adf2GeoTIFF(String adf, String tiff, Integer targetEpsg) {
        return raster2Geotiff(adf, tiff, targetEpsg);
    }

    /**
     * Raster to geotiff .
     *
     * @param fromFile   the from file
     * @param targetEpsg the target epsg, if null, same as fromFile
     * @param toFile     the to file
     * @return the status code
     */
    public static StatusCode raster2Geotiff(String fromFile, String toFile, Integer targetEpsg) {
        gdal.AllRegister();
        Dataset src = gdal.Open(fromFile, gdalconst.GA_ReadOnly);
        Driver driver = gdal.GetDriverByName(GDALDriversEnum.GTiff.name());
        Dataset out_ds = driver.CreateCopy(toFile, src);
        SpatialReference srs = new SpatialReference();
        if (targetEpsg != null) {
            srs.ImportFromEPSG(targetEpsg);
        } else {
            SpatialReference sr = new SpatialReference(src.GetProjectionRef());
            srs.ImportFromWkt(sr.ExportToWkt());
        }
        out_ds.SetProjection(srs.ExportToWkt());

        driver.delete();
        src.delete();
        out_ds.delete();
        log.debug("转换成功");
        return StatusCode.SUCCESS;
    }
}
