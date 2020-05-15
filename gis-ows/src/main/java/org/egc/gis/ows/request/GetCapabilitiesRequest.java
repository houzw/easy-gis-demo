package org.egc.gis.ows.request;

import lombok.Data;
import org.egc.gis.ows.wcs20.WCSVersion;

import java.util.List;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/11/16 20:00
 */
@Data
public class GetCapabilitiesRequest {
    private String url;
    private WCSVersion version;
    private List<WCSVersion> acceptVersions;
    private String section;
    private String updateSequence;
    private List<String> acceptFormats;

    private GetCapabilitiesRequest(Builder builder) {
        setUrl(builder.url);
        setVersion(builder.version);
        setAcceptVersions(builder.acceptVersions);
        setSection(builder.section);
        setUpdateSequence(builder.updateSequence);
        setAcceptFormats(builder.acceptFormats);
    }

    public static final class Builder {
        private String url;
        private WCSVersion version;
        private List<WCSVersion> acceptVersions;
        private String section;
        private String updateSequence;
        private List<String> acceptFormats;

        public Builder(String url, WCSVersion version) {
            this.url = url;
            this.version = version;
        }

        public Builder version(WCSVersion val) {
            version = val;
            return this;
        }

        /**
         * 1.1.0+
         * @param val
         * @return
         */
        public Builder acceptVersions(List<WCSVersion> val) {
            acceptVersions = val;
            return this;
        }

        /**
         * / or
         * /WCS_Capabilities/Service or
         * /WCS_Capabilities/Capability or
         * /WCS_Capabilities/ContentMetadata
         * @param val
         * @return
         */
        public Builder section(String val) {
            section = val;
            return this;
        }

        public Builder updateSequence(String val) {
            updateSequence = val;
            return this;
        }

        /**
         * 1.1.0+
         * @param val
         * @return
         */
        public Builder acceptFormats(List<String> val) {
            acceptFormats = val;
            return this;
        }

        public GetCapabilitiesRequest build() {
            return new GetCapabilitiesRequest(this);
        }
    }
}
