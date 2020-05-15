package org.egc.gis.ows.wcs20.entity;

import lombok.Data;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/11/6 20:09
 */
@Data
public class CoverageDescription {
    private BoundedBy boundedBy;
    private String coverageId;
    private ServiceParameters ServiceParameters;
    private Metadata metadata;
    private DomainSet domainSet;
    private CoverageFunction coverageFunction;

    @Data
    public static class ServiceParameters {
        private String coverageSubtype;
        private String nativeFormat;
    }

    @Data
    public static class Metadata {
        private Extension extension;
    }

    @Data
    public static class Extension {
        private CovMetadata covMetadata;
    }

    @Data
    public static class CovMetadata {
        private String creator;
        private String project;
        private String title;
    }


}
