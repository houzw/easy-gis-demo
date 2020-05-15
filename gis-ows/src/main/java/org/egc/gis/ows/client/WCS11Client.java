package org.egc.gis.ows.client;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.egc.gis.ows.request.GetCoverageRequest11;
import org.egc.gis.ows.wcs20.WCSVersion;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;

import static org.egc.gis.ows.Consts.*;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/11/16 20:53
 */
public class WCS11Client {

    private WCSVersion defaultVersion = WCSVersion.v1_1_0;

    /**
     *
     * @param url
     * @param section optional
     * @return
     * @throws URISyntaxException
     */
    public URI buildGetCapabilitiesURL(@NotNull String url, String section) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(url);
        uriBuilder.addParameter(SERVICE, WCS);
        uriBuilder.addParameter(VERSION, defaultVersion.toString());
        uriBuilder.addParameter(REQUEST, GET_CAPABILITIES);
        if (StringUtils.isNotBlank(section)) {
            uriBuilder.addParameter("Section", section);
        }
        return uriBuilder.build();
    }

    public URI buildGetCapabilitiesURL(String url) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(url);
        uriBuilder.addParameter(SERVICE, WCS);
        uriBuilder.addParameter(VERSION, defaultVersion.toString());
        uriBuilder.addParameter(REQUEST, GET_CAPABILITIES);
        return uriBuilder.build();
    }

    public URI buildDescribeCoverageURL(String url, String identifiers) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(url);
        uriBuilder.addParameter(SERVICE, WCS);
        uriBuilder.addParameter(VERSION, defaultVersion.toString());
        uriBuilder.addParameter(REQUEST, DESCRIBE_COVERAGE);
        uriBuilder.addParameter(IDENTIFIERS, identifiers);
        return uriBuilder.build();
    }

    public URI buildGetCoverageURL(GetCoverageRequest11 request) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(request.getUrl());
        uriBuilder.addParameter(SERVICE, WCS);
        uriBuilder.addParameter(VERSION, defaultVersion.toString());
        uriBuilder.addParameter(REQUEST, GET_COVERAGE);
        uriBuilder.addParameter(IDENTIFIER, request.getIdentifier());
        uriBuilder.addParameter(BOUNDING_BOX, request.getBbox());
        if (StringUtils.isNotBlank(request.getGridBaseCrs())) {
            uriBuilder.addParameter(GRID_BASE_CRS, request.getGridBaseCrs());
        }
        uriBuilder.addParameter(FORMAT, request.getFormat());
        if (StringUtils.isNotBlank(request.getGridCS())) {
            uriBuilder.addParameter(GRID_CS, request.getGridCS());
        }
        if (StringUtils.isNotBlank(request.getGridOrigin())) {
            uriBuilder.addParameter(GRID_ORIGIN, request.getGridOrigin());
        }
        if (StringUtils.isNotBlank(request.getGridOffsets())) {
            uriBuilder.addParameter(GRID_OFFSETS, request.getGridOffsets());
        }
        if (StringUtils.isNotBlank(request.getRangeSubset())) {
            uriBuilder.addParameter(RANGE_SUBSET, request.getRangeSubset());
        }

        return uriBuilder.build();
    }
}
