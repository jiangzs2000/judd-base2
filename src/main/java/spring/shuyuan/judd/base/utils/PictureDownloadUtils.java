package spring.shuyuan.judd.base.utils;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Sting
 * create 2018/11/22

 * 根据url下载图片工具类
 **/
public class PictureDownloadUtils {

    /**
     * 单个Url下载图片
     *
     * @param urlList  图片所在url
     * @param path     下载到具体路径
     * @param fileName 指定下载文件名称
     */
    public static void downloadPicture(String urlList, String path, String fileName) {
        URL url = null;
        try {
            url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
            //图片目录
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file.getPath() + File.separator + fileName);
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;
            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据多个图片url 批量下载图片
     *
     * @param picurls   多个图片所在urls
     * @param path      下载到具体路径
     * @param fileNames 指定下载名称
     * @throws IOException
     */
    public static void downloadPictures(String[] picurls, String path, String[] fileNames) throws IOException {
        try {
            //多个图片下载地址
            for (int i = 0; i < picurls.length; i++) {
                //根据图片地址构建URL
                URL url = new URL(picurls[i]);
                DataInputStream dataInputStream = new DataInputStream(url.openStream());
                //创建目录和图片
                File file = new File(path);
                if (!file.exists()) {
                    file.mkdirs();
                }
                //通过流复制图片
                FileOutputStream fileOutputStream = new FileOutputStream(file.getPath() + File.separator + fileNames[i]);
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = dataInputStream.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
                fileOutputStream.write(output.toByteArray());
                dataInputStream.close();
                fileOutputStream.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据多个图片url 批量下载图片并压缩
     *
     * @param picurls     多个图片所在urls
     * @param path        下载到具体路径
     * @param fileNames   指定下载名称
     * @param pictureSize 指定压缩图片大小
     * @throws IOException
     */
    public static void downloadAndCompressPictures(String[] picurls, String path, String[] fileNames, int pictureSize) {
        try {
            //多个图片下载地址
            for (int i = 0; i < picurls.length; i++) {
                //根据图片地址构建URL
                URL url = new URL(picurls[i]);
                DataInputStream dataInputStream = new DataInputStream(url.openStream());
                //创建目录和图片
                File file = new File(path);
                if (!file.exists()) {
                    file.mkdirs();
                }
                //通过流复制图片
                FileOutputStream fileOutputStream = new FileOutputStream(file.getPath() + File.separator + fileNames[i]);
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = dataInputStream.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
                fileOutputStream.write(output.toByteArray());
                dataInputStream.close();
                fileOutputStream.close();
                //压缩图片
                CompressPic(file.getPath() + File.separator + fileNames[i], pictureSize);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 压缩图片
     *
     * @param picturePath 图片地址
     * @param pictureSize 压缩图片大小
     * @throws Exception
     */
    public static String CompressPic(String picturePath, int pictureSize) throws IOException {
        double cutPercent = 0.1;
        File file = new File(picturePath);
        long fileLength = file.length();
        while ((fileLength / 1024) > pictureSize) {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedImage bufferedImage = ImageIO.read(fileInputStream);
            int width = bufferedImage.getWidth(null);
            int height = bufferedImage.getHeight(null);
            width = (int) (width * (1 - cutPercent));
            height = (int) (height * (1 - cutPercent));
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            tag.getGraphics().drawImage(bufferedImage, 0, 0, width, height, null); // 绘制缩小后的图
            FileOutputStream deskImage = new FileOutputStream(picturePath); // 输出到文件流
//            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(deskImage);
//            encoder.encode(tag); // 近JPEG编码
            String formatName = picturePath.substring(picturePath.lastIndexOf(".") + 1);
            ImageIO.write(tag, formatName, deskImage);
            //关闭流
            fileInputStream.close();
            deskImage.close();
            file = new File(picturePath);
            fileLength = file.length();
        }
        return picturePath;
    }


}
