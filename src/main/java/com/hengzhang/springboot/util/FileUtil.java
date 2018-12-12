package com.hengzhang.springboot.util;

import java.io.File;
import java.io.FileOutputStream;

/**
 * file 上传处理方法
 * @author zhangh
 * @date 2018年8月29日下午4:25:51
 */
public class FileUtil {
	
	/**
	 * 上传文件
	 * @author zhangh
	 * @date 2018年8月29日下午4:26:05
	 * @param file
	 * @param filePath
	 * @param fileName
	 * @throws Exception
	 */
	public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
		File targetFile = new File(filePath);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		FileOutputStream out = new FileOutputStream(filePath + fileName);
		out.write(file);
		out.flush();
		out.close();
	}
	
	public static String getFileType(String fileName){
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}
	
	/**
	 * 判断是图片还是视频
	 * @author zhangh
	 * @date 2018年8月31日下午3:52:45
	 * @param fileName
	 * @return 1 图片； 2 视频 -1 未知
	 */
	public String showFileType(String fileName){
		
		String fileType = getFileType(fileName);
		if("jpg".equals(fileType) || "png".equals(fileType)){
			return "1";
		}else if("avi".equals(fileType) || "mp4".equals(fileType)){
			return "2";
		}else{
			return "-1";
		}
	}
}
