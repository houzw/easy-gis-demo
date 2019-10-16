package org.egc.gis.ows.geotools;

import org.egc.gis.ows.OWSService;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.util.Version;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * <pre>
 * http://docs.geotools.org/latest/userguide/library/data/wfs-ng.html
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/9/22 20:57
 */
public class WFSClient implements OWSService {

    public static final String GET_CAPABILITIES_URL = "WFSDataStoreFactory:GET_CAPABILITIES_URL";

    public static WFSDataStore createDataStore(String baseUrl) throws IOException {
        WFSDataStoreFactory dsf = new WFSDataStoreFactory();
        URL url = new URL(baseUrl);
        URL capabilitiesRequest = WFSDataStoreFactory.createGetCapabilitiesRequest(url, new Version("1.1.0"));
        Map parameters = new HashMap<>();
//        parameters.put(WFSDataStoreFactory.TIMEOUT.key, 6000);
        parameters.put(GET_CAPABILITIES_URL, capabilitiesRequest);
        WFSDataStore dataStore = dsf.createDataStore(parameters);
        return dataStore;
    }
}
