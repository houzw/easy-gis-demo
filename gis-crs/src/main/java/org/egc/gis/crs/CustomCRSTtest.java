package org.egc.gis.crs;

import org.geotools.referencing.operation.projection.TransverseMercator;
import org.opengis.parameter.ParameterValueGroup;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/1/9 10:54
 */
public class CustomCRSTtest extends TransverseMercator {

    //利用Geotools来转换影像的坐标系
    //https://www.giserdqy.com/gis/opengis/geotools/27672/%E5%88%A9%E7%94%A8geotools%E6%9D%A5%E8%BD%AC%E6%8D%A2%E5%BD%B1%E5%83%8F%E7%9A%84%E5%9D%90%E6%A0%87%E7%B3%BB-2/

    //平面投影坐标转经纬度坐标
    //https://blog.csdn.net/m0_37821031/article/details/78858005

    public CustomCRSTtest(final ParameterValueGroup parameters) {
        super(parameters);
    }

}
