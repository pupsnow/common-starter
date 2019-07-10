package com.yishuifengxiao.common.validation.validation.email;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.context.request.ServletWebRequest;

import com.yishuifengxiao.common.properties.CodeProperties;
import com.yishuifengxiao.common.validation.entity.EmailCode;
import com.yishuifengxiao.common.validation.entity.ValidateCode;
import com.yishuifengxiao.common.validation.generator.CodeGenerator;

public class EmailCodeGenerator implements CodeGenerator {

	private CodeProperties codeProperties;

	@Override
	public ValidateCode generate(ServletWebRequest servletWebRequest) {
		String code = RandomStringUtils.random(codeProperties.getEmail().getLength(),
				codeProperties.getEmail().isContainLetter(), codeProperties.getEmail().isContainNumber());
		return new EmailCode(codeProperties.getEmail().getExpireIn(), code);
	}

	public CodeProperties getCodeProperties() {
		return codeProperties;
	}

	public void setCodeProperties(CodeProperties codeProperties) {
		this.codeProperties = codeProperties;
	}

	public EmailCodeGenerator(CodeProperties codeProperties) {

		this.codeProperties = codeProperties;
	}

	public EmailCodeGenerator() {

	}

	
	
}
