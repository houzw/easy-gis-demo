package org.egc.gis.ows.wcs20.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/11/4 22:42
 */
@Data
public class CoverageBoundingBox {
    private String crs = "EPSG:4326";
    private String lowerCorner;
    private String upperCorner;


    public ReferencedEnvelope getReferencedEnvelope() throws FactoryException {
        CoordinateReferenceSystem sourceCRS = null;
        if (StringUtils.isBlank(crs)) {
            sourceCRS = CRS.decode("EPSG:4326");
        } else {
            //http://www.opengis.net/def/crs/EPSG/0/EPSG:4326
            if (crs.contains("http://www.opengis.net/def/crs/EPSG")) {
                crs = crs.substring(crs.length() - 9);
            }
            sourceCRS = CRS.decode(crs);
        }
        // xMin,xMax,yMin,yMax
        String[] lowerCornerValues = lowerCorner.split(" ");
        String[] upperCornerValues = upperCorner.split(" ");
        Double[] lowerCornerPoint = new Double[]{Double.valueOf(lowerCornerValues[0]), Double.valueOf(lowerCornerValues[1])};
        Double[] upperCornerPoint = new Double[]{Double.valueOf(upperCornerValues[0]), Double.valueOf(upperCornerValues[1])};
        ReferencedEnvelope envelope = new ReferencedEnvelope(lowerCornerPoint[0], upperCornerPoint[0], lowerCornerPoint[1], upperCornerPoint[1], sourceCRS);
        return envelope;
    }
}
