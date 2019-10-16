package org.egc.gis.raster;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.geometry.Envelope2D;

import java.io.IOException;

/**
 * TODO
 * https://docs.geotools.org/latest/userguide/tutorial/coverage/coverage.html
 * https://github.com/geotools/geotools/blob/master/modules/library/coverage/src/test/java/org/geotools/coverage/processing/CropTest.java
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
    GridCoverage2D write(String file) throws IOException;

    /**
     * https://gis.stackexchange.com/questions/73210/how-to-crop-an-image-based-on-a-shapefile-using-geotools
     * https://github.com/geotools/geotools/blob/master/modules/unsupported/process-feature/src/test/java/org/geotools/process/vector/ClipProcessTest.java
     * @param file
     * @param shp
     * @return
     */
    GridCoverage2D clip(String file,String shp);
    GridCoverage2D clip(String file, Envelope2D boundingBox);

}
