package org.egc.gis.taudem;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.web.ExecutionStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * <pre>
 * TauDEM Basic Grid Analysis
 * </pre>
 *
 * @author houzhiwei
 * @date 2018 /10/12 16:50
 */
@Slf4j
public class BasicGridAnalysis extends BaseTauDEM {

    /**
     * Remove Pits: Get hydrologically correct elevation grid with pits removed either by filling or carving
     * http://hydrology.usu.edu/taudem/taudem5/help53/PitRemove.html
     *
     * @param Input_Elevation_Grid              Input elevation grid to be processed
     * @param Output_Pit_Removed_Elevation_Grid Output elevation grid with pits filled
     * @return {@link ExecutionStatus}
     * @see #PitRemove(String, String, boolean, String) #PitRemove(String, String, boolean, String)#PitRemove(String, String, boolean, String)
     */
    public ExecutionStatus PitRemove(String Input_Elevation_Grid, String Output_Pit_Removed_Elevation_Grid) {
        return PitRemove(Input_Elevation_Grid, Output_Pit_Removed_Elevation_Grid, false, null);
    }

    /**
     * Remove Pits: Get hydrologically correct elevation grid with pits removed either by filling or carving
     * http://hydrology.usu.edu/taudem/taudem5/help53/PitRemove.html
     *
     * @param Input_Elevation_Grid              Input elevation grid (with full path) to be processed
     * @param Output_Pit_Removed_Elevation_Grid Output elevation grid with pits filled
     * @param only4Ways                         (Optional:false) Fill Considering only 4 way neighbors (N, S, E or W neighbors)
     * @param Input_Depression_Mask_Grid        (Optional)	depression mask file
     * @return {@link ExecutionStatus}
     */
    public ExecutionStatus PitRemove(String Input_Elevation_Grid, String Output_Pit_Removed_Elevation_Grid,
                                     boolean only4Ways, String Input_Depression_Mask_Grid)
    {
        Preconditions.checkNotNull(Strings.emptyToNull(Input_Elevation_Grid),
                                   "Input Elevation Grid Can Not Be Null Nor Empty");

        CommandLine cmd = initCmd();
        cmd.addArgument(TauDEMCommands.PIT_REMOVE);
        //Input elevation grid
        cmd.addArgument("-z");
        cmd.addArgument("${demfile}");
        //Output elevation grid with pits filled
        cmd.addArgument("-fel");
        cmd.addArgument("${felfile}");
        Map files = new HashMap(2);
        files.put("demfile", Input_Elevation_Grid);
        files.put("felfile", Output_Pit_Removed_Elevation_Grid);

        if (only4Ways) {
            //only 4 directions (N, S, E or W neighbors)
            cmd.addArgument("-4way");
        }
        if (StringUtils.isNotBlank(Input_Depression_Mask_Grid)) {
            //depression maskFile file(Optional)
            cmd.addArgument("-depmask");
            cmd.addArgument("${depmaskfile}");
            files.put("depmaskfile", Input_Depression_Mask_Grid);
        }
        return execute(cmd, files);
    }

    /**
     * Flow Directions use D8.
     *
     * @param Input_Pit_Filled_Elevation_Grid Pit filled elevation input data
     * @param Output_D8_Flow_Direction_Grid   D8 flow directions output
     * @param Output_D8_Slope_Grid            D8 slopes output
     * @return {@link ExecutionStatus}
     */
    public ExecutionStatus D8Flowdir(String Input_Pit_Filled_Elevation_Grid, String Output_D8_Flow_Direction_Grid, String Output_D8_Slope_Grid) {
        Preconditions.checkNotNull(Strings.emptyToNull(Input_Pit_Filled_Elevation_Grid),
                                   "Input Pit Filled Elevation Grid Can Not Be Null Nor Empty");

        CommandLine cmd = initCmd();
        cmd.addArgument(TauDEMCommands.D8_FLOWDIR);
        //Pit filled elevation input data
        cmd.addArgument("-fel");
        cmd.addArgument("${felfile}");
        //D8 flow directions output
        cmd.addArgument("-p");
        cmd.addArgument("${pfile}");
        //D8 slopes output
        cmd.addArgument("-sd8");
        cmd.addArgument("${sd8file}");
        Map files = new HashMap(3);
        files.put("felfile", Input_Pit_Filled_Elevation_Grid);
        files.put("pfile", Output_D8_Flow_Direction_Grid);
        files.put("sd8file", Output_D8_Slope_Grid);
        return execute(cmd, files);
    }

