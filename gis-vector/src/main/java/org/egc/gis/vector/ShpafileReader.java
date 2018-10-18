package org.egc.gis.vector;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;

import java.io.File;
import java.io.IOException;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/10/9 17:45
 */
public class ShpafileReader {


    public void readShp(String file) throws IOException {
        ShapefileDataStoreFactory storeFactory = new ShapefileDataStoreFactory();
        ShapefileDataStore dataStore = (ShapefileDataStore)storeFactory.createDataStore(new File(file).toURI().toURL());
    }

}
