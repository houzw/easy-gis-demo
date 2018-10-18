package org.egc.gis.vector.test;

import com.vividsolutions.jts.geom.*;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.osgeo.proj4j.CRSFactory;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Description:
 * <pre>
 * shapefile 文件测试
 * 参考：
 * https://www.cnblogs.com/cugwx/p/3719195.html
 * http://docs.geotools.org/stable/userguide/library/data/shape.html
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/10/3 15:32
 */
public class ShapefileTest {
    String path = "H:/GIS data/hydrology_data/TaoXi_model data/TX_basin/Basin.shp";

    @Test
    public void readShp() throws Exception {
        File file = new File(path);
        ShapefileDataStoreFactory factory = new ShapefileDataStoreFactory();
        factory.createDataStore(file.toURI().toURL());

        ShapefileDataStore shpDataStore = new ShapefileDataStore(new File(path).toURI().toURL());
        shpDataStore.setCharset(Charset.forName("UTF-8"));//GBK
        String typeName = shpDataStore.getTypeNames()[0];//basin

        FeatureSource<SimpleFeatureType, SimpleFeature> source = shpDataStore.getFeatureSource(typeName);
        CoordinateReferenceSystem crs = source.getSchema().getCoordinateReferenceSystem();
        print(crs.getName().toString());//Beijing_1954_3_Degree_GK_CM_117E
        print(crs.getIdentifiers().toString());//[]

        ReferencedEnvelope bounds = source.getBounds();
        print(bounds.getUpperCorner().getCoordinate()[0]);//500598.4345536285
        print(bounds.getUpperCorner().getCoordinate()[1]);//3519147.8708414827
        //UpperCorner
        print(bounds.getMaxX());//500598.4345536285
        print(bounds.getMaxY());//3519147.8708414827
        //LowerCorner
        print(bounds.getMinX());//447168.43455362704
        print(bounds.getMinY());//3469857.8708414817
        print(CRS.lookupEpsgCode(crs, true));//null
        print(CRS.getMapProjection(crs));


        CRSFactory csFactory = new CRSFactory();
        org.osgeo.proj4j.CoordinateReferenceSystem crsProj = csFactory.createFromName("EPSG:2436");//UnknownAuthorityCodeException
        print(crsProj.getProjection().getPROJ4Description());

        FeatureCollection<SimpleFeatureType, SimpleFeature> features = source.getFeatures();
        FeatureIterator<SimpleFeature> featureIterator = features.features();
        while (featureIterator.hasNext()) {
            SimpleFeature feature = featureIterator.next();
            print(feature.getFeatureType().getName().toString());//Basin
            Collection<Property> properties = feature.getProperties();
            Iterator<Property> iterator = properties.iterator();
            Object obj = iterator.next().getValue();
            Geometry geometry = (Geometry) obj;
            print(geometry.getGeometryType());//MultiPolygon
            if (obj instanceof Point) {
                Point p = (Point) obj;
            } else if (obj instanceof MultiPoint) {
                print(iterator.next().getName().toString());
                MultiPoint mp = (MultiPoint) obj;
            } else if (obj instanceof MultiPolygon) {
                print(iterator.next().getName().toString());
                MultiPolygon mp = (MultiPolygon) obj;
            }
        }

    }

    @Test
    public void readShpGeom() throws Exception {
        ShpFiles shpFiles = new ShpFiles(path);
        ShapefileReader reader = new ShapefileReader(shpFiles, false, false, new GeometryFactory());
        while (reader.hasNext()) {
            Geometry geometry = (Geometry) reader.nextRecord().shape();
            print(geometry.toString());//MULTIPOLYGON....

        }

        reader.close();
       /*
       读取 Basin.shp.xml
       ShpXmlFileReader shpXmlFileReader = new ShpXmlFileReader(shpFiles);
        Metadata metadata = shpXmlFileReader.parse();
        print(metadata.getIdinfo().toString());
        */
    }

    @Test
    public void readVector() throws Exception {
        File file = new File(path);
        Map<String, Object> map = new HashMap<>();
        map.put("url", file.toURI().toURL());
        DataStore dataStore = DataStoreFinder.getDataStore(map);
        String typeName = dataStore.getTypeNames()[0];
        FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);
        Filter filter = Filter.INCLUDE; // ECQL.toFilter("BBOX(THE_GEOM, 10,20,30,40)")

        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(filter);
        try (FeatureIterator<SimpleFeature> features = collection.features()) {
            while (features.hasNext()) {
                SimpleFeature feature = features.next();
                System.out.print(feature.getID());//Basin
                System.out.print(": ");
                System.out.println(feature.getDefaultGeometryProperty().getValue());//MULTIPOLYGON...
            }
        }

    }


    private void print(Object s) {
        System.out.println(s + "");
    }
}
