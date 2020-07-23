package io.github.fengyueqiao.marsnode.api.req;

import lombok.Data;

import java.util.Map;

/**
 * @author QinWei on 2020/7/16 0016.
 */

@Data
public class AppInstanceDeployReq {
    /**
     * 应用名称
     */
    String appName;
    /**
     * 控制脚本模板
     */
    String scriptTemplate;
    /**
     * 占位符列表
     */
    Map<String, String> placeHolderMap;
    /**
     * 文件url
     */
    String fileUrl;
    /**
     * 部署完成后是否自动启动
     */
    boolean autoStart;
}
