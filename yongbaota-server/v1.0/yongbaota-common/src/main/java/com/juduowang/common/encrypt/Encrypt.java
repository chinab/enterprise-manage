package com.juduowang.common.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.security.SecureRandom;
//
//import javax.crypto.KeyGenerator;
//import javax.crypto.Mac;
//import javax.crypto.SecretKey;

/**
 * 使用特定的BaseKey生成HmacMD5码，并转化成16进制字符串
 * @author loveyeah
 * @date 2006-06-19
 */
public class Encrypt 
{  
	
	
	/****利用MD5进行加密
     * @param str  待加密的字符串
     * @return  加密后的字符串
     * @throws NoSuchAlgorithmException  没有这种产生消息摘要的算法
     * @throws UnsupportedEncodingException  
     */
    public static String encoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密后的字符串
        String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }
    
//    public boolean checkpassword(String newpasswd,String oldpasswd) throws NoSuchAlgorithmException, UnsupportedEncodingException{
//        if(encoderByMd5(newpasswd).equals(oldpasswd))
//            return true;
//        else
//            return false;
//    }
        
//    private static byte[] raw={ 0x01, 0x03, 0x09, 0x05, 0x05, 0x01,0x08, 0x06, 0x09, 0x06, 0x07,
//                                   'l','i','n','k','t','o','n','y'} ;
//    /**
//     * 加密生成md5码
//     * @param clearText 明文
//     * @return String
//     */
//    public static String generateKey (String clearText) throws Exception{
//        try{
//          KeyGenerator kg = KeyGenerator.getInstance("HmacMD5");
//          
//          //初始化base key
//          SecureRandom raw2 = new SecureRandom();
//          raw2.setSeed(raw);
//          kg.init(raw2);
//          SecretKey sk = kg.generateKey();
//          
//          Mac mac = Mac.getInstance("HmacMD5");
//          mac.init(sk);
//          byte[] result = mac.doFinal(clearText.getBytes());
//          StringBuffer sb = new StringBuffer();
//        
//          for(int i=0; i<result.length; i++)
//              byte2hex(result[i], sb);
//  
//          return sb.toString();
//        }catch(NoSuchAlgorithmException ne)
//        {
//          throw new NoSuchAlgorithmException("加密不成功！");
//        }catch(InvalidKeyException ie)
//        {
//          throw new InvalidKeyException("加密不成功！");
//        }        
//    }
//
//    /**
//     * 二进制转化成十六进制
//     * @param b
//     * @param buf
//     */
//    private static void byte2hex(byte b, StringBuffer buf) {
//        char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
//                            '9', 'A', 'B', 'C', 'D', 'E', 'F' };
//        int high = ((b & 0xf0) >> 4);
//        int low = (b & 0x0f);
//        buf.append(hexChars[high]);
//        buf.append(hexChars[low]);
//    }
}