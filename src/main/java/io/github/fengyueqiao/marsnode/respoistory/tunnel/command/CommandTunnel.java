package io.github.fengyueqiao.marsnode.respoistory.tunnel.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * Created by Administrator on 2019/9/10 0010.
 */

@Component
public class CommandTunnel {
    private Logger logger = LoggerFactory.getLogger(CommandTunnel.class);

    public String exec(String command) {
        logger.info("cmd:" + command);
        String returnString = "";
        Process proc = null;
        Runtime runTime = Runtime.getRuntime();
        if (runTime == null) {
            logger.error("Create runtime false!");
        }
        try {
            proc = runTime.exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            PrintWriter output = new PrintWriter(new OutputStreamWriter(proc.getOutputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                returnString = returnString + line + "\n";
            }
            input.close();
            output.close();
            proc.destroy();
        } catch (IOException ex) {
            logger.error("cmd:" + command, ex);
        }
        logger.info("ret:" + returnString);
        return returnString;
    }
}
