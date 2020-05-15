package org.egc.gis.ows.wcs10.entity;

import lombok.Data;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/11/14 20:57
 */
@Data
public class Capability {
    private CapabilityRequest request;
    private CapabilityException exception;

    @Data
    public static class CapabilityException {
        private String format;
    }
}
