package org.egc.gis.commons;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @author houzhiwei
 */
@Slf4j
@Data
public class VectorMetadata implements Serializable {
    private String name;
    private String encoding;
    /**
     * e.g. 1 ogr.wkbPoint
     */
    private int geomType;
    private String geometry;
    private long featureCount;
    private int layerCount;
    /**
     * Coordinate Reference System Identify
     */
    private String crs;
    private String crsProj4;
    private String crsWkt;
    private Integer srid;
    private String format;
    private String unit;

    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    private double[] upperLeft;
    private double[] upperRight;
    private double[] lowerLeft;
    private double[] lowerRight;


    /**
     * Get upper left .
     *
     * @return [minX, maxY]
     */
    public double[] getUpperLeft() {
        this.upperLeft = new double[]{minX, maxY};
        return upperLeft;
    }

    /**
     * Get upper right.
     *
     * @return [maxX, maxY]
     */
    public double[] getUpperRight() {
        this.upperRight = new double[]{maxX, maxY};
        return upperRight;
    }

    /**
     * Get lower left.
     *
     * @return [minX, minY]
     */
    public double[] getLowerLeft() {
        this.lowerLeft = new double[]{minX, minY};
        return lowerLeft;
    }

    /**
     * Get lower right:  .
     *
     * @return the [minX, minY]
     */
    public double[] getLowerRight() {
        this.lowerRight = new double[]{minX, minY};
        return lowerRight;
    }
}
