package org.egc.gis.vector;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.gml2.GML;
import org.geotools.gml2.GMLConfiguration;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Encoder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.awt.*;
import java.io.*;


/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/1/9 10:33
 */
public class VectorUtils {
    /**
     * Here is how you can use a SimpleFeatureType builder to create the schema for your shapefile
     * dynamically.
     *
     * <p>This method is an improvement on the code used in the main method above (where we used
     * DataUtilities.createFeatureType) because we can set a Coordinate Reference System for the
     * FeatureType and a a maximum field length for the 'name' field dddd
     */
    public SimpleFeatureType createFeatureType() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("Location");
        builder.setCRS(DefaultGeographicCRS.WGS84);
        // add attributes in order
        builder.add("the_geom", Point.class);
        // <- 15 chars width for name field
        builder.length(15).add("Name", String.class);
        builder.add("number", Integer.class);
        // build the type
        final SimpleFeatureType LOCATION = builder.buildFeatureType();
        return LOCATION;
    }

    // https://gis.stackexchange.com/questions/178890/plot-the-longitude-and-latitudes-on-map-using-geotools
    public Layer addPoint(double latitude, double longitude) {
        SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
        b.setName("MyFeatureType");
        b.setCRS(DefaultGeographicCRS.WGS84);
        b.add("location", Point.class);
        // building the type
        final SimpleFeatureType TYPE = b.buildFeatureType();

        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(latitude, longitude));
        featureBuilder.add(point);
        SimpleFeature feature = featureBuilder.buildFeature(null);
        DefaultFeatureCollection featureCollection = new DefaultFeatureCollection("internal", TYPE);
        featureCollection.add(feature);
        Style style = SLD.createSimpleStyle(TYPE, Color.red);

        Layer layer = new FeatureLayer(featureCollection, style);
        return layer;
    }

    // https://gis.stackexchange.com/questions/53863/what-is-the-best-way-to-convert-wkt-string-to-gml-string

    public static String WKTToGML2(String wkt) throws IOException, ParseException {
        WKTReader wktR = new WKTReader();
        Geometry geom = wktR.read(wkt);
        Configuration configuration = new GMLConfiguration();
        Encoder encoder = new Encoder(configuration);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        encoder.encode(geom, GML._Geometry, out);
        return out.toString();
    }

    public static String WKTToGML3(String wkt) throws IOException, ParseException {
        WKTReader wktR = new WKTReader();
        Geometry geom = wktR.read(wkt);

        Configuration configuration = new org.geotools.gml3.GMLConfiguration();
        Encoder encoder = new Encoder(configuration);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        encoder.encode(geom, org.geotools.gml3.GML.geometryMember, out);
        return out.toString();
    }

    public static String feature2WKT(SimpleFeatureCollection collection) {
        WKTWriter wktWriter = new WKTWriter();
        StringBuilder sb = new StringBuilder();
        while (collection.features().hasNext()) {
            SimpleFeature feature = collection.features().next();
            Geometry geometry = (Geometry) feature.getDefaultGeometry();
            sb.append(wktWriter.write(geometry));
        }
        return sb.toString();
    }

    public static FeatureCollection readGeoJSONFile(String geojsonFile) throws IOException {
        FeatureJSON featureJSON = new FeatureJSON();
        featureJSON.setEncodeNullValues(true);
        InputStreamReader streamReader = new InputStreamReader(new FileInputStream(geojsonFile), "utf-8");
        FeatureCollection featureCollection = featureJSON.readFeatureCollection(streamReader);
        return featureCollection;
    }

    /**
     * 若确定类型，可以直接转换
     * <pre>
     * {@code
     *      Point p = gjson.readPoint(reader);
     * }
     * @param json GeoJSON string
     * @return
     * @throws IOException
     */
    public static GeometryCollection readGeoJSON(String json) throws IOException {
        GeometryJSON gjson = new GeometryJSON();
        Reader reader = new StringReader(json);
        GeometryCollection geometryCollection = gjson.readGeometryCollection(reader);
        return geometryCollection;
    }

    public static String toGeoJSON(SimpleFeatureCollection collection) throws IOException {
        FeatureJSON fjson = new FeatureJSON(new GeometryJSON(15));
        StringWriter writer = new StringWriter();
        fjson.writeFeatureCollection(collection, writer);
        return writer.toString();
    }
}
