package io.github.fengyueqiao.marsnode.web;

import io.github.fengyueqiao.marsnode.api.AppInstanceServiceI;
import io.github.fengyueqiao.marsnode.api.req.AppInstanceDeployReq;
import io.github.fengyueqiao.marsnode.api.req.AppInstanceDestroyReq;
import io.github.fengyueqiao.marsnode.api.req.AppInstanceStartReq;
import io.github.fengyueqiao.marsnode.api.req.AppInstanceStopReq;
import io.github.fengyueqiao.marsnode.common.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author QinWei on 2020/7/21 0021.
 */

@RestController
@RequestMapping("/api/app/v1")
public class AppInstanceController {
    @Autowired
    AppInstanceServiceI appInstanceService;

    @PostMapping(value = "/deployAppInstance")
    public Response deployAppInstance(@RequestBody AppInstanceDeployReq req) {
        return appInstanceService.deployAppInstance(req);
    }

    @PostMapping(value = "/startAppInstance")
    public Response startAppInstance(@RequestBody AppInstanceStartReq req) {
        return appInstanceService.startAppInstance(req);
    }

    @PostMapping(value = "/stopAppInstance")
    public Response stopAppInstance(@RequestBody AppInstanceStopReq req) {
        return appInstanceService.stopAppInstance(req);
    }

    @PostMapping(value = "/destroyAppInstance")
    public Response destroyAppInstance(@RequestBody AppInstanceDestroyReq req) {
        return appInstanceService.destroyAppInstance(req);
    }
}
