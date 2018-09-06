package org.egc.gis.raster.test;

import it.geosolutions.imageio.gdalframework.GDALUtilities;
import org.egc.gis.raster.GeoTiffService;
import org.egc.gis.raster.impl.GeoTiffServiceImpl;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.Envelope2D;
import org.geotools.process.classify.ClassificationMethod;
import org.geotools.process.raster.CoverageClassStats;
import org.geotools.resources.coverage.CoverageUtilities;
import org.jaitools.numeric.Statistic;
import org.junit.Test;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO
 *
 * @author houzhiwei
 * @date 2017/7/27 23:35
 */
public class RasterTest {
    String tif = "H:/grass-demos/demo1/01_DEM.tif";
    String gdalinfo = "Driver: GTiff/GeoTIFF\n" +
            "Files: .\\01_DEM.tif\n" +
            "       .\\01_DEM.tif.aux.xml\n" +
            "Size is 1268, 1049\n" +
            "Coordinate System is:\n" +
            "GEOGCS[\"WGS 84\",\n" +
            "    DATUM[\"WGS_1984\",\n" +
            "        SPHEROID[\"WGS 84\",6378137,298.257223563,\n" +
            "            AUTHORITY[\"EPSG\",\"7030\"]],\n" +
            "        AUTHORITY[\"EPSG\",\"6326\"]],\n" +
            "    PRIMEM[\"Greenwich\",0],\n" +
            "    UNIT[\"degree\",0.0174532925199433],\n" +
            "    AUTHORITY[\"EPSG\",\"4326\"]]\n" +
            "Origin = (-69.883617868632996,47.178405403826673)\n" +
            "Pixel Size = (0.000833333333333,-0.000833333333333)\n" +
            "Metadata:\n" +
            "  AREA_OR_POINT=Area\n" +
            "Image Structure Metadata:\n" +
            "  COMPRESSION=LZW\n" +
            "  INTERLEAVE=BAND\n" +
            "Corner Coordinates:\n" +
            "Upper Left  ( -69.8836179,  47.1784054) ( 69d53' 1.02\"W, 47d10'42.26\"N)\n" +
            "Lower Left  ( -69.8836179,  46.3042387) ( 69d53' 1.02\"W, 46d18'15.26\"N)\n" +
            "Upper Right ( -68.8269512,  47.1784054) ( 68d49'37.02\"W, 47d10'42.26\"N)\n" +
            "Lower Right ( -68.8269512,  46.3042387) ( 68d49'37.02\"W, 46d18'15.26\"N)\n" +
            "Center      ( -69.3552845,  46.7413221) ( 69d21'19.02\"W, 46d44'28.76\"N)\n" +
            "Band 1 Block=128x64 Type=UInt16, ColorInterp=Gray\n" +
            "  Min=234.000 Max=586.000\n" +
            "  Minimum=234.000, Maximum=586.000, Mean=368.876, StdDev=67.255\n" +
            "  NoData Value=65535\n" +
            "  Metadata:\n" +
            "    STATISTICS_MAXIMUM=586\n" +
            "    STATISTICS_MEAN=368.87574941058\n" +
            "    STATISTICS_MINIMUM=234\n" +
            "    STATISTICS_STDDEV=67.255226048022";

