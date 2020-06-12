package io.github.fengyueqiao.marsnode.client.req;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * Created by Administrator on 2019/9/10 0010.
 */

@Data
public class StopApplicationReq {
    // 应用名称
    @NotEmpty
    String appName;
}
