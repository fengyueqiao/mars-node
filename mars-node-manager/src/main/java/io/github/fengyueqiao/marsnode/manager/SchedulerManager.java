package io.github.fengyueqiao.marsnode.manager;

import io.github.fengyueqiao.marsnode.dao.config.MarsConfig;
import io.github.fengyueqiao.marsnode.dao.http.MarsCenterTunnel;
import io.github.fengyueqiao.marsnode.dao.http.dto.AppInstanceStatus;
import io.github.fengyueqiao.marsnode.dao.http.dto.NodeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author QinWei on 2020/7/21 0021.
 */

@Component
public class SchedulerManager {

    @Autowired
    AppInstanceManager appInstanceManager;

    @Autowired
    MarsCenterTunnel marsCenterTunnel;

    @Autowired
    MarsConfig marsConfig;

    /**
     * 周期上报状态给Center
     */
    @Scheduled(cron="${mars.node.reportStatusCron}")
    void reportStatus() {
        // Node信息
        NodeStatus nodeStatus = new NodeStatus();
        nodeStatus.setName(marsConfig.getNodeName());
        nodeStatus.setEndpoint(marsConfig.getEndpoint());
        nodeStatus.setVersion(marsConfig.getVersion());

        // App信息
        Map<String, AppInstanceStatus> appInstanceStatusMap = appInstanceManager.getAppInstanceStatusMap();
        List<AppInstanceStatus> appInstanceStatusList = new ArrayList(appInstanceStatusMap.values());

        marsCenterTunnel.reportNodeStatus(nodeStatus, appInstanceStatusList);
    }

    /**
     * 周期更新app的状态
     */
    @Scheduled(cron="${mars.node.updateAppStatusCron}")
    void updateAppStatus() {
        appInstanceManager.updateAppStatus();
    }

}
