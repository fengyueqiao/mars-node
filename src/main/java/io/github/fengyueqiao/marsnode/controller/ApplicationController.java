package io.github.fengyueqiao.marsnode.controller;

import io.github.fengyueqiao.marsnode.client.api.ApplicationServiceI;
import io.github.fengyueqiao.marsnode.client.req.DeployApplicationReq;
import io.github.fengyueqiao.marsnode.client.req.DestroyApplicationReq;
import io.github.fengyueqiao.marsnode.client.req.StartApplicationReq;
import io.github.fengyueqiao.marsnode.client.req.StopApplicationReq;
import io.github.fengyueqiao.marsnode.common.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 2019/9/2 0002.
 */

@RestController
public class ApplicationController {
    @Autowired
    private ApplicationServiceI serviceService;

    @PostMapping(value = "/mars/node/v1/deploy_app")
    public Response deployApp(@RequestBody DeployApplicationReq req){
        return serviceService.deployApplication(req);
    }

    @PostMapping(value = "/mars/node/v1/stop_app")
    public Response stopApp(@RequestBody StopApplicationReq req){
        return serviceService.stopApplication(req);
    }

    @PostMapping(value = "/mars/node/v1/start_app")
    public Response startApp(@RequestBody StartApplicationReq req){
        return serviceService.startApplication(req);
    }

    @PostMapping(value = "/mars/node/v1/destroy_app")
    public Response destroyApp(@RequestBody DestroyApplicationReq req){
        return serviceService.destroyApplication(req);
    }

}
