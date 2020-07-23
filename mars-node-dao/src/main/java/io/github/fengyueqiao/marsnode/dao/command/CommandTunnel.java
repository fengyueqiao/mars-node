package io.github.fengyueqiao.marsnode.dao.command;

import io.github.fengyueqiao.marsnode.dao.command.exception.CommandException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * Created by Administrator on 2019/9/10 0010.
 */

@Slf4j
@Component
public class CommandTunnel {

    public String exec(String command) throws CommandException {
        log.info("cmd:" + command);
        String returnString = "";
        Process proc = null;
        Runtime runTime = Runtime.getRuntime();
        if (runTime == null) {
            log.error("Create runtime false!");
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
            log.error("cmd:" + command, ex);
            throw new CommandException();
        }
        log.info("ret:" + returnString);
        return returnString;
    }
}
