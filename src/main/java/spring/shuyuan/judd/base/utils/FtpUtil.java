package spring.shuyuan.judd.base.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.net.SocketException;

/**
 * Created by Kevin
 * description:
 * date: 2018/11/14 9:52 AM
 */
public class FtpUtil {
    /**
     * 获取FTPClient对象
     *
     * @param ftpHost     FTP主机服务器
     * @param ftpPassword FTP 登录密码
     * @param ftpUserName FTP登录用户名
     * @param ftpPort     FTP端口 默认为21
     */
    public static FTPClient getFTPClient(String ftpHost, String ftpUserName,
                                         String ftpPassword, int ftpPort) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(ftpHost, ftpPort);// 连接FTP服务器
            ftpClient.login(ftpUserName, ftpPassword);// 登陆FTP服务器
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                System.out.println("未连接到FTP，用户名或密码错误。");
                ftpClient.disconnect();
            } else {
                System.out.println("FTP连接成功。");
            }
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("FTP的IP地址可能错误，请正确配置。");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FTP的端口错误,请正确配置。");
        }
        return ftpClient;
    }

    /*
     * 从FTP服务器下载文件
     *
     * @param ftpHost FTP IP地址
     * @param ftpUserName FTP 用户名
     * @param ftpPassword FTP用户名密码
     * @param ftpPort FTP端口
     * @param ftpPath FTP服务器中文件所在路径 格式： ftptest/aa
     * @param localPath 下载到本地的位置 格式：H:/download
     * @param fileName 文件名称
     */
    public static void downloadFtpFile(String ftpHost, String ftpUserName,
                                       String ftpPassword, int ftpPort, String ftpPath, String localPath,
                                       String fileName) {

        FTPClient ftpClient = null;
        OutputStream os = null;
        InputStream in = null;
        try {
            ftpClient = getFTPClient(ftpHost, ftpUserName, ftpPassword, ftpPort);
            ftpClient.setControlEncoding("GBK"); // 中文支持
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            System.out.println("下载文件地址：" + ftpPath);
            ftpClient.changeWorkingDirectory(ftpPath);
            File localFile = new File(localPath + File.separator + fileName);
            localFile.hashCode();
            if (!localFile.exists()) {
                System.out.println("创建文件夹");
                File fileParent = localFile.getParentFile();
                if (fileParent != null) {
                    if (!fileParent.exists()) {
                        fileParent.mkdirs();
                    }
                }
                localFile.createNewFile();
            }
            os = new FileOutputStream(localFile);
//            in = ftpClient.getInputStream();
//            InputStreamReader read = new InputStreamReader(in,"UTF-8");
//            BufferedReader reader=new BufferedReader(read);
//            OutputStreamWriter write = new OutputStreamWriter(os,"UTF-8");
//            BufferedWriter writer=new BufferedWriter(write);
//            ftpClient.changeWorkingDirectory(ftpPath);
//            byte[] bytes = new byte[1024];
//            String  i ;
//            while ((i = reader.readLine()) != null) {
//                writer.flush();
//                writer.write(i);
//                writer.newLine();
//            }
//            writer.close();
//            reader.close();
            String[] a = ftpClient.listNames();
            FTPFile[] ftpFiles = ftpClient.listFiles();
            for (FTPFile file : ftpFiles) {
//                System.out.println(file.getName());
                if (fileName.equalsIgnoreCase(file.getName())) {
                    os = new FileOutputStream(localFile);
                    Boolean success = ftpClient.retrieveFile(file.getName(), os);
                    if (success) {
                        System.out.println("下载成功");
                    }
                    os.close();
                }
            }
            ftpClient.logout();

        } catch (FileNotFoundException e) {
            System.out.println("没有找到" + ftpPath + "文件");
            e.printStackTrace();
        } catch (SocketException e) {
            System.out.println("连接FTP失败.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("文件读取错误。");
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                    // do nothing
                }
            }
        }
    }

    /**
     * Description: 向FTP服务器上传文件
     *
     * @param ftpHost     FTP服务器hostname
     * @param ftpUserName 账号
     * @param ftpPassword 密码
     * @param ftpPort     端口
     * @param ftpPath     FTP服务器中文件所在路径 格式： ftptest/aa
     * @param fileName    ftp文件名称
     * @param input       文件流
     */

    public static boolean uploadFile(String ftpHost, String ftpUserName,
                                     String ftpPassword, int ftpPort, String ftpPath,
                                     String fileName, InputStream input) {
        boolean success = false;
        FTPClient ftpClient = null;
        try {
            int reply;
            ftpClient = getFTPClient(ftpHost, ftpUserName, ftpPassword, ftpPort);
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return success;
            }
            ftpClient.setControlEncoding("UTF-8"); // 中文支持
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            ftpClient.changeWorkingDirectory(ftpPath);

            ftpClient.storeFile(fileName, input);

            input.close();
            ftpClient.logout();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return success;
    }


    public static void main(String[] args) {
        String ftpHost = "210.74.1.248";
        String ftpUserName = "90000002";
        String ftpPassword = "0mj3}FPnc";
        int ftpPort = 21;
        String ftpPath = "/materials";
        String localPath = "/workspace/document/test.txt";
        String fileName = "test.txt";

        //上传一个文件
        try {
            FileInputStream in = new FileInputStream(new File(localPath));
            boolean test = FtpUtil.uploadFile(ftpHost, ftpUserName, ftpPassword, ftpPort, ftpPath, fileName, in);
            System.out.println(test);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(e);
        }

       /* //在FTP服务器上生成一个文件，并将一个字符串写入到该文件中
        try {
            InputStream input = new ByteArrayInputStream("test ftp jyf".getBytes("GBK"));
            boolean flag = FtpUtil.uploadFile(ftpHost, ftpUserName, ftpPassword, ftpPort, ftpPath, fileName,input);;
            System.out.println(flag);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //下载一个文件
        FtpUtil.downloadFtpFile(ftpHost, ftpUserName, ftpPassword, ftpPort, ftpPath, localPath, fileName);*/
    }
}
