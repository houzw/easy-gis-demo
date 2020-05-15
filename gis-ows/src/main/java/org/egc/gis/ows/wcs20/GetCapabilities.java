package org.egc.gis.ows.wcs20;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.egc.gis.ows.wcs20.entity.*;

/**
 * http://schemas.opengis.net/wcs/
 *
 * @author houzhiwei
 */
@Data
public class GetCapabilities {
    @JSONField(serialize = false, deserialize = false)
    public static final String schemaLocation = "http://schemas.opengis.net/wcs/2.0/wcsGetCapabilities.xsd";
    private String updateSequence;
    private String version;
    private OperationsMetadata operationsMetadata;
    private ServiceMetadata serviceMetadata;
    private ServiceIdentification serviceIdentification;
    private ServiceProvider serviceProvider;
    private Contents contents;
}
