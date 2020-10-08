package spring.shuyuan.judd.base.springCloud;

import java.util.concurrent.Callable;

import spring.shuyuan.judd.base.model.TraceIdHelper;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;

/**
 * Created by Kevin on 2017/7/14.
 */
public class CustomHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy{

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        CustomCallable<T> customCallable = new CustomCallable<>(callable,
                TraceIdHelper.getTraceId(),
                TraceIdHelper.getRemoteIp());
        return super.wrapCallable( customCallable );
    }
    
}