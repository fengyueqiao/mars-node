package io.github.fengyueqiao.marsnode.client.api;

import io.github.fengyueqiao.marsnode.client.dto.*;
import io.github.fengyueqiao.marsnode.client.req.*;
import io.github.fengyueqiao.marsnode.common.dto.MultiResponse;
import io.github.fengyueqiao.marsnode.common.dto.Response;

/**
 * Created by Administrator on 2019/9/2 0002.
 */

public interface ApplicationServiceI {
    // 发布服务
    public Response deployApplication(DeployApplicationReq req);

    // 启动服务
    public Response startApplication(StartApplicationReq req);

    // 关闭服务
    public Response stopApplication(StopApplicationReq req);

    // 销毁服务
    public Response destroyApplication(DestroyApplicationReq req);
}
