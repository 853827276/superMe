package com.hengzhang.springboot.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.assertj.core.util.Lists;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * excel 工具类
 * @author zhangh
 * @date 2018年8月31日下午1:14:47
 */
public class ExcelUtil {

	private static final String BOOKNOTNULLCOL = " 0,1,2,3,4,5,6,7,9";
	private static final String KNOWLEDGENOTNULLCOL = " 0,1,2";

	private static final String NOTEINFO ="1、必选条件* \r\n"
			+ "2、目录层级必须编写完整，用“/”隔开，第一级为专业知识库或聊天知识库，不符合规则无法导入，最高支持6级目录  \r\n"
			+ "3、句式会进行格式校验，不符合规则无法导入 \r\n"
			+ "4、“维度”为可选项，可单选和多选，单维度之间“，”隔开，并与系统中配置维度保持一致，否则无法导入 \r\n"
			+ "5、“指令”为可选项，单选项，并与系统中配置指令保持一致，否则无法导入 \r\n"
			+ "6、标准问重复导入到同标准问下 \r\n"
			+ "7、同标准问句式重复，去重导入  \r\n"
			+ "8、总条数不得超过50000行";
	
	/**
	 * 将工作簿输出到Response
	 * 
	 * @param wb
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static void workbook2Response(File file, HttpServletResponse resp) throws Exception {
		try {
			resp.setContentType("application/x-download"); 
			resp.setHeader("Content-Disposition", "attachment;filename="+new String(file.getName().getBytes("gbk"), "iso8859-1")); 
			InputStream ins = null;
			ins = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[ins.available()];
			ins.read(buffer);
			ServletOutputStream out = resp.getOutputStream();
			out.write(buffer);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("Excel文件没找到", e);
		}
	}
	
	/**
	 * 构造方法
	 * @author zhangh
	 * @date 2018年7月16日下午3:53:39
	 * @param columns
	 * @param list
	 * @param ExcelName
	 * @param SheetName
	 * @param resp
	 * @throws Exception
	 */
	public static void workbook2Response(List<Column> columns,List<Map<String, Object>> list, String ExcelName, HttpServletResponse resp) throws Exception {
		try {
			resp.setContentType("application/x-download");//下面三行是关键代码，处理乱码问题 
			resp.setHeader("Content-Disposition", "attachment;filename="+new String(ExcelName.getBytes("gbk"), "iso8859-1")); 
			ServletOutputStream out = resp.getOutputStream();
			getHSSFWorkbook1(columns, list).write(out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("Excel文件没找到", e);
		}
	}
	
	/**
	 * 格式化单元格
	 * @author zhangh
	 * @date 2018年8月23日下午3:37:09
	 * @param fieldType
	 * @return
	 */
	private static void dealCell(XSSFWorkbook wb,XSSFCellStyle style,String fieldType){
		DataFormat format = wb.createDataFormat();
		switch (fieldType) {
		case "tinyint":
		case "int":
		case "bigint":
			style.setDataFormat(format.getFormat("0"));
			break;
		case "float":
		case "double":
			style.setDataFormat(format.getFormat("0.00"));
			break;
		case "decimal":
			style.setDataFormat(format.getFormat("#,##0.00"));
			break;
		case "text":
		case "varchar":
		case "char":
			style.setDataFormat(format.getFormat("@"));
			break;
		}
	}
	
	
	public static XSSFWorkbook getHSSFWorkbook1(List<Column> columns,List<Map<String, Object>> list) throws Exception{
		XSSFWorkbook wb = new XSSFWorkbook();
		// 样式居左
		XSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HorizontalAlignment.LEFT);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		// 创建Excel表 
		XSSFSheet sheet = wb.createSheet("一问一答 +多问一答");
		@SuppressWarnings("unused")
		XSSFSheet sheet1 = wb.createSheet("一问多答+多问多答");
		if(ListUtil.isBlank(list)){
			return wb;
		}	
		// 渲染数据行
		for (int j = 0; j < list.size(); j++) {
			Map<String, Object> data = list.get(j);
			XSSFRow row = sheet.createRow(j);
			for (int i = 0; i < columns.size(); i++) {
				Column column = columns.get(i);
				String key = column.getKey();
				XSSFCell cell = row.createCell(i);
				if(StringUtils.equals(key, "answer")){
					if(data.get("ans") !=null){
						if(data.get("url") !=null){	
							String[] urlArr = data.get("url").toString().split(",");
							List<String> imgList = new ArrayList<String>();
							List<String> vedioList = new ArrayList<String>();
							if(urlArr !=null && urlArr.length>0){
								for (String u : urlArr) {
									String fileType = u.substring(u.lastIndexOf(".")+1).substring(0,3);
									if("jpg".equals(fileType) || "png".equals(fileType)){
										imgList.add(u);
							    	}else if("avi".equals(fileType) || "mp4".equals(fileType)){
							    		vedioList.add(u);
							    	}else{
							    		throw new Exception("暂不支持"+fileType+"类型");
							    	}
								}
								vedioList.addAll(imgList);
							}
							cell.setCellValue(data.get("ans").toString()+list2str(vedioList));
						}else{
							cell.setCellValue(data.get("ans").toString());
						}
					}else{
						cell.setCellValue(Objects.toString(data.get(key)!=null?data.get(key):""));
					}
				}else {
					cell.setCellValue(Objects.toString(data.get(key)!=null?data.get(key):""));
				}
				cell.setCellStyle(style);
			}
		}
		return wb;
	}
	
