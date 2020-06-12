package io.github.fengyueqiao.marsnode.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator on 2019/9/3 0003.
 */

@Data
@Configuration
public class MarsConfig {
    @Value("${mars.nodePath}")
    private String nodePath = "E://mars/";

    private String cmdName = "ctrl.sh";

    private String cmdSuccessStr = "SUCCESS";
}
