package org.egc.gis.raster;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.geometry.Envelope2D;

import java.io.IOException;

/**
 * TODO
 * https://docs.geotools.org/latest/userguide/tutorial/coverage/coverage.html
 * https://github.com/geotools/geotools/blob/master/modules/library/coverage/src/test/java/org/geotools/coverage/processing/CropTest.java
 *
 * @author houzhiwei
 * @date 2017 /8/28 21:23
 */
public interface RasterService {

    int NUM_HORIZONTAL_TILES = 16;
    int NUM_VERTICAL_TILES = 8;
    int TILE_SIZE = 256;
    int WORLD_LEFT = -180;
    int WORLD_RIGHT = 180;
    int WORLD_TOP = 90;
    int WORLD_BOTTOM = -90;
    int NO_DATA_VALUE = -9999;
    /**
     * This equates to "level 9" (level = (int)(1 + 8*quality))
     * https://github.com/geosolutions-it/imageio-ext/blob/master/plugin/tiff/src/main/java/it/geosolutions/imageioimpl/plugins/tiff/TIFFDeflater.java#L105
     * The compression is lossless. The level is a speed vs compression trade-off.
     */
    float GEOTIFF_COMPRESSION_QUALITY = 1F;
    String GEOTIFF_COMPRESSION_DEFLATE = "Deflate";
    String GEOTIFF_COMPRESSION_LZW = "LZW";

    /**
     * read raster file
     *
     * @param file raster file
     * @return GridCoverage2D grid coverage 2 d
     * @throws IOException the io exception
     * @see GeoTiffService#read(String) GeoTiffService#read(String)GeoTiffService#read(String)
     */
    GridCoverage2D read(String file) throws IOException;

    /**
     * Write grid coverage 2 d.
     *
     * @param coverage the coverage
     * @param file     the file
     * @return the grid coverage 2 d
     * @throws IOException the io exception
     */
    void write(GridCoverage2D coverage, String file) throws IOException;

    /**
     * https://gis.stackexchange.com/questions/73210/how-to-crop-an-image-based-on-a-shapefile-using-geotools
     * https://github.com/geotools/geotools/blob/master/modules/unsupported/process-feature/src/test/java/org/geotools/process/vector/ClipProcessTest.java
     *
     * @param file the file
     * @param shp  the shp
     * @return grid coverage 2 d
     */
    GridCoverage2D clip(String file, String shp);

    /**
     * Clip grid coverage 2 d.
     *
     * @param file        the file
     * @param boundingBox the bounding box
     * @return the grid coverage 2 d
     */
    GridCoverage2D clip(String file, Envelope2D boundingBox);


    /**
     * Convert grid coverage 2 d.
     *
     * @param srcFile the src file
     * @param dstFile the dst file
     * @return the grid coverage 2 d
     * @throws IOException the io exception
     */
    GridCoverage2D convert(String srcFile, String dstFile) throws IOException;

}
