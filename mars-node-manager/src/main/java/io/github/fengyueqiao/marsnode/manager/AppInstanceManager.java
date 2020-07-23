package io.github.fengyueqiao.marsnode.manager;

import io.github.fengyueqiao.marsnode.dao.config.MarsConfig;
import io.github.fengyueqiao.marsnode.dao.http.MarsCenterTunnel;
import io.github.fengyueqiao.marsnode.dao.http.dto.AppInstance;
import io.github.fengyueqiao.marsnode.dao.http.dto.AppInstanceStatus;
import io.github.fengyueqiao.marsnode.dao.http.dto.AppStateEnum;
import io.github.fengyueqiao.marsnode.manager.dto.ControlCommandRet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author QinWei on 2020/7/21 0021.
 */

@Slf4j
@Component
public class AppInstanceManager {
    @Autowired
    MarsCenterTunnel marsCenterTunnel;
    @Autowired
    MarsConfig marsConfig;
    @Autowired
    private AppControlCommand appControlCommand;
    @Autowired
    private AppDirManager appDirManager;

    /**
     * Key: appName
     */
    Map<String, AppInstanceStatus> appInstanceStatusMap = new HashMap<>();

    /**
     * 若为空则从Center获取所有appInstance信息
     */
    public Map<String, AppInstanceStatus> getAppInstanceStatusMap() {
        if (appInstanceStatusMap.isEmpty()) {
            updateAppInstanceStatusMap();
        }
        return appInstanceStatusMap;
    }

    /**
     * 从Center拉取最新的app实例更新
     */
    public void updateAppInstanceStatusMap() {
        List<AppInstance> appInstanceList = marsCenterTunnel.listAppInstance(marsConfig.getNodeName());
        if (appInstanceList == null) {
            return;
        }

        Map<String, AppInstanceStatus> tempAppInstanceStatusMap = new ConcurrentHashMap<>();
        for (AppInstance appInstance : appInstanceList) {
            AppInstanceStatus appInstanceStatus = new AppInstanceStatus();
            appInstanceStatus.setAppName(appInstance.getAppName());
            appInstanceStatus.setVersion(appInstance.getVersion());
            appInstanceStatus.setPid(0);
            appInstanceStatus.setPresentState(AppStateEnum.None.name());

            AppInstanceStatus oldAppInstanceStatus = appInstanceStatusMap.get(appInstance.getAppName());
            if (oldAppInstanceStatus == null) {
                appInstanceStatusMap.put(appInstance.getAppName(), appInstanceStatus);
            } else {
                if (!oldAppInstanceStatus.getSettingState().equals(appInstanceStatus.getSettingState())) {
                    log.info("app:{} setting state change {}=>{}", appInstance.getAppName(),
                            oldAppInstanceStatus.getSettingState(), appInstance.getSettingState());
                }
                if (!oldAppInstanceStatus.getVersion().equals(appInstanceStatus.getVersion())) {
                    log.warn("app:{} version is diff {}=>{}", appInstance.getAppName(),
                            oldAppInstanceStatus.getVersion(), appInstance.getVersion());
                }
            }
        }
    }

    /**
     * 定时检测app的状态
     */
    void updateAppStatus() {
        for(Map.Entry<String, AppInstanceStatus> entry : getAppInstanceStatusMap().entrySet()) {
            String appName = entry.getKey();
            AppInstanceStatus appInstanceStatus = entry.getValue();
            // 检查该app是否已经部署
            if (!appDirManager.isAppDirExist(appName)) {
                appInstanceStatus.setPid(0);
                appInstanceStatus.setPresentState(AppStateEnum.Inactive.name());
                log.warn("app:{} have not deployed", appName);
                continue;
            }

            // 通过控制脚本检查app状态
            ControlCommandRet ret = appControlCommand.status(appName);
            if (ret.isSuccess()){
                appInstanceStatus.setPid(ret.getPid());
                if (ret.getPid() == 0){
                    appInstanceStatus.setPresentState(AppStateEnum.Inactive.name());
                } else {
                    appInstanceStatus.setPresentState(AppStateEnum.Active.name());
                }
            } else {
                log.warn("get status error app:{}", appName);
            }
        }
    }


}
