package org.egc.gis.ows.wcs20;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/11/2 13:58
 */
public enum WCSVersion {
    v1_0_0("1.0.0"),
    v1_0_1("1.0.1"),
    v1_1_0("1.1.0"),
    v1_1_1("1.1.1"),
    v2_0_0("2.0.0"),
    v2_0_1("2.0.1"),
    v2_1_0("2.1.0");

    String name;

    WCSVersion(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