	private static String list2str(List<String> list){
		if(ListUtil.isBlank(list)){
			return "";
		}
		String s = "";
		for (String str : list) {
			s += str+",";
		}
		if(s!=""){
			s= s.substring(0, s.length()-1);
		}
		return s;
	}
	/**
	 * 构造一个HSSFWorkbook
	 * @author zhangh
	 * @date 2018年7月16日下午3:10:26
	 * @param columns
	 * @param list
	 * @return
	 */
	public static XSSFWorkbook getHSSFWorkbook(List<Column> columns,List<Map<String, Object>> list,String SheetName){
		XSSFWorkbook wb = new XSSFWorkbook();

		// 样式居中
		XSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		
		XSSFCellStyle notestyle = wb.createCellStyle();
		notestyle.setAlignment(HorizontalAlignment.LEFT);
		notestyle.setVerticalAlignment(VerticalAlignment.TOP);
		// 创建Excel表
		XSSFSheet sheet = wb.createSheet(SheetName==null?"导出数据":SheetName);
		//生成注释
		XSSFRow noteRow = sheet.createRow(0);
		XSSFCell notecell = noteRow.createCell(0);
		notecell.setCellStyle(notestyle);
		notecell.setCellValue(NOTEINFO);
		notestyle.setWrapText(true);
		noteRow.setHeight((short)(20*120));
		//合并
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
		// 创建列行
		XSSFRow headRow = sheet.createRow(1);
		// 创建列头
		for (int i = 0; i < columns.size(); i++) {
			sheet.setColumnWidth(i, 20 * 256);
			XSSFCell cell = headRow.createCell(i);
			cell.setCellValue(columns.get(i).getTitle());
			dealCell(wb, style, columns.get(i).getStyle());
			cell.setCellStyle(style);
		}
		if(ListUtil.isBlank(list)){
			return wb;
		}
		
		// 渲染数据行
		for (int j = 0; j < list.size(); j++) {
			Map<String, Object> data = list.get(j);
			XSSFRow row = sheet.createRow(j + 2);
			for (int i = 0; i < columns.size(); i++) {
				Column column = columns.get(i);
				String key = column.getKey();
				XSSFCell cell = row.createCell(i);
				if (StringUtils.equals(key, "_xh_")) {
					cell.setCellValue(j + 1);
				}else if(StringUtils.equals(key, "answer")){
					if(data.get("ans") !=null && data.get("url") !=null){						
						cell.setCellValue(data.get("ans").toString()+data.get("url").toString());
					}else{
						cell.setCellValue("");
					}
				}else {
					cell.setCellValue(Objects.toString(data.get(key)!=null?data.get(key):""));
				}
				cell.setCellStyle(style);
			}
		}
		return wb;
	}
	
