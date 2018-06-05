package org.egc.gis.gdal.commons;

import lombok.Data;

/**
 * Corner Coordinates in degree
 *
 * @author houzhiwei
 * @date 2017/8/24 23:01
 */
@Data
public class MapExtentInDegree {
    private String upperLeftLongitude;
    private String upperLeftLatitude;

    private String upperRightLongitude;
    private String upperRightLatitude;

    private String lowerLeftLongitude;
    private String lowerLeftLatitude;

    private String lowerRightLongitude;
    private String lowerRightLatitude;

    private String centerLongitude;
    private String centerLatitude;
}