    /**
     * Flow Directions use Dinf.
     *
     * @param Input_Pit_Filled_Elevation_Grid      Pit filled elevation input data
     * @param Output_DInfinity_Flow_Direction_Grid Dinf flow directions output
     * @param Output_Slope_Grid                    Dinf slopes output
     * @return {@link ExecutionStatus}
     */
    public ExecutionStatus DinfFlowdir(String Input_Pit_Filled_Elevation_Grid, String Output_DInfinity_Flow_Direction_Grid, String Output_Slope_Grid) {

        Preconditions.checkNotNull(Strings.emptyToNull(Input_Pit_Filled_Elevation_Grid),
                                   "Input Pit Filled Elevation Grid Can Not Be Null Nor Empty");

        CommandLine cmd = initCmd();
        cmd.addArgument(TauDEMCommands.DINF_FLOWDIR);
        //Pit filled elevation input data
        cmd.addArgument("-fel");
        cmd.addArgument("${felfile}");
        //Dinf flow directions output
        cmd.addArgument("-ang");
        cmd.addArgument("${angfile}");
        // Dinf slopes output
        cmd.addArgument("-slp");
        cmd.addArgument("${slpfile}");
        Map files = new HashMap(3);
        files.put("felfile", Input_Pit_Filled_Elevation_Grid);
        files.put("angfile", Output_DInfinity_Flow_Direction_Grid);
        files.put("slpfile", Output_Slope_Grid);
        return execute(cmd, files);
    }

    /**
     * <p>
     * Grid network.
     * <p>Layer name and layer number should not both be specified.
     * ref: http://hydrology.usu.edu/taudem/taudem5/help53/GridNetwork.html
     * </p>
     *
     * @param Input_D8_Flow_Direction_Grid       the input d8 flow direction grid
     * @param Output_Longest_Upslope_Length_Grid grid of longest flow length upstream of each point output file
     * @param Output_Total_Upslope_Length_Grid   grid of total path length upstream of each point output file
     * @param Output_Strahler_Network_Order_Grid grid of strahler order output file
     * @return {@link ExecutionStatus}
     * @see #GridNetwork(String, String, String, String, String, String, long, String, double) #GridNetwork(String, String, String, String, String, String, long, String, double)#GridNetwork(String, String, String, String, String, String, long, String, double)
     */
    public ExecutionStatus GridNetwork(String Input_D8_Flow_Direction_Grid, String Output_Longest_Upslope_Length_Grid,
                                       String Output_Total_Upslope_Length_Grid, String Output_Strahler_Network_Order_Grid)
    {
        return GridNetwork(Input_D8_Flow_Direction_Grid, Output_Longest_Upslope_Length_Grid,
                           Output_Total_Upslope_Length_Grid, Output_Strahler_Network_Order_Grid,
                           null, null, 0, null, -1);
    }

    /**
     * <p>
     * Grid network.
     * <p>Layer name and layer number should not both be specified.
     * ref: http://hydrology.usu.edu/taudem/taudem5/help53/GridNetwork.html
     * </p>
     *
     * @param Input_D8_Flow_Direction_Grid       the input d8 flow direction grid
     * @param Output_Longest_Upslope_Length_Grid grid of longest flow length upstream of each point output file
     * @param Output_Total_Upslope_Length_Grid   grid of total path length upstream of each point output file
     * @param Output_Strahler_Network_Order_Grid grid of strahler order output file
     * @param Input_Outlets                      (Optional) the input outlets (OGR readable dataset)
     * @param layerName                          (Optional) OGR layer name if outlets are not the first layer in outletfile
     * @param layerNumber                        (Optional) OGR layer number if outlets are not the first layer in outletfile
     * @param Input_Mask_Grid                    (Optional) the input mask grid
     * @param Input_Mask_Threshold_Value         (Optional) Where a maskfile is input, there is an additional option to specify a threshold.                                           The grid network is evaluated for grid cells where values of the maskfile                                           grid read as 4 byte integers are >= threshold.
     * @return {@link ExecutionStatus}
     */
    public ExecutionStatus GridNetwork(String Input_D8_Flow_Direction_Grid,
                                       String Output_Longest_Upslope_Length_Grid,
                                       String Output_Total_Upslope_Length_Grid,
                                       String Output_Strahler_Network_Order_Grid,
                                       String Input_Outlets, String layerName,
                                       long layerNumber, String Input_Mask_Grid,
                                       double Input_Mask_Threshold_Value)
    {
        Preconditions.checkNotNull(Strings.emptyToNull(Input_D8_Flow_Direction_Grid),
                                   "Input D8 Flow Direction Grid Can Not Be Null Nor Empty");

        CommandLine cmd = initCmd();
        cmd.addArgument(TauDEMCommands.GRID_NET);
        //D8 flow directions input file
        cmd.addArgument("-p");
        cmd.addArgument("${pfile}");
        //grid of longest flow length upstream of each point output file
        cmd.addArgument("-plen");
        cmd.addArgument("${plenfile}");
        //grid of total path length upstream of each point output file
        cmd.addArgument("-tlen");
        cmd.addArgument("${tlenfile}");
        //grid of strahler order output file
        cmd.addArgument("-gord");
        cmd.addArgument("${gordfile}");

        Map files = new HashMap(4);
        files.put("pfile", Input_D8_Flow_Direction_Grid);
        files.put("plenfile", Output_Longest_Upslope_Length_Grid);
        files.put("tlenfile", Output_Total_Upslope_Length_Grid);
        files.put("gordfile", Output_Strahler_Network_Order_Grid);

        if (StringUtils.isNotBlank(Input_Outlets)) {
            // input outlets file (OGR readable dataset) (Optional)
            cmd.addArgument("-o");
            cmd.addArgument("${outletfile}");
            files.put("outletfile", Input_Outlets);
        }
        if (StringUtils.isNotBlank(layerName)) {
            //OGR layer name if outlets are not the first layer in outletfile (optional)
            cmd.addArgument("-lyrname");
            cmd.addArgument(layerName);
        } else if (layerNumber > 0) {
            //OGR layer number if outlets are not the first layer in outletfile (optional)
            cmd.addArgument("-lyrno");
            cmd.addArgument(layerNumber + "");
        }

        if (StringUtils.isNotBlank(Input_Mask_Grid)) {
            //mask file(optional)
            cmd.addArgument("-mask");
            cmd.addArgument("${maskfile}");
            files.put("maskfile", Input_Mask_Grid);
        }
        if (Input_Mask_Threshold_Value > 0) {
            // Where a maskfile is input, there is an additional option to specify a threshold.
            // This has to be immediately following the maskfile on the argument list.
            cmd.addArgument("-thresh");
            cmd.addArgument(Input_Mask_Threshold_Value + "");
        }

        return execute(cmd, files);
    }

