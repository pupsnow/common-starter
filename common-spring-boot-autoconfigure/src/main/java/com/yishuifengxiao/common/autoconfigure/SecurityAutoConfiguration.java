package com.yishuifengxiao.common.autoconfigure;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import com.yishuifengxiao.common.autoconfigure.security.SecurityAuthorizeAutoConfiguration;
import com.yishuifengxiao.common.autoconfigure.security.SecurityExtendAutoConfiguration;
import com.yishuifengxiao.common.properties.SecurityProperties;
import com.yishuifengxiao.common.security.encoder.impl.CustomPasswordEncoderImpl;
import com.yishuifengxiao.common.security.manager.AuthorizeConfigManager;
import com.yishuifengxiao.common.security.manager.DefaultAuthorizeConfigManager;
import com.yishuifengxiao.common.security.provider.AuthorizeConfigProvider;
import com.yishuifengxiao.common.security.remerberme.CustomPersistentTokenRepository;
import com.yishuifengxiao.common.security.service.CustomeUserDetailsServiceImpl;
import com.yishuifengxiao.common.security.session.SessionInformationExpiredStrategyImpl;

@Configuration
@ConditionalOnClass({ DefaultAuthenticationEventPublisher.class, EnableWebSecurity.class,
		WebSecurityConfigurerAdapter.class })
@EnableConfigurationProperties(SecurityProperties.class)
@Import({ SecurityExtendAutoConfiguration.class, SecurityAuthorizeAutoConfiguration.class })
public class SecurityAutoConfiguration {

	/**
	 * 注入自定义密码加密类
	 * 
	 * @return
	 */
	@Bean("passwordEncoder")
	@ConditionalOnMissingBean
	public PasswordEncoder passwordEncoder(SecurityProperties securityProperties) {
		return new CustomPasswordEncoderImpl(securityProperties.getSecretKey());
	}

	/**
	 * 将密码加密类注入到spring security中
	 * 
	 * @return
	 */
	@Bean
	public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		return authenticationProvider;
	}

	/**
	 * 注入用户查找配置类</br>
	 * 在系统没有注入UserDetailsService时，注册一个默认的UserDetailsService实例
	 * 
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
		return new CustomeUserDetailsServiceImpl(passwordEncoder);
	}

	/**
	 * 错误提示信息国际化
	 * 
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		// messageSource.setBasenames("classpath:org/springframework/security/messages_zh_CN");
		messageSource.setBasenames("classpath*:messages_zh_CN");
		// messageSource.setBasenames("classpath:messages_CN");
		return messageSource;
	}

	/**
	 * 错误提示国际化
	 * 
	 * @return
	 */
	@Bean
	public AcceptHeaderLocaleResolver acceptHeaderLocaleResolver() {
		return new AcceptHeaderLocaleResolver();
	}

	/**
	 * 记住密码策略
	 * 
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public PersistentTokenRepository persistentTokenRepository() {
		return new CustomPersistentTokenRepository();
	}

	/**
	 * session 失效策略
	 * 
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
		return new SessionInformationExpiredStrategyImpl();
	}

	/**
	 * 授权管理器
	 * 
	 * @param authorizeProviders 系统中所有的授权提供器
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public AuthorizeConfigManager authorizeConfigManager(List<AuthorizeConfigProvider> authorizeProviders) {
		DefaultAuthorizeConfigManager authorizeConfigManager = new DefaultAuthorizeConfigManager();
		authorizeConfigManager.setAuthorizeConfigProviders(authorizeProviders);
		return authorizeConfigManager;
	}

}