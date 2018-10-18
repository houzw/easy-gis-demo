package org.egc.gis.gdal.proj;

import org.gdal.osr.SpatialReference;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/10/4 9:58
 */
public class WktAndProj4 {
    String wkt = "PROJCS[\"Beijing 1954 / 3-degree Gauss-Kruger CM 117E\",GEOGCS[\"Beijing 1954\",DATUM[\"Beijing_1954\",SPHEROID[\"Krassowsky 1940\",6378245,298.3,AUTHORITY[\"EPSG\",\"7024\"]],TOWGS84[15.8,-154.4,-82.3,0,0,0,0],AUTHORITY[\"EPSG\",\"6214\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4214\"]],PROJECTION[\"Transverse_Mercator\"],PARAMETER[\"latitude_of_origin\",0],PARAMETER[\"central_meridian\",117],PARAMETER[\"scale_factor\",1],PARAMETER[\"false_easting\",500000],PARAMETER[\"false_northing\",0],UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],AUTHORITY[\"EPSG\",\"2436\"]]";
    String proj = "+proj=tmerc +lat_0=0 +lon_0=117 +k=1 +x_0=500000 +y_0=0 +ellps=krass +towgs84=15.8,-154.4,-82.3,0,0,0,0 +units=m +no_defs";


    public String wkt2proj(String wkt) {
        SpatialReference srs = new SpatialReference();
        srs.ImportFromWkt(wkt);
        return srs.ExportToProj4();
    }

    public String proj2wkt(String proj) {
        SpatialReference srs = new SpatialReference();
        srs.ImportFromProj4(proj);
        return srs.ExportToWkt();
    }
}
