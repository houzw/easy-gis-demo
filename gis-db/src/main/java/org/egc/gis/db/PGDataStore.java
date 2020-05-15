package org.egc.gis.db;

import org.apache.commons.lang3.StringUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.postgis.PostgisNGDataStoreFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * GeoTools PostGIS data store
 * <pre>http://docs.geotools.org/stable/userguide/library/jdbc/postgis.html</pre>
 *
 * @author houzhiwei
 */
public class PGDataStore {
    private static final Logger log = LoggerFactory.getLogger(PGDataStore.class);
    private String host = "127.0.0.1";
    private int port = 5432;
    @NotBlank
    private String dbname;
    private String schema = "public";
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    private DataStore dataStore = null;

    public PGDataStore(String dbName, String username, String pw) {
        this.dbname = dbName;
        this.username = username;
        this.password = pw;
    }

    public PGDataStore(String dbName, String username, String pw, String host, int port, String schema) {
        this.dbname = dbName;
        this.username = username;
        this.password = pw;
        this.host = host;
        this.port = port;
        this.schema = schema;
    }

    public DataStore connect() throws IOException {
        Map<String, Object> params = new HashMap<>(8);
        params.put("dbtype", "postgis");
        params.put("host", this.host);
        params.put("port", this.port);
        params.put("schema", this.schema);
        params.put("database", this.dbname);
        params.put("user", this.username);
        params.put("passwd", this.password);
        params.put(PostgisNGDataStoreFactory.PREPARED_STATEMENTS.key, true);
        this.dataStore = DataStoreFinder.getDataStore(params);
        log.info("PostGIS database [ %s ] connected.", dbname);
        return dataStore;
    }

    public void dispose() {
        this.dataStore.dispose();
        this.dataStore = null;
        log.info("PostGIS database [ %s ] disposed.", dbname);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
