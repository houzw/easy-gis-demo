package org.egc.gis.gdal.raster.format;

import lombok.extern.slf4j.Slf4j;
import org.egc.gis.commons.GdalDrivers;
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
 * @date 2017/9/29 20:32
 */
@Slf4j
public class Conversion {

    /**
     * TODO test
     *
     * @param asc
     * @param epsg
     * @param tiff
     * @return
     */
    public StatusCode asc2GeoTIFF(String asc, int epsg, String tiff) {
        return raster2Geotiff(asc, epsg, tiff);
    }

    public StatusCode adf2GeoTIFF(String adf, Integer epsg, String tiff) {
        return raster2Geotiff(adf, epsg, tiff);
    }

    private StatusCode raster2Geotiff(String fromFile, Integer epsg, String toFile) {
        gdal.AllRegister();
        Dataset src = gdal.Open(fromFile, gdalconst.GA_ReadOnly);
        Driver driver = gdal.GetDriverByName(GdalDrivers.GTiff.name());
        Dataset out_ds = driver.CreateCopy(toFile, src);
        SpatialReference srs = new SpatialReference();
        if (epsg != null) {
            srs.ImportFromEPSG(epsg);
            out_ds.SetProjection(srs.ExportToWkt());
        }
        driver.delete();
        src.delete();
        out_ds.delete();
        log.debug("转换成功");
        return StatusCode.SUCCESS;
    }
}
