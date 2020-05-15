package org.egc.gis.ows.wcs20.entity;

import lombok.Data;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/11/4 15:27
 */
@Data
public class ServiceProvider {
    private String providerName;
    private String providerSite;
    private ServiceContact serviceContact;
}
