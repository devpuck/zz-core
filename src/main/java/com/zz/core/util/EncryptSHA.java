package com.zz.core.util;

import com.zz.core.api.ApiResultCode;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 使用SHA256进行不可逆加解密
 * @author xbayonet
 *
 */
public class EncryptSHA
{
	
	private static EncryptSHA encryptSha = null;
	
	private EncryptSHA()
	{
	}
	
	public static EncryptSHA getInstances()
	{
		if(null == encryptSha)
		{
			encryptSha = new EncryptSHA();
		}
		
		return encryptSha;
	}
	
	public String encrypt(String encrypt)
	{
//		System.out.println("encrypt:"+encrypt);
		String result = DigestUtils.sha512Hex(encrypt);
		return result;
	}
	
	public int checkPassword(String password, String encryptPassword)
	{
		if(null == password|| null == encryptPassword)
		{
			return ApiResultCode.PARAMETER_PARSE_EXCEPTION.getCode();
		}
		
		if (password.equals(DigestUtils.sha512Hex(encryptPassword)))
		{
			return ApiResultCode.SUCCESS.getCode();
		}
		else
		{
			return ApiResultCode.FAIL.getCode();
		}
	}
}
