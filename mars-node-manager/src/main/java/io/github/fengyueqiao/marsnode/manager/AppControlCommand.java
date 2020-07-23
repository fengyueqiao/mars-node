package io.github.fengyueqiao.marsnode.manager;

import io.github.fengyueqiao.marsnode.dao.command.CommandTunnel;
import io.github.fengyueqiao.marsnode.dao.command.exception.CommandException;
import io.github.fengyueqiao.marsnode.dao.config.MarsConfig;
import io.github.fengyueqiao.marsnode.manager.dto.ControlCommandRet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;

/**
 * @author QinWei on 2020/7/22 0022.
 */

@Slf4j
@Component
public class AppControlCommand {
    @Autowired
    MarsConfig marsConfig;

    @Autowired
    private CommandTunnel commandTunnel;

    @Autowired
    private AppDirManager appDirManager;

    private static final String CMD_PARAM_START = "start";
    private static final String CMD_PARAM_STOP = "stop";
    private static final String CMD_PARAM_STATUS = "status";

    private static final String CMD_RETURN_SUCCESS = "SUCCESS";
    private static final String CMD_RETURN_FAIL = "FAIL";



    /**
     * 查询服务状态
     * @param appName 应用名称
     * @return pid=0:应用没有运行，pid>0:应用正在运行
     */
    public ControlCommandRet status(String appName) {
        log.info("status {}", appName);
        return execControlCommand(appName, CMD_PARAM_STATUS);
    }

    /**
     * 启动服务
     * @param appName 应用名称
     * @return
     */
    public ControlCommandRet start(String appName) {
        log.info("start {}", appName);
        return execControlCommand(appName, CMD_PARAM_START);
    }

    /**
     * 关闭服务
     * @param appName 应用名称
     * @return
     */
    public ControlCommandRet stop(String appName) {
        log.info("start {}", appName);
        return execControlCommand(appName, CMD_PARAM_STOP);
    }

    /**
     * 获取命令参数
     * @param appName
     * @return
     */
    private String getCommandPath(String appName) {
        return appDirManager.getAppBinDir(appName) + marsConfig.getCmdName();
    }

    /**
     * 获取命令加参数的字符串
     * @param appName
     * @param param
     * @return
     */
    private ControlCommandRet execControlCommand(String appName, String param) {
        String commandStr = getCommandPath(appName) + " " + param;
        String retStr = "";
        try {
            retStr = commandTunnel.exec(commandStr);
        } catch (CommandException ex) {
            log.error("execControlCommand error command:{} ret:{}", commandStr, retStr);
        }
        return parseCommandReturn(retStr);
    }

    /**
     * 解析命令返回结果
     * ex:>>> [SUCCESS] start demo-0.0.1-SNAPSHOT.jar pid={1768} <<<
     * @param retStr
     * @return
     */
    private ControlCommandRet parseCommandReturn(String retStr) {
        ControlCommandRet rsp = new ControlCommandRet();
        // 获取>>> <<<之间的脚本输出信息
        String usefulStr = getContentFromStr(retStr, ">>>", "<<<");
        if (usefulStr.isEmpty()) {
            log.warn("retStr:{} not found usefulStr", retStr);
            return rsp;
        }

        // 获取脚本状态数据
        String retStatus = getContentFromStr(usefulStr, "[", "]");
        if (retStatus.isEmpty()) {
            log.warn("retStr:{} not found retStatus", usefulStr);
            return rsp;
        }
        if (retStatus.equals(CMD_RETURN_SUCCESS)) {
            rsp.setSuccess(true);
        }

        // 获取进程ID
        String pid = getContentFromStr(usefulStr, "{", "}");
        if (!pid.isEmpty()) {
            rsp.setPid(Integer.parseInt(pid));
        }
        return rsp;
    }

    /**
     * 获取括号或特征字符中的内容
     * @param text 文本内容
     * @param start 开始标记
     * @param end 结束标记
     * @return 标记区间的内容
     */
    private String getContentFromStr(String text, String start, String end) {
        int startIdx = text.indexOf(start);
        int endIdx = text.indexOf(end);
        if (startIdx == -1 || endIdx == -1 || (endIdx - startIdx) < 1) {
            log.debug("getContentFromStr text:{}, start:{}, end:{}, ret:null", text, start, end);
            return "";
        }

        String content = text.substring(startIdx+1, endIdx);
        log.debug("getContentFromStr text:{}, start:{}, end:{}, ret:{}", text, start, end, content);
        return content;
    }
}
