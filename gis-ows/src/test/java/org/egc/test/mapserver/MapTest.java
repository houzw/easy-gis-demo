package org.egc.test.mapserver;

import edu.umn.gis.mapscript.mapObj;
import edu.umn.gis.mapscript.mapscript;
import org.junit.Test;

/**
 * TODO
 *
 * @author houzhiwei
 * @date 2018/9/3 16:42
 */
public class MapTest {
    @Test
    public void test(){
//        java.lang.UnsatisfiedLinkError: no mapscript in java.library.path
        System.out.println(mapscript.msGetVersion());
        new mapObj("data/emptymap.map");
    }
}
