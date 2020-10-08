package spring.shuyuan.judd.base.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;
import java.util.HashMap;

public class RSAKeyUtil {
    /**
     * 获取.cer公钥文件并读取
     * @return       返回Base64.encodeBase64URLSafe后的公钥字符串
     */
    public static PublicKey readPublicKeyFromCer(File file) throws FileNotFoundException, CertificateException {
        return readPublicKeyFromCer(new FileInputStream(file));
    }

    public static PublicKey readPublicKeyFromCer(InputStream in) throws CertificateException {
        // 从指定流中获取数据并生成证书
        X509Certificate cert = null;
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        cert = (X509Certificate) cf.generateCertificate(in);
        return cert.getPublicKey();
    }

    /**
     *
     * @Description  获取.pfx文件并读取
     * @MethodName   readKeyPairStr
     * @return       java.util.HashMap<java.lang.String,java.lang.String>
     *
     */
    public static HashMap<String, String> readKeyPairStrFromPfx(HashMap<String, String> map, File file, String keystorePassword)
            throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        FileInputStream fis = new FileInputStream(file);
        char[] nPassword = null;
        if (StringUtils.isEmpty(keystorePassword)) {
            nPassword = null;
        } else {
            nPassword = keystorePassword.toCharArray();
        }
        // 从指定输入流中加载 KeyStore
        ks.load(fis, nPassword);
        fis.close();
        System.out.println("keystore type=" + ks.getType());
        // 获取密钥库的所有别名
        Enumeration<String> enums = ks.aliases();
        String keyAlias = null;
        // 测试此枚举是否包含更多的元素
        if (enums.hasMoreElements())
        {
            // 如果此枚举对象至少还有一个可提供的元素，则返回此枚举的下一个元素
            keyAlias = enums.nextElement();
            System.out.println("alias=[" + keyAlias + "]");
        }
        // 判断给定别名是否通过调用 setKeyEntry
        //  或者以 privateKeyEntry 或 SecretKeyEntry 为参数 setEntry 创建的
        System.out.println("is key entry=" + ks.isKeyEntry(keyAlias));
        // 获取和别名绑定的密钥，并用给定密码来恢复它
        PrivateKey prikey = (PrivateKey) ks.getKey(keyAlias, nPassword);
        // 获取和别名绑定的证书
        // Certificate / 管理各种身份证书
        // 身份证书是一个主体与由另一个主体所担保的公钥之间的绑定关系
        java.security.cert.Certificate cert = ks.getCertificate(keyAlias);
        // 从证书中获取公钥
        PublicKey pubkey = cert.getPublicKey();
        // 获取证书名
        System.out.println("cert class = " + cert.getClass().getName());
        System.out.println("cert = " + cert);
            /*
            System.out.println("public key = " + pubkey);
            System.out.println("private key = " + prikey);
            */
        String pubkeyKeyStr = Base64.encodeBase64URLSafeString(pubkey.getEncoded());
        System.out.println("-----------------公钥--------------------");
        System.out.println(pubkeyKeyStr);
        System.out.println("-----------------公钥--------------------");

        String privateKeyStr = Base64.encodeBase64URLSafeString(prikey.getEncoded());
            /*
            System.out.println("-----------------私钥--------------------");
            System.out.println(privateKeyStr);
            System.out.println("-----------------私钥--------------------");
            */
        map.put("publicKey", pubkeyKeyStr);
        map.put("privateKey", privateKeyStr);
        return map;
    }

    /**
     *
     * 从classpath中寻找xx.jks读取KeyPair, alias为文件名的名字部分，如jwt.jks,别名就是jwt
     * @param        filename ，e.g, jwt.jks
     * @param        password 库和密钥密码必须一致
     * @return       java.util.HashMap<java.lang.String,java.lang.String>
    */
    public static KeyPair readKeyPairFromJks(String filename, String password) {
        //从classpath下的证书中获取秘钥对
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(filename), password.toCharArray());
        return keyStoreKeyFactory.getKeyPair(filename.substring(0,filename.indexOf(".")), password.toCharArray());
    }

    /**
     * 从keypair中拿出public key并转为str
     */
    public static String getPubKeyStr(KeyPair kp){
        PublicKey pKey = kp.getPublic();
        return Base64.encodeBase64URLSafeString(pKey.getEncoded());
    }
    public static String getPubKeyStr(PublicKey pKey){
        return Base64.encodeBase64URLSafeString(pKey.getEncoded());
    }

    /**
     * 从keypair中拿出private key并转为str
     */
    public static String getPriKeyStr(KeyPair kp){
        PrivateKey pKey = kp.getPrivate();
        return Base64.encodeBase64URLSafeString(pKey.getEncoded());
    }
    public static String getPriKeyStr(PrivateKey pKey){
        return Base64.encodeBase64URLSafeString(pKey.getEncoded());
    }

    /**
     *
     * @Description  将数据库中读取的strURLSafe公钥转为 PublicKey
     * @MethodName   strToPublicKey
     * @param        publicKeyString, base64urlsafe
     * @return       java.security.PublicKey
     *
     */
    public static PublicKey strToPublicKey(String publicKeyString)
            throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        PublicKey publicKey;
        X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(
                //new BASE64Decoder().decodeBuffer(publicKeyString));
                Base64.decodeBase64(publicKeyString));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        // 取公钥匙对象
        publicKey = keyFactory.generatePublic(bobPubKeySpec);

        return publicKey;
    }

    /**
     *
     * @Description  将str私钥转为 PrivateKey
     * @MethodName   strToPrivateKey
     * @param        privateKeyString base64urlsafe
     * @return       java.security.PrivateKey
     */
    public static PrivateKey strToPrivateKey(String privateKeyString)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PrivateKey privateKey;
        byte[] keyBytes;
        //keyBytes = (new BASE64Decoder()).decodeBuffer(privateKeyString);
        keyBytes = Base64.decodeBase64(privateKeyString);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        // 指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        // 取私钥匙对象
        privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        return privateKey;
    }

    public static void main(String[] args){
        String pubstr = getPubKeyStr(readKeyPairFromJks("merchant1.jks","123456").getPublic());
        System.out.println(pubstr);
        String pem = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiQ2iylRNoySdbo4bjSn4p726h5xg7uXdBOxTxjeHM9/nHP4SUbYp8lYM0wRwJzR4pIBKL9ufbJtLEcWnaM2JY0KpMJ2TOk/idR6bzvPU1YgGjQKtU/6xevQEzToZnxtAW6Tt9hddIU6///GlRfArwQcmD3svqN1p9o52xmOpKSvJLjhOcI6517UYzZx7nXkK7xFz+WhsT89dA8i0chWFSDn2RKM4H3WWAdgRtgxPdmh59DQcu5kGP2/LRVSU856mNGr1nyIjPnI2/PkydfkBKJADPmiKu/2bgHL6h07qn9B0cPS7FferWQkfwKbwBBW9YB2ONB5VfmrGIj63roe4aQIDAQAB";
        System.out.println(pem);
        if(pem.contentEquals(pubstr)){
            System.out.println("metched");
        }else{
            System.out.println("no");
        }
    }
}
