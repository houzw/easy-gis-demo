package org.egc.gis.ows;

/**
 * Description:
 * <pre>
 * OGC Web Service
 * </pre>
 *
 * @author houzhiwei
 * @date 2019 /10/2 20:09
 */
public interface OWSService {

    String REQUEST = "request";

    String SERVICE = "service";

    String WCS = "WCS";

    String WFS = "WFS";

    String WMS = "WMS";

    String WMST = "WMST";

    String VERSION = "version";

    String GET_COVERAGE = "GetCoverage";

    String COVERAGE_ID = "coverageId";

    String DESCRIBE_COVERAGE = "DescribeCoverage";

    String DESCRIBE_FEATURE_TYPE = "DescribeFeatureType";

    String GET_CAPABILITIES = "GetCapabilities";
    String GET_FEATURE = "GetFeature";

    String GET_STYLES = "GetStyles";

    String VERSION_201 = "2.0.1";

    String VERSION_100 = "1.0.0";

    String VERSION_110 = "1.1.0";

    String VERSION_111 = "1.1.1";

}
