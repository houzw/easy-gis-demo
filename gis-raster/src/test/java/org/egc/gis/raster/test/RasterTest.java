package org.egc.gis.raster.test;

import org.egc.gis.raster.GeoTiffService;
import org.egc.gis.raster.impl.GeoTiffServiceImpl;
import org.geotools.coverage.grid.GridCoverage2D;
import org.junit.Test;

import java.io.IOException;

/**
 * TODO
 *
 * @author houzhiwei
 * @date 2017/7/27 23:35
 */
public class RasterTest {
    String tif = "H:/grass-demos/demo1/01_DEM.tif";

    @Test
    public void testRaster() throws IOException {
        GeoTiffService tiffService = new GeoTiffServiceImpl();
        GridCoverage2D coverage2D = tiffService.read(tif);
        System.out.println(coverage2D.getCoordinateReferenceSystem().getName().getCode());//WGS 84
        System.out.println(coverage2D.getCoordinateReferenceSystem().getIdentifiers());//[EPSG:4326]
        System.out.println(coverage2D.getCoordinateReferenceSystem().getName());//EPSG:WGS 84
        System.out.println(coverage2D.getName());//01_DEM
        System.out.println(coverage2D.getCoordinateReferenceSystem2D().toWKT());

        System.out.println("coverage2D.getCoordinateReferenceSystem().getCoordinateSystem().getAxis(0)");
        System.out.println(coverage2D.getCoordinateReferenceSystem().getCoordinateSystem().getAxis(0).getUnit());//°
        System.out.println(coverage2D.getCoordinateReferenceSystem().getCoordinateSystem().getAxis(0).getMaximumValue());//180.0
        System.out.println(coverage2D.getCoordinateReferenceSystem().getCoordinateSystem().getAxis(0).getName().getCode());//Geodetic longitude
        System.out.println(coverage2D.getCoordinateReferenceSystem().getCoordinateSystem().getAxis(0).getDirection().name());//EAST

        System.out.println("coverage2D.getCoordinateReferenceSystem().getCoordinateSystem().getAxis(1)");
        System.out.println(coverage2D.getCoordinateReferenceSystem().getCoordinateSystem().getAxis(1).getUnit());
        System.out.println(coverage2D.getCoordinateReferenceSystem().getCoordinateSystem().getAxis(1).getMaximumValue());//90.0
        System.out.println(coverage2D.getCoordinateReferenceSystem().getCoordinateSystem().getAxis(1).getName().getCode());//Geodetic latitude
        System.out.println(coverage2D.getCoordinateReferenceSystem().getCoordinateSystem().getAxis(1).getDirection().name());//NORTH

        System.out.println("coverage2D.getGridGeometry()");
        System.out.println(coverage2D.getGridGeometry().gridDimensionX);//0
        System.out.println(coverage2D.getGridGeometry().gridDimensionY);//1
        System.out.println("coverage2D.getEnvelope2D()");
        System.out.println(coverage2D.getEnvelope2D().x);//-69.883617868633
        System.out.println(coverage2D.getEnvelope2D().y);//46.304238737160006
        System.out.println(coverage2D.getEnvelope2D().width);//1.056666666666672
        System.out.println(coverage2D.getEnvelope2D().height);//0.8741666666666674
        System.out.println(coverage2D.getEnvelope2D().getMaxX());//-68.82695120196632
        System.out.println(coverage2D.getEnvelope2D().getMaxY());//47.17840540382667
        System.out.println(coverage2D.getEnvelope2D().getHeight());//0.8741666666666674
        System.out.println(coverage2D.getEnvelope2D().getUpperCorner());//DirectPosition2D[-68.82695120196632, 47.17840540382667]
        System.out.println(coverage2D.getEnvelope2D().getLowerCorner());//DirectPosition2D[-69.883617868633, 46.304238737160006]
    }


}
