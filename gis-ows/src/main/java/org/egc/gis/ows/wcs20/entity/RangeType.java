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
 * @date 2019/11/7 20:03
 */
@Data
public class RangeType {
    private DataRecord DataRecord;

    @Data
    public static class DataRecord {
        private List<Field> field;
        private String name;
    }

    @Data
    public static class Field {
        private Quantity quantity;
        private String definition;
    }

    @Data
    public static class Quantity {
        private String label;
        private String description;
        private String uom;
        private String code;
        private String constraint;
    }
}

