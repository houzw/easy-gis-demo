package org.egc.gis.ows.mapserver;

import edu.umn.gis.mapscript.mapObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 * https://github.com/mapserver/mapserver/tree/master/mapscript/java
 *
 * @author houzhiwei
 * @date 2018/9/2 22:05
 */
public class Mapfile {
    private static final Logger logger = LoggerFactory.getLogger(Mapfile.class);

    public boolean createMap(){

        mapObj map = new mapObj("mapname");
        return true;
    }
}
