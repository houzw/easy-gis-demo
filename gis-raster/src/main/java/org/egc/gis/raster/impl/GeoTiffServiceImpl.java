package org.egc.gis.raster.impl;

import org.egc.gis.commons.RasterMetadata;
import org.egc.gis.raster.GeoTiffService;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.Envelope2D;
import org.geotools.process.classify.ClassificationMethod;
import org.geotools.process.raster.CoverageClassStats;
import org.geotools.referencing.CRS;
import org.geotools.resources.coverage.CoverageUtilities;
import org.jaitools.numeric.Statistic;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.osgeo.proj4j.CRSFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 参考
 * https://gis.stackexchange.com/questions/106882/reading-each-pixel-of-each-band-of-multiband-geotiff-with-geotools-java
 *
 * @author houzhiwei
 * @date 2017/7/27 20:16
 */
@Service
public class GeoTiffServiceImpl implements GeoTiffService {

    private static final Logger logger = LoggerFactory.getLogger(GeoTiffServiceImpl.class);

    @Override
    public GridCoverage2D read(String tif) throws IOException {

        File rasterFile = new File(tif);
        GridCoverage2DReader reader = new GeoTiffReader(new FileInputStream(rasterFile));

        ParameterValue<OverviewPolicy> policy = AbstractGridFormat.OVERVIEW_POLICY.createValue();
        policy.setValue(OverviewPolicy.IGNORE);

        //this will basically read 4 tiles worth of data at once from the disk...
        ParameterValue<String> gridSize = AbstractGridFormat.SUGGESTED_TILE_SIZE.createValue();

        //Setting read type: use JAI ImageRead (true) or ImageReaders read methods (false)
        ParameterValue<Boolean> useJaiRead = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        useJaiRead.setValue(true);

        GridCoverage2D coverage = reader.read(new GeneralParameterValue[]{policy, gridSize, useJaiRead});
        return coverage;
    }

    @Override
    public RasterMetadata readMetadata(String tif) throws IOException {
        GridCoverage2D coverage2D = read(tif);
        return readMetadata(coverage2D);
    }

    @Override
    public RasterMetadata readMetadata(GridCoverage2D coverage) throws IOException {
        RasterMetadata metadata = new RasterMetadata();
        metadata.setFormat("GeoTIFF");
        double nodata = CoverageUtilities.getNoDataProperty(coverage).getAsSingleValue();
        metadata.setNodata(nodata);
        CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem();
        metadata.setCrsWkt(crs.toWKT());
        try {
            metadata.setSrid(CRS.lookupEpsgCode(crs, true));
        } catch (FactoryException e) {
            logger.error("Error occured while searching for the identifier. {}", e.getMessage());
            throw new RuntimeException("Error occured while searching for the CRS identifier");
        }
        CRSFactory csFactory = new CRSFactory();
        org.osgeo.proj4j.CoordinateReferenceSystem crsProj = csFactory.createFromName(crs.getIdentifiers().toArray()[0].toString());
        metadata.setCrsProj4(crsProj.getProjection().getPROJ4Description());
        Envelope2D envelope2D = coverage.getEnvelope2D();
        metadata.setHeight(envelope2D.height);
        metadata.setWidth(envelope2D.width);
        metadata.setMaxX(envelope2D.getMaxX());
        metadata.setMinX(envelope2D.getMinX());
        metadata.setMaxY(envelope2D.getMaxY());
        metadata.setMinY(envelope2D.getMinY());
        metadata.setCenterX(envelope2D.getCenterX());
        metadata.setCenterY(envelope2D.getCenterY());
        RenderedImage image = coverage.getRenderedImage();
        metadata.setSizeHeight(image.getHeight());
        metadata.setSizeWidth(image.getWidth());
        metadata.setPixelSize((envelope2D.getMaxY() - envelope2D.getMinY()) / image.getHeight());
        CoverageClassStats rasterProcess = new CoverageClassStats();
        Set<Statistic> set = new HashSet();
        set.add(Statistic.MAX);
        set.add(Statistic.MIN);
        set.add(Statistic.MEAN);
        set.add(Statistic.SDEV);
        int band_i = CoverageUtilities.getVisibleBand(image);
        //classes 分段数
        CoverageClassStats.Results results = null;
        try {
            results = rasterProcess.execute(coverage, set, band_i, 1,
                                            ClassificationMethod.QUANTILE, nodata, null);
        } catch (IOException e) {
            logger.error("Process raster file statistics failed");
            throw new IOException("Process raster file statistics failed");
        }
        metadata.setMinValue(results.value(0, Statistic.MIN));
        metadata.setMaxValue(results.value(0, Statistic.MAX));
        metadata.setMeanValue(results.value(0, Statistic.MEAN));
        metadata.setSdev(results.value(0, Statistic.SDEV));
        return metadata;
    }
}
