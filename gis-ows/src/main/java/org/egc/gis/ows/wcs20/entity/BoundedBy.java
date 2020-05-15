package org.egc.gis.ows.wcs20.entity;

import lombok.Data;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/11/7 19:58
 */
@Data
public class BoundedBy {
    private CoverageBoundingBox envelope;
    private String srsName;
    private String srsDimension;
    private String axisLabels;
    private String uomLabels;
}
