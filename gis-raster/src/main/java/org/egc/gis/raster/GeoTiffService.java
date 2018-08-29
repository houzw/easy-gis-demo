package org.egc.gis.raster;

import org.egc.gis.raster.dto.RasterMetadata;
import org.geotools.coverage.grid.GridCoverage2D;

import java.io.IOException;

/**
 * read geotiff file
 *
 * @author houzhiwei
 * @date 2017 /7/27 20:13
 */
public interface GeoTiffService {

    /**
     * Read GeoTiff.
     *
     * @param tif the tif file
     * @return the grid coverage 2 d
     * @throws IOException the io exception
     */
    GridCoverage2D read(String tif) throws IOException;


    /**
     * Read GeoTiff and get raster metadata.
     *
     * @param tif the tif file
     * @return the raster metadata
     * @throws IOException the io exception
     */
    RasterMetadata readMetadata(String tif) throws IOException;


    /**
     * Get Coverage2D metadata.
     *
     * @param Coverage the Coverage2D
     * @return the raster metadata
     */
    RasterMetadata readMetadata(GridCoverage2D Coverage);
}
