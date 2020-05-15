package org.egc.gis.ows.test;

import org.egc.gis.ows.geotools.WFSClient;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.factory.GeoTools;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.spatial.Intersects;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Description:
 * <pre>
 * https://docs.geotools.org/latest/userguide/library/data/wfs-ng.html
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/9/22 22:26
 */
public class WFSTest {

    @Test
    public void test() throws IOException, URISyntaxException {

//        String baseurl = "https://SDMDataAccess.sc.egov.usda.gov/Spatial/SDMWGS84Geographic.wfs";
        String baseurl = "http://geoserv.weichand.de:8080/geoserver/wfs";
        WFSClient client = new WFSClient();
        try {
            //1, connection
            WFSDataStore dataStore = client.createDataStore(baseurl);
            //2, discovery
            String typeNames[] = dataStore.getTypeNames();
            String typeName = typeNames[0];
            SimpleFeatureType schema = dataStore.getSchema(typeName);
            //3. target
//          SimpleFeatureSource source = dataStore.getFeatureSource("ali:Manategh_Tehran");
            FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);
            System.out.println("Metadata Bounds:" + source.getBounds());
            //4, query
            String geomName = schema.getGeometryDescriptor().getLocalName();
            Envelope bbox = new Envelope(-100.0, -70, 25, 40);
            FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
            Object polygon = JTS.toGeometry(bbox);
            Intersects filter = ff.intersects(ff.property(geomName), ff.literal(polygon));
            Query query = new Query(typeName, filter, new String[]{geomName});
            FeatureCollection<SimpleFeatureType, SimpleFeature> features = source.getFeatures(query);
            ReferencedEnvelope bounds = new ReferencedEnvelope();
            FeatureIterator<SimpleFeature> featureIterator = features.features();
            while (featureIterator.hasNext()) {
                Feature feature = (Feature) featureIterator.next();
                bounds.include(feature.getBounds());
            }
            System.out.println("Calculated Bounds:" + bounds);
            featureIterator.close();
            /*
            SimpleFeatureCollection fc = source.getFeatures();
            while (fc.features().hasNext()) {
                SimpleFeature sf = fc.features().next();
                System.out.println(sf.getAttribute("myname"));
            }
            */
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
