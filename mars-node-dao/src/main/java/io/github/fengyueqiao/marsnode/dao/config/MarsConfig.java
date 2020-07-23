package io.github.fengyueqiao.marsnode.dao.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator on 2019/9/3 0003.
 */

@Data
@Configuration
public class MarsConfig {
    @Value("${mars.node.appDir}")
    private String appDir;

    @Value("${mars.node.name}")
    private String nodeName;

    @Value("${mars.node.endpoint}")
    private String endpoint;

    @Value("${mars.node.version}")
    private String version;

    private final String cmdName = "ctrl.sh";

    private final String cmdSuccessStr = "SUCCESS";
}
