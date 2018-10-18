package org.egc.gis.commons;

/**
 * 格式转换状态码
 *
 * @author houzhiwei
 * @date 2017/9/29 20:36
 */
public enum StatusCode {
    SUCCESS("success"),
    FAILED("failed");
    public String description;
    private StatusCode(String description){
        this.description = description;
    }
    public String getDescription(){
        return description;
    }
}
