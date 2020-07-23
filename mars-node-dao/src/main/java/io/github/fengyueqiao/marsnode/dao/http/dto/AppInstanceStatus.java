package io.github.fengyueqiao.marsnode.dao.http.dto;

import lombok.Data;

/**
 * @author QinWei on 2020/7/10 0010.
 */

@Data
public class AppInstanceStatus {
    /**
     * 应用名称
     */
    String appName;
    /**
     * 进程ID
     */
    Integer pid;
    /**
     * 当前状态
     */
    String presentState;
    /**
     * 设置状态
     */
    String settingState;
    /**
     * 版本号
     */
    String version;

}
