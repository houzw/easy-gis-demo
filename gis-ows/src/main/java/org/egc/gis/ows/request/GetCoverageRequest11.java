package org.egc.gis.ows.request;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.egc.gis.ows.wcs20.WCSVersion;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/11/16 21:13
 */
@Data
public class GetCoverageRequest11 {
    private String url;
    private String identifier;
    private String bbox;
    private String format = "image/tiff";
    private WCSVersion version = WCSVersion.v1_1_0;

    // This is the only supported value for MapServer.

    private String gridBaseCrs;
    private String gridCS;
    private String gridType = "urn:ogc:def:method:WCS:1.1:2dGridIn2dCrs";
    private String gridOrigin;
    private String gridOffsets;
    private String rangeSubset;

    private GetCoverageRequest11(Builder builder) {
        url = builder.url;
        identifier = builder.identifier;
        bbox = builder.bbox;
        format = builder.format;
        gridBaseCrs = builder.gridBaseCrs;
        gridOrigin = builder.gridOrigin;
        rangeSubset = builder.rangeSubset;
    }

    public static final class Builder {
        /**
         * 1.1.0+
         */
        private String identifier;
        private String url;
        private String bbox;
        private String format;
        private String gridBaseCrs;
        private String gridOrigin;
        private String gridOffsets;
        private String rangeSubset;


        /**
         * 1.1.0+
         *
         * @param url
         * @param identifier
         * @param formatMimeType e.g., image/tiff
         */
        public Builder(String url, String identifier, String formatMimeType) {
            this.url = url;
            this.identifier = identifier;
            this.format = formatMimeType;
        }

        /**
         * 1.1.0+
         *
         * @param minx
         * @param miny
         * @param maxx
         * @param maxy
         * @param crsURN e.g., urn:ogc:def:crs:EPSG::4326
         * @return
         */
        public Builder bbox(double minx, double miny, double maxx, double maxy, String crsURN) {
            bbox = String.valueOf(minx) + "," + String.valueOf(miny) + "," + String.valueOf(maxx) + "," + String.valueOf(maxy) + "," + crsURN;
            return this;
        }

        /**
         * default crs: urn:ogc:def:crs:EPSG::4326
         * @return
         */
        public Builder bbox(double minx, double miny, double maxx, double maxy) {
            bbox = String.valueOf(minx) + "," + String.valueOf(miny) + "," + String.valueOf(maxx) + "," + String.valueOf(maxy) + ",urn:ogc:def:crs:EPSG::4326";
            return this;
        }

        /**
         * @param val The grid base CRS (URN).
         * @return
         */
        public Builder gridBaseCrs(String val) {
            gridBaseCrs = val;
            return this;
        }

        /**
         * The sample point for the top left pixel.
         *
         * @param x_origin
         * @param y_origin
         * @return
         */
        public Builder gridOrigin(double x_origin, double y_origin) {
            gridOrigin = x_origin + "," + y_origin;
            return this;
        }

        /**
         * The x and y step size for grid sampling (resolution). Both are positive.
         */
        public Builder gridOffsets(double xstep, double ystep) {
            gridOffsets = xstep + "," + ystep;
            return this;
        }

        /**
         * Selects a range subset, and interpolation method.
         * Currently only subsetting on bands are allowed.
         * Depending on rangeset names, this might take the form "BandsName[bands[1]]" to select band 1,
         * or "BandsName:bilinear[bands[1]]" to select band 1 with bilinear interpolation.
         */
        public Builder rangeSubset(String val) {
            rangeSubset = val;
            return this;
        }

        /**
         * Selects a range subset, and interpolation method.
         * Currently only subsetting on bands are allowed.
         *
         * @param interpolation interpolation method, e.g., bilinear. can be null or empty
         * @param band
         */
        public Builder rangeSubset(String interpolation, int band) {
            if (StringUtils.isNotBlank(interpolation)) {
                rangeSubset = "BandsName:" + interpolation + "[bands[" + band + "]]";
            } else {
                rangeSubset = "BandsName[bands[" + band + "]]";
            }
            return this;
        }

        public GetCoverageRequest11 build() {
            return new GetCoverageRequest11(this);
        }
        
    }
}
