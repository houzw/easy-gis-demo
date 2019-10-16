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
    /**
     * The constant REQUEST.
     */
    String REQUEST = "request";
    /**
     * The constant SERVICE.
     */
    String SERVICE = "service";
    /**
     * The constant WCS.
     */
    String WCS = "WCS";
    /**
     * The constant WFS.
     */
    String WFS = "WFS";
    /**
     * The constant WMS.
     */
    String WMS = "WMS";
    /**
     * The constant WMST.
     */
    String WMST = "WMST";
    /**
     * The constant VERSION.
     */
    String VERSION = "version";
    /**
     * The constant GET_COVERAGE.
     */
    String GET_COVERAGE = "GetCoverage";
    /**
     * The constant COVERAGE_ID.
     */
    String COVERAGE_ID = "coverageId";
    /**
     * The constant DESCRIBE_COVERAGE.
     */
    String DESCRIBE_COVERAGE = "DescribeCoverage";
    /**
     * The constant DESCRIBE_FEATURE_TYPE.
     */
    String DESCRIBE_FEATURE_TYPE = "DescribeFeatureType";
    /**
     * The constant GET_CAPABILITIES.
     */
    String GET_CAPABILITIES = "GetCapabilities";
    String GET_FEATURE = "GetFeature";
    /**
     * The constant GET_STYLES.
     */
    String GET_STYLES = "GetStyles";
    /**
     * The constant VERSION_201.
     */
    String VERSION_201 = "2.0.1";
    /**
     * The constant VERSION_100.
     */
    String VERSION_100 = "1.0.0";
    /**
     * The constant VERSION_110.
     */
    String VERSION_110 = "1.1.0";
    /**
     * The constant VERSION_111.
     */
    String VERSION_111 = "1.1.1";


}