	/**
	 * 读取excel
	 * @author zhangh
	 * @date 2018年7月16日下午3:54:28
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	public static Workbook importExcel(String type,String fileID,HttpServletRequest request, HttpServletResponse response) throws Exception{

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile importFile = multipartRequest.getFile(fileID);
        Workbook workbook = null;
        if(importFile != null){
            String fileName = importFile.getOriginalFilename();
            if(StringUtils.isNotBlank(fileName)){
                String[] strArr = fileName.split("\\.");
                if(strArr != null && strArr.length > 0){
                    if("xls".equals(strArr[strArr.length - 1])){
                        workbook = new HSSFWorkbook(importFile.getInputStream());
                    } else if("xlsx".equals(strArr[strArr.length - 1])){
                        workbook = new XSSFWorkbook(importFile.getInputStream());
                    } else {
                        throw new Exception("请选择正确的模板！");
                    }
					if (type.equals("1")) {
						if(!strArr[0].contains("book")){throw new Exception("请选择正确的模板！");}
					}
					if (type.equals("2")) {
						if(!strArr[0].contains("knowledge")){throw new Exception("请选择正确的模板！");}
					}
                }
            }
        } 
        return workbook;
	}
	
	/**
	 * 检查excel 中的有没有空数据
	 * @author zhangh
	 * @date 2018年7月18日下午6:12:33
	 * @param row
	 * @return true 校验通过  false  校验失败 没有通过有空值
	 */
	public static boolean checkRow(XSSFRow row){
		boolean pass = true;
		for (int i=0;i<row.getLastCellNum();i++) {
			if(null==row.getCell(i)|| StringUtils.isBlank(row.getCell(i).toString())){
				pass =false;
				break;
			}
		}
		return pass;
	}
	
	/**
	 * 处理时间格式 
	 * @author zhangh
	 * @date 2018年7月30日下午3:42:10
	 * @param cell
	 */
	public static String parseDate(XSSFCell cell){
		String str="";
		if(CellType.NUMERIC.equals(cell.getCellTypeEnum())){
			if(HSSFDateUtil.isCellDateFormatted(cell)){				
				return DateUtil.getToday();
			}else{
				BigDecimal big=new BigDecimal(cell.getNumericCellValue());
				str = big.toString();
  				if(null!=str&&!"".equals(str.trim())){
  				     String[] item = str.split("[.]");
  				     if(1 < item.length && "0".equals(item[1])){
  				    	str = item[0];
  				     }
  				}
			}
		}else{
			str = cell.toString();
		}
		return str;
	}
	
	/**
	 * 校验书 （主要是处理非空和数据去重）
	 * @author zhangh
	 * @date 2018年9月3日下午3:14:53
	 * @param Workbook
	 * @return
	 * @throws Exception 
	 */
	public static Map<String, Object> dealBook(String moduleID,XSSFWorkbook Workbook) throws Exception{
		Map<String, Object> map = new HashMap<>();
		List<String> sqlList = new ArrayList<>();
		StringBuffer errorInfo = new StringBuffer();
		XSSFSheet sheet = Workbook.getSheetAt(0);
		if(sheet.getLastRowNum()<=1){
			throw new Exception("模板内容不能为空");
		}
		List<String> ISBNList = new ArrayList<>();
		List<Map<String, Object>> bookIDAndNameList = new ArrayList<>();
		for (int i = 2; i <= sheet.getLastRowNum(); i++) {
			XSSFRow row = sheet.getRow(i);
			for (int j=0;j<row.getLastCellNum();j++) {
				if(BOOKNOTNULLCOL.indexOf(String.valueOf(j))>0){				
					if(null==row.getCell(j)|| StringUtils.isBlank(row.getCell(j).toString())){
						errorInfo.append("<div>第"+(i+1)+"行第"+(j+1)+"列 数据不能为空</div>");
					}
				}
			}
			String bookID = UUIDUtils.getId();
			if(errorInfo.length()<=0){
				sqlList.add(row2Book(i,moduleID,bookID,row));
				Map<String, Object> bookIdAndName = new HashMap<>();
				bookIdAndName.put("bookID",bookID);
				bookIdAndName.put("contentID",UUIDUtils.getId());
				bookIdAndName.put("bookName",row.getCell(0).toString());
				bookIDAndNameList.add(bookIdAndName);
			}
			if(row.getCell(9) !=null && row.getCell(9).toString().length()>0){				
				ISBNList.add(row.getCell(9).toString());	
			}
		}
		if(errorInfo.length()>0){
			throw new Exception(errorInfo.toString());
		}
//		List<String> existISBNList = SpringUtil.getBean(ContentDao.class).queryBookISBN(ISBNList);
		List<String> existISBNList = null;
		if(ListUtil.isNotBlank(existISBNList)){
			for (int i = 2; i <= sheet.getLastRowNum(); i++) {
				XSSFRow row = sheet.getRow(i);
				if(existISBNList.contains(row.getCell(9).toString())){
					errorInfo.append("<div>第"+(i+1)+"行第10列 ISBN已经存在，请重新输入</div>");
				}
			}
		}
		if(errorInfo.length()>0){
			throw new Exception(errorInfo.toString());
		}
		map.put("bookIDAndNameList", bookIDAndNameList);
		map.put("sqlList", sqlList);
		return map;
	}

