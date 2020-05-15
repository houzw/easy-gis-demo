package org.egc.gis.ows.wcs20.entity;

import lombok.Data;
import org.egc.gis.ows.ContactInfo;

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
public class ServiceContact {
    private String individualName;
    private String positionName;
    private ContactInfo contactInfo;
}
