package org.egc.gis.ows.geoserver;


import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.FilenameUtils;

import java.io.*;

/**
 * zip 文件操作
 * https://itsallbinary.com/apache-commons-compress-simplest-zip-zip-with-directory-compression-level-unzip/
 */
public class VectorPublisher extends GSManager {

    /**
     * 发布shapefile文件,layerName要与zip文件中的shp文件名相同
     *
     * @return
     * @throws FileNotFoundException
     */
    public boolean pubExternalShape(String storeName, String zipFilePath, String style) throws IOException {
        File zipFile = new File(zipFilePath);
        if (!zipFile.exists()) {
            return false;
        }
        String layerName = "";
        ZipArchiveInputStream zip = new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(zipFilePath)));
        ZipArchiveEntry ze;
        while ((ze = zip.getNextZipEntry()) != null) {
            String name = ze.getName();
            if ("shp".equalsIgnoreCase(FilenameUtils.getExtension(name))) {
                layerName = name;
                break;
            }
        }
        boolean b = publisher.publishShp(DEFAULT_WS, storeName, layerName, zipFile, "EPSG:4326", style);
        return b;
    }

}
