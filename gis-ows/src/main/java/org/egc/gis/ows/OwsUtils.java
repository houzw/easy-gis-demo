package org.egc.gis.ows;


import lombok.extern.slf4j.Slf4j;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019 /11/5 20:27
 */
@Slf4j
public class OwsUtils {

    /**
     * Gets epsg crs.
     *
     * @param crsSupported e.g., "http://www.opengis.net/def/crs/EPSG/0/3857"
     * @return the crs, e.g., "EPSG:3857"
     */
    public static String getCrs(String crsSupported) {
        return "EPSG:" + crsSupported.replace("http://www.opengis.net/def/crs/EPSG/0/", "");
    }

    /**
     * @param interpolationSupported e.g., http://www.opengis.net/def/interpolation/OGC/1/linear
     * @return e.g., linear
     */
    public static String getInterpolation(String interpolationSupported) {
        return interpolationSupported.replace("http://www.opengis.net/def/interpolation/OGC/1/", "");
    }

    public static Document readXml(String xmlPath) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(xmlPath));
        return document;
    }

    public static Map dom2Map(Document doc) {
        Map map = new HashMap();
        if (doc == null) {
            return map;
        }
        Element root = doc.getRootElement();
        for (Iterator iterator = root.attributeIterator(); iterator.hasNext(); ) {
            Attribute attr = (Attribute) iterator.next();
            map.put(attr.getName(), attr.getValue());
        }
        for (Iterator iterator = root.elementIterator(); iterator.hasNext(); ) {
            Element e = (Element) iterator.next();
            List list = e.elements();
            if (list.size() > 0) {
                map.put(e.getName(), dom2Map(e));
            } else {
                map.put(e.getName(), e.getText());
            }
        }
        return map;
    }

    public static Map dom2Map(Element e) {
        Map map = new HashMap();
        List list = e.elements();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Element iter = (Element) list.get(i);
                map = attributes2Map(iter.attributes(),map);
                List mapList = new ArrayList();
                if (iter.elements().size() > 0) {
                    Map m = dom2Map(iter);
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(m);
                        }
                        if (obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = (List) obj;
                            mapList.add(m);
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        map.put(iter.getName(), m);
                    }
                } else {
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(iter.getText());
                        }
                        if (obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = (List) obj;
                            mapList.add(iter.getText());
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        map.put(iter.getName(), iter.getText());
                    }
                }
            }
        } else {
            map.put(e.getName(), e.getText());
        }
        return map;
    }

    public static Map attributes2Map(List<Attribute> attrs, Map map) {
        for (Attribute attr : attrs) {
            map.put(attr.getName(), attr.getValue());
        }
        return map;
    }


    public static Map parseXml2Map(String xmlStr) {
        Map map = new HashMap();
        String strTitle = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Document document = null;
        try {
            if (xmlStr.indexOf("<?xml") < 0) {
                xmlStr = strTitle + xmlStr;
            }
            document = DocumentHelper.parseText(xmlStr);

        } catch (DocumentException e) {
            log.error("parse xml error", e);
        }
        Element elNode = document.getRootElement();
        for (Iterator it = elNode.elementIterator(); it.hasNext(); ) {
            Element leaf = (Element) it.next();
            map.put(leaf.getName().toLowerCase(), leaf.getData());
        }
        return map;
    }

    /****************************************************************************/
    public static Map xml2Map2(String xmlPath) throws JDOMException, IOException {
        SAXBuilder sbder = new SAXBuilder();
        Map<String, Object> map = new HashMap<String, Object>();
        org.jdom2.Document document = sbder.build(new File(xmlPath));
        org.jdom2.Element el = document.getRootElement();
        List<org.jdom2.Element> eList = el.getChildren();
        Map<String, Object> rootMap = new HashMap<String, Object>();
        rootMap = xmlToMap(eList, rootMap);
        map.put(el.getName(), rootMap);
        return map;
    }

    public static Map<String, Object> xmlToMap(List<org.jdom2.Element> eList, Map<String, Object> map) {
        for (org.jdom2.Element e : eList) {
            Map<String, Object> eMap = new HashMap<String, Object>();
            List<org.jdom2.Element> elementList = e.getChildren();
            if (elementList != null && elementList.size() > 0) {
                eMap = xmlToMap(elementList, eMap);
                Object obj = map.get(e.getName());
                if (obj != null) {
                    List<Object> olist = new ArrayList<Object>();
                    if (obj.getClass().getName().equals("java.util.HashMap")) {
                        olist.add(obj);
                        olist.add(eMap);
                    } else if (obj.getClass().getName().equals("java.util.ArrayList")) {
                        olist = (List<Object>) obj;
                        olist.add(eMap);
                    }
                    map.put(e.getName(), olist);
                } else {
                    map.put(e.getName(), eMap);
                }
            } else {
                map.put(e.getName(), e.getValue());
            }
        }
        return map;
    }
}
