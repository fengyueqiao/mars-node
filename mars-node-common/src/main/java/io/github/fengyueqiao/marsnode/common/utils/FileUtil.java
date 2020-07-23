package io.github.fengyueqiao.marsnode.common.utils;

/**
 * Created by Administrator on 2020/4/26 0026.
 */

public class FileUtil {

    /**
     * 获取文件后缀名
     * @param fileName
     * @return
     */
    public static String getExtention(String fileName){
        if(fileName!=null && fileName.length()>0 && fileName.lastIndexOf(".")>-1){
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }

}
