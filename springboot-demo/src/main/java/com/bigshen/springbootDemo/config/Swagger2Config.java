package com.bigshen.springbootDemo.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author byj
 * @date 2025/5/28
 * @Description
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {


    @ConditionalOnProperty(name = "swagger2.enabled", havingValue = "true")
    @Bean
    public Docket createDocket() {


        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())  //getApiInfo() 省略swagger信息方法
                //设置全局参数
                .globalOperationParameters(configGlobalParamer())
                //添加安全验证
                .securitySchemes(configSecurityScheme())
                .groupName("correcting data")
                .select()
                //API有效的路径
                .apis(RequestHandlerSelectors.basePackage("com.foxconn.controller"))
                .build();
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("learning-demo")
                //创建人
                .contact(new Contact("mattermost", "http://mm.xx.com/dev/channels/learning-demo", ""))
                //版本号
                .version("1.0")
                //描述
                .description("demo API")
                .build();
    }

    /**
     * 配置全局安全Scheme,用于安全访问资源
     *
     * @return
     */
    public List<ApiKey> configSecurityScheme() {


        List<ApiKey> apiKeyList = new ArrayList<ApiKey>();
        //Authorization key值固定
        apiKeyList.add(new ApiKey("Authorization", "Authorization", "header"));
        return apiKeyList;
    }

    /**
     * 配置全局参数
     *
     * @return
     */
    public List<Parameter> configGlobalParamer() {


        List<Parameter> pars = new ArrayList<>();
        ParameterBuilder headerPar = new ParameterBuilder();
        Parameter p = headerPar
                //header请求头类型
                .parameterType("header")
                .modelRef(new ModelRef("string"))
                //参数名字
                .name("empno")
                //参数描述
                .description("empno请求头中用户工号，用于校验token中的用户信息")
                //该参数是否必须，false表示非必须
                .required(false)
                .build();

        pars.add(p);
        return pars;
    }

    /**
     * 重定向
     *
     * @return
     */
    @RequestMapping("/")//重定向url
    public ModelAndView forwardSwagger() {
        ModelAndView mvc = new ModelAndView();
        mvc.setViewName("redirect:/swagger-ui.html");
        return mvc;
    }

}