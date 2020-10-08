package spring.shuyuan.judd.base.Bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Kevin on 2017/5/8.
 */
@Configuration
public class SpringBeanAutoConfig {

    @Bean("springUtil")
    public SpringUtil springUtil(){
        return new SpringUtil();
    }
}
