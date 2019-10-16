package org.egc.gis.ows.geotools;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.egc.gis.ows.HttpUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Description:
 * <pre>
 * https://opentopography.org/developers
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/9/23 11:13
 */
@Data
@Slf4j
public class OpenTopo {

    //region parameters
    /* Required Parameters
     ******************************************************************************************************************************************************************************************************************************************
     * demtype 	                |   The global raster dataset - SRTM GL3 (90m) is 'SRTMGL3', SRTM GL1 (30m) is 'SRTMGL1', SRTM GL1 (Ellipsoidal) is 'SRTMGL1_E', and ALOS World 3D 30m is 'AW3D30', ALOS World 3D (Ellipsoidal) is 'AW3D30_E' *
     * west, south, east, north |	WGS 84 bounding box coordinates                                                                                                                                                                           *
     * outputFormat 	        |   outputFormat Output Format (optional) - GTiff for GeoTiff, AAIGrid for Arc ASCII Grid, HFA for Erdas Imagine (.IMG). Defaults to GTiff if parameter is not provided                                       *
     ******************************************************************************************************************************************************************************************************************************************/

    private static final String BASE_URL = "http://opentopo.sdsc.edu/otr/";
    private static final String DEM_TYPE = "demtype";

    /**
     * SRTM GL3 (90m)
     */
    public static final String DEM_SRTMGL3 = "SRTMGL3";
    /**
     * SRTM GL1 (30m)
     */
    public static final String DEM_SRTMGL1 = "SRTMGL1";
    /**
     * SRTM GL1 (Ellipsoidal)
     */
    public static final String DEM_SRTMGL1_E = "SRTMGL1_E";
    /**
     * ALOS World 3D 30m
     */
    public static final String DEM_AW3D30 = "AW3D30";
    /**
     * ALOS World 3D (Ellipsoidal)
     */
    public static final String DEM_AW3D30_E = "AW3D30_E";

    private static final String WEST = "west";
    private static final String SOUTH = "south";
    private static final String EAST = "east";
    private static final String NORTH = "north";
    private static final String OUTPUT_FORMAT = "outputFormat";
    /**
     * default
     */
    public static final String FORMAT_GTiff = "GTiff";
    /**
     * Arc ASCII Grid
     */
    public static final String FORMAT_AAIGrid = "AAIGrid";
    /**
     * Erdas Imagine (.IMG)
     */
    public static final String FORMAT_HFA = "HFA";

    //endregion

    /**
     * @param demType one of {@link #DEM_SRTMGL3}, {@link #DEM_SRTMGL1}, {@link #DEM_SRTMGL1_E}, {@link #DEM_AW3D30}, {@link #DEM_AW3D30_E}
     * @param west    west coordinate, e.g., -120.168457
     * @param south   south coordinate, e.g., 36.738884
     * @param east    east coordinate, e.g., -118.465576
     * @param north   north coordinate, e.g., 38.091337
     * @param format  one of {@link #FORMAT_GTiff}(default, i.e., when set to null), {@link #FORMAT_AAIGrid}, {@link #FORMAT_HFA}
     * @return url, e.g.,  http://opentopo.sdsc.edu/otr/getdem?demtype=SRTMGL3&west=-120.168457&south=36.738884&east=-118.465576&north=38.091337&outputFormat=GTiff
     */
    public URI buildRestURL(String demType, String west, String south, String east, String north, String format) {
        URIBuilder uriBuilder = null;
        try {
            uriBuilder = new URIBuilder(BASE_URL);
            uriBuilder.setPath("getdem");
            uriBuilder.addParameter(DEM_TYPE, demType);
            uriBuilder.addParameter(WEST, west);
            uriBuilder.addParameter(SOUTH, south);
            uriBuilder.addParameter(EAST, east);
            uriBuilder.addParameter(NORTH, north);
            if (StringUtils.isNotBlank(format)) {
                uriBuilder.addParameter(OUTPUT_FORMAT, format);
            }
            return uriBuilder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            log.error("URI syntax error");
            return null;
        }
    }


    /**
     * @param uri
     * @param filename PathUtil.getProjectRoot() + name
     * @throws IOException
     */
    public void connect(URI uri, String filename) throws IOException {
        long start = System.currentTimeMillis();

        HttpPost httpPost = new HttpPost(uri);

        CloseableHttpClient closeableHttpClient = HttpClients.custom().setConnectionManager(HttpUtils.getPoolingConnMgr()).build();
//        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

        // 设置超时时间 ms
        int timeout = 10 * 60 * 1000;
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout)
                .setConnectTimeout(timeout).setConnectionRequestTimeout(10000)
                .build();

        httpPost.setConfig(requestConfig);

        CloseableHttpResponse httpResponse = closeableHttpClient.execute(httpPost);
        byte[] bytes = EntityUtils.toByteArray(httpResponse.getEntity());
        FileUtils.writeByteArrayToFile(new File(filename), bytes);

        long time = System.currentTimeMillis() - start;
        log.debug("Time used: %d", time);
        closeableHttpClient.close();
        httpResponse.close();
    }
}
