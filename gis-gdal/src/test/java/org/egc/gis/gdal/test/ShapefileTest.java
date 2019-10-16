package org.egc.gis.gdal.test;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import org.apache.commons.io.FilenameUtils;
import org.egc.commons.gis.VectorUtils;
import org.egc.gis.commons.Consts;
import org.egc.gis.commons.GdalDriversEnum;
import org.egc.gis.commons.VectorMetadata;
import org.gdal.gdal.gdal;
import org.gdal.ogr.*;
import org.gdal.osr.SpatialReference;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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

    String shp = "H:\\gisdemo\\in\\in\\UserStreams.shp";

    @Test
    public void testMetadata() {
        org.egc.commons.gis.VectorMetadata metadata = VectorUtils.getShapefileMetadata(shp);
        String json = JSON.toJSONString(metadata, true);
        System.out.println(json);
    }

    @Test
    public void test() {
        System.out.println(gdal.VersionInfo());
//        gdal.UseExceptions(); // 1.11.4 没有
        SpatialReference reference = new SpatialReference();
        reference.ImportFromEPSG(4206);
        System.out.println(reference.ExportToProj4());
    }

    @Test
    public void testShp() {

        ogr.RegisterAll();
        DataSource ds = ogr.Open(shp, false);
        VectorMetadata metadata = new VectorMetadata();

        Driver driver = ds.GetDriver();
        metadata.setFormat(driver.GetName());
        metadata.setLayerCount(ds.GetLayerCount());
        Layer layer = ds.GetLayer(0);
        metadata.setGeomType(layer.GetGeomType());
        metadata.setFeatureCount(layer.GetFeatureCount());
        metadata.setGeometry(layer.GetNextFeature().GetGeometryRef().GetGeometryName());
        SpatialReference sr = layer.GetSpatialRef();

        Feature feature = layer.GetNextFeature();
        if (sr == null) {
            // 没有参考说明没有prj文件，需要创建一个同名的prj文件
            sr = new SpatialReference();
//            sr.ImportFromEPSG(4326);
            sr.ImportFromWkt("PROJCS[\"Clarke_1866_Albers\",GEOGCS[\"GCS_Clarke_1866\",DATUM[\"D_Clarke_1866\",SPHEROID[\"Clarke_1866\",6378206.4,294.9786982]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\"Albers\"],PARAMETER[\"False_Easting\",0.0],PARAMETER[\"False_Northing\",0.0],PARAMETER[\"Central_Meridian\",-96.0],PARAMETER[\"Standard_Parallel_1\",29.5],PARAMETER[\"Standard_Parallel_2\",45.5],PARAMETER[\"Latitude_Of_Origin\",23.0],UNIT[\"Meter\",1.0]]");
            sr.MorphToESRI(); // 这样的话拿不到epsg，但是如果是读取自ESRI的wkt，则必须包含此句

            File prj = new File(FilenameUtils.removeExtension(shp) + ".prj");
            try {
                Files.write(prj.toPath(), sr.ExportToWkt().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(sr.IsGeographic());//0}
        System.out.println(sr.IsProjected());//1
        System.out.println(sr.IsGeocentric());//0
        String authorityCode = sr.GetAuthorityCode(null);
        String authorityName = sr.GetAuthorityName(null);
        if (!Strings.isNullOrEmpty(authorityCode)) {
            metadata.setSrid(Integer.parseInt(authorityCode));
        }
        String geogcs = sr.GetAttrValue(Consts.ATTR_GEOGCS);
        if (sr.IsProjected() == 1) {
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
        String json = JSON.toJSONString(metadata, true);
        System.out.println(json);
        gdal.GDALDestroyDriverManager();
    }

    @Test
    public void cresatePointShp() throws IOException {
        // 不可少，否则 driver 为null
        ogr.RegisterAll();
        gdal.SetConfigOption(Consts.GDAL_FILENAME_IS_UTF8, Consts.YES);
        // Input data
        double[] pointCoord = {-124.4577, 48.0135};
        String fieldName = "test", fieldValue = "test", outSHPfn = "H:\\gisdemo\\out\\test.shp";
        // Create the output shapefile
        Driver driver = ogr.GetDriverByName(GdalDriversEnum.ESRI_Shapefile.getName());
        if (new File(outSHPfn).exists()) {
            driver.DeleteDataSource(outSHPfn);
        }
        DataSource dataSource = driver.CreateDataSource(outSHPfn, null);
        SpatialReference srs = new SpatialReference();
        srs.ImportFromEPSG(4326);
        Layer layer = dataSource.CreateLayer(outSHPfn, srs, ogr.wkbPoint);
        // create point geometry
        Geometry point = new Geometry(ogr.wkbPoint);
        point.AddPoint(pointCoord[0], pointCoord[1]);
        // create a field
        FieldDefn fieldDefn = new FieldDefn(fieldName, ogr.OFTString);
        layer.CreateField(fieldDefn);

        //Create the feature and set values
        FeatureDefn layerDefn = layer.GetLayerDefn();
        Feature feature = new Feature(layerDefn);
        feature.SetGeometry(point);
        feature.SetField(fieldName, fieldValue);
        layer.CreateFeature(feature);
        layer.SyncToDisk();
        dataSource.SyncToDisk();

    }
}
