package org.egc.gis.db;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.postgis.PostgisNGDataStoreFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.egc.gis.db.ConnectionParams.*;

/**
 * PostGIS Connection
 *
 * @author houzhiwei
 * @date 2018 /6/4 20:49
 */
public class PostGISConnection {

    private static final Logger log = LoggerFactory.getLogger(PostGISConnection.class);

    /**
     * Connect.
     *
     * @param properties the properties.  Must contains keys named  "database", "user", "password"
     * @return the connection
     * @throws ClassNotFoundException the class not found exception
     * @throws SQLException           the sql exception
     */
    public Connection connect(Properties properties) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/" + "";
        Connection pgConn = DriverManager.getConnection(url, "", "");
//        Statement stmt = pgConn.createStatement();
//        stmt.executeQuery("");
//        stmt.close();
        return pgConn;
    }

    /**
     * <pre>http://docs.geotools.org/stable/userguide/library/jdbc/postgis.html</pre>
     *
     * @param properties database configuration properties. Must contains keys named  "database", "user", "password"
     * @return the data store
     * @throws IOException the io exception
     */
    public DataStore connectUseGeoTools(Properties properties) throws IOException {
        Map params = new HashMap(8);
        params.put(DBTYPE, POSTGIS);
        params.put(HOST, LOCAL_HOST);
        params.put(PORT, 5432);
        params.put(SCHEMA, "public");
        params.put(DATABASE, properties.get("database"));
        params.put(USER, properties.get("user"));
        params.put(PASSWORD, properties.get("password"));
        params.put(PostgisNGDataStoreFactory.PREPARED_STATEMENTS, true);
        DataStore store = DataStoreFinder.getDataStore(params);
        log.info("PostGIS database [ %s ] connected.", properties.get("database"));
        return store;
    }
}
