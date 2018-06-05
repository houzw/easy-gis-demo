package org.egc.gis.gdal.raster.format;

import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.osr.SpatialReference;

/**
 * 格式转换
 *
 * @author houzhiwei
 * @date 2017/9/29 20:32
 */
public class Conversion {

    public StatusCode asc2GeoTIFF(String asc, int epsg, String tiff) {
        Driver dr = gdal.GetDriverByName(FormatConsts.GEO_TIFF);
        Dataset ds = gdal.Open(asc);
        Dataset out_ds = dr.CreateCopy(tiff, ds);
        SpatialReference srs = new SpatialReference();
        srs.ImportFromEPSG(epsg);
        out_ds.SetProjection(srs.ExportToWkt());
        return StatusCode.SUCCESS;
    }
}
