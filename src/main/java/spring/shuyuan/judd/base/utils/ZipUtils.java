package spring.shuyuan.judd.base.utils;


import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Sting
 * create 2018/11/22

 * 压缩文件夹到zip包
 **/
public class ZipUtils {

    private static final int BUFFSIZE = 2048;

    /**
     * @param srcfilePath    源文件地址
     * @param targetFilePath 目标文件地址
     */
    public static void zipFiles(String srcfilePath, String targetFilePath) {

        FileOutputStream zipFile = null;
        BufferedOutputStream bufferedOutputStream = null;
        ZipOutputStream out = null;
        try {
            File srcfile = new File(srcfilePath);
            File targetFile = new File(targetFilePath);
            zipFile = new FileOutputStream(targetFile);
            bufferedOutputStream = new BufferedOutputStream(zipFile);
            out = new ZipOutputStream(bufferedOutputStream);
            compress(srcfile, out, "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
                if (zipFile != null) {
                    zipFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 压缩文件夹里的文件
     * 起初不知道是文件还是文件夹--- 统一调用该方法
     *
     * @param file
     * @param out
     * @param basedir
     */
    private static void compress(File file, ZipOutputStream out, String basedir) throws IOException {
        /* 判断是目录还是文件 */
        if (file.isDirectory()) {
            zipDirectory(file, out, basedir);
        } else {
            zipFile(file, out, basedir);
        }
    }

    /**
     * 压缩单个文件
     *
     * @param srcfile
     */
    public static void zipFile(File srcfile, ZipOutputStream out, String basedir) {
        if (!srcfile.exists()) {
            return;
        }
        byte[] buf = new byte[1024];
        FileInputStream in = null;
        try {
            int len;
            in = new FileInputStream(srcfile);
            out.putNextEntry(new ZipEntry(basedir + srcfile.getName()));
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.closeEntry();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 压缩文件夹
     *
     * @param dir
     * @param out
     * @param basedir
     */
    public static void zipDirectory(File dir, ZipOutputStream out, String basedir) throws IOException {
        if (!dir.exists()) {
            return;
        }
        File[] files = dir.listFiles();
        if (files.length == 0) {
            ZipEntry zipEntry = new ZipEntry(basedir + dir.getName() + "/");
            out.putNextEntry(zipEntry);
            out.closeEntry();
        }
        for (int i = 0; i < files.length; i++) {
            /* 递归 */
            compress(files[i], out, basedir + dir.getName() + "/");
        }
    }

    /**
     * 压缩单个文件夹内的内容（不包含文件夹）到指定zip根目录
     *
     * @param srcPath
     * @param targetPath
     */
    public static void zipSingleDirectory(String srcPath, String targetPath) {
        InputStream ins = null;
        ZipOutputStream outs = null;
        try {
            File srcFile = new File(srcPath);
            File dstFile = new File(targetPath);
            File[] files = srcFile.listFiles();
            outs = new ZipOutputStream(new FileOutputStream(dstFile));
            for (File file : files) {
                ins = new BufferedInputStream(new FileInputStream(file),
                        BUFFSIZE);
                byte[] buf = new byte[BUFFSIZE];
                ZipEntry entry = new ZipEntry(file.getName());
                outs.putNextEntry(new ZipEntry(entry));
                int count = 0;
                while ((count = ins.read(buf)) != -1) {
                    outs.write(buf, 0, count);
                    outs.flush();
                }
                ins.close();
            }
            outs.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (Exception e) {
                }
            }
            if (outs != null) {
                try {
                    outs.close();
                } catch (Exception e) {
                }
            }
        }
    }
}
