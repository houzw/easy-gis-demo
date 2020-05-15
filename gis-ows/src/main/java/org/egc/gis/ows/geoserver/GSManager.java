package org.egc.gis.ows.geoserver;

import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;

import java.net.MalformedURLException;
import java.net.URL;

public class GSManager {
    // 应当从配置文件中读取
    public static final String DEFAULT_WS = "global";

    public static final String RESTURL = "http://localhost:8088/geoserver";

    public static final String RESTUSER = "admin";

    public static final String RESTPW = "houzw2018";

    // geoserver target version
    public static final String GS_VERSION = "2.13.1";
    public static URL URL;
    public static GeoServerRESTManager manager;

    public static GeoServerRESTReader reader;

    public static GeoServerRESTPublisher publisher;

    static {
        try {
            URL = new URL(RESTURL);
            manager = new GeoServerRESTManager(URL, RESTUSER, RESTPW);
            reader = manager.getReader();
            publisher = manager.getPublisher();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
