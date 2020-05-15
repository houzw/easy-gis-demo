package org.egc.gis.commons;

import java.util.HashMap;
import java.util.Map;

public class GDALCommons {
    public static final Map<String,String> gdalDrivers;
    public static final Map<String, String> ogrDrivers;

    static {
        gdalDrivers = new HashMap<String, String>();
        gdalDrivers.put("vrt", "VRT");
//                {"vrt": "VRT", "tif": "GTiff", "ntf": "NITF", "toc": "RPFTOC", "xml": "ECRGTOC", "img": "SRP", "gff": "GFF", "asc": "AAIGrid", "ddf": "SDTS", "png": "PNG",
//                "jpg": "JPEG", "mem": "JDEM", "gif": "BIGGIF", "n1": "ESAT", "xpm": "XPM", "bmp": "BMP", "pix": "PCIDSK", "map": "PCRaster", "mpr/mpl": "ILWIS", "rgb": "SGI",
//                "hgt": "SRTMHGT", "ter": "Terragen", "nc": "netCDF", "hdf": "HDF4", "jp2": "JP2OpenJPEG", "grb": "GRIB", "rsw": "RMF", "nat": "MSGN", "rst": "RST",
//                "grd": "NWT_GRD", "hdr": "SNODAS", "rda": "R", "sqlite": "Rasterlite", "mbtiles": "MBTiles", "pnm": "PNM", "bt": "BT", "lcp": "LCP", "gtx": "GTX", "gsb": "NTv2",
//                "ACE2": "ACE2", "kro": "KRO", "rik": "RIK", "dem": "USGSDEM", "gxf": "GXF", "kea": "KEA", "hdf5": "HDF5", "grc": "NWT_GRC", "gen": "ADRG", "blx": "BLX",
//                "sdat": "SAGA", "xyz": "XYZ", "hf2": "HF2", "e00": "E00GRID", "dat": "ZMap", "bin": "NGSGEOID", "ppi": "IRIS", "prf": "PRF", "gpkg": "GPKG", "dwg": "CAD"}
//        ogrDrivers = {"pix": "PCIDSK", "nc": "netCDF", "jp2": "JP2OpenJPEG", "shp": "ESRI Shapefile", "000": "S57", "dgn": "DGN", "vrt": "OGR_VRT", "rec": "REC", "bna": "BNA",
//                "csv": "CSV",
//                "xml": "NAS", "gml": "GML", "gpx": "GPX", "kml": "KML", "gmt": "OGR_GMT", "gpkg": "GPKG", "map": "WAsP", "mdb": "Geomedia", "gdb": "OpenFileGDB", "dat": "XPlane",
//                "dxf": "DXF",
//                "dwg": "CAD", "vfk": "VFK", "thf": "EDIGEO", "svg": "SVG", "vct": "Idrisi", "xls": "XLS", "ods": "ODS", "xlsx": "XLSX", "sxf": "SXF", "jml": "JML", "e00": "AVCE00"}
        ogrDrivers = new HashMap<String, String>();
    }

}
