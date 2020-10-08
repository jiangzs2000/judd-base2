package spring.shuyuan.judd.base.springCloud;

import spring.shuyuan.judd.base.model.TraceIdHelper;

import java.util.concurrent.Callable;

/**
 * Created by Kevin on 2017/7/14.
 */
public class CustomCallable<T> implements Callable<T> {
    private Callable<T> realCallable;
    private String traceId;
    private String remoteIp;

    public CustomCallable(Callable<T> callable, String traceId, String remoteIp) {
        this.realCallable = callable;
        this.traceId = traceId;
        this.remoteIp = remoteIp;
    }

    @Override
    public T call() throws Exception {
        TraceIdHelper.setTraceId( traceId );
        TraceIdHelper.setRemoteIp(remoteIp);
        return realCallable.call();
    }
}