    /**
     * 使用D8计算汇流面积。
     *
     * @param Input_D8_Flow_Direction_Grid Input flow directions grid
     * @param Output_D8_Contributing_Area_Grid                      Output contributing area grid
     * @return {@link ExecutionStatus}
     * @see #D8ContributingArea(String, String, String, String, boolean, String, Integer) #D8ContributingArea(String, String, String, String, boolean, String, Integer)#D8ContributingArea(String, String, String, String, boolean, String, Integer)
     */
    public ExecutionStatus D8ContributingArea(String Input_D8_Flow_Direction_Grid, String Output_D8_Contributing_Area_Grid) {
        return D8ContributingArea(Input_D8_Flow_Direction_Grid, Output_D8_Contributing_Area_Grid, null, null, false, null, null);
    }

    /**
     * <pre>
     * 使用D8计算汇流面积。
     * optional 参数不需要时，应赋值为 null
     * Layer name and layer number should not both be specified.
     * </pre>
     * 命令行语法([]中为可选参数)
     * <p> {@code mpiexec -n <number of processors> AreaD8  -p <pfile> -ad8 <ad8file>
     * [ -o <outletfile>] [ -wg <wgfile>] [ -nc ]  [ -lyrname <layer name>] [ -lyrno <layer number>]
     * }****
     *
     * @param Input_D8_Flow_Direction_Grid     Input flow directions grid (pfile)
     * @param Output_D8_Contributing_Area_Grid Output contributing area grid (ad8file)
     * @param Input_Outlets                    input outlets file (OGR readable dataset, optional)
     * @param Input_Weight_Grid                Input weight grid (optional)
     * @param Check_for_edge_contamination     Flag for edge contamination
     * @param layerName                        OGR layer name if outlets are not the first layer in outletFile (optional)
     * @param layerNumber                      OGR layer number if outlets are not the first layer in outletFile (optional)
     * @return {@link ExecutionStatus}
     */
    public ExecutionStatus D8ContributingArea(String Input_D8_Flow_Direction_Grid,
                                              String Output_D8_Contributing_Area_Grid,
                                              String Input_Outlets, String Input_Weight_Grid,
                                              boolean Check_for_edge_contamination,
                                              String layerName, Integer layerNumber)
    {
        Preconditions.checkNotNull(Strings.emptyToNull(Input_D8_Flow_Direction_Grid),
                                   "Input D8 Flow Direction Grid Can Not Be Null Nor Empty");

        CommandLine cmd = initCmd();
        cmd.addArgument(TauDEMCommands.D8_Contributing_Area);
        cmd.addArgument("-p");
        cmd.addArgument("${pfile}");
        cmd.addArgument("-ad8");
        cmd.addArgument("${ad8file}");
        Map files = new HashMap(4);
        files.put("pfile", Input_D8_Flow_Direction_Grid);
        files.put("ad8file", Output_D8_Contributing_Area_Grid);

        if (StringUtils.isNotBlank(Input_Outlets)) {
            cmd.addArgument("-o");
            cmd.addArgument("${outletfile}");
            files.put("outletfile", Input_Outlets);
        }
        if (StringUtils.isNotBlank(Input_Weight_Grid)) {
            cmd.addArgument("-wg");
            cmd.addArgument("${wgfile}");
            files.put("wgfile", Input_Weight_Grid);
        }
        if (Check_for_edge_contamination) {
            cmd.addArgument("-nc");
        }

        if (StringUtils.isNotBlank(layerName)) {
            cmd.addArgument("-lyrname");
            cmd.addArgument(layerName);
        } else if (layerNumber != null && layerNumber > 0) {
            cmd.addArgument("-lyrno");
            cmd.addArgument("" + layerNumber);
        }
        return execute(cmd, files);
    }

