package org.egc.gis.raster.dto;

/**
 * TODO
 *
 * @author houzhiwei
 * @date 2018/8/28 18:55
 */
public class RasterMetadata {

    private String rasterName;
    /**
     * Coordinate Reference System Identify
     */
    private String crs;
    private String crsWkt;
    private String crsCode;
    private float nodata;
    private String format;
    private double upperLeft;
    private double upperRight;
    private double lowerLeft;
    private double lowerRight;
    private double center;
    private double pixelSize;


    public String getRasterName() {
        return rasterName;
    }

    public void setRasterName(String rasterName) {
        this.rasterName = rasterName;
    }

    public String getCrs() {
        return crs;
    }

    public void setCrs(String crs) {
        this.crs = crs;
    }

    public String getCrsWkt() {
        return crsWkt;
    }

    public void setCrsWkt(String crsWkt) {
        this.crsWkt = crsWkt;
    }

    public String getCrsCode() {
        return crsCode;
    }

    public void setCrsCode(String crsCode) {
        this.crsCode = crsCode;
    }

    public float getNodata() {
        return nodata;
    }

    public void setNodata(float nodata) {
        this.nodata = nodata;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public double getUpperLeft() {
        return upperLeft;
    }

    public void setUpperLeft(double upperLeft) {
        this.upperLeft = upperLeft;
    }

    public double getUpperRight() {
        return upperRight;
    }

    public void setUpperRight(double upperRight) {
        this.upperRight = upperRight;
    }

    public double getLowerLeft() {
        return lowerLeft;
    }

    public void setLowerLeft(double lowerLeft) {
        this.lowerLeft = lowerLeft;
    }

    public double getLowerRight() {
        return lowerRight;
    }

    public void setLowerRight(double lowerRight) {
        this.lowerRight = lowerRight;
    }

    public double getCenter() {
        return center;
    }

    public void setCenter(double center) {
        this.center = center;
    }

    public double getPixelSize() {
        return pixelSize;
    }

    public void setPixelSize(double pixelSize) {
        this.pixelSize = pixelSize;
    }

}
