package org.egc.gis.taudem;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.command.CommonsExec;
import org.egc.commons.web.ExecutionStatus;

import java.io.IOException;
import java.util.Map;

/**
 * http://hydrology.usu.edu/taudem/taudem5/documentation.html
 *
 * @author houzhiwei
 * @date 2018 /9/3 22:15
 */
@Slf4j
public abstract class BaseTauDEM {

    protected static int processors = Runtime.getRuntime().availableProcessors();

    protected ExecutionStatus execute(CommandLine cmd, Map files) {
        if (files != null) {
            cmd.setSubstitutionMap(files);
        }
        try {
            Map map = CommonsExec.execWithOutput(cmd);
            String out = (String) map.get("out");
            String error = (String) map.get("error");
            log.info(out);
            if (StringUtils.isNotBlank(error)) {
                log.error(error);
                return ExecutionStatus.FAILED;
            }
        } catch (IOException io) {
            log.error(io.getMessage(), io);
            return ExecutionStatus.FAILED;
        }
        return ExecutionStatus.SUCCEEDED;
    }

    protected CommandLine initCmd() {
        CommandLine cmd = new CommandLine(TauDEMCommands.MPIEXEC);
        cmd.addArgument("-n");
        cmd.addArgument(processors + "");
        return cmd;
    }
}
