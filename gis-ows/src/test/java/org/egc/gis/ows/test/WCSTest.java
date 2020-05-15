package org.egc.gis.ows.test;

import net.opengis.wcs20.GetCoverageType;
import net.opengis.wcs20.Wcs20Factory;
import net.opengis.wcs20.impl.Wcs20FactoryImpl;
import org.geotools.wcs.WCSConfiguration;
import org.geotools.xsd.Parser;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Description:
 * <pre>
 * https://docs.geoserver.org/stable/en/user/services/wcs/requestbuilder.html
 * https://github.com/geotools/geotools/blob/master/modules/extension/xsd/xsd-wcs/src/test/java/org/geotools/wcs/v2_0/GetCoverageTest.java
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/10/30 15:50
 */
public class WCSTest {
    Parser parser = new Parser(new WCSConfiguration());
    String baseurl = "https://geoservice.dlr.de/eoc/elevation/wcs?SERVICE=WCS&REQUEST=GetCapabilities";
    String url = "http://gisserver.domain.com:6080/arcgis/services/World/Temperature/ImageServer/WCSServer";



    @Test
    public void testURLs() throws URISyntaxException {
//        URI uri = WCS20Client.describeCoverageURL(url, WCSVersion.v1_0_0, "1");
//        System.out.println(uri);
    }

    @Test
    public void testGetCoverage() throws IOException {
        Wcs20Factory factory = Wcs20FactoryImpl.init();
        GetCoverageType gc = factory.createGetCoverageType();

    }


}
