package io.github.fengyueqiao.marsnode.dao.http.dto;

import lombok.Data;

/**
 * @author QinWei on 2020/7/21 0021.
 */
@Data
public class AppInstance {
    /**
     * ID
     */
    String id;
    /**
     * 应用ID
     */
    String appId;
    /**
     * 应用名称
     */
    String appName;
    /**
     * 节点ID
     */
    String nodeId;
    /**
     * 节点名称
     */
    String nodeName;
    /**
     * 进程ID
     */
    String pid;
    /**
     * 应用版本
     */
    String version;
    /**
     * 设置状态
     */
    String settingState;
    /**
     * 当前状态
     */
    String presentState;
}
