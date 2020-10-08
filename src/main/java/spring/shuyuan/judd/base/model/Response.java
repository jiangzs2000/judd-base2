package spring.shuyuan.judd.base.model;


import java.io.Serializable;
/**
 * Created by Kevin on 06/11/18.
 */
public class Response<T> implements Serializable {
    private static final long serialVersionUID = 642651043174947736L;


    //public static final String CODE_SUCCESS = "0";
    //public static final String CODE_ERROR = "-1";

    private String code = Code.SUCCESS.getCode();
    private String msg;
    private T data;
    private String traceId;


/*
    public static <T extends Object> Response<T> createSuccessMapIsBackDate(Map<String, String> jsonMap) {
        Response<T> result = new Response<T>();
        result.setCode(CODE_SUCCESS);
        result.setSendData(jsonMap);
        result.setTraceId(TraceIdHelper.getTraceId());
        return result;
    }
*/
//    public Response(Boolean success, String msg) {
////        this.success = success;
//        this.msg = msg;
//    }
//
//    public Response(T data, Boolean success, String msg) {
//        this.data = data;
////        this.success = success;
//        this.msg = msg;
//    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public static <T extends Object> Response<T> createSuccess() {
        Response<T> result = new Response<>();
        result.setCode(Code.SUCCESS.getCode());
        result.setTraceId(TraceIdHelper.getTraceId());
        return result;
    }



    public static <T extends Object> Response<T> createSuccess(T data) {
        Response<T> result = Response.createSuccess();
        result.setData(data);
        return result;
    }

    public static <T extends Object> Response<T> createProcess(String code) {
        return createProcess(code, Code.getMsgByCode(code));
    }

    public static <T extends Object> Response<T> createProcess(String code, String msg) {
        Response<T> result = new Response<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setTraceId(TraceIdHelper.getTraceId());
        return result;
    }

    public static <T extends Object> Response<T> createFail(String code) {
        return createFail(code, Code.getMsgByCode(code));
    }

    public static <T extends Object> Response<T> createFail(String code, String msg) {
        Response<T> result = new Response<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setTraceId(TraceIdHelper.getTraceId());
        return result;
    }

    public static <T extends Object> Response<T> createNativeFail(String msg) {
        Response<T> result = new Response<>();
        result.setCode(Code.FAIL.getCode());
        result.setMsg(msg);
        result.setTraceId(TraceIdHelper.getTraceId());
        return result;
    }
    public enum Code {
        SUCCESS("0000", "操作成功"),
        FAIL("-1", "操作失败"),
        VALIDATE_FAILED("404", "参数检验失败"),
        UNAUTHORIZED("401", "暂未登录或token已经过期"),
        FORBIDDEN("403", "没有相关权限"),
        PAYCHANNEL_NOT_REGISTED("-2", "没有完成渠道进件");

        private String code;
        private String msg;
        private Code(String code, String msg){
            this.code = code;
            this.msg = msg;
        }
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public static String getMsgByCode(String code){
            if (null == code) {
                return null;
            }
            for (Code temp : Code.values()) {
                if (temp.getCode().equals(code)) {
                    return temp.getMsg();
                }
            }
            return "未知错误";
        }
    }
}
