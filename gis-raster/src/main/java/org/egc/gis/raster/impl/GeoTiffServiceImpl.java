package org.egc.gis.raster.impl;

import org.egc.gis.raster.GeoTiffService;
import org.egc.gis.raster.dto.RasterMetadata;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

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
        GridCoverage2DReader reader = new GeoTiffReader(rasterFile);

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
    public RasterMetadata readMetadata(GridCoverage2D coverage) {
        RasterMetadata metadata = new RasterMetadata();
        metadata.setFormat("GeoTIFF");
        if (coverage.getName() != null) {
            metadata.setRasterName(coverage.getName().toString());
        }
        coverage.getCoordinateReferenceSystem().getName();
        double[] up = coverage.getEnvelope2D().getUpperCorner().getCoordinate();
        metadata.setUpperLeft(up[0]);
        metadata.setUpperRight(up[1]);
        double[] low = coverage.getEnvelope2D().getLowerCorner().getCoordinate();
        return metadata;
    }


}
