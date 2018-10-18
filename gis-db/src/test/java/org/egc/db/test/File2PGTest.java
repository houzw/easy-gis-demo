package org.egc.db.test;

import org.egc.commons.raster.PostGISInfo;
import org.egc.gis.db.File2PostGIS;
import org.junit.Before;
import org.junit.Test;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/10/9 17:36
 */
public class File2PGTest {

    PostGISInfo pginfo = new PostGISInfo();
    String shp = "D:\\SWAT\\ArcSWAT\\Databases\\ExInputs\\UserWatersheds.shp";
    @Before
    public void initPGinfo(){
        pginfo.setDatabase("pgis");
        pginfo.setPassword("pg_gis");
        pginfo.setUsername("giser");
        pginfo.setShapeTable("shptest");
    }

    @Test
    public void testShp(){

        File2PostGIS.shp2PostGIS(null, shp, pginfo);
    }

}
