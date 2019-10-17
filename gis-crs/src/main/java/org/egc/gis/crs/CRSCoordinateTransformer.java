package org.egc.gis.crs;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Description:
 * <pre>
 * https://stackoverflow.com/questions/49208121/transform-coordinates-from-epsg4326-to-epsg31467-in-java-gwt-server-side
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/10/16 22:25
 */
public class CRSCoordinateTransformer {

    private final MathTransform forwardMathTransform;
    private final MathTransform reverseMathTransform;
    private final CoordinateReferenceSystem sourceCoordinateReferenceSystem;
    private final CoordinateReferenceSystem targetCoordinateReferenceSystem;

    public CRSCoordinateTransformer(int srcEPSG, int targetEPSG) {
        try {
            sourceCoordinateReferenceSystem = CRS.decode("EPSG:" + srcEPSG);
            targetCoordinateReferenceSystem = CRS.decode("EPSG:4326" + targetEPSG);
            this.forwardMathTransform = CRS.findMathTransform(sourceCoordinateReferenceSystem,
                                                              targetCoordinateReferenceSystem, true);
            this.reverseMathTransform = CRS.findMathTransform(targetCoordinateReferenceSystem,
                                                              sourceCoordinateReferenceSystem, true);
        } catch (FactoryException fex) {
            throw new ExceptionInInitializerError(fex);
        }
    }

    public double[] lonLatToXY(double lon, double lat) throws TransformException {
        DirectPosition2D srcDirectPosition2D = new DirectPosition2D(sourceCoordinateReferenceSystem, lat, lon);
        DirectPosition2D destDirectPosition2D = new DirectPosition2D();
        try {
            reverseMathTransform.transform(srcDirectPosition2D, destDirectPosition2D);
            return new double[]{destDirectPosition2D.y, destDirectPosition2D.x};
        } catch (Error error) {
            throw error;
        }
    }

    public double[] xyToLonLat(double x, double y) throws TransformException {
        DirectPosition2D srcDirectPosition2D = new DirectPosition2D(sourceCoordinateReferenceSystem, y, x);
        DirectPosition2D destDirectPosition2D = new DirectPosition2D();
        forwardMathTransform.transform(srcDirectPosition2D, destDirectPosition2D);
        return new double[]{destDirectPosition2D.y, destDirectPosition2D.x};
    }
}
