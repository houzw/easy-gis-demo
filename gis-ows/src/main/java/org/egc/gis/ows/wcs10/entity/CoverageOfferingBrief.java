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
 * @date 2019/11/14 20:31
 */
@Data
public class CoverageOfferingBrief {
    private String srsName;
    private String name;
    private String description;
    private String href;
    private String metadataLink;
    private String label;
    private String type;
    private String metadataType;
    private Keywords keywords;
    private LonLatEnvelope lonLatEnvelope;
}
