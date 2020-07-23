package io.github.fengyueqiao.marsnode.dao.http.dto;

import lombok.Data;

/**
 * @author QinWei on 2020/7/21 0021.
 */

@Data
public class AppInstanceListReq {
    String appId;
    String nodeId;
    String nodeName;
}
