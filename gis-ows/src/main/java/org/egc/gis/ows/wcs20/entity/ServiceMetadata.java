package org.egc.gis.ows.wcs20.entity;

import lombok.Data;

import java.util.List;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/11/4 15:15
 */
@Data
public class ServiceMetadata {
    private List<String> formatSupported;
    private Extension extension;

    @Data
    public static class Extension {
        public static final String crsUrl = "http://www.opengis.net/def/crs/EPSG/0/";
        public static final String interpolationUrl = "http://www.opengis.net/def/interpolation/OGC/1/";
        private List<String> crsSupported;
        private List<String> interpolationSupported;
    }
}