    @Test
    public void testRaster() throws Exception {
        GeoTiffService tiffService = new GeoTiffServiceImpl();
        GridCoverage2D coverage2D = tiffService.read(tif);
        print(coverage2D.getName());//01_DEM
        print(CoverageUtilities.getNoDataProperty(coverage2D).getAsSingleValue());//65535.0
//        print(CoverageUtilities.getResolution());//65535.0
        CoordinateReferenceSystem crs = coverage2D.getCoordinateReferenceSystem();
        print(crs);
        print(crs.getName());//EPSG:WGS 84
        print(crs.getName().getCode());//WGS 84
        print(crs.getIdentifiers());//[EPSG:4326]
        print(crs.toWKT());
        print(crs.toString());
        print("customCRS");
        //CRSFactory factory = ReferencingFactoryFinder.getCRSFactory(null);
        //CoordinateReferenceSystem customCRS = factory.createFromWKT(crs.toWKT());
        //print(customCRS);//还是wkt

        print("crs.getCoordinateSystem()");
        print(crs.getCoordinateSystem());
        print(crs.getCoordinateSystem().getAxis(0).getUnit());//°
        print(crs.getCoordinateSystem().getAxis(0).getMaximumValue());//180.0
        print(crs.getCoordinateSystem().getAxis(0).getName().getCode());//Geodetic longitude
        print(crs.getCoordinateSystem().getAxis(0).getDirection().name());//EAST

        print(crs.getCoordinateSystem().getAxis(1).getUnit());
        print(crs.getCoordinateSystem().getAxis(1).getMaximumValue());//90.0
        print(crs.getCoordinateSystem().getAxis(1).getName().getCode());//Geodetic latitude
        print(crs.getCoordinateSystem().getAxis(1).getDirection().name());//NORTH

        print("coverage2D.getGridGeometry()");
        print(coverage2D.getGridGeometry().gridDimensionX);//0
        print(coverage2D.getGridGeometry().gridDimensionY);//1

        print("coverage2D.getEnvelope2D()");
        Envelope2D envelope2D = coverage2D.getEnvelope2D();
        print(envelope2D.x);//-69.883617868633
        print(envelope2D.y);//46.304238737160006
        print(envelope2D.width);//1.056666666666672
        print(envelope2D.height);//0.8741666666666674
        print(envelope2D.getMaxX());//-68.82695120196632
        print(envelope2D.getMaxY());//47.17840540382667
        print(envelope2D.getHeight());//0.8741666666666674
        print(envelope2D.getUpperCorner());//DirectPosition2D[-68.82695120196632, 47.17840540382667]
        print(envelope2D.getLowerCorner());//DirectPosition2D[-69.883617868633, 46.304238737160006]
        RenderedImage image = coverage2D.getRenderedImage();
        //size
        print(image.getHeight());//1049
        print(image.getWidth());//1268

        print(GDALUtilities.isGDALAvailable());
    }


    @Test
    public void test2() {
        File tifile = new File(tif);
        try {
            AbstractGridCoverage2DReader reader = new GeoTiffReader(new FileInputStream(tifile));

//            print(reader.getOriginalGridRange());
//            print(reader.getOriginalEnvelope());
//            print(reader.getGridCoverageCount()); // band num
//            print(reader.getMetadataNames()); // band num
//            print(reader.getGridCoverageNames()); //
//            print(reader.getFormat()); //
//
//            print(reader.getCoordinateReferenceSystem());
//            print(reader.getFormat().getName());
//            print(reader.getResolutionLevels());
//            print(reader.getImageLayout());
            GridCoverage2D coverage2D = reader.read(null);

//            BandSelectProcess bandselect = new BandSelectProcess();
//            GridCoverage2D selected1 = bandselect.execute(coverage2D, new int[]{0}, null);
            CoverageClassStats rasterProcess = new CoverageClassStats();
            Set<Statistic> set = new HashSet();
            set.add(Statistic.MAX);
            set.add(Statistic.MIN);
            set.add(Statistic.MEAN);
            set.add(Statistic.SDEV);
            // ClassificationMethod.EQUAL_INTERVAL 等距
//            CoverageClassStats.Results results = rasterProcess.execute(coverage2D, set,
//                                                                       null, null, null, 65535d, null);
            // classes 分段数
            CoverageClassStats.Results results2 = rasterProcess.execute(coverage2D, set,

                                                                        null, 1, ClassificationMethod.QUANTILE, 65535d, null);
            // 统计值分段数
//            results2.size();
//            results2.value(0,Statistic.MAX);
            results2.print();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void print(Object o) {
        System.out.println(o);
    }

}
