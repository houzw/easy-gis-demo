package org.egc.gis.gdal.raster.format;

/**
 * 格式转换状态码
 *
 * @author houzhiwei
 * @date 2017/9/29 20:36
 */
public enum StatusCode {
    SUCCESS("Format conversion succeed"),
    FAILED("Format conversion failed");
    public String description;
    private StatusCode(String description){
        this.description = description;
    }
    public String getDescription(){
        return description;
    }
}
