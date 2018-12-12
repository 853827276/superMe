package com.hengzhang.springboot.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * md5 工具类
 * @author zhangh
 * @date 2018年9月6日下午12:13:17
 */
public class MD5Util {
	
	private static final String SALT = "iflytek2018_md5";
    
    /**
     * MD5方法
     * 
     * @param text 明文
     * @param key 密钥
     * @return 密文
     * @throws Exception
     */
    public static String md5(String text ) throws Exception {
        //加密后的字符串
        String encodeStr=DigestUtils.md5Hex(SALT+text);
        System.out.println("MD5加密后的字符串为:encodeStr="+encodeStr);
        return encodeStr;
    }
}
