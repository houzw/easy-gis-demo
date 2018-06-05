package org.egc.gis.raster;

import org.geotools.coverage.grid.GridCoverage2D;

import java.io.IOException;

/**
 * read geotiff file
 *
 * @author houzhiwei
 * @date 2017/7/27 20:13
 */
public interface GeoTiffService {

    GridCoverage2D read(String file) throws IOException;
}
