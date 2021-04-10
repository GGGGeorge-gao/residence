package com.anju.residence.config;

import com.anju.residence.params.FileParams;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author cygao
 * @date 2021/2/22 1:18 下午
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler(FileParams.WATER_IMAGE_URL).addResourceLocations(FileParams.WATER_IMAGE_LOCATION);
  }
}
