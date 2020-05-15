package org.egc.gis.ows.wcs10.entity;

import lombok.Data;
import org.egc.gis.ows.ContactInfo;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/11/14 21:06
 */
@Data
public class ResponsibleParty {
    private String positionName;
    private String organisationName;
    private ContactInfo contactInfo;
    private String individualName;
}