	public static String row2Book(int i,String moduleID, String contentID,XSSFRow row) throws Exception{
		String sql = "insert into book(id,top,description,name,author,year,press,age,contentDesc,category,isbn,categoryNo,location,authorDesc,cover) values ";
		String name =row.getCell(0).toString();
		String author =row.getCell(1).toString();
		String year =row.getCell(2).toString();
		String press =row.getCell(3).toString();
		String age =row.getCell(4).toString();
		String contentDesc =row.getCell(6).toString();
		String category =row.getCell(5).toString();
		String isbn =row.getCell(9).toString();
		String categoryNo =row.getCell(10).toString();
		String location =row.getCell(11).toString();
		String authorDesc =row.getCell(7).toString();
		String coverID="";
		if(row.getCell(8) !=null){			
//			Map<String, Object> resourceMap = SpringUtil.getBean(IResourceDao.class).findResourceUrl(moduleID,row.getCell(8).toString(),"0");
			Map<String, Object> resourceMap = null;
			if(resourceMap !=null){
//				coverID = resourceMap.get("id").toString();resourceUrl
				coverID = resourceMap.get("resourceUrl").toString();
			}else{
				throw new Exception("第"+(i+1)+" 行 第9列封面资源不存在！");
			}
		}
		String value ="('"+contentID+"',2,null,'"+name+"','"+author+"','"+year+"','"+press+"','"+age+"','"+contentDesc+"','"+category+"','"+isbn+"','"+categoryNo+"','"+location+"','"+authorDesc+"','"+coverID+"')";
		return sql+value;
	}
	
	public static String row2KnowLegde(String moduleName, String detailID,XSSFRow row){
		String sql = "insert into  knowledgepoint (id, question, answer, direction, top, description) values ";
		String question =row.getCell(1).toString().replaceAll("'", "");
		String answer =row.getCell(2).toString().replaceAll("'", "");
		String direction =row.getCell(0).toString()+moduleName;
		
		String value ="('"+detailID+"', '"+question+"', '"+answer+"', '"+direction+"', 2, null)";
		return sql+value;
	}
	
	public static String row2KnowLegdeResource(String resourceID, String detailID){
		String sql = "insert into knowledgepoint_resource(id, knowledgePointID, resourceID) values";
		String value ="('"+UUIDUtils.getId()+"', '"+detailID+"', '"+resourceID+"')";
		return sql+value;
	}
	
	@SuppressWarnings("unused")
	private static Map<String, Object> queryTempMap(int i,String moduleID,String moduleName, XSSFRow row) throws Exception{
		Map<String, Object> map = new HashMap<>();
		map.put("location", row.getCell(0).toString()+moduleName);
		map.put("name", moduleName);
		map.put("question", row.getCell(1).toString());
		map.put("pattern", "");
		map.put("dimension", moduleName);
		map.put("instruct", "");
		map.put("answer", row.getCell(2).toString()+dealImgAndVideo(i,moduleID,row));
		return map;
	}
	private static Map<String, Object> queryTempMap1(int i,String moduleID,String moduleName, XSSFRow row) throws Exception{
		Map<String, Object> map = new HashMap<>();
		map.put("question", row.getCell(1).toString());
		map.put("answer", row.getCell(2).toString()+dealImgAndVideo(i,moduleID,row));
		map.put("icon", "高兴");
		return map;
	}
	
