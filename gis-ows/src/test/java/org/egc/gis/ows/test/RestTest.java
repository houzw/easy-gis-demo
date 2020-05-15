package org.egc.gis.ows.test;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * <pre>
 * demtype 	The global raster dataset - SRTM GL3 (90m) is 'SRTMGL3', SRTM GL1 (30m) is 'SRTMGL1', SRTM GL1 (Ellipsoidal) is 'SRTMGL1_E', and ALOS World 3D 30m is 'AW3D30', ALOS World 3D (Ellipsoidal) is 'AW3D30_E'
 * west, south, east, north 	WGS 84 bounding box coordinates
 * outputFormat 	outputFormat Output Format (optional) - GTiff for GeoTiff, AAIGrid for Arc ASCII Grid, HFA for Erdas Imagine (.IMG). Defaults to GTiff if parameter is not provided
 * </pre>
 * https://stackoverflow.com/questions/14043843/how-to-add-parameters-to-all-httpclient-request-methods
 * @author houzhiwei
 * @date 2019/9/23 9:41
 */
public class RestTest {

    private static final String DEM_TYPE = "demtype";
    private static final String WEST = "west";
    private static final String SOUTH = "south";
    private static final String EAST = "east";
    private static final String NORTH = "north";
    private static final String OUTPUT_FORMAT = "outputFormat";
    private static final String GTiff = "GTiff";
    private static final String AAIGrid = "AAIGrid";
    private static final String HFA = "HFA";

    @Test
    public void testURI() throws URISyntaxException {
        String url = "http://opentopo.sdsc.edu/otr/getdem";
        URIBuilder uriBuilder = new URIBuilder(url);

//        http://opentopo.sdsc.edu/otr/getdem?demtype=SRTMGL3&west=-120.168457&south=36.738884&east=-118.465576&north=38.091337&outputFormat=GTiff
        uriBuilder.addParameter(DEM_TYPE, "SRTMGL3");
        uriBuilder.addParameter(WEST, "-120.168457");
        uriBuilder.addParameter(SOUTH, "36.738884");
        uriBuilder.addParameter(EAST, "-118.465576");
        uriBuilder.addParameter(NORTH, "38.091337");
        uriBuilder.addParameter(OUTPUT_FORMAT, GTiff);
        System.out.println(uriBuilder.build());
    }

    @Test
    public void test() throws IOException, URISyntaxException {
        long start = System.currentTimeMillis();
        Map connParameter = new HashMap();
//        String url = "http://ot-data1.sdsc.edu:9090/otr/getdem";
        String url = "http://opentopo.sdsc.edu/otr/getdem";
        URIBuilder uriBuilder = new URIBuilder(url);

//        http://opentopo.sdsc.edu/otr/getdem?demtype=SRTMGL3&west=-120.168457&south=36.738884&east=-118.465576&north=38.091337&outputFormat=GTiff
        uriBuilder.addParameter(DEM_TYPE, "SRTMGL3");
        uriBuilder.addParameter(WEST, "-120.168457");
        uriBuilder.addParameter(SOUTH, "36.738884");
        uriBuilder.addParameter(EAST, "-118.465576");
        uriBuilder.addParameter(NORTH, "38.091337");
        uriBuilder.addParameter(OUTPUT_FORMAT, GTiff);
        System.out.println(uriBuilder.build());
        HttpGet httpGet = new HttpGet(uriBuilder.build());

        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

        // 设置超时时间
        int timeout = 10 * 60 * 1000; // ms
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout)
                .setConnectTimeout(timeout).setConnectionRequestTimeout(10000)
                .build();
//
        httpGet.setConfig(requestConfig);
//
        CloseableHttpResponse httpResponse = closeableHttpClient.execute(httpGet);
//
//         文本类型的返回
        byte[] bytes = EntityUtils.toByteArray(httpResponse.getEntity());
        FileUtils.writeByteArrayToFile(new File("H:\\gisdemo\\restdem2.tif"), bytes);

        long time = System.currentTimeMillis() - start;
        System.out.println(time);
        closeableHttpClient.close();
        httpResponse.close();
    }
}
