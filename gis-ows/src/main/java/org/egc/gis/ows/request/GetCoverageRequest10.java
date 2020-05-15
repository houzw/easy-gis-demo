package org.egc.gis.ows.request;

import lombok.Data;
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
public class GetCoverageRequest10 {

    private String url;
    private String coverage;
    private String crs;
    private String bbox;
    private String format;

    private WCSVersion version = WCSVersion.v1_0_0;
    private String response_crs;
    private String time;
    private int height;
    private int width;
    private double resx;
    private double resy;

    private GetCoverageRequest10(Builder builder) {
        url = builder.url;
        coverage = builder.coverage;
        crs = builder.crs;
        bbox = builder.bbox;
        format = builder.format;
        response_crs = builder.response_crs;
        time = builder.time;
        height = builder.height;
        width = builder.width;
        resx = builder.resx;
        resy = builder.resy;
    }

    public static final class Builder {
        private String coverage;
        private String crs;
        private String url;
        private String bbox;
        private String format;
        private String response_crs;
        private String time;
        private int height;
        private int width;
        private double resx;
        private double resy;

        /**
         * @param url      request url
         * @param coverage Name of an available coverage, as stated in the GetCapabilities
         * @param crs      Coordinate Reference System in which the request is expressed.
         * @param format   Output format of map, as stated in the DescribeCoverage response.
         */
        public Builder(String url, String coverage, String crs, String format) {
            this.url = url;
            this.coverage = coverage;
            this.crs = crs;
            this.format = format;
        }

        /**
         * minx,miny,maxx,maxy: Bounding box corners (lower left, upper right) in CRS units. One of BBOX or TIME is required.
         */
        public Builder bbox(double minx, double miny, double maxx, double maxy) {
            bbox = String.valueOf(minx) + "," + String.valueOf(miny) + "," + String.valueOf(maxx) + "," + String.valueOf(maxy);
            return this;
        }


        /**
         * @param val Coordinate Reference System in which to express coverage responses.
         * @return
         */
        public Builder response_crs(String val) {
            response_crs = val;
            return this;
        }

        /**
         * "TIME=2016-01-01",
         * "TIME=2016-01-01/2016-02-01/P1D".
         *
         * @param val
         * @return
         */
        public Builder time(String val) {
            time = val;
            return this;
        }

        /**
         * @param val Height in pixels of map picture. One of WIDTH/HEIGHT or RESX/Y is required.
         * @return
         */
        public Builder height(int val) {
            height = val;
            return this;
        }

        /**
         * @param val Width in pixels of map picture. One of WIDTH/HEIGHT or RESX/Y is required.
         * @return
         */
        public Builder width(int val) {
            width = val;
            return this;
        }

        /**
         * @param val When requesting a georectified grid coverage, this requests a subset with a specific spatial resolution.
         *            One of WIDTH/HEIGHT or RESX/Y is required
         * @return
         */
        public Builder resx(double val) {
            resx = val;
            return this;
        }

        /**
         * @param val When requesting a georectified grid coverage, this requests a subset with a specific spatial resolution.
         *            One of WIDTH/HEIGHT or RESX/Y is required.
         * @return
         */
        public Builder resy(double val) {
            resy = val;
            return this;
        }

        public GetCoverageRequest10 build() {
            return new GetCoverageRequest10(this);
        }
    }
}
