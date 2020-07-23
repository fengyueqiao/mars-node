package io.github.fengyueqiao.marsnode.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by Administrator on 2019/9/3 0003.
 */

public class FileZipUtil {
    static private Logger logger = LoggerFactory.getLogger(FileZipUtil.class);

    private static void zipFile(ZipOutputStream zipOutputStream, File file, String parentFileName){
        FileInputStream in = null;
        try {
            ZipEntry zipEntry = new ZipEntry( parentFileName+file.getName() );
            zipOutputStream.putNextEntry( zipEntry );
            in = new FileInputStream( file);
            int len;
            byte [] buf = new byte[8*1024];
            while ((len = in.read(buf)) != -1){
                zipOutputStream.write(buf, 0, len);
            }
            zipOutputStream.closeEntry(  );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 递归压缩目录结构
     * @param zipOutputStream
     * @param file
     * @param parentFileName
     */
    private static void directory(ZipOutputStream zipOutputStream,File file,String parentFileName){
        File[] files = file.listFiles();
        String parentFileNameTemp = null;
        for (File fileTemp:
                files) {
            parentFileNameTemp =  StringUtils.isEmpty(parentFileName)?fileTemp.getName():parentFileName+"/"+fileTemp.getName();
            if (fileTemp.isDirectory()){
                directory(zipOutputStream,fileTemp, parentFileNameTemp);
            }else{
                zipFile(zipOutputStream,fileTemp,parentFileNameTemp);
            }
        }
    }

    /**
     * 压缩文件目录
     * @param source 源文件目录（单个文件和多层目录）
     * @param destit 目标目录
     */
    public static void zipFiles(String source,String destit) {
        File file = new File( source );
        ZipOutputStream zipOutputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream( destit );
            zipOutputStream = new ZipOutputStream( fileOutputStream );
            if (file.isDirectory()) {
                directory( zipOutputStream, file, "" );
            } else {
                zipFile( zipOutputStream, file, "" );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                zipOutputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * zip解压
	 * @param srcFilePath    zip源文件
	 * @param destDirPath	  解压后的目标文件夹
	 * @throws RuntimeException 解压失败会抛出运行时异常
	 */
    public static void unZip(String srcFilePath, String destDirPath) throws RuntimeException {
        long start = System.currentTimeMillis();
        // 开始解压
        ZipFile zipFile = null;
        try {
            File srcFile = new File(srcFilePath);
            zipFile = new ZipFile(srcFile);
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                logger.info("unzip:" + entry.getName());
                // 如果是文件夹，就创建个文件夹
                if (entry.isDirectory()) {
                    String dirPath = destDirPath + "/" + entry.getName();
                    File dir = new File(dirPath);
                    dir.mkdirs();
                } else {
                    // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                    File targetFile = new File(destDirPath + "/" + entry.getName());
                    // 保证这个文件的父文件夹必须要存在
                    if (!targetFile.getParentFile().exists()) {
                        targetFile.getParentFile().mkdirs();
                    }
                    targetFile.createNewFile();
                    // 将压缩文件内容写入到这个文件中
                    InputStream is = zipFile.getInputStream(entry);
                    FileOutputStream fos = new FileOutputStream(targetFile);
                    int len;
                    byte[] buf = new byte[1024];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    // 关流顺序，先打开的后关闭
                    fos.close();
                    is.close();
                }
            }
            long end = System.currentTimeMillis();
            logger.info("解压完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("unzip error from ZipUtils", e);
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    }
