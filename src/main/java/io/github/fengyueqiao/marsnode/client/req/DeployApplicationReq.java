package io.github.fengyueqiao.marsnode.client.req;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

/**
 * Created by Administrator on 2019/9/10 0010.
 */

@Data
public class DeployApplicationReq {
    // 应用名称
    @NotEmpty
    String appName;

    // 控制脚本
    String scriptTemplate;

    // 占位符列表
    Map<String, String> placeHolderMap;

    // 文件url
    String fileUrl;

    // 部署完成后是否自动启动
    boolean autoStart;
}
