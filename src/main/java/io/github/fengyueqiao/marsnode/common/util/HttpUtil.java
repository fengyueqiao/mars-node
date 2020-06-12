package io.github.fengyueqiao.marsnode.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *  http 请求工具类
 */
@Slf4j
public class HttpUtil {
    public static final int REQUEST_SUCCESS = 200;

    public static String post(String url,String jsonStr){
        try {
            HttpResponse httpResponse = HttpUtil.sendPostRequest(url,jsonStr);
            return EntityUtils.toString(httpResponse.getEntity() ,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static HttpResponse sendPostRequest(String url,String jsonStr) throws IOException{
            HttpResponse httpResponse =  Request.Post(url)
                .connectTimeout(60000)
                .socketTimeout(60000 )
                .addHeader("Accept-Encoding", "gzip")
                .bodyString(jsonStr, ContentType.APPLICATION_JSON)
                .execute()
                .returnResponse();
            return httpResponse;
    }


    public static HttpResponse sendPostRequest(String url,String jsonStr,String token) throws IOException{
        HttpResponse httpResponse =  Request.Post(url)
            .connectTimeout(60000)
            .socketTimeout(60000)
            .addHeader("Accept-Encoding", "gzip")
            .addHeader("Authorization", "Bearer "+token)
            .bodyString(jsonStr, ContentType.APPLICATION_JSON)
            .execute()
            .returnResponse();
        return httpResponse;
    }

    public static HttpResponse sendGetRequest(String url,String token) throws IOException{
        HttpResponse httpResponse =  Request.Get(url)
            .connectTimeout(60000)
            .socketTimeout(60000)
            .addHeader("Accept-Encoding", "gzip")
            .addHeader("Authorization", "Bearer "+token)
            .execute()
            .returnResponse();
        return httpResponse;
    }

    public static HttpResponse sendGetRequest(String url) throws IOException{
        HttpResponse httpResponse =  Request.Get(url)
                .connectTimeout(60000)
                .socketTimeout(60000)
                .addHeader("Accept-Encoding", "gzip")
                .execute()
                .returnResponse();
        return httpResponse;
    }


    /**
     * 发送post请求并返回str结果集
     * @param jsonParam 请求参数
     * @param url       请求地址
     * @return
     * @throws Exception
     */
    public static String sendPostRequestReturnStr(String url,String jsonParam) throws Exception{
        HttpResponse httpResponse = HttpUtil.sendPostRequest(url,jsonParam);
        if(httpResponse.getStatusLine().getStatusCode()!= REQUEST_SUCCESS){
            log.error("url:" + url);
            log.error("json:" + jsonParam);
            log.error("Code:" + httpResponse.getStatusLine().getStatusCode());
            log.error(EntityUtils.toString(httpResponse.getEntity() ,"UTF-8"));
            throw new Exception("返回代码："+httpResponse.getStatusLine().getStatusCode());
        }
        return  EntityUtils.toString(httpResponse.getEntity() ,"UTF-8");
    }
    /**
     * 发送post请求并返回str结果集
     * @param jsonParam 请求参数
     * @param url       请求地址
     * @return
     * @throws Exception
     */
    public static String sendPostRequestReturnStr(String url,String jsonParam,String token) throws Exception{
        HttpResponse httpResponse = HttpUtil.sendPostRequest(url,jsonParam,token);
        if(httpResponse.getStatusLine().getStatusCode()!= REQUEST_SUCCESS){
            log.error("url:" + url);
            log.error("json:" + jsonParam);
            log.error("Code:" + httpResponse.getStatusLine().getStatusCode());
            log.error(EntityUtils.toString(httpResponse.getEntity() ,"UTF-8"));
            throw new Exception("返回代码："+httpResponse.getStatusLine().getStatusCode());
        }
        return  EntityUtils.toString(httpResponse.getEntity() ,"UTF-8");
    }

    /**
     * 发送post请求并返回str结果集
     * @param url       请求地址
     * @return
     * @throws Exception
     */
    public static String sendGetRequestReturnStr(String url,String token) throws Exception{
        HttpResponse httpResponse = HttpUtil.sendGetRequest(url,token);
            if(httpResponse.getStatusLine().getStatusCode()!= REQUEST_SUCCESS){
            log.error("url:" + url);
            log.error("Code:" + httpResponse.getStatusLine().getStatusCode());
            log.error(EntityUtils.toString(httpResponse.getEntity() ,"UTF-8"));
            throw new Exception("返回代码："+httpResponse.getStatusLine().getStatusCode());
        }
        return  EntityUtils.toString(httpResponse.getEntity() ,"UTF-8");
    }


    /**
     * 从网络Url中下载文件
     */

    public static void  downLoadFromUrl(String urlStr,String filPath) throws IOException{
        FileOutputStream fos = null;
        InputStream inputStream = null;
        try
        {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(30*1000);

            //得到输入流
            inputStream = conn.getInputStream();
            //获取自己数组
            byte[] getData = readInputStream(inputStream);

            File file = new File(filPath);
            fos = new FileOutputStream(file);
            fos.write(getData);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fos!=null){
                fos.close();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        }
    }

    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

}