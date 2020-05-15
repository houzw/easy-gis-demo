package org.egc.gis.ows.wcs10.entity;

import lombok.Data;

import java.util.List;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/11/14 20:59
 */
@Data
public class CapabilityRequest {
    private GetCoverageRequest getCoverage;
    private GetCapabilitiesRequest getCapabilities;
    private DescribeCoverageRequest describeCoverage;

    @Data
    public static class GetCoverageRequest {
        private List<DCPType> DCPType;
    }

    @Data
    public static class GetCapabilitiesRequest {
        private List<DCPType> DCPType;
    }

    @Data
    public static class DescribeCoverageRequest {
        private List<DCPType> DCPType;
    }

    @Data
    public static class DCPType {
        private DCPTypeHTTP HTTP;
    }

    @Data
    public static class DCPTypeHTTP {
        private HTTPGet get;
        private HTTPGet post;
    }

    @Data
    public static class HTTPGet {
        private String onlineResource;
        private String href;
    }

    @Data
    public static class HTTPPost {
        private String onlineResource;
        private String href;
    }
}
