package spring.shuyuan.judd.base.utils;

import com.alibaba.fastjson.JSONObject;
import spring.shuyuan.judd.base.model.Response;

import java.util.HashMap;
import java.util.Map;

public class SensitiveInfoMaskUtil {
    private static final String[] fields = new String[]{"card_no", "id_no", "mobile_no", "mobile", "valid_date", "cvv2"};

    public static <T> Map<String, Object> mask(Map<String, T> map){
        if(map == null){
            return null;
        }
        Map<String, Object> newMap = new HashMap<>(map);
        for(String f : fields){
            String v = (String)newMap.get(f);
            if(v != null){
                if(v.length() > 4){
                    newMap.put(f, String.format("%s%s", v.substring(0, v.length()-4).replaceAll(".","*"),v.substring(v.length()-4)));
                } else {
                    newMap.put(f, v.replaceAll(".","*"));
                }
            }
        }
        return newMap;
    }

    public static <T> JSONObject mask(Response<T> res){
        if(res == null){
            return null;
        }
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(res));
        JSONObject data = json.getJSONObject("data");
        if(data != null) {
            for (String f : fields) {
                String v = data.getString(f);
                if (v != null) {
                    if (v.length() > 4) {
                        data.put(f, String.format("%s%s", v.substring(0, v.length() - 4).replaceAll(".", "*"), v.substring(v.length() - 4)));
                    } else {
                        data.put(f, v.replaceAll(".", "*"));
                    }
                }
            }
            json.put("data", data);
        }
        return json;
    }

    public static JSONObject mask(JSONObject json){
        if(json == null){
            return null;
        }
        JSONObject newJson = (JSONObject) json.clone();
        for (String f : fields) {
            String v = newJson.getString(f);
            if (v != null) {
                if (v.length() > 4) {
                    newJson.put(f, String.format("%s%s", v.substring(0, v.length() - 4).replaceAll(".", "*"), v.substring(v.length() - 4)));
                } else {
                    newJson.put(f, v.replaceAll(".", "*"));
                }
            }
        }
        return newJson;
    }

    public static void main(String[] args){
        System.out.println("***********map***********");
        Map<String, String> m = new HashMap<>();
        m.put("card_no", "asfdoauerr23fdfeet5");
        m.put("cvv2","234");
        Map<String, Object> newMap = mask(m);
        System.out.println(newMap.toString());
        System.out.println(m.toString());
        System.out.println("***********json***********");
        JSONObject json = new JSONObject();
        json.put("card_no", "asfdoauerr23fdfeet5");
        json.put("cvv2","234");
        Response res = Response.createSuccess(m);
        JSONObject newJson = mask(res);
        System.out.println(newJson.toJSONString());
        System.out.println(json.toJSONString());

    }
}
