package spring.shuyuan.judd.base.utils;

import org.apache.commons.lang.StringUtils;
import spring.shuyuan.judd.base.model.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestParamVerifier {
    public static Response<Map<String, String>> verify(Map<String, String> request, String[] requiredFields){
        List<String> absence = new ArrayList<>();
        for(String s : requiredFields){
            if(StringUtils.isBlank(request.get(s))){
                absence.add(s);
            }
        }
        if(absence.size()>0){
            StringBuffer sb = new StringBuffer(("以下参数缺失{"));
            for(String s : absence){
                sb.append(s).append(",");
            }
            return Response.createNativeFail(sb.toString().substring(0,sb.length()-1)+"}");
        }else{
            return Response.createSuccess();
        }
    }

    public static void main(String [] args){
        System.out.println(RequestParamVerifier.verify(new HashMap<>(), new String[]{"test", "test1"}).getMsg());
    }
}
