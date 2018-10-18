package org.egc.gis.gdal.test;

import com.google.common.base.Strings;
import org.egc.gis.commons.Consts;
import org.egc.gis.commons.ShapefileMetadata;
import org.gdal.gdal.gdal;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Driver;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;
import org.gdal.osr.SpatialReference;
import org.junit.Test;

/**
 * Description:
 * <pre>
 * https://gdal.org/1.11/ogr/
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/10/8 21:03
 */
public class ShapefileTest {

    String shp = "H:\\gisdemo\\in\\UserWatersheds.shp";

    @Test
    public void testShp() {

        ogr.RegisterAll();
        DataSource ds = ogr.Open(shp, false);
        ShapefileMetadata metadata = new ShapefileMetadata();

        Driver driver = ds.GetDriver();
        metadata.setFormat(driver.GetName());
        metadata.setLayerCount(ds.GetLayerCount());
        Layer layer = ds.GetLayer(0);
        SpatialReference sr = layer.GetSpatialRef();
        layer.GetGeomType();

        System.out.println(sr.IsGeographic());//0
        System.out.println(sr.IsProjected());//1
        System.out.println(sr.IsGeocentric());//0
        String authorityCode = sr.GetAuthorityCode(null);
        String authorityName = sr.GetAuthorityName(null);
        if (!Strings.isNullOrEmpty(authorityCode)) {
            metadata.setSrid(Integer.parseInt(authorityCode));
        }
        String geogcs = sr.GetAttrValue(Consts.ATTR_GEOGCS);
        if (sr.IsProjected()==1) {
            String projcs = sr.GetAttrValue(Consts.ATTR_PROJCS);
            metadata.setCrs(projcs);
        } else {
            metadata.setCrs(geogcs);
        }
        metadata.setUnit(sr.GetLinearUnitsName());
        metadata.setCrsProj4(sr.ExportToProj4());
        metadata.setCrsWkt(sr.ExportToWkt());
        double[] extent = layer.GetExtent();
        metadata.setMinX(extent[0]);
        metadata.setMaxX(extent[1]);
        metadata.setMinY(extent[2]);
        metadata.setMaxY(extent[3]);

        ds.delete();
        driver.delete();
        System.out.println(metadata.toString());
        gdal.GDALDestroyDriverManager();
    }

}
