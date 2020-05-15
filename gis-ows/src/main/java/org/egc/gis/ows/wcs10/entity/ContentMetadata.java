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
 * @date 2019/11/14 20:29
 */
@Data
public class ContentMetadata {
    private List<CoverageOfferingBrief> coverageOfferingBrief;
}
