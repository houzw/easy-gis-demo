package org.egc.gis.ows.test;

import it.geosolutions.geoserver.rest.decoder.RESTWorkspaceList;
import it.geosolutions.geoserver.rest.encoder.GSResourceEncoder;
import it.geosolutions.geoserver.rest.encoder.coverage.GSCoverageEncoder;
import it.geosolutions.geoserver.rest.encoder.datastore.GSPostGISDatastoreEncoder;
import it.geosolutions.geoserver.rest.encoder.datastore.GSShapefileDatastoreEncoder;
import it.geosolutions.geoserver.rest.encoder.metadata.GSDimensionInfoEncoder;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.FilenameUtils;
import org.egc.gis.ows.geoserver.GSManager;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class GSTest {
    String newWS = "egc";

    @Test
    public void ws() {
        RESTWorkspaceList workspaces = GSManager.reader.getWorkspaces();
        for (RESTWorkspaceList.RESTShortWorkspace workspace : workspaces) {
            System.out.println(workspace.getName());
            //Data stores
            System.out.println(GSManager.reader.getDatastores(workspace.getName()).getNames());
        }
    }

    @Test
    public void createWS() {
        System.out.println(GSManager.reader.existsWorkspace(newWS));
        boolean b = GSManager.publisher.createWorkspace(newWS);
        System.out.println(b);
    }


    @Test
    public void layerNameFromZip() throws IOException {
        URL resource = this.getClass().getClassLoader().getResource("ne_50m_populated_places.zip");

        File zipFile = new File(resource.getFile());
        String layerName = "";
        ZipArchiveInputStream zip = new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
        ZipArchiveEntry ze;
        while ((ze = zip.getNextZipEntry()) != null) {
            String name = ze.getName();
            if ("shp".equalsIgnoreCase(FilenameUtils.getExtension(name))) {
                layerName = name;
                break;
            }
        }
        System.out.println(layerName);
    }

    public String getShpNameFromZippedFile(String zipFilePath) throws IOException {
        File zipFile = new File(zipFilePath);
        String layerName = "";
        ZipArchiveInputStream zip = new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
        ZipArchiveEntry ze;
        while ((ze = zip.getNextZipEntry()) != null) {
            String name = ze.getName();
            if ("shp".equalsIgnoreCase(FilenameUtils.getExtension(name))) {
                layerName = name;
                break;
            }
        }
        return layerName;
    }


    @Test
    public void pubStyle() throws IOException {
        ClassPathResource resource = new ClassPathResource("world_dem.sld");
        boolean b = GSManager.publisher.publishStyleInWorkspace(newWS, resource.getFile(),"world_dem");
        System.out.println(b);
    }

    @Test
    public void resource() throws IOException {
        ClassPathResource resource = new ClassPathResource("world_dem.sld");
        System.out.println(resource.getFilename());
        System.out.println(resource.getFile().getAbsolutePath());
    }

    @Test
    public void pubShp() {

    }

    @Test
    public void pubTif() {

    }

    public static final String WGS84 = "GEOGCS[\"WGS84(DD)," +
            "DATUM[\"WGS84\"," +
            "SPHEROID[\"WGS84\", 6378137.0, 298.257223563]]," +
            "PRIMEM[\"Greenwich\", 0.0]," +
            "UNIT[\"degree\", 0.017453292519943295]," +
            "AXIS[\"Geodetic longitude\", EAST]," +
            "AXIS[\"Geodetic latitude\", NORTH]]";

    @Test
    public void createPGStore() throws MalformedURLException {
        GSPostGISDatastoreEncoder pgStore = new GSPostGISDatastoreEncoder("pgStore");
        //pgStore属性：连接参数等
        pgStore.setDatabase("db");

        boolean b = GSManager.reader.existsDatastore("easy", "pgStore");
        GSManager.manager.getStoreManager().create("easy", pgStore);
        //url 是相对与$GEOSERVER_DATA_DIR的路径
        GSShapefileDatastoreEncoder shpStore = new GSShapefileDatastoreEncoder("shpStore", new URL("url"));
        GSCoverageEncoder coverageEncoder = new GSCoverageEncoder();
        coverageEncoder.setNativeFormat("GTiff");
        coverageEncoder.addKeyword("KEYWORD_1");

        final GSDimensionInfoEncoder timeDimension = new GSDimensionInfoEncoder(true);
        timeDimension.setPresentation(GSDimensionInfoEncoder.Presentation.CONTINUOUS_INTERVAL);
        coverageEncoder.setMetadataDimension("time", timeDimension);


        GSResourceEncoder re = new GSCoverageEncoder();
        re.setProjectionPolicy(GSResourceEncoder.ProjectionPolicy.FORCE_DECLARED);
        re.setSRS("EPSG:4326");
        re.setNativeCRS("EPSG:4326");
        re.setLatLonBoundingBox(-180d, 90d, 180d, -90d, WGS84);
//        GSManager.publisher.publishExternalGeoTIFF();
    }
}
