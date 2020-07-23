package io.github.fengyueqiao.marsnode.manager;

import io.github.fengyueqiao.marsnode.common.Constants;
import io.github.fengyueqiao.marsnode.common.Response;
import io.github.fengyueqiao.marsnode.common.exception.ErrorCode;
import io.github.fengyueqiao.marsnode.common.utils.FileZipUtil;
import io.github.fengyueqiao.marsnode.common.utils.HttpUtil;
import io.github.fengyueqiao.marsnode.dao.command.CommandTunnel;
import io.github.fengyueqiao.marsnode.dao.command.exception.CommandException;
import io.github.fengyueqiao.marsnode.dao.config.MarsConfig;
import io.github.fengyueqiao.marsnode.dao.file.LocalFileTunnel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author QinWei on 2020/7/22 0022.
 */

@Slf4j
@Component
public class AppDirManager {
    @Autowired
    private LocalFileTunnel localFileTunnel;

    @Autowired
    MarsConfig marsConfig;

    @Autowired
    private CommandTunnel commandTunnel;

    private static final String APP_DIR_BIN = "bin";
    private static final String APP_DIR_LOG = "log";
    private static final String APP_DIR_CONFIG = "config";

    /**
     * 获取App目录路径
     * @param appName
     * @return
     */
    public String getAppDirPath(String appName) {
        if (StringUtils.isEmpty(appName)) {
            log.error("appName is empty");
            throw new RuntimeException("appName cannot be empty");
        }
        return marsConfig.getAppDir() + appName + File.separatorChar;
    }

    /**
     * 获取app的执行目录
     * @param appName
     * @return
     */
    public String getAppBinDir(String appName) {
        return getAppDirPath(appName) + APP_DIR_BIN + File.separatorChar;
    }

    /**
     * 获取app的执行目录
     * @param appName
     * @return
     */
    public String getAppConfigDir(String appName) {
        return getAppDirPath(appName) + APP_DIR_CONFIG + File.separatorChar;
    }

    /**
     * 获取app的执行目录
     * @param appName
     * @return
     */
    public String getAppLogDir(String appName) {
        return getAppDirPath(appName) + APP_DIR_LOG + File.separatorChar;
    }


    public boolean isAppDirExist(String appName) {
        return localFileTunnel.isExist(getAppDirPath(appName));
    }

    /**
     * 创建app目录
     * @param appName
     * @return
     */
    public boolean genAppDir(String appName) {
        String appDirPath = getAppDirPath(appName);
        boolean ret1 = localFileTunnel.createDirIfAbsent(appDirPath);
        boolean ret2 = localFileTunnel.createDirIfAbsent(getAppBinDir(appName));
        boolean ret3 = localFileTunnel.createDirIfAbsent(getAppConfigDir(appName));
        boolean ret4 = localFileTunnel.createDirIfAbsent(getAppLogDir(appName));
        if (ret1 && ret2 && ret3 && ret4) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 下载保存程序的执行文件
     * @param appName
     * @param fileUrl
     * @return
     */
    public boolean genAppBinFile(String appName, String fileUrl) {
        // 下载程序,解压缩
        String binDir = getAppBinDir(appName);
        String zipFilePath = binDir + appName + ".zip";
        try {
            HttpUtil.downLoadFromUrl(fileUrl, zipFilePath);
            FileZipUtil.unZip(zipFilePath, binDir);
        } catch (IOException ex) {
            log.error("download file error url: " + fileUrl, ex);
            return false;
        } catch (RuntimeException ex) {
            log.error("unzip file error : " + zipFilePath, ex);
            return false;
        }
        // 清除压缩文件
        localFileTunnel.deleteFile(zipFilePath);
        return true;
    }

    /**
     * 生成控制命令文件
     * @param appName
     * @param fileContent
     * @return
     */
    public boolean genControlCommandFile(String appName, String fileContent) {
        // 生成控制脚本
        try {
            String ctrlCommandFilePath = getAppBinDir(appName) + marsConfig.getCmdName();
            localFileTunnel.saveFile(ctrlCommandFilePath, fileContent.getBytes(Constants.DEFAULT_CHARSET));
            String commandStr = "chmod +x " + ctrlCommandFilePath;
            commandTunnel.exec(commandStr);
        } catch (UnsupportedEncodingException ex) {
            log.error("gen ctrl script error", ex);
            return false;
        } catch (CommandException ex) {
            log.error("chmod file error", ex);
            return false;
        }
        return true;
    }

    /**
     * 删除app目录
     * @param appName
     * @return
     */
    public boolean delAppDir(String appName) {
        // 检查目录是否存在
        if (!isAppDirExist(appName)) {
            return true;
        }
        // 检查目录是否合法
        String appDirPath = getAppDirPath(appName);
        if (appDirPath.length() < 5) {
            log.error("appDirPath:{} is invalid", appDirPath);
            return false;
        }
        // 执行命令删除目录
        try {
            String commandStr = "rm -rf " + appDirPath;
            commandTunnel.exec(commandStr);
        } catch (CommandException ex) {
            log.error("rm file error", ex);
            return false;
        }

        // 检查是否删除成功
        return !isAppDirExist(appName);
    }

}
