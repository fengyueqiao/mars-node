package io.github.fengyueqiao.marsnode.api;

import io.github.fengyueqiao.marsnode.api.req.AppInstanceDeployReq;
import io.github.fengyueqiao.marsnode.api.req.AppInstanceDestroyReq;
import io.github.fengyueqiao.marsnode.api.req.AppInstanceStartReq;
import io.github.fengyueqiao.marsnode.api.req.AppInstanceStopReq;
import io.github.fengyueqiao.marsnode.common.Response;

/**
 * @author QinWei on 2020/7/10 0010.
 */

public interface AppInstanceServiceI {
    /**
     * 启动应用实例
     * @param req
     * @return
     */
    Response deployAppInstance(AppInstanceDeployReq req);
    /**
     * 启动应用实例
     * @param req
     * @return
     */
    Response startAppInstance(AppInstanceStartReq req);
    /**
     * 关闭应用实例
     * @param req
     * @return
     */
    Response stopAppInstance(AppInstanceStopReq req);
    /**
     * 销毁应用实例
     * @param req
     * @return
     */
    Response destroyAppInstance(AppInstanceDestroyReq req);
}
