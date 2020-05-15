package org.egc.gis.db;

import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.referencing.CRS;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 参考 https://github.com/yieryi/gts4vect
 */
public class PGVectorIO {
    private static final Logger log = LoggerFactory.getLogger(PGVectorIO.class);





    public boolean table2shp(DataStore dataStore, String tableName, String shpPath, String geomField) {
        boolean result = false;
        try {

            FeatureSource featureSource = dataStore.getFeatureSource(tableName);

            // 初始化 ShapefileDataStore
            File file = new File(shpPath);
            Map<String, Serializable> params = new HashMap<String, Serializable>();
            params.put(ShapefileDataStoreFactory.URLP.key, file.toURI().toURL());
            ShapefileDataStore shpDataStore = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);

            //postgis获取的Featuretype获取坐标系代码
            SimpleFeatureType pgfeaturetype = ((SimpleFeatureSource) featureSource).getSchema();
            String srid = pgfeaturetype.getGeometryDescriptor().getUserData().get("nativeSRID").toString();
            SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
            typeBuilder.init(pgfeaturetype);
            if (!srid.equals("0")) {
                CoordinateReferenceSystem crs = CRS.decode("EPSG:" + srid, true);
                typeBuilder.setCRS(crs);
            }
            pgfeaturetype = typeBuilder.buildFeatureType();
            //设置成utf-8编码
            shpDataStore.setCharset(Charset.forName("utf-8"));
            shpDataStore.createSchema(pgfeaturetype);
            write2shp(featureSource.getFeatures(), shpDataStore, geomField);
            result = true;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    public boolean write2shp(FeatureCollection featureCollection, ShapefileDataStore dataStore, String geomField) {
        boolean b = false;
        try {
            FeatureIterator<SimpleFeature> iterator = featureCollection.features();
            FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriter = dataStore.getFeatureWriter(dataStore.getTypeNames()[0], Transaction.AUTO_COMMIT);
            while (iterator.hasNext()) {
                Feature feature = iterator.next();
                SimpleFeature simpleFeature = featureWriter.next();
                Collection<Property> properties = feature.getProperties();
                Iterator<Property> propertyIterator = properties.iterator();
                while (propertyIterator.hasNext()) {
                    Property property = propertyIterator.next();
                    if (property.getName().toString().equalsIgnoreCase(geomField)) {
                        simpleFeature.setAttribute("the_geom", property.getValue());
                    } else {
                        simpleFeature.setAttribute(property.getName().toString(), property.getValue());
                    }
                }
                featureWriter.write();
            }
            iterator.close();
            featureWriter.close();
            dataStore.dispose();
            b = true;
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
        return b;
    }

    public boolean shp2pg(DataStore dataStore, String shpPath, String pgTableName, Charset shpCharset) {
        boolean result = false;
        try {
            ShapefileDataStore shapefileDataStore = new ShapefileDataStore(new File(shpPath).toURI().toURL());
            shapefileDataStore.setCharset(shpCharset);
            FeatureCollection featureCollection = shapefileDataStore.getFeatureSource().getFeatures();
            write2pg(dataStore, featureCollection, pgTableName);
            result = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    public boolean write2pg(DataStore dataStore, FeatureCollection featureCollection, String tableName) {
        boolean b = false;
        try {
            SimpleFeatureType simpleFeatureType = (SimpleFeatureType) featureCollection.getSchema();
            SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
            typeBuilder.init(simpleFeatureType);
            typeBuilder.setName(tableName);
            SimpleFeatureType type = typeBuilder.buildFeatureType();
            dataStore.createSchema(type);
            FeatureIterator iterator = featureCollection.features();
            FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriter = dataStore.getFeatureWriterAppend(tableName, Transaction.AUTO_COMMIT);
            while (iterator.hasNext()) {
                Feature feature = iterator.next();
                SimpleFeature simpleFeature = featureWriter.next();
                Collection<Property> properties = feature.getProperties();
                Iterator<Property> propertyIterator = properties.iterator();
                while ((propertyIterator.hasNext())) {
                    Property property = propertyIterator.next();
                    simpleFeature.setAttribute(property.getName().toString(), property.getValue());
                }
                featureWriter.write();
            }
            iterator.close();
            featureWriter.close();
            b = true;
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
        return b;
    }

    public boolean geojson2pg(DataStore dataStore, String geojsonPath, String tableName) {
        boolean result = false;
        try {
            FeatureJSON featureJSON = new FeatureJSON();
            FeatureCollection featureCollection = featureJSON.readFeatureCollection(new FileInputStream(geojsonPath));
            write2pg(dataStore, featureCollection, tableName);
            result = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }


}
