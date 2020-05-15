package org.egc.gis.ows.wcs20;

import lombok.Data;
import org.egc.gis.ows.wcs20.entity.CoverageDescription;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/11/5 21:22
 */
@Data
public class DescribeCoverage {
    private String schemaLocation;
    private CoverageDescription coverageDescription;
}
