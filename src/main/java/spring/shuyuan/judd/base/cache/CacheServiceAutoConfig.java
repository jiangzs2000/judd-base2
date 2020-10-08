package spring.shuyuan.judd.base.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class CacheServiceAutoConfig {

    @Bean
    @ConditionalOnBean(RedisConnectionFactory.class)
    public RedisTemplate<String, Object> juddRedisTemplate(@Autowired RedisConnectionFactory connectionFactory ) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer( new StringRedisSerializer() );
        //默认使用JDK的序列化
        template.setValueSerializer( new JdkSerializationRedisSerializer());
        /*
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        Jackson2JsonRedisSerializer<?> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        */
        // template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();

        return template;
    }

    @Bean
    @ConditionalOnMissingBean(CacheService.class)
    @ConditionalOnBean({RedisConnectionFactory.class})
    public CacheService cacheService(@Autowired StringRedisTemplate stringRedisTemplate,
                                         @Autowired RedisTemplate juddRedisTemplate){
        // stringRedisTemplate.setEnableTransactionSupport(true);
        // juddRedisTemplate.setEnableTransactionSupport(true);
        return new CacheServiceImpl(juddRedisTemplate, stringRedisTemplate);
    }

}