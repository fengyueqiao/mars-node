package io.github.fengyueqiao.marsnode.service;

import com.alibaba.fastjson.JSON;
import io.github.fengyueqiao.marsnode.client.api.ApplicationServiceI;

import io.github.fengyueqiao.marsnode.client.req.DeployApplicationReq;
import io.github.fengyueqiao.marsnode.client.req.DestroyApplicationReq;
import io.github.fengyueqiao.marsnode.client.req.StartApplicationReq;
import io.github.fengyueqiao.marsnode.client.req.StopApplicationReq;
import io.github.fengyueqiao.marsnode.common.dto.Response;
import io.github.fengyueqiao.marsnode.common.exception.ErrorCode;
import io.github.fengyueqiao.marsnode.common.util.FileZipUtil;
import io.github.fengyueqiao.marsnode.common.util.HttpUtil;
import io.github.fengyueqiao.marsnode.config.MarsConfig;
import io.github.fengyueqiao.marsnode.domain.ScriptTemplate.ScriptTemplate;
import io.github.fengyueqiao.marsnode.respoistory.tunnel.command.CommandTunnel;
import io.github.fengyueqiao.marsnode.respoistory.tunnel.file.dataobject.LocalFileTunnel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2019/9/2 0002.
 */

@Service
public class ApplicationServiceImpl implements ApplicationServiceI {
    private Logger logger = LoggerFactory.getLogger(ApplicationServiceI.class);

    @Autowired
    private MarsConfig marsConfig;

    @Autowired
    private LocalFileTunnel localFileTunnel;

    @Autowired
    private CommandTunnel commandTunnel;

    @Override
    public Response deployApplication(DeployApplicationReq req)  {
        logger.info("deployApplication" + JSON.toJSONString(req));

        // 检查输入参数
        if(req.getAppName().isEmpty()) {
            Response.buildFailure(ErrorCode.E_Node_requestParamError);
        }

        // 获取控制脚本
        ScriptTemplate scriptTemplate = new ScriptTemplate();
        scriptTemplate.setTemplateProfile(req.getScriptTemplate());
        scriptTemplate.setPlaceHolderMap(req.getPlaceHolderMap());
        String controlScript = scriptTemplate.getProfile();

        // 关闭和删除原来的服务
        DestroyApplicationReq destroyApplicationReq = new DestroyApplicationReq();
        destroyApplicationReq.setAppName(req.getAppName());
        destroyApplication(destroyApplicationReq);

        // 创建目录结构
        String appDirPath = marsConfig.getNodePath() + req.getAppName();
        if(!localFileTunnel.createDirIfAbsent(appDirPath)) {
            return Response.buildFailure(ErrorCode.E_Node_fileIOError);
        }

        // 下载程序,解压缩
        String zipFilePath = appDirPath + File.separatorChar + req.getAppName() + ".zip";
        try {
            HttpUtil.downLoadFromUrl(req.getFileUrl(), zipFilePath);
            FileZipUtil.unZip(zipFilePath, appDirPath);
        } catch (IOException ex) {
            logger.info("download file error url: " + req.getFileUrl(), ex);
            return Response.buildFailure(ErrorCode.E_Node_fileIOError);
        } catch (RuntimeException ex) {
            logger.info("unzip file error : " + zipFilePath, ex);
            return Response.buildFailure(ErrorCode.E_Node_fileIOError);
        }
        localFileTunnel.deleteFile(zipFilePath);

        // 生成控制脚本
        String ctrlScriptFilePath = appDirPath + File.separatorChar + marsConfig.getCmdName();
        localFileTunnel.writeFile(ctrlScriptFilePath, controlScript);
        String commandStr = "chmod +x " + marsConfig.getNodePath() + req.getAppName() + File.separatorChar + marsConfig.getCmdName();
        commandTunnel.exec(commandStr);

        // 启动服务
        if(req.isAutoStart()) {
            StartApplicationReq startApplicationReq = new StartApplicationReq();
            startApplicationReq.setAppName(req.getAppName());
            startApplication(startApplicationReq);
        }
        return Response.buildSuccess();
    }

    // 启动服务
    public Response startApplication(StartApplicationReq req){
        logger.info("startApplication" + JSON.toJSONString(req));
        String commandStr = marsConfig.getNodePath() + req.getAppName() + File.separatorChar + marsConfig.getCmdName()
                + " start";
        String retStr = commandTunnel.exec(commandStr);
        if(!retStr.contains(marsConfig.getCmdSuccessStr())){
            return Response.buildFailure(ErrorCode.E_Node_cmdExecError);
        }
        return Response.buildSuccess();
    }

    // 关闭服务
    public Response stopApplication(StopApplicationReq req){
        logger.info("stopApplication" + JSON.toJSONString(req));
        String commandStr = marsConfig.getNodePath() + req.getAppName() + File.separatorChar + marsConfig.getCmdName()
                            + " stop";
        String retStr = commandTunnel.exec(commandStr);
        if(!retStr.contains(marsConfig.getCmdSuccessStr())){
            return Response.buildFailure(ErrorCode.E_Node_cmdExecError);
        }
        return Response.buildSuccess();
    }

    // 销毁服务
    public Response destroyApplication(DestroyApplicationReq req){
        logger.info("destroyApplication" + JSON.toJSONString(req));
        String appDirPath = marsConfig.getNodePath() + req.getAppName();
        if(!localFileTunnel.isExist(appDirPath)) {
            return Response.buildSuccess();
        }

        // 关闭应用
        String ctrlPath = marsConfig.getNodePath() + req.getAppName() + File.separatorChar + marsConfig.getCmdName();
        if(localFileTunnel.isExist(ctrlPath)) {
            StopApplicationReq stopApplicationReq = new StopApplicationReq();
            stopApplicationReq.setAppName(req.getAppName());
            stopApplication(stopApplicationReq);
            if(req.getAppName().isEmpty()) {
                return Response.buildFailure(ErrorCode.E_Node_requestParamError);
            }
        }

        // 删除应用目录
        String commandStr = "rm -rf " + appDirPath;
        String retStr = commandTunnel.exec(commandStr);
        if(!retStr.contains(marsConfig.getCmdSuccessStr())){
            return Response.buildFailure(ErrorCode.E_Node_cmdExecError);
        }
        return Response.buildSuccess();
    }
}