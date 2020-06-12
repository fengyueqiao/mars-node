package io.github.fengyueqiao.marsnode.respoistory.tunnel.file.dataobject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Created by Administrator on 2019/9/3 0003.
 */

@Component
public class LocalFileTunnel {

    private Logger logger = LoggerFactory.getLogger(LocalFileTunnel.class);

    public boolean createDirIfAbsent(String dirPath) {
        try {
            Path path = Paths.get(dirPath);
            if(!Files.exists(path)) {
                Files.createDirectories(path);
                logger.info("createDirectories dirPath:" + dirPath);
            }

        } catch (IOException e) {
            logger.error("create dir " + dirPath, e);
            return false;
        }
        return true;
    }

    public boolean isExist(String pathStr) {
        Path path = Paths.get(pathStr);
        return Files.exists(path);
    }

    public boolean saveFile(String filePath, MultipartFile file) {
        logger.info("saveFile filePath:" + filePath + " size:" + file.getSize());
        try {
            Path path = Paths.get(filePath);

            // 若文件存在将文件删除，并创建新的文件
            if(Files.exists(path)) {
                Files.delete(path);
            }
            Files.createFile(path);

            // 写入文件数据
            byte[] bytes = file.getBytes();
            Files.write(path, bytes);

        } catch (IOException e) {
            logger.error("saveFile " + filePath, e);
            return false;
        }
        return true;
    }

    public boolean writeFile(String filePath, String contentStr) {
        logger.info("saveFile filePath:" + filePath + " size:" + contentStr.length());
        try {
            Path path = Paths.get(filePath);

            // 若文件存在将文件删除，并创建新的文件
            if(Files.exists(path)) {
                Files.delete(path);
            }
            Files.createFile(path);

            // 写入文件数据
            byte[] bytes = contentStr.getBytes("UTF-8");
            Files.write(path, bytes);

        } catch (IOException e) {
            logger.error("saveFile " + filePath, e);
            return false;
        }
        return true;
    }

    public boolean deleteFile(String filePath) {
        logger.info("deleteFile filePath:" + filePath);
        try {
            Path path = Paths.get(filePath);

            // 若文件存在将文件删除，并创建新的文件
            if(Files.exists(path)) {
                Files.delete(path);
            }

        } catch (IOException e) {
            logger.error("saveFile " + filePath, e);
            return false;
        }
        return true;
    }

    public String getFileSuffix(String fileName) {
        String fileSuffix = "";
        int dotIdx = fileName.lastIndexOf(".");
        if( dotIdx >= 0) {
            fileSuffix = fileName.substring(dotIdx);
        }
        return fileSuffix;
    }

    public boolean moveFile(String srcFile, String desFile) {
        logger.info("moveFile src:" + srcFile + " des:" + desFile);
        try {
            Path srcPath = Paths.get(srcFile);
            Path desPath = Paths.get(desFile);

            Files.move(srcPath, desPath);
        } catch (IOException e) {
            logger.error("moveFile " + srcFile + " to " + desFile);
            return false;
        }
        return true;
    }

    public boolean readFile(String filePath, OutputStream outputStream) {
        logger.info("readFile src:" + filePath);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(new File(filePath)));
            int i = bis.read(buff);
            while (i != -1) {
                outputStream.write(buff, 0, buff.length);
                outputStream.flush();
                i = bis.read(buff);
            }
        } catch (FileNotFoundException e1) {
            logger.error("file not exist " + filePath, e1);
            return false;
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }




}
