package org.egc.gis.raster.test;

import it.geosolutions.imageio.gdalframework.GDALUtilities;
import org.egc.gis.commons.RasterMetadata;
import org.egc.gis.raster.GeoTiffService;
import org.egc.gis.raster.impl.GeoTiffServiceImpl;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.Envelope2D;
import org.geotools.process.classify.ClassificationMethod;
import org.geotools.process.raster.CoverageClassStats;
import org.geotools.referencing.CRS;
import org.geotools.resources.coverage.CoverageUtilities;
import org.jaitools.numeric.Statistic;
import org.junit.Test;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.osgeo.proj4j.CRSFactory;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TODO
 *
 * @author houzhiwei
 * @date 2017/7/27 23:35
 */
public class GeotoolsRasterTest {
    String tif = "H:\\dem_TX.tif";
    String tif2 = "D:\\data\\WebSites\\egcDataFiles\\upload\\20181006\\raster\\bdbb2fce7d81d3d70d284e37d92bb08b.tif";

    @Test
    public void testRaster() throws Exception {

        RasterMetadata metadata = new RasterMetadata();

        GeoTiffService tiffService = new GeoTiffServiceImpl();
        GridCoverage2D coverage2D = tiffService.read(tif);
        metadata.setRasterName(coverage2D.getName().toString());//
        print(coverage2D.getName().toString());//geotiff_coverage
        double nodata = CoverageUtilities.getNoDataProperty(coverage2D).getAsSingleValue();
        metadata.setNodata(nodata);//65535.0
        CoordinateReferenceSystem crs = coverage2D.getCoordinateReferenceSystem();
        metadata.setCrsWkt(crs.toWKT());
        print("code "+crs.getName().getCode());//WGS 84

        print(crs.getName());//EPSG:WGS 84
        print(crs.getName().getCodeSpace());//EPSG
//        print(crs.getIdentifiers().toArray()[0]);//EPSG:4326
        CRSFactory csFactory = new CRSFactory();
        try {
            print(CRS.lookupEpsgCode(crs, false));
            print(CRS.lookupEpsgCode(crs, true));
            org.osgeo.proj4j.CoordinateReferenceSystem crsProj = csFactory.createFromName(crs.getIdentifiers().toArray()[0].toString());
            print(crsProj.getProjection().getPROJ4Description());
            print(crsProj.getParameters().length);//3
            print(crsProj.getParameters()[1]);//3
        } catch (Exception e) {
            e.printStackTrace();
        }

        //print(crs);
        print("customCRS");
        //CRSFactory factory = ReferencingFactoryFinder.getCRSFactory(null);
        //CoordinateReferenceSystem customCRS = factory.createFromWKT(crs.toWKT());
        //print(customCRS);//还是wkt
            print(CRS.getProjectedCRS(crs).getName().toString());

        print("crs.getCoordinateSystem()");
        //print(crs.getCoordinateSystem());
        print("unit "+crs.getCoordinateSystem().getAxis(0).getUnit().toString());//°
        print(crs.getCoordinateSystem().getAxis(0).getMaximumValue());//180.0
        print(crs.getCoordinateSystem().getAxis(0).getName().getCode());//Geodetic longitude
        print(crs.getCoordinateSystem().getAxis(0).getDirection().name());//EAST

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

        metadata.setHeight(envelope2D.height);//0.8741666666666674
        metadata.setWidth(envelope2D.width);//1.056666666666672
        metadata.setMaxX(envelope2D.getMaxX()); //-68.82695120196632
        metadata.setMinX(envelope2D.getMinX()); //-69.883617868633
        metadata.setMaxY(envelope2D.getMaxY()); //47.17840540382667
        metadata.setMinY(envelope2D.getMinY()); //46.304238737160006
        metadata.setCenterX(envelope2D.getCenterX());
        metadata.setCenterY(envelope2D.getCenterY());

//        print(envelope2D.getUpperCorner());//DirectPosition2D[-68.82695120196632, 47.17840540382667]
//        print(envelope2D.getLowerCorner());//DirectPosition2D[-69.883617868633, 46.304238737160006]

        RenderedImage image = coverage2D.getRenderedImage();

        //size
        metadata.setSizeHeight(image.getHeight());//1049
        metadata.setSizeWidth(image.getWidth());//1268
        int band_i = CoverageUtilities.getVisibleBand(image);
        int numXTiles = image.getNumXTiles();//3
        int tileHeight = image.getTileHeight();//512x512
        print(numXTiles);
        print(image.getTileWidth());
        print("pixel y size:"+ (envelope2D.getMaxY()-envelope2D.getMinY())/image.getHeight());
        print("pixel x size:"+ (envelope2D.getMaxX()-envelope2D.getMinX())/image.getWidth());
        CoverageClassStats rasterProcess = new CoverageClassStats();

        Set<Statistic> set = new HashSet();
        set.add(Statistic.MAX);
        set.add(Statistic.MIN);
        set.add(Statistic.MEAN);
        set.add(Statistic.SDEV);
        set.add(Statistic.VARIANCE);
        // ClassificationMethod.EQUAL_INTERVAL 等距
//            CoverageClassStats.Results results = rasterProcess.execute(coverage2D, set,
//                                                                       null, null, null, 65535d, null);
        // classes 分段数
        CoverageClassStats.Results results = rasterProcess.execute(coverage2D, set, band_i, 1, ClassificationMethod.QUANTILE, nodata, null);
        // 统计值分段数
//            results.size();
        metadata.setMinValue(results.value(0, Statistic.MIN));//186
        metadata.setMaxValue(results.value(0, Statistic.MAX));//592
        metadata.setMeanValue(results.value(0, Statistic.MEAN));//371.21721566492147
        metadata.setSdev(results.value(0, Statistic.SDEV));//60.676674688824484

        print("metadata.toString()");
//        print(metadata.toString());
        results.print();
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


            print(GDALUtilities.isGDALAvailable());
            List<String> list = GDALUtilities.getGDALImageMetadata(tif);
            for (String l : list) {
                print("l: " + l);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() throws IOException {
        GeoTiffService tiffService = new GeoTiffServiceImpl();
        GridCoverage2D coverage2D = tiffService.read(tif2);
        CoordinateReferenceSystem referenceSystem = coverage2D.getCoordinateReferenceSystem2D();
        print(referenceSystem.toWKT());
        print(referenceSystem.getName().toString());//Datum = Krasovsky
    }


    private void print(Object o) {
        System.out.println(o);
    }

}
