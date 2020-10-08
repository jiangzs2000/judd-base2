package spring.shuyuan.judd.base.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;


import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <br>
 *
 * @author Jessci
 * create by 2019/5/22
 */
public class AliyunOSSUtil {
    public static final String endpoint = "oss-cn-shanghai.aliyuncs.com";
    public static final String accessKeyId = "LTAIbGCmS8z6lpM9";
    public static final String accessKeySecret = "VRgGpejKkPSURzeF0GuPxiwFbrvQYC";
    public static final String bucketName = "shuidijingrong";
    public static final String fileHost = "test";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static String upload(File file, String fileUrl) {
        System.out.println("=========>OSS文件上传开始：" + file.getName());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(new Date());
        if (null == file) {
            return null;
        }
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            //容器不存在，就创建
            if (!ossClient.doesBucketExist(bucketName)) {
                ossClient.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                ossClient.createBucket(createBucketRequest);
            }
            //创建文件路径
            //上传文件
            PutObjectResult result = ossClient.putObject(new PutObjectRequest(bucketName, fileUrl, file));
            //设置权限 这里是公开读
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            if (null != result) {
                System.out.println("==========>OSS文件上传成功,OSS地址：" + fileUrl);
                return fileUrl;
            }
        } catch (OSSException oe) {
            oe.printStackTrace();
        } catch (ClientException ce) {
            ce.printStackTrace();
        } finally {
            //关闭
            ossClient.shutdown();
        }
        return null;
    }

    public static File wangce(String date, int userId, String fileadd) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
//        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
// 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
//        String accessKeyId = "<yourAccessKeyId>";
//        String accessKeySecret = "<yourAccessKeySecret>";
//        String bucketName = "<yourBucketName>";
//        String objectName = "<yourObjectName>";

// 创建OSSClient实例。
        File file = null;
        try {
            date = simpleDateFormat.format(sdf.parse(date));
            file = File.createTempFile("对账单", ".csv");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

// ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
        OSSObject ossObject = ossClient.getObject(bucketName, fileadd + "/" + date + "/" + userId + "_" + date + ".csv");
        ObjectMetadata meta = ossObject.getObjectMetadata();
        // 获取Object的输入流
        InputStream objectContent = ossObject.getObjectContent();
        ObjectMetadata objectData = ossClient.getObject(new GetObjectRequest(bucketName, fileadd + "/" + date + "/" + userId + "_" + date + ".csv"),
                file);
        // 关闭数据流
        try {
            objectContent.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
// 读取文件内容。
        /*System.out.println("Object content:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent(), Charset.forName("utf-8")));
        File file = new File("C:\\Users\\porter\\Desktop\\test.xlsx");
        try {
            FileOutputStream out = new FileOutputStream(file);

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(out));

            while (reader.readLine() != null) {
                String line = null;
                line = reader.readLine();
                if (line == null) break;
                bufferedWriter.flush();
                bufferedWriter.write(line);
                bufferedWriter.newLine();
                System.out.println("\n" + line);
            }
// 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

// 关闭OSSClient。
        ossClient.shutdown();*/

    }

    public static void main(String[] args) {
       /* File file = new File("C:/Users/porter/Desktop/巴乐兔测试.csv");
        String fileUrl = "2019-05-28/80000000120190528.csv";
        upload(file, fileUrl);*/

//        wangce();
    }
}
