/**
 * 
 */
package com.yishuifengxiao.common.validation.entity;

/**
 * 短信验证码
 * 
 * @author yishui
 * @date 2019年1月22日
 * @version v1.0.0
 */
public class SmsCode extends ValidateCode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2551649951684072174L;

	/**
	 * 验证码内容
	 */
	private String code;

	private SmsCode(long expireTimeInSeconds) {
		super(expireTimeInSeconds);
	}
    /**
     * 
     * @param expireTimeInSeconds 验证码的失效时间
     * @param code 验证码的内容
     */
	public SmsCode(long expireTimeInSeconds, String code) {
		super(expireTimeInSeconds);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	@Override
	public String toString() {
		return "SmsCode [code=" + code + ", isExpired()=" + isExpired() + ", getExpireTime()=" + getExpireTime() + "]";
	}
	
	

	
}