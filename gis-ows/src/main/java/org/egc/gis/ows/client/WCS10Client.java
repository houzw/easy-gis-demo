package org.egc.gis.ows.client;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.egc.gis.ows.request.GetCoverageRequest10;
import org.egc.gis.ows.wcs20.WCSVersion;

import java.net.URI;
import java.net.URISyntaxException;

import static org.egc.gis.ows.Consts.*;

/**
 * Description:
 * <pre>
 * https://mapserver.gis.umn.edu/id/ogc/wcs_server.html
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/11/16 20:49
 */
public class WCS10Client {

    /*
     * e.g., http://modwebsrv.modaps.eosdis.nasa.gov/wcs/5/MOD09/
     */


    public static URI buildGetCapabilitiesURL(String url) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(url);
        uriBuilder.addParameter(SERVICE, WCS);
        uriBuilder.addParameter(VERSION, WCSVersion.v1_0_0.toString());
        uriBuilder.addParameter(REQUEST, GET_CAPABILITIES);
        return uriBuilder.build();
    }

    public static URI buildGetCapabilitiesURL(String url, String section) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(url);
        uriBuilder.addParameter(SERVICE, WCS);
        uriBuilder.addParameter(VERSION, WCSVersion.v1_0_0.toString());
        uriBuilder.addParameter(REQUEST, GET_CAPABILITIES);
        if (StringUtils.isNotBlank(section)) {
            uriBuilder.addParameter("Section", section);
        }
        return uriBuilder.build();
    }

       /*
        * WCS 1.0.0: http://gisserver.domain.com:6080/arcgis/services/World/Temperature/ImageServer/WCSServer?
        * SERVICE=WCS
        * &VERSION=1.0.0
        * &REQUEST=DescribeCoverage
        * &COVERAGE=1
        *
        */

    public static URI buildDescribeCoverageURL(String url, String coverage) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(url);
        uriBuilder.addParameter(SERVICE, WCS);
        uriBuilder.addParameter(VERSION, WCSVersion.v1_0_0.toString());
        uriBuilder.addParameter(REQUEST, DESCRIBE_COVERAGE);
        uriBuilder.addParameter(COVERAGE, coverage);
        return uriBuilder.build();
    }

      /* http://gsky.nci.org.au/ows/dea?service=WCS
       * &crs=EPSG:4326
       * &format=NetCDF
       * &request=GetCoverage
       * &height=256
       * &width=256
       * &version=1.0.0
       * &bbox=147,-37,148,-35
       * &coverage=landsat8_nbart_16day
       * &time=2013-04-20T00:00:00.000Z
       * &Styles=tc.
       */
    //http://modwebsrv.modaps.eosdis.nasa.gov/wcs/5/MOD021KM/
    // getCoverage?service=WCS
    // &version=1.0.0
    // &request=GetCoverage
    // &coverage=EV_500_Aggr1km_RefSB:Day
    // &bbox=-80,35,-75,40&time=2010-10-21T00:00:00
    // &format=hdf4
    // &response_crs=EPSG:4326
    // &Band_500M=2/2/1
    // &resx=0.01
    // &resy=0.01

    public static URI buildGetCoverageURL(GetCoverageRequest10 request) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(request.getUrl());
        uriBuilder.addParameter(SERVICE, WCS);
        uriBuilder.addParameter(VERSION, request.getVersion().toString());
        uriBuilder.addParameter(REQUEST, GET_COVERAGE);
        uriBuilder.addParameter(COVERAGE, request.getCoverage());
        uriBuilder.addParameter(CRS, request.getCrs());
        uriBuilder.addParameter(BBOX, request.getBbox());
        if (StringUtils.isNotBlank(request.getTime())) {
            uriBuilder.addParameter(TIME, request.getTime());
        }
        if (StringUtils.isNotBlank(request.getFormat())) {
            uriBuilder.addParameter(FORMAT, request.getFormat());
        } else {
            uriBuilder.addParameter(FORMAT, "GTiff");
        }
        if (request.getHeight() > 0) {
            uriBuilder.addParameter(HEIGHT, request.getHeight() + "");
        }
        if (request.getWidth() > 0) {
            uriBuilder.addParameter(WIDTH, request.getWidth() + "");
        }
        if (request.getResx() > 0) {
            uriBuilder.addParameter(RES_X, request.getResx() + "");
        }
        if (request.getResy() > 0) {
            uriBuilder.addParameter(RES_Y, request.getResy() + "");
        }
        return uriBuilder.build();
    }
}
