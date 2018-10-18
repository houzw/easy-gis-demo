package org.egc.gis.crs;

/**
 * Description:
 * <pre>
 * https://epsg.io/?q=China
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/10/7 17:02
 */
public enum ChinaEPSGEnum {
    CGCS2000_120E_3DEGREE(4490, "CGCS2000 / 3-degree Gauss-Kruger CM 120E"),
    CGCS2000(4479, "China Geodetic Coordinate System 2000"),
    Beijing54_117E_3DEGREE(2436, "Beijing 1954 / 3-degree Gauss-Kruger CM 117E"),
    Beijing54_117E(21460, "Beijing 1954 / Gauss-Kruger CM 117E"),
    Beijing54(4214, "Beijing 1954"),
    Xian80(4610, "Xian 1980");

    private int epsg;
    private String name;

    private ChinaEPSGEnum(int epsg, String name) {
        this.epsg = epsg;
        this.name = name;
    }

    public int getEpsg() {
        return epsg;
    }

    public String getName() {
        return name;
    }
}
