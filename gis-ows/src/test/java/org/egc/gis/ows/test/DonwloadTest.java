package org.egc.gis.ows.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author houzhiwei
 * @date 2020/5/5 18:22
 */
@Slf4j
public class DonwloadTest {
    @Test
    public void down() throws IOException {
        InputStream inputStream = new URL("").openStream();
        Files.copy(inputStream, Paths.get("H:/gisdemo/SRTM.tif"), StandardCopyOption.REPLACE_EXISTING);
    }
}
