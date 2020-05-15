package org.egc.gis.gdal.vector.format;

import lombok.extern.slf4j.Slf4j;
import org.egc.gis.commons.Consts;
import org.egc.gis.commons.GDALDriversEnum;
import org.egc.gis.commons.StatusCode;
import org.gdal.gdal.gdal;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Driver;
import org.gdal.ogr.ogr;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/10/10 21:56
 */
@Slf4j
public class VectorConversion {


    public StatusCode shp2GeoJSON(String shp, String json) {
        return shpConvert(GDALDriversEnum.GeoJSON, shp, json);
    }

    public StatusCode shp2KML(String shp, String kml) {
        return shpConvert(GDALDriversEnum.KML, shp, kml);
    }

    private StatusCode shpConvert(GDALDriversEnum type, String fromFile, String toFile) {
        // 注册所有的驱动
        ogr.RegisterAll();
        // 为了支持中文路径，请添加下面这句代码
        gdal.SetConfigOption(Consts.GDAL_FILENAME_IS_UTF8, Consts.YES);
        // 为了使属性表字段支持中文，请添加下面这句
        gdal.SetConfigOption(Consts.SHAPE_ENCODING, "");
        DataSource ds = ogr.Open(fromFile, 0);
        Driver dv = ogr.GetDriverByName(type.getName());
        if (dv == null) {
            log.error("打开驱动失败！");
            return StatusCode.FAILED;
        }
        DataSource out = dv.CopyDataSource(ds, toFile);
        ds.delete();
        out.delete();
        dv.delete();
        log.debug("转换成功！");
        return StatusCode.SUCCESS;
    }
}
