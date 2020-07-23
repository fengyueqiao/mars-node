package io.github.fengyueqiao.marsnode.service;

import com.alibaba.fastjson.JSON;
import io.github.fengyueqiao.marsnode.api.AppInstanceServiceI;
import io.github.fengyueqiao.marsnode.api.req.AppInstanceDeployReq;
import io.github.fengyueqiao.marsnode.api.req.AppInstanceDestroyReq;
import io.github.fengyueqiao.marsnode.api.req.AppInstanceStartReq;
import io.github.fengyueqiao.marsnode.api.req.AppInstanceStopReq;
import io.github.fengyueqiao.marsnode.common.Constants;
import io.github.fengyueqiao.marsnode.common.Response;
import io.github.fengyueqiao.marsnode.common.exception.ErrorCode;
import io.github.fengyueqiao.marsnode.common.utils.FileZipUtil;
import io.github.fengyueqiao.marsnode.common.utils.HttpUtil;
import io.github.fengyueqiao.marsnode.dao.command.CommandTunnel;
import io.github.fengyueqiao.marsnode.dao.config.MarsConfig;
import io.github.fengyueqiao.marsnode.dao.file.LocalFileTunnel;
import io.github.fengyueqiao.marsnode.dao.http.dto.AppInstanceStatus;
import io.github.fengyueqiao.marsnode.dao.http.dto.AppStateEnum;
import io.github.fengyueqiao.marsnode.dao.http.dto.AppTypeEnum;
import io.github.fengyueqiao.marsnode.manager.AppControlCommand;
import io.github.fengyueqiao.marsnode.manager.AppDirManager;
import io.github.fengyueqiao.marsnode.manager.AppInstanceManager;
import io.github.fengyueqiao.marsnode.manager.dto.ScriptTemplate;
import io.github.fengyueqiao.marsnode.manager.dto.ControlCommandRet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

/**
 * @author QinWei on 2020/7/16 0016.
 */

@Slf4j
@Service
public class AppInstanceImpl implements AppInstanceServiceI {
    @Autowired
    MarsConfig marsConfig;
    @Autowired
    private AppControlCommand appControlCommand;

    @Autowired
    private AppDirManager appDirManager;

    @Autowired
    private AppInstanceManager appInstanceManager;

    @Override
    public Response deployAppInstance(AppInstanceDeployReq req) {
        // 检查输入参数
        if (req.getAppName().isEmpty()) {
            Response.buildFailure(ErrorCode.E_RequestParameterError);
        }
        //模板脚本base64解码
        try {
            final BASE64Decoder decoder = new BASE64Decoder();
            req.setScriptTemplate(new String(decoder.decodeBuffer(req.getScriptTemplate()), Constants.DEFAULT_CHARSET));
        } catch (Exception ex) {
            log.error("decode ScriptTemplate error", ex);
            return Response.buildFailure(ErrorCode.E_RequestParameterError);
        }

        // 获取控制脚本
        ScriptTemplate scriptTemplate = new ScriptTemplate();
        scriptTemplate.setTemplateProfile(req.getScriptTemplate());
        scriptTemplate.setPlaceHolderMap(req.getPlaceHolderMap());
        String controlScript = scriptTemplate.getProfile();

        // 关闭和删除原来的服务
        if (appDirManager.isAppDirExist(req.getAppName())) {
            AppInstanceDestroyReq destroyApplicationReq = new AppInstanceDestroyReq();
            destroyApplicationReq.setAppName(req.getAppName());
            destroyAppInstance(destroyApplicationReq);
        }

        // 创建目录结构
        if (!appDirManager.genAppDir(req.getAppName())) {
            return Response.buildFailure(ErrorCode.E_FileIOError.getErrCode(), "创建目录失败");
        }

        // 下载程序,解压缩
        if (req.getAppType().equals(AppTypeEnum.Deploy.name())) {
            if (!appDirManager.genAppBinFile(req.getAppName(), req.getFileUrl())) {
                return Response.buildFailure(ErrorCode.E_FileIOError.getErrCode(), "下载程序失败");
            }
        }


        // 生成控制脚本
        if (!appDirManager.genControlCommandFile(req.getAppName(), controlScript)) {
            return Response.buildFailure(ErrorCode.E_FileIOError.getErrCode(), "保存控制命令文件失败");
        }

        // 启动服务
        if (req.isAutoStart()) {
            AppInstanceStartReq startApplicationReq = new AppInstanceStartReq();
            startApplicationReq.setAppName(req.getAppName());
            startAppInstance(startApplicationReq);
        }
        // 更新缓存
        appInstanceManager.getAppInstanceStatusMap().remove(req.getAppName());
        appInstanceManager.updateAppInstanceStatusMap();
        return Response.buildSuccess();
    }

    @Override
    public Response startAppInstance(AppInstanceStartReq req) {
        // 检查输入参数
        if (req.getAppName().isEmpty()) {
            Response.buildFailure(ErrorCode.E_RequestParameterError);
        }
        // 关闭服务
        ControlCommandRet ret = appControlCommand.stop(req.getAppName());
        if (!ret.isSuccess()){
            return Response.buildFailure(ErrorCode.E_Node_cmdExecError);
        }
        // 启动服务
        ret = appControlCommand.start(req.getAppName());
        if (ret.isSuccess()){
            return Response.buildSuccess();
        } else {
            return Response.buildFailure(ErrorCode.E_Node_cmdExecError);
        }
    }

    @Override
    public Response stopAppInstance(AppInstanceStopReq req) {
        // 检查输入参数
        if (req.getAppName().isEmpty()) {
            Response.buildFailure(ErrorCode.E_RequestParameterError);
        }
        ControlCommandRet ret = appControlCommand.stop(req.getAppName());
        if (ret.isSuccess()){
            return Response.buildSuccess();
        } else {
            return Response.buildFailure(ErrorCode.E_Node_cmdExecError);
        }
    }

    @Override
    public Response destroyAppInstance(AppInstanceDestroyReq req) {
        // 检查输入参数
        if (req.getAppName().isEmpty()) {
            Response.buildFailure(ErrorCode.E_RequestParameterError);
        }
        // 关闭应用
        ControlCommandRet ret = appControlCommand.stop(req.getAppName());
        if(!ret.isSuccess()){
            return Response.buildFailure(ErrorCode.E_InternalError.getErrCode(), "停止服务错误");
        }

        // 删除应用目录
        if (appDirManager.delAppDir(req.getAppName())) {
            return Response.buildSuccess();
        } else {
            return Response.buildFailure(ErrorCode.E_Node_cmdExecError);
        }
    }

}
