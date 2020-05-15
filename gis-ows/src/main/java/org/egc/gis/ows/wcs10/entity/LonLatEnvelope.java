package org.egc.gis.ows.wcs10.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.util.List;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/11/14 20:33
 */
@Data
public class LonLatEnvelope {
    private List<String> pos;

    /**
     * @param srsName e.g., EPSG:4326 or “OGC URN” syntax: "urn:ogc:def:ellipsoid:EPSG:6.0:7001",
     *                use "EPSG:4326" if null
     * @return
     * @throws FactoryException
     */
    public ReferencedEnvelope getReferencedEnvelope(String srsName) throws FactoryException {
        CoordinateReferenceSystem sourceCRS = null;
        if (StringUtils.isNotBlank(srsName)) {
            sourceCRS = CRS.decode(srsName);
        } else {
            sourceCRS = CRS.decode("EPSG:4326");
        }
        // xMin,xMax,yMin,yMax
        String[] lowerCornerValues = pos.get(0).split(" ");
        String[] upperCornerValues = pos.get(1).split(" ");
        Double[] lowerCornerPoint = new Double[]{Double.valueOf(lowerCornerValues[0]), Double.valueOf(lowerCornerValues[1])};
        Double[] upperCornerPoint = new Double[]{Double.valueOf(upperCornerValues[0]), Double.valueOf(upperCornerValues[1])};
        ReferencedEnvelope envelope = new ReferencedEnvelope(lowerCornerPoint[0], upperCornerPoint[0], lowerCornerPoint[1], upperCornerPoint[1], sourceCRS);
        return envelope;
    }
}
