package org.egc.gis.gdal.proj;

import org.egc.gis.commons.Consts;
import org.egc.gis.commons.GdalDriversEnum;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;

/**
 * Description:
 * <pre>
 * 命令行方式参考
 * https://gdal.org/tutorials/osr_api_tut.html
 * https://www.geos.ed.ac.uk/~smudd/TopoTutorials/html/tutorial_raster_conversion.html
 * https://jgomezdans.github.io/gdal_notes/reprojection.html
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/8/12 14:55
 */
public class Reprojection {

    public static String reproject2Wgs84() {
        gdal.AllRegister();

        return null;
    }

    /**
     * get Universal Transverse Mercator (UTM) Zone
     *
     * @param centralLongitude the central longitude
     * @return the utm zone
     * @link http ://www.dmap.co.uk/utmworld.htm
     */
    public static int utmZone(double centralLongitude) {
        // Math.round() 四舍五入
        // Math.ceil() 向上取整
        return (int) Math.ceil((centralLongitude + 180) / 6);
    }

    /**
     * Determines if given latitude is northern for UTM
     *
     * @param centralLatitude the central latitude
     * @return the int
     */
    public static int isNorthern(double centralLatitude) {
        return centralLatitude > 0 ? 1 : 0;
    }

    /**
     * TODO test
     *
     * @param centralLon
     * @param centralLat
     * @return
     */
    public static SpatialReference getWGS84UTM(double centralLon, double centralLat) {
        SpatialReference utm = new SpatialReference();
        //Set geographic coordinate system to handle lat/lon
        utm.SetWellKnownGeogCS("WGS84");
        utm.SetUTM(utmZone(centralLon), isNorthern(centralLat));
        return utm;
    }

    public static double[] transformWGS84ToUTM(double lon, double lat) {
        SpatialReference utm = new SpatialReference();
        //Set geographic coordinate system to handle lat/lon
        utm.SetWellKnownGeogCS("WGS84");
        utm.SetUTM(utmZone(lon), isNorthern(lat));
        ////# Clone ONLY the geographic coordinate system
        SpatialReference wgs84 = utm.CloneGeogCS();
        CoordinateTransformation transformation = CoordinateTransformation.CreateCoordinateTransformation(wgs84, utm);
        //# returns easting, northing, altitude
        return transformation.TransformPoint(lon, lat);
    }

    public static double[] transformUTMToWGS84(double easting, double northing, int zone) {
        SpatialReference utm = new SpatialReference();
        //Set geographic coordinate system to handle lat/lon
        utm.SetWellKnownGeogCS("WGS84");
        utm.SetUTM(zone, northing > 0 ? 1 : 0);
        // Clone ONLY the geographic coordinate system
        SpatialReference wgs84 = utm.CloneGeogCS();
        //wgs84.SetDataAxisToSRSAxisMapping(gdalconst.OAMS_TRADITIONAL_GIS_ORDER);
        CoordinateTransformation transformation = CoordinateTransformation.CreateCoordinateTransformation(utm, wgs84);
        // returns lon,lat, altitude
        return transformation.TransformPoint(easting, northing, 0);
    }

    public int reproject(String srcFile, String dstFile, SpatialReference tSRS) {
        Driver driver = gdal.GetDriverByName(GdalDriversEnum.GTiff.name());
        driver.Register();
        Dataset src = gdal.Open(srcFile);
        String dstWKT = tSRS.ExportToWkt();
        //create new raster
        Dataset dst = gdal.GetDriverByName(GdalDriversEnum.GTiff.name()).CreateCopy(dstFile, src);
        dst.SetGeoTransform(src.GetGeoTransform());
        //perform re-projection
        return gdal.ReprojectImage(src, dst, src.GetProjection(), dstWKT);
    }

    public int reproject2Utm(String srcFile, String dstFile) {

        Driver driver = gdal.GetDriverByName(GdalDriversEnum.GTiff.name());
        driver.Register();
        Dataset src = gdal.Open(srcFile);
        SpatialReference sSRS = new SpatialReference(src.GetProjectionRef());
        if (sSRS.IsProjected() > 0) {
            String projcs = sSRS.GetAttrValue(Consts.ATTR_PROJCS);
            int s_utmZone = sSRS.GetUTMZone();
        } else if (sSRS.IsGeographic() > 0) {
            String geogcs = sSRS.GetAttrValue(Consts.ATTR_GEOGCS);
        }

        SpatialReference tSRS = new SpatialReference();
        tSRS.SetWellKnownGeogCS("WGS84");

        CoordinateTransformation transformation = CoordinateTransformation.CreateCoordinateTransformation(sSRS, tSRS);
        //是不是应该使用转换之后的坐标来判断带号？
        double[] gt = src.GetGeoTransform();
        int utmZone = utmZone(gt[0] + gt[1] * src.GetRasterXSize() / 2);
        int north = isNorthern(gt[3] + gt[5] * src.GetRasterYSize() / 2);


        String dstWKT = tSRS.ExportToWkt();
        //create new raster
        Dataset dst = gdal.GetDriverByName(GdalDriversEnum.GTiff.name()).CreateCopy(dstFile, src);
        dst.SetGeoTransform(gt);
        //perform re-projection
        return gdal.ReprojectImage(src, dst, src.GetProjection(), dstWKT);
    }

   /* int project(String srcFile, String dstFile, SpatialReference tSRS){
        Dataset ds = IOFactory.rasterIO().read(srcFile);
        String dstWKT = tSRS.ExportToWkt();
        // # error threshold --> use same value as in gdalwarp
        double error_threshold = 0.125;
        ds.GetGeoTransform();
        ds.GetProjection();
        int resampling = gdalconst.GRA_NearestNeighbour;
        // Call AutoCreateWarpedVRT() to fetch default values for target raster dimensions and geotransform
        // src_wkt : left to default value --> will use the one from source
        Dataset tmp_ds = gdal.AutoCreateWarpedVRT(ds, null, dstWKT, resampling, error_threshold);
        //perform re-projection
        //create new raster
        Dataset dst = gdal.GetDriverByName(GdalDriversEnum.GTiff.name()).CreateCopy(dstFile, ds);

    }*/
}
