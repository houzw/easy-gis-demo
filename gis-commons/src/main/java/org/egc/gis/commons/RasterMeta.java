package org.egc.gis.commons;

import lombok.Data;

/**
 * raster file metadata POJO
 *
 * @author houzhiwei
 * @date 2017 /7/29 17:04
 * @date 2017 8-24
 */
@Data
public class RasterMeta {

    private String filename;
    private float scaleFactor;
    private Integer noData;
    private String areaOrPoint;
    /**
     * CoordinateReferenceSystem in WKT format
     */
    private String crsWkt;
    private String driverNameLong;
    private String driverNameShort;
    //xSize
    private int cols;
    //ySize
    private int rows;
    private int rasterBands;
    private String projection;
    //adfGeoTransform[1]  w-e pixel resolution
    private int pixelWidth;
    //adfGeoTransform[5] n-s pixel resolution (negative value)
    private int pixelHeight;
    //adfGeoTransform[0]
    private float upperLeftX;
    //adfGeoTransform[3]
    private float upperLeftY;
    private float min;
    private float max;
    private float mean;
    private float stdev;
}
