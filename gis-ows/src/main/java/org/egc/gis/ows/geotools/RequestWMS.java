package org.egc.gis.ows.geotools;

import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.*;
import org.geotools.ows.wms.request.GetMapRequest;
import org.geotools.ows.wms.response.GetMapResponse;
import org.geotools.ows.wms.StyleImpl;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

/**
 * TODO
 * http://docs.geotools.org/latest/userguide/extension/wms/wms.html
 *
 * @author houzhiwei
 * @date 2018/9/3 10:04
 */
public class RequestWMS {
    String url = "http://atlas.gc.ca/cgi-bin/atlaswms_en?VERSION=1.1.1&Request=GetCapabilities&Service=WMS";
    WebMapServer wms = null;

    public RequestWMS(String urlStr) {
        URL url = null;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            //will not happen
            System.out.println("MalformedURLException");
        }

        try {
            wms = new WebMapServer(url);
        } catch (IOException e) {
            //There was an error communicating with the server
            //For example, the server is down
        } catch (ServiceException e) {
            //The server returned a ServiceException (unusual in this case)
        } catch (SAXException e) {
            //Unable to parse the response from the server
            //For example, the capabilities it returned was not valid
        }
    }


    public void wms() {
        WMSCapabilities capabilities = wms.getCapabilities();
        String serverName = capabilities.getService().getName();
        String serverTitle = capabilities.getService().getTitle();
        System.out.println("Capabilities retrieved from server: " + serverName + " (" + serverTitle + ")");
        if (capabilities.getRequest().getGetFeatureInfo() != null) {
            //This server supports GetFeatureInfo requests!
            // We could make one if we wanted to.
        }
        //Get formats for GetMap operation
        List<String> formats = wms.getCapabilities().getRequest().getGetMap().getFormats();
        //gets the top most layer, which will contain all the others
        Layer rootLayer = capabilities.getLayer();

        //gets all the layers in a flat list, in the order they appear in
        //the capabilities document (so the rootLayer is at index 0)
        List layersList = capabilities.getLayerList();
        //To retrieve all the layers that can be requested (i.e. they have a name)
        Layer[] layers = WMSUtils.getNamedLayers(capabilities);

    }

    public void styles(Layer[] layers) {
        //For each layer you can get its available styles:

        for (int i = 0; i < layers.length; i++) {
            // Print layer info
            System.out.println("Layer: (" + i + ")" + layers[i].getName());
            System.out.println("       " + layers[i].getTitle());
            System.out.println("       " + layers[i].getChildren().length);
            System.out.println("       " + layers[i].getBoundingBoxes());
            CRSEnvelope env = layers[i].getLatLonBoundingBox();
            System.out.println("       " + env.getLowerCorner() + " x " + env.getUpperCorner());

            // Get layer styles
            List styles = layers[i].getStyles();
            for (Iterator it = styles.iterator(); it.hasNext(); ) {
                StyleImpl elem = (StyleImpl) it.next();
                // Print style info
                System.out.println("Style:");
                System.out.println("  Name:" + elem.getName());
                System.out.println("  Title:" + elem.getTitle());
            }
        }
    }

    public void maprequest() throws IOException, ServiceException {
        GetMapRequest request = wms.createGetMapRequest();
        request.setFormat("image/png");
        request.setDimensions("583", "420"); //sets the dimensions of the image to be returned from the server
        request.setTransparent(true);
        request.setSRS("EPSG:4326");
        request.setBBox("-131.13151509433965,46.60532747661736,-117.61620566037737,56.34191403281659");
        //Note: you can look at the layer metadata to determine a layer's bounding box for a SRS
        WMSCapabilities capabilities = wms.getCapabilities();
        for ( Layer layer : WMSUtils.getNamedLayers(capabilities) ) {
            request.addLayer(layer);
        }
        GetMapResponse response = (GetMapResponse) wms.issueRequest(request);
        BufferedImage image = ImageIO.read(response.getInputStream());


        Layer[] layers = WMSUtils.getNamedLayers(capabilities);
        List styles = layers[2].getStyles();
        StyleImpl style = (StyleImpl) styles.get(2);

        request.addLayer(layers[2]);
        request.addLayer(layers[2], style);
        request.addLayer(layers[2], style.getName());
        request.addLayer(layers[2].getName(), style);
        //Get formats for GetMap operation
        List<String> formats = wms.getCapabilities().getRequest().getGetMap().getFormats();    }
}
