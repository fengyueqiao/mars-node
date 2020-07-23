package io.github.fengyueqiao.marsnode.dao.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.fengyueqiao.marsnode.common.MultiResponse;
import io.github.fengyueqiao.marsnode.common.Response;
import io.github.fengyueqiao.marsnode.common.utils.HttpUtil;
import io.github.fengyueqiao.marsnode.dao.http.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/9/10 0010.
 */

@Slf4j
@Component
public class MarsCenterTunnel {
    @Value("${mars.center.url}")
    String marsCenterUrl;
    private final static String LIST_APP_INSTANCE_PATH = "/api/instance/v1/listAppInstance";
    private final static String REPORT_NODE_STATUS_PATH = "/api/node/v1/reportNodeStatus";


    public Boolean reportNodeStatus(NodeStatus nodeStatus, List<AppInstanceStatus> appStatusList) {
        log.info("reportNodeStatus");
        NodeStatusReportReq req = new NodeStatusReportReq();
        req.setNodeStatus(nodeStatus);
        req.setAppStatusList(appStatusList);
        String url = marsCenterUrl + REPORT_NODE_STATUS_PATH;
        String param = JSON.toJSONString(req);
        try {
            String rspStr = HttpUtil.sendPostRequestReturnStr(url, param);
            log.info("reportNodeStatus url:{}, param:{}, rsp: {}", url, param, rspStr);
            Response rsp = JSON.parseObject(rspStr, MultiResponse.class);
            return rsp.isSuccess();
        } catch (Exception ex) {
            log.error("reportNodeStatus error url:{}, param:{}", url, param, ex);
        }
        return null;
    }

    public List<AppInstance> listAppInstance(String nodeName) {
        log.info("listAppInstance nodeName:{}", nodeName);
        AppInstanceListReq req = new AppInstanceListReq();
        req.setNodeName(nodeName);
        String url = marsCenterUrl + LIST_APP_INSTANCE_PATH;
        String param = JSON.toJSONString(req);
        try {
            String rspStr = HttpUtil.sendPostRequestReturnStr(url, param);
            log.info("listAppInstance url:{}, param:{}, rsp: {}", url, param, rspStr);
            MultiResponse<JSONObject> rsp = JSON.parseObject(rspStr, MultiResponse.class);
            List<AppInstance> appInstanceList = new ArrayList<>(rsp.getData().size());
            rsp.getData().forEach(jsonObj -> {
                AppInstance appInstance = JSON.toJavaObject(jsonObj, AppInstance.class);
                appInstanceList.add(appInstance);
            });

            return appInstanceList;
        } catch (Exception ex) {
            log.error("listAppInstance error url:{}, param:{}", url, param, ex);
        }
        return null;
    }
}
