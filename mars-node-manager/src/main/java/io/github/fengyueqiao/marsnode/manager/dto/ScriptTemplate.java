package io.github.fengyueqiao.marsnode.manager.dto;

import lombok.Data;
import java.util.Map;

/**
 * Created by Administrator on 2019/9/12 0012.
 */

@Data
public class ScriptTemplate {

    private static final String SEPARATOR = "%";
    private static final String WINDOWS_LINE_END = "\r\n";
    private static final String UNIX_LINE_END = "\n";
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
        // DOS2UNIX
        String profile = templateProfile.replaceAll(WINDOWS_LINE_END, UNIX_LINE_END);
        for(Map.Entry<String, String> entry : placeHolderMap.entrySet()) {
            String key = SEPARATOR + entry.getKey() + SEPARATOR;
            profile = profile.replaceAll(key, entry.getValue());
        }
        return profile;
    }

}