    /**
     * <p>D-infinity contributing area execution status.
     * <p>Layer name and layer number should not both be specified.
     *
     * @param Input_DInfinity_Flow_Direction_Grid           the input d-infinity flow direction grid
     * @param Output_DInfinity_Specific_Catchment_Area_Grid the output d-infinity specific catchment area grid
     * @return the execution status
     */
    public ExecutionStatus DInfinityContributingArea(String Input_DInfinity_Flow_Direction_Grid,
                                                     String Output_DInfinity_Specific_Catchment_Area_Grid)
    {
        return DInfinityContributingArea(Input_DInfinity_Flow_Direction_Grid, Output_DInfinity_Specific_Catchment_Area_Grid,
                                         null, null, false, null, null);
    }

    /**
     * <p>D-infinity contributing area execution status.
     * <p>Layer name and layer number should not both be specified.
     *
     * @param Input_DInfinity_Flow_Direction_Grid           the input d-infinity flow direction grid
     * @param Output_DInfinity_Specific_Catchment_Area_Grid the output d-infinity specific catchment area grid
     * @param Input_Outlets                                 (Optional) the input outlets
     * @param Input_Weight_Grid                             (Optional) the input weight grid
     * @param Check_for_Edge_Contamination                  the check for edge contamination
     * @param layerName                                     (Optional) the layer name
     * @param layerNumber                                   (Optional) the layer number
     * @return the execution status
     */
    public ExecutionStatus DInfinityContributingArea(String Input_DInfinity_Flow_Direction_Grid,
                                                     String Output_DInfinity_Specific_Catchment_Area_Grid,
                                                     String Input_Outlets, String Input_Weight_Grid,
                                                     boolean Check_for_Edge_Contamination, String layerName, Integer layerNumber)
    {
        Preconditions.checkNotNull(Strings.emptyToNull(Input_DInfinity_Flow_Direction_Grid),
                                   "Input D-Infinity Flow Direction Grid Can Not Be Null Nor Empty");
        CommandLine cmd = initCmd();
        cmd.addArgument(TauDEMCommands.D_Infinity_Contributing_Area);
        // Input Dinfflow directions grid
        cmd.addArgument("-ang");
        cmd.addArgument("${angfile}");
        //Output Dinf contributing area grid
        cmd.addArgument("-sca");
        cmd.addArgument("${scafile}");
        Map files = new HashMap(4);
        files.put("angfile", Input_DInfinity_Flow_Direction_Grid);
        files.put("scafile", Output_DInfinity_Specific_Catchment_Area_Grid);

        if (StringUtils.isNotBlank(Input_Outlets)) {
            // input outlets file (OGR readable dataset)
            cmd.addArgument("-o");
            cmd.addArgument("${outletfile}");
            files.put("outletfile", Input_Outlets);
        }
        if (StringUtils.isNotBlank(Input_Weight_Grid)) {
            //Input weight grid (optional)
            cmd.addArgument("-wg");
            cmd.addArgument("${wgfile}");
            files.put("outletfile", Input_Weight_Grid);
        }

        if (Check_for_Edge_Contamination) {
            //Flag for edge contamination
            cmd.addArgument("-nc");
        }

        if (StringUtils.isNotBlank(layerName)) {
            //OGR layer name if outlets are not the first layer in outletfile (optional)
            cmd.addArgument("-lyrname");
            cmd.addArgument(layerName);
        } else if (layerNumber != null && layerNumber > 0) {
            //OGR layer number if outlets are not the first layer in outletfile (optional)
            cmd.addArgument("-lyrno");
            cmd.addArgument(layerNumber + "");
        }
        return execute(cmd, files);
    }

}
