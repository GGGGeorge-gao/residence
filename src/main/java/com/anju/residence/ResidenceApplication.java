package com.anju.residence;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.anju.residence.dto.wx.WxUserDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import springfox.documentation.oas.annotations.EnableOpenApi;

import java.util.Arrays;

@EnableCaching
@EnableOpenApi
@ServletComponentScan
@EnableJpaAuditing
@SpringBootApplication
public class ResidenceApplication {


  @Bean
  public HttpMessageConverters fastJsonConverter() {
    FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

    // 2.添加fastjson的配置信息，比如: 是否需要格式化返回的json数据
    FastJsonConfig fastJsonConfig = new FastJsonConfig();

    fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
    // 3.在converter中添加配置信息
    fastConverter.setFastJsonConfig(fastJsonConfig);
    // 5.返回HttpMessageConverters对象
    return new HttpMessageConverters(fastConverter);
  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  public static void main(String[] args) {
    SpringApplication.run(ResidenceApplication.class, args);
  }

}
