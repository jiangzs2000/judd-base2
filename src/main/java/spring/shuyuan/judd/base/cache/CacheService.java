package spring.shuyuan.judd.base.cache;

import java.util.Map;

public interface CacheService {

    void putObject(String key, Object value) throws Exception;

    void putObject(String key, Object value, int expire) throws Exception;

    void deleteObjectByKey(String key) throws Exception;

    void expire(String key, int expire) throws Exception;

    Object getObject(String key) throws Exception;

    void putString(String key, String value) throws Exception;

    void putString(String key, String value, int expire) throws Exception;

    String getString(String key) throws Exception;

    void putStringToHash(String key, String hashKey, String value) throws Exception;

    String getStringFromHash(String key, String field) throws Exception;

    Long deleteHashKey(String key, Object... fields) throws Exception;

    boolean existHash(String key, String field) throws Exception;

    Map getAllFromHash(String key) throws Exception;
}
