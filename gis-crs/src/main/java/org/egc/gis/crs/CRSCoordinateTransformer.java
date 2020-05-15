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

    /**
     * 根据分带号求中央经线
     *
     * @param zone    带号
     * @param isZone3 是否3°分带
     * @return 中央经线经度
     */
    public int utmCentralMeridian(int zone, boolean isZone3) {
        if (isZone3) {
            return zone * 3;
        } else {
            return zone * 6 - 3;
        }
    }

    /**
     * 根据中央经线度数求带号
     *
     * @param centralMeridian 中央经线经度
     * @param isZone3         是否3°分带
     * @return
     */
    public int utmZoneCentral(int centralMeridian, boolean isZone3) {
        if (isZone3) {
            return centralMeridian / 3;
        } else {
            return (centralMeridian + 3) / 6;
        }
    }

    /**
     * 根据经度获取带号
     * @param lon 经度
     * @param isZone3 是否3°带
     * @return 带号
     */
    public int utmZone(int lon, boolean isZone3) {
        int zone = 0;
        if (isZone3) {
            if (lon % 3 > 1.5) {
                zone = lon / 3 + 1;
            } else {
                zone = lon / 3;
            }
        } else {
            if (Math.abs((lon + 3) % 6) < 3) {
                zone = (lon + 3) / 6;
            } else {
                zone = (lon + 3) / 6 + 1;
            }
        }
        return zone;
    }
}
