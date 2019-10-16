package org.egc.gis.crs.test;

import com.vividsolutions.jts.geom.Coordinate;
import org.egc.gis.crs.CoordinateTransformUtil;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Description:
 * <pre>
 *  https://stackoverflow.com/questions/30619092/coordinate-projection-with-geotools
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/10/7 15:08
 */
public class ByGeotools {
    String customWKT = "PROJCS[ \"UTM Zone 10, Northern Hemisphere\",\n" +
            "  GEOGCS[\"GRS 1980(IUGG, 1980)\",\n" +
            "    DATUM[\"unknown\"," +
            "       SPHEROID[\"GRS80\",6378137,298.257222101]," +
            "       TOWGS84[0,0,0,0,0,0,0]" +
            "    ],\n" +
            "    PRIMEM[\"Greenwich\",0],\n" +
            "    UNIT[\"degree\",0.0174532925199433]\n" +
            "  ],\n" +
            "  PROJECTION[\"Transverse_Mercator\"],\n" +
            "  PARAMETER[\"latitude_of_origin\",0],\n" +
            "  PARAMETER[\"central_meridian\",-123],\n" +
            "  PARAMETER[\"scale_factor\",0.9996],\n" +
            "  PARAMETER[\"false_easting\",1640419.947506562],\n" +
            "  PARAMETER[\"false_northing\",0],\n" +
            "  UNIT[\"Foot (International)\",0.3048]\n" +
            "]";

    @Test
    public void test() throws FactoryException, TransformException {
        CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326");
        CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:2227");
        boolean lenient = true; // allow for some error due to different datums
        MathTransform mathTransform = CRS.findMathTransform(sourceCRS, targetCRS, lenient);
        // x 竖轴，纬度
        // y 横轴，经度
        Coordinate source = new Coordinate(37.95657778d, -121.3128278d);
        Coordinate target = new Coordinate();

        JTS.transform(source, target, mathTransform);
        System.out.println(target.x);//6327319.22839835
        System.out.println(target.y);//2171792.148353282

//        Geometry targetGeometry = JTS.transform(sourceGeometry, transform);

    }

    @Test
    public void testCGCS() throws FactoryException {
//        CoordinateReferenceSystem crs = CRS.decode("EPSG:9122");//No code "EPSG:9122"/1024 from authority
        CoordinateReferenceSystem crs = CRS.decode("EPSG:21460");
        System.out.println(crs.toWKT());
    }

    //http://www.thinkgis.cn/topic/541999f9bcea5603e802d89b
    @Test
    public void testBj54ToWgs84() throws TransformException, FactoryException {

        Coordinate coord = CoordinateTransformUtil.fromBeijing54ToWGS84(115.1237245,37.34498051 );//?118.611065451529,40.4621006468135
        System.out.println(coord.x);
        System.out.println(coord.y);
        Assert.assertEquals(37.3449, coord.x, 0.2);

    }
}
