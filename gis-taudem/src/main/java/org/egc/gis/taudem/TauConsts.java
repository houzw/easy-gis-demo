package org.egc.gis.taudem;

import org.egc.commons.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * 常量
 *
 * @author jjc & houzhiwei
 * @date 2016/11/23 11:26
 */
public class TauConsts {
    private static final Logger logger = LoggerFactory.getLogger(TauConsts.class);

    static {
        try {
            Properties p = PropertiesUtil.readPropertiesFromConfig("taudem");
            TauDEMPath = p.getProperty("TauDEMPath");
            INPUT_DATA_DIR = p.getProperty("data.input");
            OUT_DATA_DIR = p.getProperty("data.output").trim();
            exepath = p.getProperty("exepath").trim();
            ODE_BasePath = p.getProperty("ODE.BasePath").trim();
            SHAPEPATH = p.getProperty("SHAPEPATH").trim();
            PROCESSES = Integer.parseInt(p.getProperty("processes", 4 + ""));
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug(e.getLocalizedMessage());
        }
    }

    public static int PROCESSES;
    public static String TauDEMPath;
    public static String INPUT_DATA_DIR;
    public static String OUT_DATA_DIR;
    public static String ODE_BasePath;
    public static String SrcBasePath;
    public static String HttpFilePath;
    public static String MapFilePath;
    public static String SHAPEPATH;
    public static String exepath;


    //    service
    static {
        try {
            Properties props = PropertiesUtil.readPropertiesFromConfig("service");
            wms_onlineresource = props.getProperty("wms_onlineresource").trim();
            Service_RasterMetaData_Path = props.getProperty("Service_RasterMetaData_Path").trim();
            Service_ShpMetaData_Path = props.getProperty("Service_ShpMetaData_Path").trim();
            Service_Point2Shp_Path = props.getProperty("Service_Point2Shp_Path").trim();
            Service_ModifyRasterShp_Path = props.getProperty("Service_ModifyRasterShp_Path").trim();
            Service_Raster2Line_Path = props.getProperty("Service_Raster2Line_Path").trim();
            Service_RasterFormatConvert_Path = props.getProperty("Service_RasterFormatConvert_Path").trim();

        } catch (Exception e) {
            e.printStackTrace();
            logger.debug(e.getLocalizedMessage());
        }
    }

    public static String wms_onlineresource;
    public static String Service_RasterMetaData_Path;
    public static String Service_ShpMetaData_Path;
    public static String Service_Point2Shp_Path;
    public static String Service_ModifyRasterShp_Path;
    public static String Service_Raster2Line_Path;
    public static String Service_RasterFormatConvert_Path;


}
