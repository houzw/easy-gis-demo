package org.egc.gis.gdal.vector.create;

import org.apache.commons.lang3.StringUtils;
import org.egc.gis.commons.Consts;
import org.egc.gis.commons.GDALDriversEnum;
import org.gdal.gdal.gdal;
import org.gdal.ogr.*;
import org.gdal.osr.SpatialReference;

import java.io.File;

/**
 * Description:
 * <pre>
 * https://pcjericks.github.io/py-gdalogr-cookbook/vector_layers.html#create-point-shapefile-with-attribute-data
 * https://blog.csdn.net/h4x0r_007/article/details/43085615
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/10/26 15:11
 */
public class CreateShapefile {

    /**
     * create a point shapefile with data from a csv file
     *
     * @param csv            input csv file
     * @param latitudeField  default X
     * @param longitudeField default Y
     * @param shp            output shapefile path
     * @return
     */
    public boolean createPointFromCSV(String csv, String latitudeField, String longitudeField, String shp) {
        if (StringUtils.isBlank(latitudeField)) {
            latitudeField = "X";
        }
        if (StringUtils.isBlank(longitudeField)) {
            longitudeField = "Y";
        }
        ogr.RegisterAll();
        gdal.SetConfigOption(Consts.GDAL_FILENAME_IS_UTF8, Consts.YES);


        // Create the output shapefile
        Driver driver = ogr.GetDriverByName(GDALDriversEnum.ESRI_Shapefile.getName());
        if (new File(shp).exists()) {
            driver.DeleteDataSource(shp);
        }
        DataSource dataSource = driver.CreateDataSource(shp, null);
        SpatialReference srs = new SpatialReference();
        // WGS84
        srs.ImportFromEPSG(4326);
        Layer layer = dataSource.CreateLayer(shp, srs, ogr.wkbPoint);
        // create point geometry
        Geometry point = new Geometry(ogr.wkbPoint);

        //TODO for loop

        //TODO read Input data from csv
        double[] pointCoord = {-124.4577, 48.0135};

        point.AddPoint(pointCoord[0], pointCoord[1]);

        //TODO read fields from csv
        String fieldName = "test", fieldValue = "test";

        //Create the feature
        FeatureDefn layerDefn = layer.GetLayerDefn();
        Feature feature = new Feature(layerDefn);
        feature.SetGeometry(point);
        layer.CreateFeature(feature);

        // create fields

        FieldDefn fid = new FieldDefn("FID", ogr.OFTInteger);
        fid.SetWidth(5);
        layer.CreateField(fid);

        FieldDefn lat = new FieldDefn("lat", ogr.OFTString);
        lat.SetWidth(30);
        layer.CreateField(lat);

        FieldDefn lon = new FieldDefn("lon", ogr.OFTString);
        lon.SetWidth(30);
        layer.CreateField(lon);

        //others
        FieldDefn fieldDefn = new FieldDefn(fieldName, ogr.OFTString);
        layer.CreateField(fieldDefn);


        // set field values
        feature.SetField("FID", fieldValue);//i
        feature.SetField("lat", fieldValue);//x
        feature.SetField("lon", fieldValue);//y
        feature.SetField(fieldName, fieldValue);


        layer.SyncToDisk();
        dataSource.SyncToDisk();
        return true;
    }
}
