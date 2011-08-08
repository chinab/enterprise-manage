package com.juduowang.utils;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * String Utility Class This is used to encode passwords programmatically
 * 
 * @author <a href="mailto:yexinhua@rtdata.cn">loveyeah</a>
 */
public class StringUtil {
    //~ Static fields/initializers =============================================

    private final static Log log = LogFactory.getLog(StringUtil.class);

    //~ Methods ================================================================

    /**
     * Encode a string using algorithm specified in web.xml and return the
     * resulting encrypted password. If exception, the plain credentials
     * string is returned
     *
     * @param password Password or other credentials to use in authenticating
     *        this username
     * @param algorithm Algorithm used to do the digest
     *
     * @return encypted password based on the algorithm.
     */
    public static String encodePassword(String password, String algorithm) {
        byte[] unencodedPassword = password.getBytes();

        MessageDigest md = null;

        try {
            // first create an instance, given the provider
            md = MessageDigest.getInstance(algorithm);
        } catch (Exception e) {
            log.error("Exception: " + e);

            return password;
        }

        md.reset();

        // call the update method one or more times
        // (useful when you don't know the size of your data, eg. stream)
        md.update(unencodedPassword);

        // now calculate the hash
        byte[] encodedPassword = md.digest();

        StringBuffer buf = new StringBuffer();

        for (byte anEncodedPassword : encodedPassword) {
            if ((anEncodedPassword & 0xff) < 0x10) {
                buf.append("0");
            }

            buf.append(Long.toString(anEncodedPassword & 0xff, 16));
        }

        return buf.toString();
    }

    /**
     * Encode a string using Base64 encoding. Used when storing passwords
     * as cookies.
     *
     * This is weak encoding in that anyone can use the decodeString
     * routine to reverse the encoding.
     *
     * @param str
     * @return String
     */
    public static String encodeString(String str)  {
        Base64 encoder = new Base64();
        return String.valueOf(encoder.encode(str.getBytes())).trim();
    }

    /**
     * Decode a string using Base64 encoding.
     *
     * @param str
     * @return String
     */
    public static String decodeString(String str) {
        Base64 dec = new Base64();
        try {
            return String.valueOf(dec.decode(str));
        } catch (DecoderException de) {
            throw new RuntimeException(de.getMessage(), de.getCause());
        }
    }
//    public static void main(String[] args) {
//		System.out.println("substring"+substring("aaaa中国",4,""));
//		System.out.println("substring1"+substring("aaaa中国",5,""));
//		System.out.println("substring2"+substring("aaaa中国",6,""));
//		System.out.println(new String("aaaa中国").substring(0, 6));
//		System.out.println(StringUtils.substring("aaaa中国",0,6));
//	}
    /** 
    * 取字符串的前toCount个字符
    * @param str 被处理字符串 
    * @param toCount 截取长度 
    * @param suffix 后缀字符串 
    * @return String 
    */ 
    public static String substring(String str, int toCount, String... suffix) {
		int reInt = 0;
		String reStr = "";
		if (str == null){
			return "";
		}
		char[] tempChar = str.toCharArray();
		for (int kk = 0; (kk < tempChar.length && toCount > reInt); kk++) {
			String s1 = String.valueOf(tempChar[kk]);
				//str.valueOf(tempChar[kk]);
			byte[] b = s1.getBytes();
			reInt += b.length;
			reStr += tempChar[kk];
		}
		if (toCount == reInt || (toCount == reInt - 1)){
			if(suffix!=null){
				reStr += suffix;
			}
		}
			
		return reStr;
	}
    /**
	 * 转换字符串的第一个字母为大写
	 * 
	 * @param str
	 * @return
	 */
    public static String toFirstUpper(String str) {
        if(str==null||str.length()<1) return "";
        if(str.length()==1)return str.substring(0, 1).toUpperCase();
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    /**
     * 转换字符串的第一个字母为小写
     * @param str
     * @return
     */
    public static String toFirstLower(String str) {
        if(str==null||str.length()<1) return "";
        if(str.length()==1)return str.substring(0, 1).toLowerCase();
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
    
    /**
     * 获取到最大匹配字符串
     * @param a
     * @param b
     * @return
     */
    public static String getMaxMatch(String a, String b) {
		StringBuffer tmp = new StringBuffer();
		String maxString = "";
		int max = 0;
		int len = 0;
		char[] aArray = a.toCharArray();
		char[] bArray = b.toCharArray();
		int posA = 0;
		int posB = 0;
		while (posA < aArray.length - max) {
			posB = 0;
			while (posB < (bArray.length - max)) {
				if (aArray[posA] == bArray[posB]) {
					len = 1;
					tmp = new StringBuffer();
					tmp.append(aArray[posA]);
					while ((posA + len < aArray.length)
							&& (posB + len < bArray.length)
							&& (aArray[posA + len] == bArray[posB + len])) {
						tmp.append(aArray[posA + len]);
						len++;
					}
					if (len > max) {
						max = len;
						maxString = tmp.toString();
					}
				}
				posB++;
			}
			posA++;
		}
		return maxString;
	}
    /**
     * source是否包含target,如果在target字串后面加上"$",那么就是最后匹配
     * @param source
     * @param target
     * @return
     */
    public static boolean isMatch(String source, String target) {
		Pattern p = Pattern.compile(target);
		Matcher m = p.matcher(source);
		return m.find();
	}
}
