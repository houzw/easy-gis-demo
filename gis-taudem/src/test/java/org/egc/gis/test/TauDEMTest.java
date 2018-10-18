package org.egc.gis.test;

import org.apache.commons.exec.CommandLine;
import org.egc.commons.command.CommonsExec;
import org.egc.gis.taudem.TauDEMCommands;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/**
 * TODO
 *
 * @author houzhiwei
 * @date 2018/9/4 14:36
 */
public class TauDEMTest {
    String tif = "H:/grass-demos/demo1/01_DEM.tif";


    private String insert2Filename(String filename, String insertStr) {
        StringBuilder sb = new StringBuilder(filename);
        sb.insert(filename.lastIndexOf("."), "_" + insertStr);
        return sb.toString();
    }

    // out.tif 生成到了 F:\workspace\easy-gis\gis-taudem
    @Test
    public void test() throws IOException {

        CommandLine cmd = new CommandLine("cmd");
        cmd.addArgument("/c");
        cmd.addArgument("cd H:/gisdemo/out/taudem", false);
        cmd.addArgument("&");
        cmd.addArgument(TauDEMCommands.MPIEXEC);
        cmd.addArgument("-n 4", false);
        cmd.addArgument(TauDEMCommands.PIT_REMOVE);
        cmd.addArgument("-z " + tif, false);
        cmd.addArgument("-fel out.tif", false);
        String cmdstr = String.join(" ", cmd.toStrings());
        System.out.println(String.join(" ", cmd.toStrings()));
        Map map = CommonsExec.execWithOutput(cmdstr);
        System.out.println(map.get("out"));
        System.out.println(map.get("error"));
    }

    @Test
    public void testPitRemove() throws IOException {
        CommandLine cmd = new CommandLine(TauDEMCommands.MPIEXEC);
        cmd.addArgument("-n");
        cmd.addArgument("4");
        cmd.addArgument(TauDEMCommands.PIT_REMOVE);
        cmd.addArgument("-z" );
        cmd.addArgument(tif);
        cmd.addArgument("-fel");
        cmd.addArgument("H:/gisdemo/out/taudem/out.tif" );
        String cmdstr = String.join(" ", cmd.toStrings());
        System.out.println(String.join(" ", cmd.toStrings()));
        System.out.println(cmd.toString());
        Map map = CommonsExec.execWithOutput(cmd);
        System.out.println(map.get("out"));
        System.out.println(map.get("error"));
    }

}
