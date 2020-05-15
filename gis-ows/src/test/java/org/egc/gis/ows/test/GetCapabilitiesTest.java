package org.egc.gis.ows.test;

import com.alibaba.fastjson.JSON;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.egc.gis.ows.KeywordsFilter;
import org.egc.gis.ows.OwsUtils;
import org.egc.gis.ows.Consts;
import org.egc.gis.ows.wcs20.DescribeCoverage;
import org.egc.gis.ows.wcs20.GetCapabilities;
import org.geotools.wcs.WCSConfiguration;
import org.geotools.xsd.Parser;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Map;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/11/3 11:18
 */
public class GetCapabilitiesTest {
    Parser parser = new Parser(new WCSConfiguration());

    @Test
    public void testParseGetCapabilities() throws IOException, ParserConfigurationException, SAXException {
        Map<String, Object> document = (Map<String, Object>) parser.parse(this.getClass().getClassLoader().getResourceAsStream("getcaps.xml"));
//        printKVP(document);
        String version = (String) document.get(Consts.VERSION);
        GetCapabilities getCapabilities = JSON.parseObject(JSON.toJSONString(document, new KeywordsFilter()), GetCapabilities.class);
        System.out.println(getCapabilities);

    }


    @Test
    public void testParse10GetCapabilities() throws DocumentException {
        Document document = OwsUtils.readXml(this.getClass().getClassLoader().getResource("GetCapabilities100.xml").getPath());
        System.out.println(JSON.toJSONString(OwsUtils.dom2Map(document), true));
        org.egc.gis.ows.wcs10.GetCapabilities getCapabilities = JSON.parseObject(JSON.toJSONString(OwsUtils.dom2Map(document)), org.egc.gis.ows.wcs10.GetCapabilities.class);
        System.out.println(getCapabilities);

        //        printKVP(document);
//        String version = (String) document.get(Consts.VERSION);
    }

    @Test
    public void testParseDescribeCoverage() throws DocumentException {

        Document document = OwsUtils.readXml(this.getClass().getClassLoader().getResource("DescribeCoverage2.xml").getPath());
        System.out.println(JSON.toJSONString(OwsUtils.dom2Map(document), true));
        DescribeCoverage describeCoverage = JSON.parseObject(JSON.toJSONString(OwsUtils.dom2Map(document)), DescribeCoverage.class);
        System.out.println(describeCoverage);
    }

    /*
    http://saocompute.eurac.edu/rasdaman/ows?
    &SERVICE=WCS&VERSION=2.0.1
    &REQUEST=GetCoverage
    &COVERAGEID=MOSAIC_TEST
    &SUBSET=Y(2137248.24574,2163828.24574)
    &SUBSET=X(3843270.76798,3844640.76798)
    &FORMAT=image/tiff
    &MEDIATYPE=multipart/related
    * */
    private void printKVP(Map<String, Object> document) {
        for (String key : document.keySet()) {
            System.out.print(key + ": ");
            System.out.println(document.get(key));
        }
    }
}
