package org.egc.gis.taudem;

import org.apache.commons.exec.CommandLine;
import org.egc.commons.command.CommonsExec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author houzhiwei
 * @date 2018 /9/3 22:15
 */
public class RunTauDEM {

    private static final Logger logger = LoggerFactory.getLogger(RunTauDEM.class);

    private String cmdPath = TauConsts.TauDEMPath;
    private String inputDir = TauConsts.INPUT_DATA_DIR + File.separator;
    private String outputDir = TauConsts.OUT_DATA_DIR + File.separator;
    private int processes = TauConsts.PROCESSES;


    /**
     * Remove Pits: Get hydrologically correct elevation grid with pits removed either by filling or carving
     *
     * @param inputDEM  input DEM file (with full path) to be processed
     * @param filledDEM pits removed DEM file name
     * @return 0 : success <br/> 1: failed
     */
    public int PitRemove(String inputDEM, String filledDEM) {
        CommandLine cmd = new CommandLine(TauDEMCommands.MPIEXEC);
        cmd.addArgument("-n " + processes);
        cmd.addArgument(TauDEMCommands.PIT_REMOVE);
        cmd.addArgument("-z");
        cmd.addArgument("${input}");
        cmd.addArgument("-fel");
        cmd.addArgument("${filled}");
        Map files = new HashMap(2);
        files.put("input", inputDEM);

        files.put("filled", filledDEM);
        cmd.setSubstitutionMap(files);
        try {
            CommonsExec.execWithOutput(cmd);
        } catch (IOException io) {
            logger.error(io.getMessage(), io);
            return 1;
        }
        return 0;
    }

    /**
     * Flow Directions use D8.
     *
     * @param filledDEM the filled dem
     * @param flowdir   the flow direction dem
     * @return 0 : success <br/> 1: failed
     */
    public int D8Flowdir(String filledDEM, String flowdir) {
        CommandLine cmd = new CommandLine(TauDEMCommands.MPIEXEC);
        cmd.addArgument("-n " + processes);
        cmd.addArgument(TauDEMCommands.D8_FLOWDIR);
        cmd.addArgument("-fel");
        cmd.addArgument("${filled}");
        cmd.addArgument("-p");
        cmd.addArgument("${flow}");
        cmd.addArgument("-sd8");
        cmd.addArgument("${slop}");
        Map files = new HashMap(3);
        files.put("filled", filledDEM);
        files.put("flow", flowdir);
        files.put("slop", "-slp.tif");
        cmd.setSubstitutionMap(files);
        try {
            CommonsExec.execWithOutput(cmd);
        } catch (IOException io) {
            logger.error(io.getMessage(), io);
            return 1;
        }
        return 0;
    }

    /**
     * Flow Directions use Dinf.
     *
     * @param filledDEM the filled dem
     * @param flowdir   the flow direction dem
     * @return 0 : success <br/> 1: failed
     */
    public int DinfFlowdir(String filledDEM, String flowdir) {
        CommandLine cmd = new CommandLine(TauDEMCommands.MPIEXEC);
        cmd.addArgument("-n " + processes);
        cmd.addArgument(TauDEMCommands.DINF_FLOWDIR);
        cmd.addArgument("-fel");
        cmd.addArgument("${filled}");
        cmd.addArgument("-ang");
        cmd.addArgument("${flow}");
        cmd.addArgument("-slp");
        cmd.addArgument("${slop}");
        Map files = new HashMap(3);
        files.put("filled", filledDEM);
        files.put("flow", flowdir);
        files.put("slop", "-slp.tif");
        cmd.setSubstitutionMap(files);
        try {
            CommonsExec.execWithOutput(cmd);
        } catch (IOException io) {
            logger.error(io.getMessage(), io);
            return 1;
        }
        return 0;
    }

    /**
     * @param inputDEM
     * @param outputFileName
     * @return
     */
    private String getOutputFilename(String inputDEM, String outputFileName) {
        File input = new File(inputDEM);
        input.getName();
        return "";
    }
}
