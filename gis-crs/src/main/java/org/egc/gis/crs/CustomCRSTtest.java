package org.egc.gis.crs;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.processing.Operations;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.projection.TransverseMercator;
import org.geotools.util.factory.Hints;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.io.File;
import java.io.IOException;

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

    /**
     * "We can use the resample(..) method to transform any GridCoverage to another CRS."
     * https://docs.geotools.org/latest/userguide/library/coverage/grid.html
     *
     * @param srcFile
     * @param targetEpsg
     * @return
     * @throws IOException
     * @throws FactoryException
     */
    public static GridCoverage2D reproject(String srcFile, int targetEpsg) throws IOException, FactoryException {
        File rasterFile = new File(srcFile);
        AbstractGridFormat format = GridFormatFinder.findFormat(rasterFile);
        Hints hints = new Hints();
        if (format instanceof GeoTiffFormat) {
            hints.put(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, DefaultGeographicCRS.WGS84);
            hints.put(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
        }
        GridCoverage2DReader reader = format.getReader(rasterFile, hints);
        GridCoverage2D src = reader.read(null);
        System.out.println(src.getCoordinateReferenceSystem().getName());
        CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:" + targetEpsg);
        GridCoverage2D resampled = (GridCoverage2D) Operations.DEFAULT.resample(src, targetCRS);
        return resampled;
    }
}