	private static List<String> queryResourceID(String moduleID,XSSFRow row) throws Exception{
		List<String> resourceIDs = new ArrayList<>();
		if(!StringUtil.isEmpty(row.getCell(4))){
			String[] resourceNames = row.getCell(4).toString().split(",");
			if(resourceNames !=null){
				for (String resourceName : resourceNames) {					
//					Map<String, Object> resourceMap = SpringUtil.getBean(IResourceDao.class).findResourceUrl(moduleID,resourceName,"1");
					Map<String, Object> resourceMap = null;
					if(resourceMap !=null){
						resourceIDs.add(resourceMap.get("id").toString());
					}
				}
			}
		}
		if(!StringUtil.isEmpty(row.getCell(3))){
			String[] resourceNames = row.getCell(3).toString().split(",");
			if(resourceNames !=null){
				for (String resourceName : resourceNames) {					
//					Map<String, Object> resourceMap = SpringUtil.getBean(IResourceDao.class).findResourceUrl(moduleID,resourceName,"0");
					Map<String, Object> resourceMap = null;
					if(resourceMap !=null){
						resourceIDs.add(resourceMap.get("id").toString());
					}
				}
			}
		}
		return resourceIDs;
	}
	/**
	 * 处理视频和音频
	 * @author zhangh
	 * @date 2018年9月11日上午10:00:41
	 * @param row
	 * @return
	 * @throws Exception 
	 */
	private static String dealImgAndVideo(int i,String moduleID,XSSFRow row) throws Exception{
		String imgAndVideo = "";
		String img="";
		String video="";
		if(!StringUtil.isEmpty(row.getCell(3))){
			String[] resourceNames = row.getCell(3).toString().split(",");
			if(resourceNames !=null){
				for (String resourceName : resourceNames) {
//					Map<String, Object> resourceMap = SpringUtil.getBean(IResourceDao.class).findResourceUrl(moduleID,resourceName.trim(),"0");
					Map<String, Object> resourceMap = null;
					if(resourceMap !=null && resourceMap.get("resourceUrl")!=null){						
						String resourceUrl =resourceMap.get("resourceUrl").toString();
						if(!StringUtil.isEmpty(resourceUrl)){
							img +="[["+resourceUrl+"]],";
							//todo
						}else{
							throw new Exception("第"+(i+1)+" 行 第4列资源不存在！");
						}
					}else{
						throw new Exception("第"+(i+1)+" 行 第4列资源不存在！");
					}
				}
			}
		}
		if(!StringUtil.isEmpty(row.getCell(4))){
			String[] resourceNames = row.getCell(4).toString().split(",");
			if(resourceNames !=null){
				for (String resourceName : resourceNames) {
//					Map<String, Object> resourceMap = SpringUtil.getBean(IResourceDao.class).findResourceUrl(moduleID,resourceName,"1");
					Map<String, Object> resourceMap = null;
					if(resourceMap !=null && resourceMap.get("resourceUrl")!=null){						
						String resourceUrl =resourceMap.get("resourceUrl").toString();
						if(!StringUtil.isEmpty(resourceUrl)){
							video +="[["+resourceUrl+"]],";
						}else{
							throw new Exception("第"+(i+1)+" 行 第5列资源不存在！");
						}
					}else{
						throw new Exception("第"+(i+1)+" 行 第5列资源不存在！");
					}
				}
			}
		}
		
		if(video !=null &&video.length()>0){
			if(img !=null &&img.length()>0){
				img =img.substring(0, img.length()-1);
			}
		}else{
			if(img !=null &&img.length()>0){
				img =img.substring(0, img.length()-1);
			}
		}
		imgAndVideo = video+img;
		return imgAndVideo;
	}
	
