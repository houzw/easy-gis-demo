package org.egc.gis.ows.wcs20.entity;

import lombok.Data;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/11/7 20:06
 */
@Data
public class CoverageFunction {
    private GridFunction gridFunction;

    @Data
    public static class GridFunction {
        private String sequenceRule;
        private String axisOrder;
        private String startPoint;

        public double[] startPointXy() {
            return new double[]{Double.valueOf(startPoint.split(" ")[0]), Double.valueOf(startPoint.split(" ")[1])};
        }

    }
}
