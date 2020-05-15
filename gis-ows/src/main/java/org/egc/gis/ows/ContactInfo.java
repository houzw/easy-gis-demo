package org.egc.gis.ows;

import lombok.Data;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/11/14 21:09
 */
@Data
public class ContactInfo {
    private String phone;
    private Address address;
    private String onlineResource;

    @Data
    public static class Address {
        private String postalCode;
        private String administrativeArea;
        private String deliveryPoint;
        private String city;
        private String country;
        private String electronicMailAddress;
    }
}
