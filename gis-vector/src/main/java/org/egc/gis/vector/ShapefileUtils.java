package org.egc.gis.vector;

import lombok.extern.slf4j.Slf4j;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * <pre>
 * https://docs.geotools.org/latest/userguide/library/data/shape.html
 * https://gis.stackexchange.com/questions/165471/create-shapefile-based-on-geojson-data-with-geotools-java?rq=1
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/10/9 17:45
 */
@Slf4j
public class ShapefileUtils {

    public static SimpleFeatureSource readShp(File shpFile) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put(ShapefileDataStoreFactory.URLP.key, shpFile.toURI().toURL());
        ShapefileDataStore dataStore = (ShapefileDataStore) DataStoreFinder.getDataStore(params);
        // 处理中文
        Charset charset = Charset.forName("GBK");
        dataStore.setCharset(charset);
        // 图层名称
        String typeName = dataStore.getTypeNames()[0];
        SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);
        return featureSource;
    }

    /**
     * Write the features to the shapefile
     * <p>
     * The Shapefile format has a couple limitations:
     * - "the_geom" is always first, and used for the geometry attribute name
     * - "the_geom" must be of type Point, MultiPoint, MuiltiLineString, MultiPolygon
     * - Attribute names are limited in length
     * - Not all data types are supported (example Timestamp represented as Date)
     * <p>
     * Each data store has different limitations so check the resulting SimpleFeatureType.
     *
     * @param collection Simple Feature Collection
     * @param shpFile    shapefile
     * @param TYPE       TYPE is used as a template to describe the file contents
     * @return boolean
     * @throws IOException
     */
    public static boolean writeShp(SimpleFeatureCollection collection, File shpFile, SimpleFeatureType TYPE) throws IOException {
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        //参数设置
        Map<String, Serializable> params = new HashMap<>();
        params.put(ShapefileDataStoreFactory.URLP.key, shpFile.toURI().toURL());
        params.put(ShapefileDataStoreFactory.CREATE_SPATIAL_INDEX.key, Boolean.TRUE);
        ShapefileDataStore dataStore = (ShapefileDataStore) dataStoreFactory.createDataStore(params);
        //创建文件描述内容
        dataStore.createSchema(TYPE);

        Transaction transaction = new DefaultTransaction("create");
        String typeName = dataStore.getTypeNames()[0];
        SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);
        //check how closely the shapefile was able to match our template (the SimpleFeatureType TYPE).
        SimpleFeatureType SHAPE_TYPE = featureSource.getSchema();
        log.info("SHAPE:" + SHAPE_TYPE);

        if (featureSource instanceof SimpleFeatureStore) {
            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;

            featureStore.setTransaction(transaction);
            try {
                featureStore.addFeatures(collection);
                transaction.commit();
            } catch (Exception problem) {
                log.error(problem.getMessage());
                transaction.rollback();
            } finally {
                transaction.close();
            }
            log.info("Create shapefile succeed!");
            return true;
        } else {
            log.error(typeName + " does not support read/write access");
        }
        return false;
    }

    /**
     * SimpleFeatureStore has a method to add features from a
     * SimpleFeatureCollection object, so we use the ListFeatureCollection
     * class to wrap our list of features.
     */
    public static SimpleFeatureCollection simpleFeatureCollection(List<SimpleFeature> features, SimpleFeatureType TYPE) {
        SimpleFeatureCollection collection = new ListFeatureCollection(TYPE, features);
        return collection;
    }

    public static void toShp(File geojson, String shpFilePath) throws IOException {
        File shpFile = new File(shpFilePath);
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put(ShapefileDataStoreFactory.URLP.key, shpFile.toURI().toURL());
        params.put(ShapefileDataStoreFactory.CREATE_SPATIAL_INDEX.key, Boolean.TRUE);
        ShapefileDataStore shpDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);

        int decimals = 15;
        GeometryJSON gjson = new GeometryJSON(decimals);
        FeatureJSON fjson = new FeatureJSON(gjson);
        InputStream in = new FileInputStream(geojson);
        FeatureCollection fc = fjson.readFeatureCollection(in);
        SimpleFeatureType type = (SimpleFeatureType) fc.getSchema();

        //设置成utf-8编码
        shpDataStore.setCharset(Charset.forName("UTF-8"));
        shpDataStore.createSchema(type);

        Transaction transaction = new DefaultTransaction("create");
        String typeName = shpDataStore.getTypeNames()[0];
        SimpleFeatureSource featureSource = shpDataStore.getFeatureSource(typeName);
        if (featureSource instanceof FeatureStore) {
            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
            featureStore.setTransaction(transaction);
            try {
                featureStore.addFeatures(fc);
                transaction.commit();
            } catch (Exception ex) {
                log.error(ex.getMessage());
                transaction.rollback();
            } finally {
                transaction.close();
            }
            log.info("succeed!");
        } else {
            log.error("{} does not support read/write access", typeName);
        }
    }

    public boolean shp2geojson(String shpPath, String geojsonPath, Charset shpCharset) {
        boolean result = false;
        try {
            ShapefileDataStore shapefileDataStore = new ShapefileDataStore(new File(shpPath).toURI().toURL());
            shapefileDataStore.setCharset(shpCharset);
            ContentFeatureSource featureSource = shapefileDataStore.getFeatureSource();
            ContentFeatureCollection contentFeatureCollection = featureSource.getFeatures();
            FeatureJSON featureJSON = new FeatureJSON(new GeometryJSON(15));
            featureJSON.writeFeatureCollection(contentFeatureCollection, new File(geojsonPath));
            shapefileDataStore.dispose();
            result = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    public static String geojson2shp(String json) {
        int decimals = 15;
        GeometryJSON gjson = new GeometryJSON(decimals);
        FeatureJSON fjson = new FeatureJSON(gjson);
        return "";
    }
}
