package org.egc.gis.raster.impl;

import org.egc.gis.raster.RasterService;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.factory.Hints;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * TODO
 *
 * @author houzhiwei
 * @date 2017/8/28 21:30
 */
@Service
public class RasterServiceImpl implements RasterService {
    @Override
    public GridCoverage2D read(String file) throws IOException {

        File rasterFile = new File(file);
        AbstractGridFormat format = GridFormatFinder.findFormat(rasterFile);
        //this is a bit hacky but does make more geotiffs work
        Hints hints = new Hints();
        if (format instanceof GeoTiffFormat) {
            hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
        }
        GridCoverage2DReader reader = format.getReader(rasterFile, hints);
        return (GridCoverage2D) reader.read(null);
    }
}
