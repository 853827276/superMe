package com.hengzhang.springboot.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	/**
	 * 判断是linux系统还是其他系统  如果是Linux系统，返回true，否则返回false 
	 * @author zhangh
	 * @date 2018年8月29日下午4:23:24
	 * @return
	 */
	public static boolean isLinux() { 
		return System.getProperty("os.name").toLowerCase().indexOf("linux") >= 0; 
	}
	
	/**
	 * 去掉字符串首尾的空格 判断字符串是否为空，为空直接返回，不为空去掉空格后返回
	 * <p/>
	 */
	public static String trim(String str) {
		if (isNullOrEmpty(str))
			return str;
		return str.trim();
	}

	/**
	 * 去掉字符串首尾的空格及全角空格 判断字符串是否为空，为空直接返回，不为空去掉空格后返回
	 * <p/>
	 */
	public static String trim2(String str) {
		if (isNullOrEmpty(str))
			return str;
		return trimFilling(trimFilling(str.trim(), '　', false), '　', true);
	}

	/**
	 * 去除字串中的填充字符
	 * 
	 * @param str
	 *            字串
	 * @param filling
	 *            填充字符
	 * @param leftJustified
	 *            true：字串左对齐；false：字串右对齐
	 * @return 去除填充字符后的字串
	 */
	public static String trimFilling(String str, char filling, boolean leftJustified) {
		if (str == null || str.length() == 0)
			return str;
		if (leftJustified) {
			int pos = str.length() - 1;
			for (; pos >= 0; pos--) {
				if (str.charAt(pos) != filling)
					break;
			}
			return str.substring(0, pos + 1);
		}
		int pos = 0;
		for (; pos < str.length(); pos++) {
			if (str.charAt(pos) != filling)
				break;
		}
		return str.substring(pos);
	}

	/**
	 * 去除HTML字串中的控制字符及不可视字符
	 * 
	 * @param str
	 *            HTML字串
	 * @return 返回的字串
	 */
	public static String escapeHTML(String str) {
		int length = str.length();
		int newLength = length;
		boolean someCharacterEscaped = false;
		for (int i = 0; i < length; i++) {
			char c = str.charAt(i);
			int cint = 0xffff & c;
			if (cint < 32)
				switch (c) {
				case 11:
				default:
					newLength--;
					someCharacterEscaped = true;
					break;

				case '\t':
				case '\n':
				case '\f':
				case '\r':
					break;
				}
			else
				switch (c) {
				case '"':
					newLength += 5;
					someCharacterEscaped = true;
					break;

				case '&':
				case '\'':
					newLength += 4;
					someCharacterEscaped = true;
					break;

				case '<':
				case '>':
					newLength += 3;
					someCharacterEscaped = true;
					break;
				}
		}
		if (!someCharacterEscaped)
			return str;

		StringBuffer sb = new StringBuffer(newLength);
		for (int i = 0; i < length; i++) {
			char c = str.charAt(i);
			int cint = 0xffff & c;
			if (cint < 32)
				switch (c) {
				case '\t':
				case '\n':
				case '\f':
				case '\r':
					sb.append(c);
					break;
				}
			else
				switch (c) {
				case '"':
					sb.append("&quot;");
					break;

				case '\'':
					sb.append("&apos;");
					break;

				case '&':
					sb.append("&amp;");
					break;

				case '<':
					sb.append("&lt;");
					break;

				case '>':
					sb.append("&gt;");
					break;

				default:
					sb.append(c);
					break;
				}
		}
		return sb.toString();
	}

	/**
	 * 去除SQL字串中的控制字符
	 * 
	 * @param str
	 *            SQL字串
	 * @return 返回的字串
	 */
	public static String escapeSQL(String str) {
		int length = str.length();
		int newLength = length;
		for (int i = 0; i < length;) {
			char c = str.charAt(i);
			switch (c) {
			case 0:
			case '"':
			case '\'':
			case '\\':
				newLength++;
			default:
				i++;
				break;
			}
		}
		if (length == newLength)
			return str;

		StringBuffer sb = new StringBuffer(newLength);
		for (int i = 0; i < length; i++) {
			char c = str.charAt(i);
			switch (c) {
			case '\\':
				sb.append("\\\\");
				break;

			case '"':
				sb.append("\\\"");
				break;

			case '\'':
				sb.append("\\'");
				break;

			case 0:
				sb.append("\\0");
				break;

			default:
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * 将一个字符串分解成几个段
	 * 
	 * @param str
	 *            字符串
	 * @param segLen
	 *            每段限长
	 * @param segNum
	 *            分解段数
	 * @return 分解后的字串组
	 */
	public static String[] split(String str, int segLen, int segNum) {
		String[] result = new String[segNum];
		if (str == null || str.length() == 0)
			return result;

		byte[] strByte;
		try {
			strByte = str.getBytes("GBK");
		} catch (UnsupportedEncodingException ex) {
			strByte = str.getBytes();
		}
		int pos = 0;
		for (int i = 0; i < segNum; i++) {
			int actLen = ((strByte.length - pos) < segLen) ? (strByte.length - pos) : segLen;
			byte[] b = new byte[actLen];
			System.arraycopy(strByte, pos, b, 0, actLen);
			result[i] = new String(b);
			pos += actLen;
			if (pos >= strByte.length)
				break;
		}
		return result;
	}

	/**
	 * 将一个字符串分解成几个段，每段都能正常转换为IBM935字符集
	 * 
	 * @param str
	 *            字符串
	 * @param segLen
	 *            每段限长
	 * @param segNum
	 *            分解段数
	 * @return 分解后的字串组
	 */
	public static String[] split4Cp935(String str, int segLen, int segNum) {

		String[] result = new String[segNum];

		byte[] strByte;
		try {
			strByte = str.getBytes("GBK");
		} catch (UnsupportedEncodingException ex) {
			strByte = str.getBytes();
		}
		int strLen = strByte.length;
		byte[] tmpByte = new byte[2 * strLen + segLen];

		int head;
		int flag;
		int count;

		int strCount = 0;
		int segCount = 0;
		int lastStrCount = 0;

		for (flag = 0, head = 0, count = 0; strCount < strLen && segCount < segNum; count++) {
			if ((strByte[strCount] & 0x80) != 0) {
				head = head == 1 ? 0 : 1;
				if (flag == 0) {
					tmpByte[count] = (byte) ' ';
					flag = 1;
					count++;
				}

				if ((count == ((count / segLen) + 1) * segLen - 2) && (head == 1)) {
					tmpByte[count] = (byte) ' ';
					tmpByte[count + 1] = (byte) ' ';
					result[segCount] = str.substring(lastStrCount, strCount);
					lastStrCount = strCount;
					segCount++;
					tmpByte[count + 2] = (byte) ' ';
					flag = 1;
					count = count + 3;
				} else if (((segLen * ((count + 1) / segLen) - 1) == count) && (head == 1)) {
					tmpByte[count] = (byte) ' ';
					byte[] tmp = new byte[strCount - lastStrCount];
					System.arraycopy(strByte, lastStrCount, tmp, 0, tmp.length);
					result[segCount] = new String(tmp);
					lastStrCount = strCount;
					segCount++;
					tmpByte[count + 1] = (byte) ' ';
					count = count + 2;
					flag = 1;
				} else if (((segLen * (count / segLen)) == count) && (head == 1)) {
					byte[] tmp = new byte[strCount - lastStrCount];
					System.arraycopy(strByte, lastStrCount, tmp, 0, tmp.length);
					result[segCount] = new String(tmp);
					lastStrCount = strCount;
					segCount++;
					tmpByte[count] = (byte) ' ';
					count++;
					flag = 1;
				}
			} else {
				if (flag == 1) {
					tmpByte[count] = (byte) ' ';
					count++;
					flag = 0;
					if (count == (count / segLen) * segLen) {
						byte[] tmp = new byte[strCount - lastStrCount];
						System.arraycopy(strByte, lastStrCount, tmp, 0, tmp.length);
						result[segCount] = new String(tmp);
						lastStrCount = strCount;
						segCount++;
					} else {
						if ((segLen * ((count + 1) / segLen) - 1) == count) {
							byte[] tmp = new byte[strCount - lastStrCount + 1];
							System.arraycopy(strByte, lastStrCount, tmp, 0, tmp.length);
							result[segCount] = new String(tmp);
							lastStrCount = strCount + 1;
							segCount++;
						}
					}
				} else {
					if ((segLen * ((count + 1) / segLen) - 1) == count) {
						byte[] tmp = new byte[strCount - lastStrCount + 1];
						System.arraycopy(strByte, lastStrCount, tmp, 0, tmp.length);
						result[segCount] = new String(tmp);
						lastStrCount = strCount + 1;
						segCount++;
					}
				}

			}
			tmpByte[count] = strByte[strCount];
			strCount++;
			if (strCount >= strLen && segCount < segNum) {
				byte[] tmp = new byte[strCount - lastStrCount];
				System.arraycopy(strByte, lastStrCount, tmp, 0, tmp.length);
				result[segCount] = new String(tmp);
				lastStrCount = strCount;
				segCount++;
			}

			if ((count + 1) % segLen == 0) {
				flag = 0;
			}
		}

		return result;
	}

	/**
	 * 取得字符串字节长度,使用缺省字符集
	 * 
	 * @param str
	 * @return length
	 */
	public static int getByteLength(String str) {
		return str.getBytes().length;
	}

	/**
	 * 截取最长为length字节的字符串
	 * 
	 * @param str
	 * @param length
	 * @return str
	 */
	public static String subString(String str, String encode, int length) {
		if (str == null || length < 0)
			return null;
		int zhl = 0, enl = 0;
		if (zhLen.containsKey(encode)) {
			zhl = ((Integer) zhLen.get(encode)).intValue();
			enl = ((Integer) enLen.get(encode)).intValue();
		} else {
			try {
				zhl = zh.getBytes(encode).length;
			} catch (UnsupportedEncodingException e) {
				zhl = zh.getBytes().length;
			}
			try {
				enl = en.getBytes(encode).length;
			} catch (UnsupportedEncodingException e) {
				enl = en.getBytes().length;
			}
			zhLen.put(encode, new Integer(zhl));
			enLen.put(encode, new Integer(enl));
		}

		int len = 0;
		int strlen = str.length();
		int i;
		for (i = 0; i < strlen; i++) {
			if (str.charAt(i) < 0x80)
				len += enl;
			else
				len += zhl;
			if (len > length)
				break;

		}

		return str.substring(0, i);
	}

	/**
	 * 将带分隔符的字符串转换成list
	 */
	public static List<String> splitString(String str, String separator) {
		return new ArrayList<String>(Arrays.asList(str.split(separator)));
	}

	public static String xml2String(byte[] xml) {
		if (xml == null)
			return null;
		if (xml.length == 0)
			return "";
		try {
			if (xml.length > 20) {
				int max = Math.min(100, xml.length);
				int i = 0;
				while (i < max && xml[i] != '<')
					i++;
				int j = i;
				while (j < max && xml[j] != '>')
					j++;
				if (j - i > 15) {
					String head = new String(xml, i, j - i + 1, "ISO-8859-1");
					i = head.indexOf("encoding=");
					if (i != -1) {
						i += "encoding=".length();
						char q = head.charAt(i);
						j = head.indexOf(q, i + 1);
						if (j != -1) {
							head = head.substring(i + 1, j);
							return new String(xml, head);
						}
					}
				}
			}
		} catch (Throwable t) {
		}
		return new String(xml);
	}

	/**
	 * 得到以head开头,tail结尾的子串
	 * 
	 * @param buffer
	 *            String
	 * @return String
	 */
	public static String subString(String buffer, String head, String tail) {
		if (buffer == null || head == null || tail == null)
			return buffer;
		int startNum = buffer.indexOf(head);
		int endNum = buffer.lastIndexOf(tail);

		startNum = startNum >= 0 ? startNum : 0;
		endNum = endNum >= 0 ? endNum + tail.length() : buffer.length();
		return buffer.substring(startNum, endNum);
	}

	/**
	 * 将List中内的MAP字串转换为MAP对象
	 * 
	 * @param listMapInner
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List listString2Map(List listMapInner) {
		List list = new ArrayList();
		for (int i = 0; i < listMapInner.size(); i++) {
			Object map = listMapInner.get(i);
			if (map != null) {
				list.add(string2Map(map.toString()));
			}
		}
		return list;
	}

	/**
	 * 将MAP对象转换为字串
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String map2String(Map map) {
		StringBuffer sb = new StringBuffer();
		boolean isFirst = true;
		for (
		Iterator it = map.entrySet().iterator(); it.hasNext();) {
			Entry entry = (Entry) it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			if (key != null && value != null) {
				if (!isFirst) {
					sb.append(",");
				} else {
					isFirst = false;
				}
				sb.append(normalizeMapString(key.toString().trim()));
				sb.append("=");
				sb.append(normalizeMapString(value.toString().trim()));
			}
		}
		return sb.toString();
	}

	/**
	 * 将字串对象转换为MAP
	 * 
	 * @param str
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map string2Map(String str) {
		Map map = new HashMap();
		String[] entrys = tokenizeToStringArray(str, ",");
		for (int i = 0; i < entrys.length; i++) {
			String[] entry = tokenizeToStringArray(entrys[i], "=");
			if (entry.length == 2) {
				map.put(specializeMapString(entry[0]), specializeMapString(entry[1]));
			}
		}
		return map;
	}

	private static final String[] ESC_TABLE = new String[] { "&cma&", "&equ&" };

	private static final String[] TALBLE = new String[] { ",", "=" };

	public static String[] tokenizeToStringArray(String str, String delimiters) {
		return tokenizeToStringArray(str, delimiters, true, true);
	}

	public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens,
			boolean ignoreEmptyTokens) {

		if (str == null) {
			return null;
		}
		StringTokenizer st = new StringTokenizer(str, delimiters);
		List<String> tokens = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
			if (!ignoreEmptyTokens || token.length() > 0) {
				tokens.add(token);
			}
		}
		return toStringArray(tokens);
	}

	public static String[] toStringArray(Collection<String> collection) {
		if (collection == null) {
			return null;
		}
		return collection.toArray(new String[collection.size()]);
	}

	public static String replace(String inString, String oldPattern, String newPattern) {
		if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null) {
			return inString;
		}
		StringBuilder sb = new StringBuilder();
		int pos = 0; // our position in the old string
		int index = inString.indexOf(oldPattern);
		// the index of an occurrence we've found, or -1
		int patLen = oldPattern.length();
		while (index >= 0) {
			sb.append(inString.substring(pos, index));
			sb.append(newPattern);
			pos = index + patLen;
			index = inString.indexOf(oldPattern, pos);
		}
		sb.append(inString.substring(pos));
		// remember to append any characters to the right of a match
		return sb.toString();
	}

	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}

	public static boolean hasLength(String str) {
		return hasLength((CharSequence) str);
	}

	private static String normalizeMapString(String str) {
		if (str == null) {
			return null;
		}
		String result = str;
		for (int i = 0; i < TALBLE.length; i++) {
			result = replace(result, TALBLE[i], ESC_TABLE[i]);
		}
		return result;
	}

	private static String specializeMapString(String str) {
		if (str == null) {
			return null;
		}
		String result = str;
		for (int i = 0; i < TALBLE.length; i++) {
			result = replace(result, ESC_TABLE[i], TALBLE[i]);
		}
		return result;
	}


	/**
	 * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNullOrEmpty(Object obj) {
		if (obj == null)
			return true;

		if (obj instanceof CharSequence)
			return ((CharSequence) obj).length() == 0;

		if (obj instanceof Collection)
			return ((Collection<?>) obj).isEmpty();

		if (obj instanceof Map)
			return ((Map<?, ?>) obj).isEmpty();

		if (obj instanceof Object[]) {
			Object[] object = (Object[]) obj;
			boolean empty = true;
			for (int i = 0; i < object.length; i++)
				if (!isNullOrEmpty(object[i])) {
					empty = false;
					break;
				}
			return empty;
		}
		return false;
	}

	/**
	 * 将用separator分隔的String转化为List，如果str中没有separator则返回的List中只有 str一项
	 * 
	 * @param str
	 * @param separator
	 * @return
	 */
	public static List<String> str2List(String str, String separator) {
		List<String> result = new ArrayList<String>();
		if (str.indexOf(separator) < 0) {
			result.add(str);
		} else {
			String[] strArray = str.split(separator);

			for (int i = 0; i < strArray.length; i++) {
				result.add(strArray[i]);
			}
		}

		return result;
	}

	/**
	 * 去除字符串中空白字符
	 * 
	 * @param str
	 * @return
	 */
	public static String filterSBCCase(String str) {
		char[] ch = str.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ch.length; i++) {
			if (!Character.isWhitespace(ch[i])) {
				sb.append(String.valueOf(ch[i]));
			}
		}
		return sb.toString();
	}

	/**
	 * 字符串是否全数字（无符号、小数点）
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDigit(String str) {
		char c;
		for (int i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			if (c < '0' || c > '9')
				return false;
		}
		return true;
	}

	/**
	 * 字符串是否全数字或英文字母（无符号、小数点）
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isAlphaDigit(String str) {
		char c;
		for (int i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			if (c < '0' || c > '9' && c < 'A' || c > 'Z' && c < 'a' || c > 'z')
				return false;
		}
		return true;
	}

	/**
	 * get substring in bytes length
	 * 
	 * @param orgString
	 *            original string
	 * @param lengthInBytes
	 *            bytes length
	 * @return substring
	 */
	public static final String subStringInBytes(String orgString, int startPos, int lengthInBytes) {

		if (orgString == null)
			return null;

		byte[] orgBytes;
		try {
			orgBytes = orgString.getBytes("GBK");
		} catch (UnsupportedEncodingException ex) {
			orgBytes = orgString.getBytes();
		}
		if (startPos < 0 || startPos > orgBytes.length)
			return null;
		else if (lengthInBytes < startPos)
			return null;

		byte[] newBytes;
		int newLength = orgBytes.length - startPos;
		if (lengthInBytes < newLength)
			newLength = lengthInBytes;

		newBytes = new byte[newLength];
		System.arraycopy(orgBytes, startPos, newBytes, 0, newLength);

		return new String(newBytes);
	}

	/**
	 * 按千位分割格式格式化数字
	 * 
	 * @param v
	 * @param scale
	 * @return
	 */
	public static String parseStringPattern(Object v, int scale) {
		String temp = "###,###,###,###,###,###,###,##0";
		if (scale > 0)
			temp += ".";
		for (int i = 0; i < scale; i++)
			temp += "0";
		DecimalFormat format = new DecimalFormat(temp);
		return format.format(v).toString();
	}

	/**
	 * 转为String[]
	 * 
	 * @param object
	 * @return
	 */
	public static String[] getStringArray(Object object) {
		if (object instanceof String[])
			return (String[]) object;
		if (object instanceof String) {
			String tmpStrs[] = new String[1];
			tmpStrs[0] = (String) object;
			return tmpStrs;
		}
		return null;
	}

	private static Map<String, Integer> zhLen = new HashMap<String, Integer>(), enLen = new HashMap<String, Integer>();

	private static final String zh = "汉";

	private static final String en = "A";

	/**
	 * 取得类的简单名
	 * 
	 * @param obj
	 * @return 类的简单名
	 */
	public static String getSimpleName(Object obj) {
		if (obj == null)
			return null;
		String name = obj.getClass().getName();
		if (name.toLowerCase().indexOf("$proxy") >= 0) {
			name = obj.toString();
			int idx = name.lastIndexOf("@");
			if (idx > 0)
				name = name.substring(0, idx);
		}
		return name.substring(name.lastIndexOf(".") + 1);
	}

	/**
	 * 截字串
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static String subString(String str, int length) {
		if (str == null) {
			return "";
		}
		int end = str.length();
		if (length > end) {
			end = length;
		}
		return str.substring(0, end);
	}

	/**
	 * 获取字符串的长度, 如果为null则长度为0
	 * 
	 * @param str
	 * @return
	 */
	public static int length(String str) {
		if (isNullOrEmpty(str))
			return 0;
		else
			return str.trim().length();

	}

	/**
	 * 海外对公交易查询汇总公用方法 将总金额按照币种分类，组成字符串
	 * 
	 * @param list
	 * @return String
	 */
	public static String ListToString(List<?> list) {
		String listString = "";
		if (list != null && list.size() != 0) {
			for (int i = 0; i < list.size(); i++) {
				String curr = (String) (((Map<?, ?>) (list.get(i))).get("currency"));
				String amout = ((Map<?, ?>) (list.get(i))).get("amount").toString();
				listString = listString + curr + "|" + amout + "@";
			}
			return listString;
		} else {
			return null;
		}
	}

	/**
	 * <p>
	 * 方法shortClassName()。
	 * </p>
	 * 
	 * @param obj
	 * @return
	 */
	public static String shortClassName(Object obj) {
		if (obj == null) {
			return "null";
		}

		Class<?> clazz = obj instanceof Class ? (Class<?>) obj : obj.getClass();
		String fullname = clazz.getName();
		int packageNameLength = clazz.getPackage().getName().length();
		return packageNameLength > 0 ? fullname.substring(packageNameLength + 1) : fullname;
	}

	public static String CRLF = System.getProperty("line.separator");

	public static String ellipsisText(String longText, int maxLength) {
		if (longText == null)
			return "null";
		int index = longText.indexOf(CRLF);
		String firstLine = index >= 0 ? longText.substring(0, index) : longText;
		return firstLine.length() > maxLength ? firstLine.substring(0, maxLength) + "..." : firstLine;
	}

	public static String stringOf(Exception ex) {
		if (ex != null) {
			StringBuffer sb = new StringBuffer();
			sb.append("Class: " + shortClassName(ex));
			sb.append("Message: " + ellipsisText(ex.getMessage(), 32));
			return sb.toString();
		} else {
			return "null";
		}
	}

	public static String stringOf(Date date) {
		return stringOf(TimeZone.getDefault(), date);
	}

	public static String stringOf(TimeZone timeZone, Date date) {
		if (date == null) {
			return "null";
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		if (timeZone != null) {
			formatter.setTimeZone(timeZone);
		}
		return formatter.format(date);
	}

	/**
	 * 返回srcStr字符串的前truncByteLen字节长度的子串。<br>
	 * <p/>
	 * 如果截取的末字节是半个汉字，则略去该字节，要求返回完整的汉字。<br>
	 * <p/>
	 * (1)srcStr == null || truncByteLen < 0 返回""<br>
	 * (2)truncByteLen > srcStr的实际字节长度，则原样返回srcStr
	 * 
	 * @param srcStr
	 *            待截取字符串
	 * @param charsetName
	 *            编码方式
	 * @param truncByteLen
	 *            截取字节数
	 * @return 截取后的字符串
	 *         <p/>
	 *         e.g. srcStr="ab你好", truncByteLen=3, 返回"ab"; truncByteLen=4,
	 *         返回"ab你"
	 *         <p/>
	 */
	public static String truncate(String srcStr, String charsetName, int truncByteLen) {
		if (srcStr == null || truncByteLen < 0)
			return "";

		int strLen = srcStr.length();
		String tempStr = "";

		try {
			if (truncByteLen <= strLen)
				tempStr = srcStr.substring(0, truncByteLen);// truncByteLen=0,则返回""
			else
				tempStr = srcStr;
		} catch (IndexOutOfBoundsException iobe) {
			return "";
		}

		int tempByteLen;
		try {
			tempByteLen = tempStr.getBytes(charsetName).length;
		} catch (UnsupportedEncodingException e) {
			tempByteLen = tempStr.getBytes().length;
		}

		while (tempByteLen > truncByteLen) {
			tempStr = tempStr.substring(0, tempStr.length() - 1);
			try {
				tempByteLen = tempStr.getBytes(charsetName).length;
			} catch (UnsupportedEncodingException e) {
				tempByteLen = tempStr.getBytes().length;
			}
		}
		return tempStr;
	}

	public static Locale toLocale(String localeStr) {
		if (localeStr == null)
			return null;
		String[] part = localeStr.split("_");
		if (part.length == 1)
			return new Locale(part[0].toLowerCase());
		if (part.length == 2)
			return new Locale(part[0].toLowerCase(), part[1].toUpperCase());
		if (part.length == 3)
			return new Locale(part[0].toLowerCase(), part[1].toUpperCase(), part[2].toUpperCase());
		return new Locale(localeStr);
	}

	/**
	 * 使用“,”对原有描述进行分隔，对所有为空的内容替换为“-”，重新组合后返回
	 * <p/>
	 * 特殊注意：当传入值为null时，返回”“ 当传入值为”“时，返回”-“ 当传入值只包含”,“时，返回”-,-“
	 * 
	 * @param args
	 * @return fillStr
	 */
	public static String fillNullArgs(String args) {

		String fillStr = "";

		// 参数为空指针返回空串
		if (args == null) {
			return fillStr;
		}

		// 无限次匹配，保留末尾空数组
		String[] strArgs = args.split(",", -1);

		// 替换空元素
		for (int i = 0; i < strArgs.length; i++) {
			if (StringUtil.isNullOrEmpty(strArgs[i])) {
				strArgs[i] = "-";
			}
			fillStr = fillStr + strArgs[i] + ",";
		}

		// 去掉最后的”,“
		if (!StringUtil.isNullOrEmpty(fillStr)
				&& (fillStr.substring(fillStr.length() - 1, fillStr.length()).equals(","))) {
			fillStr = fillStr.substring(0, fillStr.length() - 1);
		}

		return fillStr;
	}


	/**
	 * 把源字符串从一种编码转为另一种编码
	 * 
	 * @param src
	 *            原始字符串
	 * @param charset1
	 *            原解码类型
	 * @param charset2
	 *            新编码类型
	 * @return 转换后的编码 String
	 */
	public static String encoding(String src, String charset1, String charset2) throws UnsupportedEncodingException {
		String target = null;
		if (src != null) {
			target = new String(src.getBytes(charset1), charset2);
		}
		return target;
	}


	/**
	 * checkEmailValid
	 * 
	 * @param email
	 * @return boolean
	 */
	public static boolean checkEmailValid(String email) {

		Pattern pattern = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");
		Matcher matcher = pattern.matcher(email);

		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 验证Obj 是否为null以及trim()后是否为""
	 * @author zhangh
	 * @date 2018年8月2日上午9:39:12
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj){
		if(obj == null){
			return true;
		}else{
			String objStr = obj.toString();
			return null==objStr||"".equals(objStr.trim()) || "[]".equals(obj.toString());
		}
	}

	public static String first2letter(String str) {
		return str.substring(0, 1).toLowerCase()+str.substring(1);
	} 
	public static String first2Upper(String str) {
		return str.substring(0, 1).toUpperCase()+str.substring(1);
	} 
	/**
	 * URL 编码, Encode默认为UTF-8. 
	 * @throws UnsupportedEncodingException 
	 */
	public static String urlEncode(String part) throws UnsupportedEncodingException {
			return URLEncoder.encode(part, "UTF-8");
	}

	/**
	 * URL 解码, Encode默认为UTF-8. 
	 * @throws UnsupportedEncodingException 
	 */
	public static String urlDecode(String part) throws UnsupportedEncodingException {
			return URLDecoder.decode(part, "UTF-8");
	}

}
