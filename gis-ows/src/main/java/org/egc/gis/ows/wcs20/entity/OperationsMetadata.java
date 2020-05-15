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
 * @date 2019/11/4 20:50
 */
@Data
public class OperationsMetadata {
    private Constraint constraint;
    private List<Operation> operation;

    @Data
    public static class Operation {
        private List<String> dcp;
        private String name;
    }

    @Data
    public static class Constraint {
        private List<String> allowedValues;
        private String name;
    }
}
