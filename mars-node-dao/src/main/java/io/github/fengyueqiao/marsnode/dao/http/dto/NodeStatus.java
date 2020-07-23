package io.github.fengyueqiao.marsnode.dao.http.dto;

import lombok.Data;

/**
 * @author QinWei on 2020/7/10 0010.
 */

@Data
public class NodeStatus {
    /**
     * 节点名称
     */
    String name;
    /**
     * 服务地址
     */
    String endpoint;
    /**
     * 版本号
     */
    String version;
}
