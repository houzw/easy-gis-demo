package org.egc.gis.ows.wcs10.entity;

import lombok.Data;
import org.egc.gis.ows.Keywords;

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
public class Service {
    private String fees;
    private String about;
    private String name;
    private Keywords keywords;
    private ResponsibleParty responsibleParty;
    private String description;
    private String metadataLink;
    private String label;
    private String type;
    private String accessConstraints;
    private String metadataType;
}
