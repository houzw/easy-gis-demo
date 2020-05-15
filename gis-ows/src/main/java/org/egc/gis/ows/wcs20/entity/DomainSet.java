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
 * @date 2019/11/7 20:01
 */
@Data
public class DomainSet {
    private RectifiedGrid rectifiedGrid;
    private String dimension;
    private String id;

    @Data
    public static class RectifiedGrid {
        private Limits limits;
        private String axisLabels;
        private List<String> offsetVector;
        private String srsName;
        private Origin origin;
    }

    @Data
    public static class Origin {
        private String srsName;
        private String id;
        private Point point;
    }

    @Data
    public static class Point {
        private String pos;

        public double[] posxy() {
            return new double[]{Double.valueOf(pos.split(" ")[0]), Double.valueOf(pos.split(" ")[1])};
        }
    }

    @Data
    public static class Limits {
        private GridEnvelope gridEnvelope;
    }

    @Data
    public static class GridEnvelope {
        private String high;
        private String low;

        public double[] highxy() {
            return new double[]{Double.valueOf(high.split(" ")[0]), Double.valueOf(high.split(" ")[1])};
        }

        public double[] lowxy() {
            return new double[]{Double.valueOf(low.split(" ")[0]), Double.valueOf(low.split(" ")[1])};
        }
    }


}
