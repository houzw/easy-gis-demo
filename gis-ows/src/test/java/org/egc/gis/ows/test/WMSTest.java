package org.egc.gis.ows.test;

import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.WMSCapabilities;
import org.geotools.ows.wms.WebMapServer;
import org.geotools.ows.wms.map.WMSLayer;
import org.geotools.ows.wms.request.GetMapRequest;
import org.geotools.ows.wms.response.GetMapResponse;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Description:
 * <pre>
 * http://docs.geotools.org/latest/userguide/tutorial/raster/image.html
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/9/22 22:44
 */
public class WMSTest {
    @Test
    public void test() throws IOException, ServiceException {

        URL url = new URL("http://atlas.gc.ca/cgi-bin/atlaswms_en?VERSION=1.1.1&Request=GetCapabilities&Service=WMS");

        WebMapServer wms = new WebMapServer(url);
        WMSCapabilities capabilities = wms.getCapabilities();

        List<Layer> wmsLayers = capabilities.getLayerList();

        for (Layer wmsLayer : wmsLayers) {
            WMSLayer displayLayer = new WMSLayer(wms, wmsLayer);
        }

        GetMapRequest request = wms.createGetMapRequest();
        request.setFormat("image/png");
        request.setDimensions("583", "420"); //sets the dimensions to be returned from the server
        request.setTransparent(true);
        request.setSRS("EPSG:4326");
        request.setBBox("-131.13151509433965,46.60532747661736,-117.61620566037737,56.34191403281659");

        GetMapResponse response = (GetMapResponse) wms.issueRequest(request);
        BufferedImage image = ImageIO.read(response.getInputStream());

    }
}
