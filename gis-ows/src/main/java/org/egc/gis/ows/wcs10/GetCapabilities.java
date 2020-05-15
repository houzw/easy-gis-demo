package org.egc.gis.ows.wcs10;

import lombok.Data;
import org.egc.gis.ows.wcs10.entity.Capability;
import org.egc.gis.ows.wcs10.entity.ContentMetadata;
import org.egc.gis.ows.wcs10.entity.Service;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/11/5 21:22
 */
@Data
public class GetCapabilities {
    private String schemaLocation;
    private String updateSequence;
    private String version;
    private Capability capability;
    private Service service;
    private ContentMetadata contentMetadata;
}
