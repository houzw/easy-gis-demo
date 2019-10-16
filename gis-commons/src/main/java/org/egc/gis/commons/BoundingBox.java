package org.egc.gis.commons;

import lombok.Data;

/**
 * Description:
 * <pre>
 * BoundingBox:
 *  (min_x, max_y, max_x, min_y)
 *  (upper_left_x, upper_left_y, lower_right_x, lower_right_y)
 *  (top_left_lon, top_left_lat, bottom_right_lon, bottom_right_lat)
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/10/14 22:11
 */
@Data
public class BoundingBox {
    private double minX;
    private double minY;
    private double maxX;
    private double maxY;

    private double upper_left_x;
    private double upper_left_y;
    private double lower_right_x;
    private double lower_right_y;

    private double top_left_lon;
    private double top_left_lat;
    private double bottom_right_lon;
    private double bottom_right_lat;

    /**
     * @param minX min_x, upper_left_x, top_left_lon
     * @param maxY max_y, upper_left_y, top_left_lat
     * @param maxX max_x, lower_right_x, bottom_right_lon
     * @param minY min_y, lower_right_y, bottom_right_lat
     */
    public BoundingBox(double minX, double maxY, double maxX, double minY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;

        this.upper_left_x = minX;
        this.upper_left_y = maxY;
        this.lower_right_x = maxX;
        this.lower_right_y = minY;

        this.top_left_lon = minX;
        this.top_left_lat = maxY;
        this.bottom_right_lon = maxX;
        this.bottom_right_lat = minY;
    }

    /**
     * To avoid no data around the edges, it is better to round up or down (according to which edge you are
     * on) by 0.2° or other values. Make sure you round outwards from your area rather than into it.
     *
     * @param val 0.2 or other values
     * @return
     */
    public BoundingBox expand(double val) {
        val = Math.abs(val);
        double new_top_left_lon = top_left_lon - val;
        double new_top_left_lat = top_left_lat + val;
        double new_bottom_right_lon = bottom_right_lon + val;
        double new_bottom_right_lat = bottom_right_lat + val;
        return new BoundingBox(new_top_left_lon, new_top_left_lat, new_bottom_right_lon, new_bottom_right_lat);
    }

    @Override
    public String toString() {
        return this.upper_left_x + " " + this.upper_left_y + " " + lower_right_x + " " + lower_right_y;
    }

}
