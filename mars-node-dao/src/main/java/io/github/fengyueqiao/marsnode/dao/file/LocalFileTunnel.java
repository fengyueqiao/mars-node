package io.github.fengyueqiao.marsnode.dao.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * @author QinWei on 2020/7/10 0010.
 */

@Slf4j
@Component
public class LocalFileTunnel {
    public static final int BUFFER_SIZE = 1024;

    public boolean createDirIfAbsent(String dirPath) {
        try {
            Path path = Paths.get(dirPath);
            if(!Files.exists(path)) {
                Files.createDirectories(path);
                log.info("createDirectories dirPath:" + dirPath);
            }

        } catch (IOException e) {
            log.error("create dir " + dirPath, e);
            return false;
        }
        return true;
    }

    public boolean saveFile(String filePath, byte[] bytes) {
        log.info("saveFile filePath:" + filePath);
        try {
            Path path = Paths.get(filePath);

            // 若文件存在将文件删除，并创建新的文件
            if(Files.exists(path)) {
                Files.delete(path);
            }
            Files.createFile(path);

            // 写入文件数据
            Files.write(path, bytes);

        } catch (IOException e) {
            log.error("saveFile " + filePath, e);
            return false;
        }
        return true;
    }

    public boolean saveFile(String filePath, InputStream inputStream) {
        log.info("saveFile filePath:" + filePath);
        OutputStream outputStream = null;

        try {
            Path path = Paths.get(filePath);

            // 若文件存在将文件删除，并创建新的文件
            if(Files.exists(path)) {
                Files.delete(path);
            }
            Files.createFile(path);

            // 写入文件数据
            outputStream = new FileOutputStream(filePath);
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                log.debug("read " + len + " bytes.");
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();

        } catch (IOException e) {
            log.error("saveFile " + filePath, e);
            return false;
        } finally {
            try {
                if(outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException ex) {
                log.error("close FileOutputStream error", ex);
            }

        }
        return true;
    }

    public boolean deleteFile(String filePath) {
        log.info("deleteFile filePath:" + filePath);
        try {
            Path path = Paths.get(filePath);

            // 若文件存在将文件删除，并创建新的文件
            if(Files.exists(path)) {
                Files.delete(path);
            }

        } catch (IOException e) {
            log.error("saveFile " + filePath, e);
            return false;
        }
        return true;
    }

    public boolean isExist(String pathStr) {
        Path path = Paths.get(pathStr);
        return Files.exists(path);
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
        log.info("moveFile src:" + srcFile + " des:" + desFile);
        try {
            Path srcPath = Paths.get(srcFile);
            Path desPath = Paths.get(desFile);

            Files.move(srcPath, desPath);
        } catch (IOException e) {
            log.error("moveFile " + srcFile + " to " + desFile);
            return false;
        }
        return true;
    }

    public boolean readFile(String filePath, OutputStream outputStream) {
        log.info("readFile src:" + filePath);
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
            log.error("file not exist " + filePath, e1);
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
