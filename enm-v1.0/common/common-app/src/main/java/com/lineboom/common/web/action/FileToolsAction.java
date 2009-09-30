package com.lineboom.common.web.action;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletOutputStream;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;

import com.lineboom.common.tools.date.DateTools;
import com.lineboom.common.tools.file.FileTools;

public class FileToolsAction extends BaseActionSupport{
	private static final long serialVersionUID = 1L;

    private File doc;    
    private String fileName;    
	private String contentType;
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	/**
	 * 下载文件<br>
	 * 调用这个action要从前台传来三个参数<br><ul>
	 * <li>bizfield:业务域，在下面选择<ul><li>"data-files"</li><li>"doc-files"</li><li>"media-files"</li><li>"exception-files"</li><li>"other-files"</li></ul></li>
	 * <li>filepath:要保存的文件名</li>
	 * <li>contentType:文件类型<ul>参考<a href="http://192.168.32.92/mwiki/index.php/Content_Type%E6%9F%A5%E8%AF%A2">Content Type查询</a></li>
	 * <ul>
	 */
    public void download() {
		String bizfield = getStringParam("bizfield");
		String filePath = FileTools.getRealFileName(getStringParam("filePath"));
		String contentType = getStringParam("contentType");
		String fileFullName = FileTools.getAppFilesPathByBusiness(bizfield)	+ filePath;
		String fileName = filePath.contains("/") ? filePath.substring(filePath.lastIndexOf("/") + 1) : filePath;

		try {
			File file = new File(fileFullName);
			ServletOutputStream outputStream = getResponse().getOutputStream();
			getResponse().setContentType(contentType);
			getResponse().addHeader(
					"Content-Disposition",
					"attachment; filename=\""
							+ new String(fileName.getBytes("GB2312"),
									"ISO_8859_1") + "\"");
			FileInputStream inputStream = new FileInputStream(file);
			FileTools.copyStream(outputStream, inputStream);
			outputStream.close();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    /**
	 * 上转文件<br>
	 * 调用这个action要从前台传来两个参数<br><ul>
	 * <li>bizfield:业务域，在下面选择<ul><li>"data-files"</li><li>"doc-files"</li><li>"media-files"</li><li>"exception-files"</li><li>"other-files"</li></ul></li>
	 * <li>folderpath:要保存的文件目录</li>
	 * <ul>
	 */
    public void upload(){
    	String bizfield = getStringParam("bizfield");
		String folderpath = FileTools.getRealFileName(getStringParam("folderpath"));
		String targetDirectory = FileTools.getAppFilesPathByBusiness(bizfield);
		
        String fileType = FileTools.getExtendName(fileName);
		String targetFileName = folderpath + "/upload"+DateTools.getDateStr("yyyyMMddhhmmssSS")+"."+fileType;
		
		FileBean fileBean = new FileBean(bizfield,targetFileName,fileName,doc.length(),contentType,fileType);
        JSONObject object = JSONObject.fromObject(fileBean);
        try {
        	getResponse().setCharacterEncoding("UTF-8");
			getResponse().getWriter().print("{\"success\":true,\"message\":\"OK, "+doc.length()+" byte uploaded!\",\"fileInfo\":"+object.toString()+"}");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        
        try {
        	File target = new File(targetDirectory, targetFileName);
			FileUtils.copyFile(doc, target);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
	 * 浏览文件系统<br>
	 * 调用这个action要从前台传来三个参数<br><ul>
	 * <li>bizfield:业务域，在下面选择<ul><li>"data-files"</li><li>"doc-files"</li><li>"media-files"</li><li>"exception-files"</li><li>"other-files"</li></ul></li>
	 * <li>isFolder:表示是否是树上显示，如果是就是true，否则就是flash</li>
	 * <li>filePath:要浏览的文件夹</li>
	 * <li><b>以上参数被存放在node参数中，因为树的请求一般只传一个参数</b></li>
	 * <ul>
	 */
    public void getFiles(){
    	JSONArray jsonArray = new JSONArray();
    	String nodeId = getStringParam("node");
    	String[] strs = nodeId.split("\\*");
    	String bizfield = "root";
    	String filePath = "/";
    	FileFilter fileFilter = null;
    	if(strs.length>=2){
    		final boolean isFolder = Boolean.valueOf(strs[1]);
    		bizfield = strs[0];
    		fileFilter = new FileFilter(){
        		public boolean accept(File subFile) {
					return !isFolder||subFile.isDirectory();
        		}
        	};
        	if(strs.length>=3){
        		filePath = strs[2];
        	}

    		if(bizfield.equals("root")){
    			String[] bizfields = { FileTools.DATA_FILE_PATH,
    					FileTools.DOC_FILE_PATH, FileTools.EXCEPTION_FILE_PATH,
    					FileTools.MEDIA_FILE_PATH, FileTools.OTHER_FILE_PATH };
    			for (String bizfieldTemp : bizfields) {
    				FileTools.createFolder(FileTools.getAppFilesPathByBusiness(bizfieldTemp));
    				FileBean fileBean = new FileBean(bizfieldTemp,FileTools.getFileByBusiness(bizfieldTemp, "/"));
    				fileArrToJsonArr(jsonArray, fileFilter, fileBean);
    			}
    		}else{
    			File file = FileTools.getFileByBusiness(bizfield, filePath);
    			if(file.isDirectory()){
    				File[] subFiles = FileTools.browseFolder(file, fileFilter);
    				for (File subFile : subFiles) {
    					FileBean fileBean = new FileBean(bizfield,subFile);
    					fileArrToJsonArr(jsonArray, fileFilter, fileBean);
    				}
    			}
    		}
    		String resultStr = jsonArray.toString();
    		if(!isFolder){
    			resultStr = "{'totalCount':"+jsonArray.size()+",'success':false,'files':"+resultStr+"}";
    		}
			renderJSON(resultStr);
    	}
    }

    /**
     * 将文件加入到json数组中
     * @param jsonArray
     * @param fileFilter
     * @param fileBean
     */
	private void fileArrToJsonArr(JSONArray jsonArray, FileFilter fileFilter,
			FileBean fileBean) {
		JSONObject jsonObject = JSONObject.fromObject(fileBean);
		jsonObject.accumulate("id", fileBean.getBizfield()+"*true*"+fileBean.getFilePath());
		jsonObject.accumulate("text", fileBean.getFileName());
		boolean isLeaf = FileTools.browseFolder(fileBean.toFile(), fileFilter).length==0;
		if(isLeaf)jsonObject.accumulate("iconCls", "icon-folder-close-leaf");
		jsonObject.accumulate("leaf", isLeaf);
		jsonArray.add(jsonObject);
	}

       
    public class FileBean{
    	private String fileName;
    	private String bizfield;
    	private String filePath;
    	private Long size;
    	private String contentType;
    	private String fileType;
    	private File file;
    	public FileBean(String bizfield,String filePath){
    		File file = FileTools.getFileByBusiness(bizfield, filePath);
    		this.bizfield = bizfield;
    		this.filePath = filePath;
    		this.fileName = file.getName();
    		this.size = file.length();
    		this.fileType = FileTools.getExtendName(file);
    		this.file = file;
    	}
    	public FileBean(String bizfield,File file){
    		this.bizfield = bizfield;
    		this.filePath = file.getAbsolutePath().substring(file.getAbsolutePath().indexOf(bizfield)+bizfield.length());
    		this.fileName = file.getName();
    		this.size = file.length();
    		this.fileType = FileTools.getExtendName(file);
    		this.file = file;
    	}
    	public FileBean(String bizfield,String filePath,String fileName,Long size,String contentType,String fileType){
    		this.bizfield = bizfield;
    		this.filePath = filePath;
    		this.fileName = fileName;
    		this.size = size;
    		this.contentType = contentType;
    		this.fileType = fileType;
    	}
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		public Long getSize() {
			return size;
		}
		public void setSize(Long size) {
			this.size = size;
		}
		public String getFilePath() {
			return filePath;
		}
		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
		public String getContentType() {
			return contentType;
		}
		public void setContentType(String contentType) {
			this.contentType = contentType;
		}
		public String getFileType() {
			return fileType;
		}
		public void setFileType(String fileType) {
			this.fileType = fileType;
		}
		public String getBizfield() {
			return bizfield;
		}
		public void setBizfield(String bizfield) {
			this.bizfield = bizfield;
		}
		public File toFile(){
			if(file==null)
				return FileTools.getFileByBusiness(bizfield, filePath);
			return file;
		}
		public String getLastModified() {
			if(file!=null&&file.exists())
				return dateFormat.format(new Date(file.lastModified()));
			else return "";
		}
		
    }
        
    public void setDoc(File file) {
        this.doc = file;
    }    
        
    public void setDocFileName(String fileName) {
        this.fileName = fileName;    
    }
        
    public void setDocContentType(String contentType) {
        this.contentType = contentType;
    }
	
}
