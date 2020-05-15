package org.egc.gis.ows.wcs20;

import lombok.Data;

/**
 * Description:
 * <pre>
 * v2_0_1
 * recommended version
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/11/16 21:13
 */
@Data
public class GetCoverageRequest {
    private String url;
    private String coverageId;
    private String format = "image/tiff";
    private WCSVersion version = WCSVersion.v2_0_1;

    private String interpolation;
    private String scaleAxes;
    private float scaleFactor;
    private String scaleSize;
    private String outputCrs;
    private String subsettingCrs = "imageCRS";
    private String scaleExtent;
    private String subsetX;
    private String subsetY;
    private String rangeSubset;
    // when multipart XML/image output is desired. It should be set to 'multipart/related'
    // (which is currently the only possible value for this parameter).
    private String mediatype = "multipart/related";

    private GetCoverageRequest(Builder builder) {
        url = builder.url;
        coverageId = builder.coverageId;
        format = builder.format;
        outputCrs = builder.outputCrs;
        scaleAxes = builder.scaleAxes;
        scaleFactor = builder.scaleFactor;
        subsettingCrs = builder.subsettingCrs;
        interpolation = builder.interpolation;
        scaleSize = builder.scaleSize;
        scaleExtent = builder.scaleExtent;
        subsetX = builder.subsetX;
        subsetY = builder.subsetY;
        rangeSubset = builder.rangeSubset;
    }

    public static final class Builder {

        private String coverageId;
        private String url;
        private String format;
        private String outputCrs;
        private float scaleFactor;
        private String scaleAxes;
        private String subsettingCrs;
        private String interpolation;
        private String scaleSize;
        private String scaleExtent;
        private String subsetX;
        private String subsetY;
        private String rangeSubset;


        /**
         * @param url
         * @param coverageId
         * @param formatMimeType e.g., image/tiff
         */
        public Builder(String url, String coverageId, String formatMimeType) {
            this.url = url;
            this.coverageId = coverageId;
            this.format = formatMimeType;
        }

        public Builder(String url, String coverageId) {
            this.url = url;
            this.coverageId = coverageId;
        }

        /**
         * optional<br/>
         * This parameter defines in which crs the output image should be expressed in.
         */
        public Builder outputCrs(String val) {
            outputCrs = val;
            return this;
        }

        /**
         * optional<br/>
         * With this parameter (a positive float) the size of the output image can be adjusted. All axes are scaled equally.
         */
        public Builder scaleFactor(float factor) {
            scaleFactor = factor;
            return this;
        }

        /**
         * optional<br/>
         * axis(value)[,axis(value)]: With this parameter, an axis specific scaling can be applied. Any axis not mentioned
         */
        public Builder scaleAxes(String val) {
            scaleAxes = val;
            return this;
        }

        /**
         * optional<br/>
         *
         * @param val
         * @return
         */
        public Builder scaleSize(String val) {
            scaleSize = val;
            return this;
        }

        /**
         * e.g., http://www.opengis.net/def/crs/EPSG/0/4326
         * This parameter defines the crs subsetting all SUBSETs are expressed in,
         * and also the output CRS if no OUTPUTCRS is specified.
         * By default all subsets are interpreted to be relative to the coverages CRS.
         */
        public Builder subsettingCrs(String val) {
            subsettingCrs = val;
            return this;
        }

        /**
         * optional<br/>
         * intperolation_method: This defines the interpolation method used, for rescaled images.
         * Possible values are "NEAREST", "BILINEAR" and "AVERAGE".
         */
        public Builder interpolation(String val) {
            interpolation = val;
            return this;
        }

        /**
         * optional<br/>
         * axis(min:max)[,axis(min:max)]: This parameter is treated like a SCALESIZE parameter with "axis(max-min)".
         */
        public Builder scaleExtent(String val) {
            scaleExtent = val;
            return this;
        }

        /**
         * axis[,crs](low,high): This parameter subsets the coverage on the given axis.
         * This parameter can be given multiple times, but only once for each axis.
         * The optional sub-parameter crs can either be an EPSG definition (like EPSG:4326), an URN or an URI or 'imageCRS' (which is the default).
         * All crs sub-parameters from all SUBSET parameters must be equal.
         * (e.g: you cannot subset one axis in imageCRS and another in EPSG:4326).
         */
        public Builder subsetX(double low, double high) {
            subsetX = "X(" + low + "," + high + ")";
            return this;
        }

        public Builder subsetY(double low, double high) {
            subsetY = "Y(" + low + "," + high + ")";
            return this;
        }


        public GetCoverageRequest build() {
            return new GetCoverageRequest(this);
        }
    }
}
