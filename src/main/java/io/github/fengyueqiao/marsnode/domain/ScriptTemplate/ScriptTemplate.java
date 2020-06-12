package io.github.fengyueqiao.marsnode.domain.ScriptTemplate;

import lombok.Data;

import java.util.Map;

/**
 * Created by Administrator on 2019/9/12 0012.
 */

@Data
public class ScriptTemplate {

    public static final String SEPARATOR = "%";

    /**
     * 模板内容
     */
    String templateProfile;

    /**
     * 占位符
     */
    Map<String, String> placeHolderMap;

    /**
     * 获取站位符替换后的脚本内容
     */
    public String getProfile() {
        String profile = templateProfile.replaceAll("\r\n", "\n"); // DOS2UNIX
        for(Map.Entry<String, String> entry : placeHolderMap.entrySet()) {
            String key = SEPARATOR + entry.getKey() + SEPARATOR;
            profile = profile.replaceAll(key, entry.getValue());
        }
        return profile;
    }

}
