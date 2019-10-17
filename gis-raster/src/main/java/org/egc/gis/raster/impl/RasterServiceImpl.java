package org.egc.gis.raster.impl;

import it.geosolutions.imageio.stream.output.ImageOutputStreamAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.egc.gis.raster.RasterService;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.processing.Operations;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffWriteParams;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.factory.Hints;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.media.jai.TiledImage;
import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;


/**
 * TODO
 * http://docs.geotools.org/latest/userguide/tutorial/coverage/coverage.html
 * http://docs.geotools.org/latest/userguide/tutorial/raster/image.html
 * https://www.programcreek.com/java-api-examples/?api=org.geotools.coverage.grid.GridCoverageFactory
 * https://gis.stackexchange.com/questions/251526/converting-images-to-different-file-formats-using-geotools
 *
 * @author houzhiwei
 * @date 2017/8/28 21:30
 */
@Slf4j
@Service
public class RasterServiceImpl implements RasterService {


    @Override
    public GridCoverage2D read(String file) throws IOException {

        File rasterFile = new File(file);
        AbstractGridFormat format = GridFormatFinder.findFormat(rasterFile);
        //this is a bit hacky but does make more geotiffs work
        Hints hints = new Hints();
        if (format instanceof GeoTiffFormat) {
            hints.put(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, DefaultGeographicCRS.WGS84);
            hints.put(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
        }
        GridCoverage2DReader reader = format.getReader(rasterFile, hints);
        return (GridCoverage2D) reader.read(null);
    }

    public GridCoverage2D createCoverage(float[] data, int columns, int rows) {
        GridCoverageFactory gcf = CoverageFactoryFinder.getGridCoverageFactory(null);
        TiledImage img = createImageContent(data, columns, rows);
        ReferencedEnvelope env = createEnvelope();
        return gcf.create("coverage", img, env);
    }

    @Override
    //https://github.com/geotools/geotools/blob/master/modules/plugin/geotiff/src/test/java/org/geotools/gce/geotiff/GeoTiffWriterTest.java
    public void write(GridCoverage2D coverage, String file) throws IOException {
        final File destFile = File.createTempFile(file, ".tif", new File(FilenameUtils.getFullPath(file)));
        final GeoTiffWriter writer = new GeoTiffWriter(destFile);
        writer.write(coverage, createWriteParameters());
        writer.dispose();
    }

    private ReferencedEnvelope createEnvelope() {
        ReferencedEnvelope env = null;
        try {
            env = new ReferencedEnvelope(
                    WORLD_BOTTOM, WORLD_TOP, WORLD_LEFT, WORLD_RIGHT,
                    CRS.decode("EPSG:4326"));
        } catch (FactoryException e) {
            log.error("Cannot create geo-referenced Envelope: " + e.getMessage(), e);
        }
        return env;
    }

    private TiledImage createImageContent(float[] data, int columns, int rows) {
        SampleModel sm = new ComponentSampleModel(DataBuffer.TYPE_DOUBLE,
                                                  TILE_SIZE, TILE_SIZE, 1, TILE_SIZE, new int[]{0});

        ColorModel cm = TiledImage.createColorModel(sm);
        TiledImage img = new TiledImage(0, 0, columns, rows, 0, 0, sm, cm);

        int i = 0;
        int halfColumns = (int) ((double) columns / 2.0);
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                if (x >= halfColumns) {
                    img.setSample(x - halfColumns, y, 0, data[i]);
                } else {
                    img.setSample(x + halfColumns, y, 0, data[i]);
                }
                i++;
            }
        }
        return img;
    }

    /**
     * Collect parameters to configure a tiles and compressed GeoTiff
     * see http://docs.geotools.org/stable/javadocs/org/geotools/gce/geotiff/GeoTiffWriteParams.html
     *
     * @return
     */
    private GeneralParameterValue[] createWriteParameters() {

        final GeoTiffFormat format = new GeoTiffFormat();
        final GeoTiffWriteParams wp = new GeoTiffWriteParams();
        wp.setCompressionMode(GeoTiffWriteParams.MODE_EXPLICIT);
        wp.setCompressionType(GEOTIFF_COMPRESSION_LZW);
        wp.setTilingMode(GeoToolsWriteParams.MODE_EXPLICIT);
        wp.setTiling(TILE_SIZE, TILE_SIZE);
        wp.setCompressionQuality(GEOTIFF_COMPRESSION_QUALITY);
        final ParameterValueGroup params = format.getWriteParameters();
        params.parameter(
                AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName().toString()).setValue(wp);
        GeneralParameterValue[] writeParameters = params.values().toArray(new GeneralParameterValue[1]);
        return writeParameters;
    }


    @Override
    public GridCoverage2D clip(String file, String shp) {

        return null;
    }

    @Override
    public GridCoverage2D clip(String file, Envelope2D boundingBox) {


        return null;
    }

    /**
     * @param coverage
     * @param r        the Rectangle will have it's origin in the top left corner. Rectangle(0, 0, 250, 600)
     * @return
     */
    public GridCoverage2D clip(GridCoverage2D coverage, Rectangle r) {

        ReferencedEnvelope envelope = new ReferencedEnvelope(coverage.getCoordinateReferenceSystem());
        GridGeometry2D grid = coverage.getGridGeometry();
        GridEnvelope2D genv = new GridEnvelope2D(r);
        try {
            Envelope2D e = grid.gridToWorld(genv);
            envelope.expandToInclude(e.getLowerCorner());
            envelope.expandToInclude(e.getUpperCorner());
        } catch (TransformException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return clip(coverage, envelope);
    }


    /**
     * https://docs.geotools.org/latest/userguide/library/coverage/grid.html
     * http://docs.geotools.org/latest/userguide/tutorial/coverage/coverage.html
     *
     * @param coverage
     * @param envelope
     * @return
     */
    public GridCoverage2D clip(GridCoverage2D coverage, ReferencedEnvelope envelope) {
        CoverageProcessor processor = CoverageProcessor.getInstance();
        // or use Operations.DEFAULT.crop()
        // An example of manually creating the operation and parameters we want
        final ParameterValueGroup param = processor.getOperation("CoverageCrop").getParameters();
        param.parameter("Source").setValue(coverage);
        param.parameter("Envelope").setValue(envelope);
        return (GridCoverage2D) processor.doOperation(param);
    }

    private GridCoverage2D scaleCoverage(GridCoverage2D coverage, double tileScale) {
        Operations ops = new Operations(null);
        coverage = (GridCoverage2D) ops.scale(coverage, tileScale, tileScale, 0, 0);
        return coverage;
    }

    @Override
    public GridCoverage2D convert(String srcFile, String dstFile) throws IOException {
        GridCoverage2D src = read(srcFile);
        File outFile = new File(dstFile);
        String suffix = FilenameUtils.getExtension(dstFile);
        Iterator<ImageWriter> writers = ImageIO.getImageWritersBySuffix(suffix);
        ImageWriter writer = null;
        while (writers.hasNext()) {
            writer = writers.next();
            break;
        }
        writer.setOutput(new ImageOutputStreamAdapter(new FileOutputStream(outFile)));
        writer.write(src.getRenderedImage());
        return null;
    }

}
