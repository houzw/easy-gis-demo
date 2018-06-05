package org.egc.gis.raster.impl;

import lombok.extern.slf4j.Slf4j;
import org.egc.gis.raster.GeoTiffService;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
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
@Slf4j
@Service
public class GeoTiffServiceImpl implements GeoTiffService {

    @Override
    public GridCoverage2D read(String file) throws IOException {

        File rasterFile = new File(file);
        ParameterValue<OverviewPolicy> policy = AbstractGridFormat.OVERVIEW_POLICY.createValue();
        policy.setValue(OverviewPolicy.IGNORE);

        //this will basically read 4 tiles worth of data at once from the disk...
        ParameterValue<String> gridSize = AbstractGridFormat.SUGGESTED_TILE_SIZE.createValue();

        //Setting read type: use JAI ImageRead (true) or ImageReaders read methods (false)
        ParameterValue<Boolean> useJaiRead = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        useJaiRead.setValue(true);

        GridCoverage2DReader reader = new GeoTiffReader(rasterFile);

        GridCoverage2D coverage = reader.read(new GeneralParameterValue[]{policy, gridSize, useJaiRead});
        return coverage;
    }


}
