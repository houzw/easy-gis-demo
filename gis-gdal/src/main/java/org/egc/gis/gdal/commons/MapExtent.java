package org.egc.gis.gdal.commons;

import lombok.Data;

/**
 * Corner Coordinates<br/>
 * 图像的原点与地理坐标系的原点不同。<br/>
 * 图像的原点位于左上角，而地图坐标系的原点位于左下角。<br/>
 * 图像中的行值从原点开始向下逐渐增大，而地图中的 y 坐标值从原点开始向上逐渐增大。
 *
 * @author houzhiwei
 * @date 2017 /8/24 22:57
 */
@Data
public class MapExtent {
    /**
     * minX
     */
    private double upperLeftX;
    /**
     * maxY
     */
    private double upperLeftY;
    /**
     * maxX
     */
    private double upperRightX;
    /**
     * maxY
     */
    private double upperRightY;
    /**
     * minX
     */
    private double lowerLeftX;
    /**
     * minY
     */
    private double lowerLeftY;
    /**
     * maxX
     */
    private double lowerRightX;
    /**
     * minY
     */
    private double lowerRightY;

    public double[] getUpperLeftXY() {
        return upperLeftXY;
    }

    public void setUpperLeftXY(double x, double y) {
        this.upperLeftXY = new double[]{x, y};
    }

    public double[] getLowerLeftXY() {
        return lowerLeftXY;
    }

    public void setLowerLeftXY(double x, double y) {
        this.lowerLeftXY = new double[]{x, y};
    }

    public double[] getUpperRightXY() {
        return upperRightXY;
    }

    public void setUpperRightXY(double x, double y) {
        this.upperRightXY = new double[]{x, y};
    }

    public double[] getLowerRightXY() {
        return lowerRightXY;
    }

    public void setLowerRightXY(double x, double y) {
        this.lowerRightXY = new double[]{x, y};
    }

    private double[] upperLeftXY;
    private double[] lowerLeftXY;
    private double[] upperRightXY;
    private double[] lowerRightXY;

    private double centerX;
    private double centerY;

}
