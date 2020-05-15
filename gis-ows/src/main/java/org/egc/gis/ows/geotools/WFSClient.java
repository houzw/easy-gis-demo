package org.egc.gis.ows.geotools;

import org.egc.gis.ows.OWSService;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.ReprojectingFeatureCollection;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.Version;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * <pre>
 * http://docs.geotools.org/latest/userguide/library/data/wfs-ng.html
 * https://stackoverflow.com/questions/44584553/retrieve-map-data-via-wfs-with-geotools
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/9/22 20:57
 */
public class WFSClient implements OWSService {

    public  final String WFS_GET_CAPABILITIES_URL = "WFSDataStoreFactory:GET_CAPABILITIES_URL";
    WFSDataStore dataStore = null;
    public  WFSDataStore createDataStore(String wfsUrl) throws IOException {
        WFSDataStoreFactory dsf = new WFSDataStoreFactory();
        URL url = new URL(wfsUrl);
        URL capabilitiesRequest = WFSDataStoreFactory.createGetCapabilitiesRequest(url, new Version("1.1.0"));
        Map parameters = new HashMap<>();
//        parameters.put(WFSDataStoreFactory.TIMEOUT.key, 6000);
        parameters.put(WFS_GET_CAPABILITIES_URL, capabilitiesRequest);
         dataStore = dsf.createDataStore(parameters);
        return dataStore;
    }

    public void features() throws IOException, CQLException {

        SimpleFeatureSource source = dataStore.getFeatureSource("ali:Manategh_Tehran");
        SimpleFeatureCollection fc = source.getFeatures();
        //identifier of a type of data or a schema
        for(String s : dataStore.getTypeNames()){
            System.out.println(s);

        }
        //a Feature is the actual piece of data that has position and attributes.
        while(fc.features().hasNext()){
            SimpleFeature sf = fc.features().next();
            System.out.println(sf.getAttribute("myname"));
        }
        // query
        // https://docs.geoserver.org/stable/en/user/tutorials/cql/cql_tutorial.html
        Filter filter = ECQL.toFilter("BBOX(the_geom, 500000, 700000, 501000, 701000)");
        //http://docs.geotools.org/latest/javadocs/org/geotools/data/Query.html
        Query query = new Query( "countries", filter );
        query.setFilter(filter);
        query.setMaxFeatures(10);
        SimpleFeatureCollection qfc = source.getFeatures(query);

        ReprojectingFeatureCollection rfc = new ReprojectingFeatureCollection(fc, DefaultGeographicCRS.WGS84);
    }
}
