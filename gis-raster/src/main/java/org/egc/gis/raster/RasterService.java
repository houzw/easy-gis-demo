package org.egc.gis.raster;

import org.geotools.coverage.grid.GridCoverage2D;

import java.io.IOException;

/**
 * TODO
 *
 * @author houzhiwei
 * @date 2017/8/28 21:23
 */
public interface RasterService {
    /**
     * read raster file
     *
     * @param file raster file
     * @return GridCoverage2D
     * @throws IOException
     * @see GeoTiffService#read(String)
     */
    GridCoverage2D read(String file) throws IOException;
}
