package spring.shuyuan.judd.base.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

/**
 * Created by Kevin on 5/11/18.
 */
public class HttpClientUtil {

    private static final String UTF_8 = "utf-8";

    private static int time_out = 10*1000;
    private static int retry_time = 2;

    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 30000;

    static {
        // 设置连接池
        connMgr = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connMgr.setMaxTotal(100);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
        // 在提交请求之前 测试连接是否可用
        configBuilder.setStaleConnectionCheckEnabled(true);
        requestConfig = configBuilder.build();
    }


    /**
     * 发送post数据
     * @param url
     * @param treeMap
     */
    public static Optional<String> post(String url, TreeMap<String, String> treeMap) {
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(time_out)
                .setConnectTimeout(time_out)
                .build();//设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        CloseableHttpResponse response = null;

        List<BasicNameValuePair> formparams = HttpClientUtil.getNamedValuePairFromTreeMap( treeMap );
        int times = 0;
        while ( times < retry_time ) {
            try {
                if ( null != formparams && !formparams.isEmpty() ) {
                    httpPost.setEntity(new UrlEncodedFormEntity(formparams, UTF_8));
                }
                CloseableHttpClient httpclient = HttpClients.createDefault();
                response = httpclient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                String content = EntityUtils.toString( entity );
                EntityUtils.consume(entity);
                return Optional.ofNullable(content);
            }catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (null!=response) response.close();
                } catch (IOException e) {}
            }
            times ++;
        }
        return Optional.empty();
    }

    public static Optional<String> postWidthBody(String url, String body, String contentType) {
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(time_out)
                .setConnectTimeout(time_out)
                .build();//设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        if ( !Strings.isNullOrEmpty(body) ) {
            StringEntity stringEntity = new StringEntity(body, UTF_8);
            if (!Strings.isNullOrEmpty(contentType)){
                stringEntity.setContentType(contentType);
            }
            httpPost.setEntity(stringEntity);
        }
        
        int times = 0;
        while ( times < retry_time ) {
            try {
                CloseableHttpClient httpclient = HttpClients.createDefault();
                response = httpclient.execute(httpPost);
                
                HttpEntity entity = response.getEntity();
                String content = EntityUtils.toString( entity );
                EntityUtils.consume(entity);
                return Optional.ofNullable(content);
            }catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (null!=response) response.close();
                } catch (IOException e) {}
            }
            times ++;
        }
        return Optional.empty();
    }

    public static Optional<String> postWidthBody(String url, String body){
        return postWidthBody(url, body, null);
    }

    public static Optional<String> post(String url) {
        return HttpClientUtil.post(url, null);
    }

    private static List<BasicNameValuePair> getNamedValuePairFromTreeMap(TreeMap<String, String> treeMap ) {
        List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
        if (null != treeMap && !treeMap.isEmpty()) {
            Iterator<String> keyIte = treeMap.keySet().iterator();
            while (keyIte.hasNext()) {
                String key = keyIte.next();
                String value = treeMap.get(key);
                if ( Strings.isNullOrEmpty(key) || Strings.isNullOrEmpty(value) ) {
                    continue;
                }
                formparams.add(new BasicNameValuePair(key, value));
            }
        }
        return formparams;
    }
    
    public static Optional<String> postMultiPart(String url, String fileName, byte[] fileData){
    	if (Strings.isNullOrEmpty(fileName)) {
    		fileName = "file.jpg";
		}
    	if ( null == fileData ) {
			return Optional.empty();
		}
    	
    	HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(time_out)
                .setConnectTimeout(time_out)
                .build();//设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    	builder.addBinaryBody("upload_file", fileData, ContentType.MULTIPART_FORM_DATA, fileName);
    	
    	HttpEntity requestEntity = builder.build();
    	
    	httpPost.setEntity(requestEntity);
    	
        int times = 0;
        while ( times < retry_time ) {
            try {
                CloseableHttpClient httpclient = HttpClients.createDefault();
                response = httpclient.execute(httpPost);
                
                HttpEntity entity = response.getEntity();
                String content = EntityUtils.toString( entity );
                EntityUtils.consume(entity);
                return Optional.ofNullable(content);
            }catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (null!=response) response.close();
                } catch (IOException e) {}
            }
            times ++;
        }
        return Optional.empty();
    }
    
    public static String getParamStringFromMap(TreeMap<String, String> treeMap) {
    	if ( null == treeMap || treeMap.isEmpty() ) {
			return "";
		}
    	
    	List<String> keyValueList = new ArrayList<>();
        if (null != treeMap && !treeMap.isEmpty()) {
            Iterator<String> keyIte = treeMap.keySet().iterator();
            while (keyIte.hasNext()) {
                String key = keyIte.next();
                String value = treeMap.get(key);
                if ( Strings.isNullOrEmpty(key) || Strings.isNullOrEmpty(value) ) {
                    continue;
                }
                keyValueList.add( String.format("%s=%s", key,value) );
            }
        }
		return Joiner.on("&").skipNulls().join(keyValueList);
	}
    
    /**
     * 发送get数据
     * @param url
     * @param treeMap
     */
    public static Optional<String> get(String url, TreeMap<String, String> treeMap) {
        String innerUrl = url;
        String paramStr = HttpClientUtil.getParamStringFromMap(treeMap);
        if ( !Strings.isNullOrEmpty(paramStr) ) {
            innerUrl += "?"+ paramStr;
        }

        HttpGet httpGet = new HttpGet(innerUrl);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(time_out)
                .setConnectTimeout(time_out)
                .build();//设置请求和传输超时时间
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        int times = 0;
        while ( times < retry_time ) {
            try {
                CloseableHttpClient httpclient = HttpClients.createDefault();
                response = httpclient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                String content = EntityUtils.toString( entity );
                EntityUtils.consume(entity);
                return Optional.ofNullable(content);
            }catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (null!=response) response.close();
                } catch (IOException e) {}
            }
            times ++;
        }
        return Optional.empty();
    }

    public static Optional<String> get(String url) {
        return  HttpClientUtil.get(url, null);
    }
    
    /**
     * 发送get数据
     * @param url
     * @param treeMap
     */
    public static Optional<String> delete(String url, TreeMap<String, String> treeMap) {
        String innerUrl = url;
        String paramStr = HttpClientUtil.getParamStringFromMap(treeMap);
        if ( !Strings.isNullOrEmpty(paramStr) ) {
            innerUrl += "?"+ paramStr;
        }

        HttpDelete httpDelete = new HttpDelete(innerUrl);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(time_out)
                .setConnectTimeout(time_out)
                .build();//设置请求和传输超时时间
        httpDelete.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        int times = 0;
        while ( times < retry_time ) {
            try {
                CloseableHttpClient httpclient = HttpClients.createDefault();
                response = httpclient.execute(httpDelete);
                HttpEntity entity = response.getEntity();
                String content = EntityUtils.toString( entity );
                EntityUtils.consume(entity);
                return Optional.ofNullable(content);
            }catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (null!=response) response.close();
                } catch (IOException e) {}
            }
            times ++;
        }
        return Optional.empty();
    }

    public static Optional<String> delete(String url) {
        return  HttpClientUtil.delete(url, null);
    }

    public static final byte[] readBytes(InputStream is, int contentLen) {
        if (contentLen > 0) {
            int readLen = 0;
            int readLengthThisTime = 0;
            byte[] message = new byte[contentLen];
            try {
                while (readLen != contentLen) {
                    readLengthThisTime = is.read(message, readLen, contentLen - readLen);
                    if (readLengthThisTime == -1) {// Should not happen.
                        break;
                    }
                    readLen += readLengthThisTime;
                }
                return message;
            } catch (IOException e) {

            }
        }
        return new byte[] {};
    }
    
    public static String getRequestBodyByString(InputStream is,int size) {
        byte[] reqBodyBytes = readBytes(is, size);
        String res = new String(reqBodyBytes);
        return res;
    }











    /**
     * 发送 POST 请求（HTTP），不带输入数据
     *
     * @param apiUrl
     */
    public static String doPost(String apiUrl) {
        return doPost(apiUrl, new HashMap<String, Object>());
    }

    /**
     * 发送 POST 请求（HTTP），K-V形式
     *
     * @param apiUrl API接口URL
     * @param params 参数map
     */
    public static String doPost(String apiUrl, Map<String, Object> params) {
        return doPost(apiUrl, params, new HashMap());
    }

    public static String doPost(String apiUrl, Map<String, Object> params, Map<String, String> header) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;

        try {
            httpPost.setConfig(requestConfig);
            for (Map.Entry<String, String> entry : header.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
            List<NameValuePair> pairList = new ArrayList<>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry
                        .getValue().toString());
                pairList.add(pair);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    /**
     * 发送 POST 请求（HTTP），JSON形式
     *
     * @param apiUrl
     * @param json   json对象
     */
    public static String doPost(String apiUrl, Object json) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;

        try {
            httpPost.setConfig(requestConfig);
            StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");//解决中文乱码问题
//            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    public static String doPostJson(String apiUrl, Object json, Map<String, String> header) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;

        try {
            httpPost.setConfig(requestConfig);
            StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");
            Iterator var8 = header.entrySet().iterator();

            while (var8.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry) var8.next();
                httpPost.setHeader((String) entry.getKey(), (String) entry.getValue());
            }

            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException var18) {
            var18.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException var17) {
                    var17.printStackTrace();
                }
            }
        }

        return httpStr;
    }

    /**
     * 发送 SSL POST 请求（HTTPS），K-V形式
     *
     * @param apiUrl API接口URL
     * @param params 参数map
     */
    public static String doPostSSL(String apiUrl, Map<String, Object> params) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        String httpStr = null;

        try {
            httpPost.setConfig(requestConfig);
            List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry
                        .getValue().toString());
                pairList.add(pair);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("utf-8")));
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            httpStr = EntityUtils.toString(entity, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    /**
     * 发送 SSL POST 请求（HTTPS），JSON形式
     *
     * @param apiUrl API接口URL
     * @param json   JSON对象
     */
    public static String doPostSSL(String apiUrl, Object json) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        String httpStr = null;

        try {
            httpPost.setConfig(requestConfig);
            StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");//解决中文乱码问题
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            httpStr = EntityUtils.toString(entity, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    /**
     * 创建SSL安全连接
     *
     */
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                @Override
                public void verify(String host, SSLSocket ssl) throws IOException {
                }

                @Override
                public void verify(String host, X509Certificate cert) throws SSLException {
                }

                @Override
                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                }
            });
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return sslsf;
    }

    /**
     * 发送httpput请求 代码冗余   待优化
     *
     * @param apiUrl
     * @param params
     */
    public static String doPutUrl(String apiUrl, Map<String, Object> params, Map<String, String> header) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String httpStr = null;
        HttpPut httpPut = new HttpPut(apiUrl);
        CloseableHttpResponse response = null;

        try {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                httpPut.addHeader(entry.getKey(), entry.getValue());
            }
            httpPut.setConfig(requestConfig);
            List<NameValuePair> pairList = new ArrayList<>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry
                        .getValue().toString());
                pairList.add(pair);
            }
            httpPut.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
            response = httpClient.execute(httpPut);
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    public static String doPutUrl(String apiUrl, Map<String, Object> params) {
        return doPutUrl(apiUrl, params);
    }

    /**
     * 发送httpPatch请求
     *
     * @param apiUrl
     * @param params
     */
    public static String doPatchUrl(String apiUrl, Map<String, Object> params, Map<String, String> header) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String httpStr = null;
        HttpPatch httpPatch = new HttpPatch(apiUrl);
        CloseableHttpResponse response = null;

        try {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                httpPatch.addHeader(entry.getKey(), entry.getValue());
            }
            httpPatch.setConfig(requestConfig);
            List<NameValuePair> pairList = new ArrayList<>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry
                        .getValue().toString());
                pairList.add(pair);
            }
            httpPatch.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
            response = httpClient.execute(httpPatch);
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }


    public static String doPatchUrl(String apiUrl, Map<String, Object> params) {
        return doPatchUrl(apiUrl, params, new HashMap());
    }

    public static String doPutJson(String apiUrl, String stringJson, Map<String, String> header) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String httpStr = null;
        HttpPut httpPut = new HttpPut(apiUrl);
        CloseableHttpResponse response = null;
        try {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                httpPut.addHeader(entry.getKey(), entry.getValue());
            }
            httpPut.setConfig(requestConfig);
            //组织请求参数
            StringEntity stringEntity = new StringEntity(stringJson, "UTF-8");
            httpPut.setEntity(stringEntity);
            response = httpClient.execute(httpPut);
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }


}
