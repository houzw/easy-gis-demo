package org.egc.gis.ows.client;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.egc.gis.ows.wcs20.GetCoverageRequest;
import org.egc.gis.ows.wcs20.GetCapabilities;
import org.egc.gis.ows.wcs20.WCSVersion;
import org.egc.gis.ows.wcs20.entity.Contents;
import org.egc.gis.ows.wcs20.entity.ServiceMetadata;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.egc.gis.ows.Consts.*;

/**
 * Description:
 * <pre>
 * http://support.supermap.com.cn/DataWarehouse/WebDocHelp/iServer/API/wcs/WCS112/GetCapabilitiesOperaton.htm
 * https://mapserver.gis.umn.edu/id/ogc/wcs_server.html#http-post-support
 * </pre>
 *
 * @author houzhiwei
 * @date 2019 /10/2 19:34
 */
public class WCS20Client {

    /**
     * The constant DEFAULT_OUTPUT_FORMAT.
     */
    public final String DEFAULT_OUTPUT_FORMAT = "GeoTiff";
    private WCSVersion defaultVer = WCSVersion.v2_0_1;
    /**
     * Gets GetCapabilities request url.
     * simplified
     * @param url the server url
     * @return the GetCapabilities url
     * @throws URISyntaxException the uri syntax exception
     */
    public URI buildGetCapabilitiesURL(String url) throws URISyntaxException {
        return buildGetCapabilitiesURL(url, null, null, null, null);
    }

    /**
     * Gets capabilities url.
     *
     * @param url            server url,
     * @param acceptVersions 可选。请求的 WCS 服务的版本号序列，各个版本间用逗号隔开，并把最希望返回的版本的服务写在最前面。 <br/>
     *                       WCS versions separated by comma (,), e.g., 2.0.1,2.0.0,1.1.1
     * @param sections       可选。请求返回的服务端元信息的部分（Sections）
     * @param updateSequence 可选。the update sequence 服务元信息文档的版本号
     * @param acceptFormats  可选。the accept formats 请求服务描述文档的格式, 默认 text/xml
     * @return GetCapabilities request url
     * @throws URISyntaxException the uri syntax exception
     */
    public URI buildGetCapabilitiesURL(@NotNull String url, List<WCSVersion> acceptVersions,
                                              List<String> sections, String updateSequence,
                                              List<String> acceptFormats) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(url);
        uriBuilder.addParameter(SERVICE, WCS);
        uriBuilder.addParameter(VERSION, WCSVersion.v2_0_1.toString());
        uriBuilder.addParameter(REQUEST, GET_CAPABILITIES);
        if (CollectionUtils.isNotEmpty(acceptVersions)) {
            uriBuilder.addParameter("AcceptVersions", StringUtils.join(acceptVersions, ","));
        }
        if (CollectionUtils.isNotEmpty(sections)) {
            uriBuilder.addParameter("Section", StringUtils.join(sections, ","));
        }
        if (StringUtils.isNotBlank(updateSequence)) {
            uriBuilder.addParameter("updateSequence", updateSequence);
        }
        if (CollectionUtils.isNotEmpty(acceptFormats)) {
            uriBuilder.addParameter("AcceptFormats", StringUtils.join(acceptFormats, ","));
        }
        return uriBuilder.build();
    }

    /**
     * @param url        the url, e.g., http://gisserver.domain.com:6080/arcgis/services/demo/OceanModel_HYCOM/ImageServer/WCSServer?
     * @param coverageId the coverage id, e.g., Coverage1
     * @return uri
     * @throws URISyntaxException the uri syntax exception
     */
    public URI buildDescribeCoverageURL(@NotNull String url, @NotNull String coverageId) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(url);
        uriBuilder.addParameter(SERVICE, WCS);
        uriBuilder.addParameter(VERSION, defaultVer.toString());
        uriBuilder.addParameter(REQUEST, DESCRIBE_COVERAGE);
        uriBuilder.addParameter(COVERAGE_ID, coverageId);
        return uriBuilder.build();
    }


/*
* http://saocompute.eurac.edu/rasdaman/ows?
* &SERVICE=WCS
* &VERSION=2.0.1
* &REQUEST=GetCoverage
* &COVERAGEID=MOSAIC_TEST
* &SUBSET=Y(2137248.24574,2163828.24574)
* &SUBSET=X(3843270.76798,3844640.76798)
* &FORMAT=application/gml+xml
* */

    public GetCoverageRequest buildBasicGetCoverageRequest(String url,String coverageId){
        GetCoverageRequest.Builder builder = new GetCoverageRequest.Builder(url,coverageId);
        return builder.build();
    }
    /**
     * @return
     * @throws URISyntaxException
     */
    public URI buildGetCoverageURL(GetCoverageRequest request) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(request.getUrl());
        uriBuilder.addParameter(SERVICE, WCS);
        uriBuilder.addParameter(VERSION, request.getVersion().toString());
        uriBuilder.addParameter(REQUEST, GET_COVERAGE);
        uriBuilder.addParameter(COVERAGE_ID, request.getCoverageId());
        uriBuilder.addParameter(FORMAT, request.getFormat());
        if (StringUtils.isNotBlank(request.getSubsetX())) {
            uriBuilder.addParameter(SUBSET, request.getSubsetX());
        }
        if (StringUtils.isNotBlank(request.getSubsetY())) {
            uriBuilder.addParameter(SUBSET, request.getSubsetY());
        }
        if (StringUtils.isNotBlank(request.getSubsettingCrs())) {
            uriBuilder.addParameter(SUBSETTING_CRS, request.getSubsettingCrs());
        }
        if (request.getScaleFactor() > 0) {
            uriBuilder.addParameter(SCALE_FACTOR, request.getScaleFactor() + "");
        }
        if (StringUtils.isNotBlank(request.getInterpolation())) {
            uriBuilder.addParameter(INTERPOLATION, request.getInterpolation());
        }
        return uriBuilder.build();
    }

    /**
     *
     * @param capabilities
     * @return
     */
    public List<String> getCoverageIds(GetCapabilities capabilities) {
        List<Contents.CoverageSummary> coverageSummaries = capabilities.getContents().getCoverageSummary();
        List<String> ids = new ArrayList<>();
        for (Contents.CoverageSummary coverageSummary : coverageSummaries) {
            ids.add(coverageSummary.getCoverageId());
        }
        return ids;
    }

    public List<String> getSupportedFormats(GetCapabilities capabilities) {
        ServiceMetadata serviceMetadata = capabilities.getServiceMetadata();
        List<String> ids = new ArrayList<>();
        return serviceMetadata.getFormatSupported();
    }

    public List<String> getSupportedCrs(GetCapabilities capabilities) {
        ServiceMetadata serviceMetadata = capabilities.getServiceMetadata();
        List<String> ids = new ArrayList<>();
        return serviceMetadata.getExtension().getCrsSupported();
    }

    public List<String> getSupportedInterpolations(GetCapabilities capabilities) {
        ServiceMetadata serviceMetadata = capabilities.getServiceMetadata();
        List<String> ids = new ArrayList<>();
        return serviceMetadata.getExtension().getInterpolationSupported();
    }
}
