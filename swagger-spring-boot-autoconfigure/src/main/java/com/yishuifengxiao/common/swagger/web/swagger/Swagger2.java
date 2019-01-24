package com.yishuifengxiao.common.swagger.web.swagger;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.yishuifengxiao.common.swagger.properties.SwaggerProperties;
import com.yishuifengxiao.common.tool.collections.EmptyUtil;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2生成接口文档
 * 
 * @author yishui
 * @date 2018年6月15日
 * @version 0.0.1
 */
@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(prefix = "yishuifengxiao.swagger", name = { "basePackage" })
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class Swagger2 {
	@Autowired
	private SwaggerProperties swaggerProperties;

	@Bean
	public Docket createRestApi() {

		List<Parameter> pars = new ArrayList<Parameter>();

		if (EmptyUtil.notEmpty(swaggerProperties.getAuths())) {
			swaggerProperties.getAuths().forEach(t -> {
				ParameterBuilder authorizationPar = new ParameterBuilder();
				authorizationPar.name(t.getName()).description(t.getDescription())
						.modelRef(new ModelRef(t.getModelRef())).parameterType(t.getParameterType()).build();
				pars.add(authorizationPar.build());
			});
		}

		return new Docket(DocumentationType.SWAGGER_2).groupName(swaggerProperties.getGroupNmae()).apiInfo(apiInfo())
				.select().apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
				.paths(PathSelectors.any()).build().globalOperationParameters(pars);

	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title(swaggerProperties.getTitle()).description(swaggerProperties.getDescription())
				.termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
				.contact(new Contact(swaggerProperties.getContact().getName(), swaggerProperties.getContact().getUrl(),
						swaggerProperties.getContact().getEmail()))
				.version(swaggerProperties.getVersion()).build();
	}

}