	private static boolean checkRow1(XSSFRow row){
		XSSFCell a = row.getCell(0);
		XSSFCell b = row.getCell(1);
		XSSFCell c = row.getCell(2);
		if((null == a || StringUtils.isBlank(a.toString())) && (null == b || StringUtils.isBlank(b.toString())) && (null == c || StringUtils.isBlank(c.toString()))){
			return true;
		}
		return false;
	}
	/**
	 * 处理知识点
	 * @author zhangh
	 * @date 2018年9月3日下午5:06:10
	 * @param detailID
	 * @param Workbook
	 * @return
	 * @throws Exception 
	 */
	public static Map<String, Object> dealKnowLedge(String moduleID,String moduleName, XSSFWorkbook Workbook) throws Exception{
		Map<String, Object> map = new HashMap<>();
		List<String> sqlList = new ArrayList<>();
		List<String> sql1List = new ArrayList<>();
		StringBuffer errorInfo = new StringBuffer();
		List<Map<String, Object>> tempList = new ArrayList<>();//封装的模板数据
		XSSFSheet sheet = Workbook.getSheetAt(0);
		if(sheet.getLastRowNum()<=1){
			throw new Exception("模板内容不能为空");
		}
		List<String> questionList = new ArrayList<>();
		List<Map<String, Object>> questionIDAndQuestionNameList = new ArrayList<>();
		for (int i = 2; i <= sheet.getLastRowNum(); i++) {
			
			XSSFRow row = sheet.getRow(i);
			if(checkRow1(row)){
				continue;
			}
//			tempList.add(queryTempMap(i,moduleID,moduleName, row));
			tempList.add(queryTempMap1(i,moduleID,moduleName, row));
			for (int j=0;j<row.getLastCellNum();j++) {
				if(KNOWLEDGENOTNULLCOL.indexOf(String.valueOf(j))>0){				
					if(null==row.getCell(j)|| StringUtils.isBlank(row.getCell(j).toString())){
						errorInfo.append("<div>第"+(i+1)+"行第"+(j+1)+"列 数据不能为空</div>");
					}
				}
			}
			if(row.getCell(1)!=null && row.getCell(1).toString().length()>128){
				errorInfo.append("<div>第"+(i+1)+"行第2列 标准问长度不能超过128</div>");
			}
			if(row.getCell(2)!=null && row.getCell(2).toString().length()>1000){
				errorInfo.append("<div>第"+(i+1)+"行第3列 标准答长度不能超过1000</div>");
			}
			String detailID = UUIDUtils.getId();
			if(errorInfo.length()<=0){				
				sqlList.add(row2KnowLegde(moduleName,detailID,row));
				List<String> resourceIDs = queryResourceID(moduleID, row);
				if(ListUtil.isNotBlank(resourceIDs)){
					for (String resourceID : resourceIDs) {
						sql1List.add(row2KnowLegdeResource(resourceID, detailID));
					}
				}
			}
			String questionName = "";
			if(row.getCell(1) !=null && row.getCell(1).toString().length()>0){				
				questionName = row.getCell(1).toString();	
			}
			Map<String, Object> questionIDAndQuestionNameMap = new HashMap<>();
			questionIDAndQuestionNameMap.put("detailID", detailID);
			questionIDAndQuestionNameMap.put("contentID", UUIDUtils.getId());
			questionIDAndQuestionNameMap.put("questionName", questionName);
			questionIDAndQuestionNameList.add(questionIDAndQuestionNameMap);
		}
		if(errorInfo.length()>0){
			throw new Exception(errorInfo.toString());
		}
//		List<String> existQuestionList = SpringUtil.getBean(ContentDao.class).queryKnowLedge(questionList);
		List<String> existQuestionList = Lists.newArrayList();
		if(ListUtil.isNotBlank(existQuestionList)){
			for (int i = 2; i <= sheet.getLastRowNum(); i++) {
				XSSFRow row = sheet.getRow(i);
				
				if(existQuestionList.contains(row.getCell(1).toString())){
					errorInfo.append("<div>第"+(i+1)+"行第2列 标准问已经存在，请重新输入</div>");
				}
			}
		}
		if(errorInfo.length()>0){
			throw new Exception(errorInfo.toString());
		}
		map.put("questionIDAndQuestionNameList", questionIDAndQuestionNameList);
		map.put("sqlList", sqlList);
		map.put("sql1List", sql1List);
		map.put("tempList", tempList);
		return map;
	}
	
	/**
	 * 旧模板
	 * @author zhangh
	 * @date 2018年9月17日上午9:30:52
	 * @return
	 */
	public static List<Column> queryKnowLedgeColumns(){
		List<Column> list = new ArrayList<>();
		list.add(new Column("目录", "location","varchar"));
		list.add(new Column("知识名称", "name","varchar"));
		list.add(new Column("标准问", "question","varchar"));
		list.add(new Column("句式", "pattern","varchar"));
		list.add(new Column("维度", "dimension","varchar"));
		list.add(new Column("指令", "instruct","varchar"));
		list.add(new Column("标准答案", "answer","varchar"));
		return list;
	}
	
	/**
	 * 新模板
	 * @author zhangh
	 * @date 2018年9月17日上午9:30:43
	 * @return
	 */
	public static List<Column> queryKnowLedgeColumns1(){
		List<Column> list = new ArrayList<>();
		list.add(new Column("标准问", "question","varchar"));
		list.add(new Column("标准答案", "answer","varchar"));
		list.add(new Column("表情", "icon","varchar"));
		return list;
	}
}
