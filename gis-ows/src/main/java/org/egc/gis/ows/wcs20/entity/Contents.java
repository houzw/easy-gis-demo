package org.egc.gis.ows.wcs20.entity;

import lombok.Data;
import org.egc.gis.ows.Keywords;

import java.util.List;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/11/4 15:20
 */
@Data
public class Contents {
    private List<CoverageSummary> coverageSummary;

    @Data
    public static class CoverageSummary {
        private String title;
        private String abstractContent;
        private String coverageId;
        private String coverageSubtype;
        private Metadata metadata;
        private Keywords keywords;
        private CoverageBoundingBox boundingBox;
        private CoverageBoundingBox WGS84BoundingBox;
    }

    @Data
    public static class Metadata {
        private String href;
        private String type;
    }
}
