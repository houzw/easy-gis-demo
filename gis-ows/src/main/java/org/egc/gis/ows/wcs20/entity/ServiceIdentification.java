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
 * @date 2019/11/4 15:25
 */
@Data
public class ServiceIdentification {
    private String title;
    private String Abstract;
    private String serviceType;
    private List<String> serviceTypeVersion;
    private List<String> keywords;
    private List<String> profile;
    private String fees;
    private String accessConstraints;
}
