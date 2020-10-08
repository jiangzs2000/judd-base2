package spring.shuyuan.judd.base.springCloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import spring.shuyuan.judd.base.StarterConstants;
import spring.shuyuan.judd.base.model.TraceIdHelper;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * Created by Kevin on 2017/5/27.
 */
@Component
public class TraceIdFeignHttpRequrestInterceptor implements RequestInterceptor {
	
	private static final Logger logger = LoggerFactory.getLogger( TraceIdFeignHttpRequrestInterceptor.class );
	
    @Override
    public void apply(RequestTemplate template) {
    	
    	try {
    		template.header( StarterConstants.TRACE_ID_KEY, TraceIdHelper.getTraceId() );
        	template.header( StarterConstants.IP_HEADER_NAME, TraceIdHelper.getRemoteIp() );
        	
            String bodyStr = "";
            byte[] body = template.body();
            if ( null != body && body.length > 0 ) {
    			bodyStr = new String( body );
    		}
            logger.info( "traceId:{}, request -> path:{}, headers:{}, querys:{}, body:{}",
            		TraceIdHelper.getTraceId(), template.request().url(), JSON.toJSONString(template.request().headers()),
            		JSON.toJSONString(template.queries()), bodyStr );			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
