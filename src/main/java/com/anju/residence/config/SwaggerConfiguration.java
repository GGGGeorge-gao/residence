package com.anju.residence.config;

import com.anju.residence.enums.ResultCode;
import com.anju.residence.security.jwt.JwtProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author cygao
 * @date 2021/1/26 3:24 下午
 **/
@EnableOpenApi
@Configuration
public class SwaggerConfiguration {

  @Bean
  public Docket createRestApi() {
    List<Response> responsesList = new ArrayList<>();
    Arrays.stream(ResultCode.values()).forEach(resultCode -> {
      responsesList.add(new ResponseBuilder().code(String.valueOf(resultCode.getCode())).description(resultCode.getMsg()).build());
    });

    Docket docket = new Docket(DocumentationType.OAS_30).pathMapping("/")

            .enable(SwaggerProperty.enable)

            .apiInfo(apiInfo())

            .host(SwaggerProperty.tryHost)


            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.regex("(?!/error.*).*"))
            .build();

    docket.globalResponses(HttpMethod.GET, responsesList);
    docket.globalResponses(HttpMethod.POST, responsesList);
    docket.globalResponses(HttpMethod.PATCH, responsesList);
    docket.globalResponses(HttpMethod.PUT, responsesList);
    docket.globalResponses(HttpMethod.DELETE, responsesList);

    return docket;
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
            .title("安居项目后端api文档")
            .description("如有疑问请询问开发者 高诚裕\n \n" +
                    "请求的url为 https://ahapocket.cn/anju_api + $api文档中标注的url$ \n \n" +
                    "登录请求分为两种：1.账号密码  2.微信登录 \n \n" +
                    "1.账号密码登录\n  url:https://ahapocket.cn/anju_api" + JwtProperty.PASSWORD_LOGIN_URL +
                    "   POST请求 json body {\"username\":\"yourUsername\", \"password\":\"yourPassword\"}， \n \n" +
                    "2.微信登录 请详见微信API \n \n" +
                    "以上两种登录认证通过都后会在response的header中" + JwtProperty.TOKEN_HEADER + "字段中保存token， \n" +
                    "请在每次请求的 header 中附加" + JwtProperty.TOKEN_HEADER + "字段带上token即可通过权限认证（api文档中有特殊标注则无需附带token）")
            .version("1.0")
            .build();
  }

}
