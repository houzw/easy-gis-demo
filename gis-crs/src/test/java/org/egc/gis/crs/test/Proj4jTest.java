package org.egc.gis.crs.test;

import org.junit.Test;
import org.osgeo.proj4j.*;

/**
 * Description:
 * <pre>
 * https://github.com/jdeolive/proj4j/blob/master/src/test/java/org/osgeo/proj4j/ExampleTest.java
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/9/20 19:49
 */
public class Proj4jTest {
    @Test
    public void proj4j() {
        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        CRSFactory csFactory = new CRSFactory();

        CoordinateReferenceSystem crs = csFactory.createFromName("EPSG:2227");
        CoordinateReferenceSystem crs4326 = csFactory.createFromName("EPSG:4326");
        CoordinateReferenceSystem crsWGS84 = csFactory.createFromParameters("WGS84", "+proj=longlat +datum=WGS84 +no_defs");
        System.out.println(crs4326.getProjection().getPROJ4Description());
        System.out.println(crsWGS84.getProjection().getPROJ4Description());

        System.out.println(crs.getParameters()[0]);//+proj=lcc
        System.out.println(crs.getParameters()[1]);//+lat_1=38.43333333333333
        //+proj=lcc +a=6378137.0 +es=0.006694380022900787 +lon_0=-120d30 +lat_0=36d30 +x_0=2000000.0001016 +y_0=500000.0001016001 +fr_meters=3.2808333333333355
        System.out.println(crs.getProjection().getPROJ4Description());
        System.out.println(crs.getProjection().getEPSGCode());//0?
        System.out.println(crs.getDatum().getCode());//NAD83

        final String WGS84_PARAM = "+title=long/lat:WGS84 +proj=longlat +ellps=WGS84 +datum=WGS84 +units=degrees";
        CoordinateReferenceSystem WGS84 = csFactory.createFromParameters("WGS84", WGS84_PARAM);
        System.out.println(WGS84.getProjection().getEPSGCode());//0?
        //从3005转换到4326
        CoordinateTransform trans = ctFactory.createTransform(WGS84, crs);

        System.out.println(crs.getName());//EPSG:2227
        /*
         * Create input and output points.
         * These can be constructed once per thread and reused.
         */
        ProjCoordinate p = new ProjCoordinate();
        ProjCoordinate p2 = new ProjCoordinate();//target
        p.x = -121.3128278;//lon;,
        p.y = 37.95657778; //lat;

        /*
         * Transform point
         */
        trans.transform(p, p2);
        System.out.println(p2.x);
        System.out.println(p2.y);

    }

    //http://www.osgeo.cn/post/2427g
    //http://www.thinkgis.cn/topic/541999f9bcea5603e802d89b
    @Test
    public void testBeijing54towgs84() {
        CRSFactory csFactory = new CRSFactory();
        String params54 = "+proj=lcc +ellps=krass " +
                "+lat_1=25n +lat_2=47n +lon_0=117e " +
                "+x_0=20500000 +y_0=0 +units=m +k=1.0 " +
                "+towgs84=22,-118,30.5,0,0,0,0";
        CoordinateReferenceSystem beijing54 = csFactory.createFromParameters("Beijing54", params54);
        CoordinateReferenceSystem wgs84 = csFactory.createFromName("EPSG:4326");
        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        CoordinateTransform trans = ctFactory.createTransform(beijing54, wgs84);
        ProjCoordinate p = new ProjCoordinate();
        ProjCoordinate p2 = new ProjCoordinate();//target
        p.x = 20634500;
        p.y = 4660000;
        trans.transform(p, p2);
        System.out.println(p2.x);//121.44779962362234
        System.out.println(p2.y);//80.53230865703577

    }


    @Test
    public void testBeijing54LCCtowgs84() {
        CRSFactory csFactory = new CRSFactory();

        CoordinateReferenceSystem beijing54 = csFactory.createFromName("EPSG:4214");
        CoordinateReferenceSystem wgs84 = csFactory.createFromName("EPSG:4326");
        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        CoordinateTransform trans = ctFactory.createTransform(beijing54, wgs84);
        ProjCoordinate p = new ProjCoordinate();
        ProjCoordinate p2 = new ProjCoordinate();//target
        p.x = 115.1237245;
//        p.x = 20634500;
//        p.y = 4660000;
        p.y = 37.34498051;
        trans.transform(p, p2);
        System.out.println(p2.x);//121.44779962362234
        System.out.println(p2.y);//80.53230865703577

    }

    @Test
    public void testWkt() {
        //CGCS2000
        CRSFactory csFactory = new CRSFactory();
        CoordinateReferenceSystem crsCGCS = csFactory.createFromName("EPSG:4214");
        System.out.println(crsCGCS.getParameterString());//
        System.out.println(crsCGCS.getProjection().getEPSGCode());//不管什么参考系统都是 0
    }
}
