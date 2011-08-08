package com.juduowang.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;

import com.juduowang.utils.listeners.WebappContextListener;


/**
 * 对文件和文件夹的处理<br>
 * 管理整个系统中的文件，上传下载，文件路径的确定<br>
 * 文件读，写<br>
 * 文件流拷贝<br>
 * 文件浏览<br>
 * 文件域管理等功能实现<br>
 * 
 * ~ 2010-02-26修改,使用IOUtils简化代码
 * 
 * @author yinshuwei
 */
public class FileUtil {
	/**
	 * 文件系统中的数据业务域<br>
	 * 所有的数据文件将存放在此目录下
	 */
	public static final String DATA_FILE_PATH = "data-files";
	
	/**
	 * 文件系统中的文档（如图片，档案，资料等）业务域<br>
	 * 有关图片，档案，资料等类似的文件将存放在此目录下
	 */
	public static final String DOC_FILE_PATH = "doc-files";
	
	/**
	 * 文件系统中的多媒体文件业务域<br>
	 * 有关多媒体的文件将存放在此目录下
	 */
	public static final String MEDIA_FILE_PATH = "media-files";
	
	/**
	 * 文件系统中的异常日志业务域<br>
	 * 异常日志文件将存放在此目录下
	 */
	public static final String EXCEPTION_FILE_PATH = "exception-files";
	
	/**
	 * 文件系统中的其他业务域<br>
	 * 除以上类型文件的其他文件都将存放在此目录下
	 */
	public static final String OTHER_FILE_PATH = "other-files";
	
	/**
	 * 从文件系统中获得业务域路径
	 * （业务域是文件系统的根）
	 * 
	 * @return
	 */
	public static File[] getRootFolders(){
		File[] files = new File[5];
		int i=0;
		String[] bizfields = { FileUtil.DATA_FILE_PATH,
				FileUtil.DOC_FILE_PATH, FileUtil.EXCEPTION_FILE_PATH,
				FileUtil.MEDIA_FILE_PATH, FileUtil.OTHER_FILE_PATH };
		for (String bizfield : bizfields) {
			createFolder(getAppFilesPathByBusiness(bizfield));
			files[i++] = getFileByBusiness(bizfield, "/");
		}
		return files;
	}
	
	/**
	 * 从文件系统中获得文件<br>
	 * 如，要获取一个异常日志文件<br>
	 * File logFile =
	 * FileTools.getFileByBusiness(FileTools.EXCEPTION_FILE_PATH,"UploadFiles/myFilePath/log20090601.log");
	 * 
	 @param bizfield
	 *            业务划分的目录 如：<ui>
	 *            <li>FileTools.DATA_FILE_PATH</li>
	 *            <li>FileTools.DOC_FILE_PATH</li>
	 *            <li>FileTools.MEDIA_FILE_PATH</li>
	 *            <li>FileTools.EXCEPTION_FILE_PATH</li>
	 *            <li>FileTools.OTHER_FILE_PATH</li>
	 *            </ui>
	 * @param subPath 下级目录 如："UploadFiles/myFilePath/log20090601.log"
	 * @return 
	 */
	public static File getFileByBusiness(String bizfield,String subPath) {
		return new File(getRealFileName(getAppFilesPath() + bizfield + "/"+subPath));
	}

	/**
	 * 将输入流的内容拷贝到输出流<br>
	 * 如来前台获得了一个流(输入流)：<br>
	 * ServletInputStream inputStream = request.getInputStream();<br>
	 * 有必要放入输出流，作为响应返回前台：<br>
	 * OutputStream outputStream = response.getOutputStream();<br>
	 * FileTools.copyStream(outputStream, inputStream);<br>
	 * 或,保存到文件：<br>
	 * OutputStream outputStream = new FileOutputStream(path);<br>
	 * FileTools.copyStream(outputStream, inputStream);<br>
	 * 
	 * @param outputStream 输出流
	 * @param inputStream 输入流
	 */
	public static void copyStream(OutputStream outputStream, InputStream inputStream) {
		/**
		int c;
		try {
			while ((c = inputStream.read()) != -1)
				outputStream.write(c);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		**/
		//~ 2010-02-26修改,大部分情况下，下面的方法要比上面的效率高
        try {
			IOUtils.copy(inputStream, outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获得保存各业务文件的路径，下级目录可以自行定义<br>
	 * 如，要保存一个异常日志文件<br>
	 * String path =
	 * FileTools.getAppFilesPathByBusiness(FileTools.EXCEPTION_FILE_PATH)+"myFilePath/log20090601.log";<br>
	 * 其中"myFilePath"就是你自己的下级目录，也可以定义更多级别，log20090601.log就是文件名<br>
	 * 
	 * 
	 * @param bizfield
	 *            业务划分的目录 如：<ui>
	 *            <li>FileTools.DATA_FILE_PATH</li>
	 *            <li>FileTools.DOC_FILE_PATH</li>
	 *            <li>FileTools.MEDIA_FILE_PATH</li>
	 *            <li>FileTools.EXCEPTION_FILE_PATH</li>
	 *            <li>FileTools.OTHER_FILE_PATH</li>
	 *            </ui>
	 * @return 目录路径
	 */
	public static String getAppFilesPathByBusiness(String bizfield) {
		return getAppFilesPath() + bizfield + "/";
	}

	/**
	 * 获得程序生成的文件存放的目录 如上传文件 动态抓取的文件等<br>
	 * 不向外开放，由getAppFilesPathByBusiness方法代替<br>
	 * 原理：先获得servlet上下文绝对路径，上塑5级，可以脱离服务器的限制
	 * 
	 * @return 目录路径
	 */
	private static String getAppFilesPath() {
		return FileUtil.getParentDirectory(
				WebappContextListener.servletContext.getRealPath("/"), 5)
				+ "appFiles/";
	}

	/**
	 * webapp的目录 path表示要获得的子目录
	 * 
	 * @param path
	 * @return 目录路径
	 */
	public static String getRealPath(String path) {
		return WebappContextListener.servletContext.getRealPath(path);
	}

	/**
	 * webapp的目录
	 * 
	 * @param path
	 * @return 目录路径
	 */
	public static String getRealPath() {
		return getRealPath("/");
	}

	/**
	 * 在rootPath下创建一个名为folderName的文件夹,已存在就不创建，rootPath不存在会先创建rootPath路径
	 * 
	 * @param rootPath
	 *            文件夹的路径
	 * @param folderName
	 *            文件夹的名字
	 * @return 新创建文件夹的目录
	 */
	public static String createFolder(String rootPath, String folderName) {
		try {
			folderName = folderName.replace("/", "");
			rootPath = getRealFolderPath(rootPath);
			File file = new File(rootPath);
			if (!file.exists()) {
				String newRootPath = rootPath.substring(0,
						rootPath.length() - 1);
				createFolder(rootPath
						.substring(0, newRootPath.lastIndexOf("/")), rootPath
						.substring(newRootPath.lastIndexOf("/")));
			}
			file = new File(rootPath + folderName);
			if (!file.exists())
				file.mkdir();
			return rootPath + folderName + (folderName.equals("") ? "" : "/");
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 创建一个名为folderName的文件夹,已存在就不创建，rootPath不存在会先创建rootPath路径
	 * 
	 * @param folderPath
	 *            要创建的文件夹的路径
	 * @return 新创建文件夹的目录
	 */
	public static String createFolder(String folderPath) {
		return createFolder(folderPath, "");
	}

	/**
	 * 给出上级目录的路径
	 * 
	 * @param folderPath
	 * @return 上级目录的路径
	 */
	public static String getParentDirectory(String folderPath) {
		File file = new File(getRealFolderPath(folderPath));
		file = file.getParentFile()==null?file:file.getParentFile();
		return getRealFolderPath(file.getPath());
	}

	/**
	 * 给出向上n级目录的路径
	 * 
	 * @param folderPath
	 * @return 向上n级目录的路径
	 */
	public static String getParentDirectory(String folderPath, int n) {
		File file = new File(getRealFolderPath(folderPath));
		while (n-- > 0) {
			file = file.getParentFile()==null?file:file.getParentFile();
		}
		return getRealFolderPath(file.getPath());
	}

	/**
	 * 删除指定文件或文件夹
	 * 
	 * @param filePath
	 *            文件或文件夹路径
	 */
	public static void deleteFile(String filePath) {
		try {
			File f = new File(filePath);// 定义文件路径
			if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
				if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
					f.delete();
				} else {// 若有则把文件放进数组，并判断是否有下级目录
					File delFile[] = f.listFiles();
					int i = f.listFiles().length;
					for (int j = 0; j < i; j++) {
						if (delFile[j].isDirectory()) {
							deleteFile(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
						}
						delFile[j].delete();// 删除文件
					}
				}
				deleteFile(filePath);// 递归调用
			} else if (f.exists()) {
				f.delete();
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 浏览目录（文件夹）
	 * 
	 * @param folderPath
	 *            目录的路径
	 * @param fileFilter 文件过滤器
	 * @return 该目录下所有文件列表
	 */
	public static File[] browseFolder(String folderPath,FileFilter fileFilter) {
		try {
			File f = new File(folderPath);// 定义文件路径
			if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
				if(fileFilter!=null)
					return f.listFiles(fileFilter);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return new File[] {};
	}

	/**
	 * 浏览目录（文件夹）
	 * 
	 * @param f 目录的路径
	 * @param fileFilter 文件过滤器
	 * 
	 * @return 该目录下所有文件列表
	 */
	public static File[] browseFolder(File f,FileFilter fileFilter) {
		try {
			if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
				if(fileFilter!=null)
				return f.listFiles(fileFilter);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return new File[] {};
	}

	/**
	 * 将目录路径转换为Linux与Windows都识别的格式 即转换成比较正规的路径字符串
	 * 
	 * @param path
	 *            原始路径
	 * @return 转换后的路径
	 */
	public static String getRealFolderPath(String path) {
		path = path.replace("\\", "/");
		while (path.contains("//")) {
			path = path.replace("//", "/");
		}
		if (!path.endsWith("/"))
			path += "/";
		return path;
	}

	/**
	 * 将文件路径转换为Linux与Windows都识别的格式 即转换成比较正规的路径字符串
	 * 
	 * @param path
	 *            原始路径
	 * @return 转换后的路径
	 */
	public static String getRealFileName(String path) {
		path = path.replace("\\", "/");
		while (path.contains("//")) {
			path = path.replace("//", "/");
		}
		return path;
	}
	
	/**
	 * 由文件名获得扩展名
	 * @param fileName 文件名
	 * @return 扩展名
	 */
	public static String getExtendName(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
	}

	/**
	 * 由文件获得扩展名
	 * @param file 文件
	 * @return 扩展名
	 */
	public static String getExtendName(File file) {
		if(file.isDirectory()) return "folder";
		return getExtendName(file.getName());
	}
	
	/**
	 * 追加文本到文本类型文件尾部（UTF-8编码）<br>
	 * 与createFile不一样，createFile是重新生成一个文件覆盖以前的文件
	 * 
	 * @param content
	 *            追加的文本内容
	 * @param rootPath
	 *            父目录
	 * @param fileName
	 *            文件名
	 */
	public static void appendContentToFile(String content, String rootPath,
			String fileName){
		String str = content;
		try {
			appendContentToFile(str.getBytes("UTF-8"), rootPath, fileName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建文本类型文件（UTF-8编码）<br>
	 * 如果已存在，就覆盖以前的内容
	 * 
	 * @param content
	 *            文本内容
	 * @param rootPath
	 *            目录
	 * @param fileName
	 *            文件名
	 */
	public static void createFile(String content, String rootPath,
			String fileName) {
		try {
			createFile(content.getBytes("UTF-8"), rootPath, fileName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建文本类型文件（UTF-8编码）
	 * 
	 * @param content
	 *            文本内容
	 * @param rootPath
	 *            目录
	 * @param fileFullName
	 *            文件名
	 */
	public static void createFile(String content, String fileFullName) {
		try {
			createFile(content.getBytes("UTF-8"), fileFullName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取文本类型文件（UTF-8编码）
	 * 
	 * @param rootPath
	 *            目录
	 * @param fileName
	 *            文件名
	 * @return 文件内容
	 */
	public static String readCharsFile(String rootPath, String fileName) {
		try {
			return new String(readBytesFile(rootPath, fileName), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 读取文本类型文件（UTF-8编码）
	 * 
	 * @param fileFullName
	 *            文件目录
	 * @return 文件内容
	 */
	public static String readCharsFile(String fileFullName) {
		try {
			return new String(readBytesFile(fileFullName), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 保存错误日志
	 * 
	 * @param exceptionLogContent
	 */
	public static void saveExceptionLog(String exceptionLogContent){
		String fileName = "exceptionLog-"+DateUtil.getDateStr("yyyyMMdd")+".log";
		appendContentToFile(exceptionLogContent, getAppFilesPathByBusiness(EXCEPTION_FILE_PATH)+"",fileName);
	}
	
	/**
	 * 追加二进制到二进制类型文件尾部<br>
	 * 与createFile不一样，createFile是重新生成一个文件覆盖以前的文件
	 * 
	 * @param content
	 *            追加的二进制内容
	 * @param rootPath
	 *            父目录
	 * @param fileName
	 *            文件名
	 */
	public static void appendContentToFile(byte[] content, String rootPath,
			String fileName){
		File file = null;
		RandomAccessFile randomAccessFile = null;
		try {
			fileName = fileName.replace("/", "");
			rootPath = getRealFolderPath(rootPath);
			createFolder(rootPath);
			String filePath = rootPath + fileName;
			file = new File(filePath);
			randomAccessFile = new RandomAccessFile(filePath, "rw");
			if (content != null) {
				if(file.exists()){
					randomAccessFile.seek(randomAccessFile.length());
				}else{
					randomAccessFile.setLength(0);
				}
				randomAccessFile.write(content);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (randomAccessFile != null) {
				try {
					randomAccessFile.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * 创建二进制类型文件<br>
	 * 如果已存在，就覆盖以前的内容
	 * 
	 * @param content
	 *            二进制内容
	 * @param rootPath
	 *            目录
	 * @param fileName
	 *            文件名
	 */
	public static void createFile(byte[] content, String rootPath,
			String fileName) {
		RandomAccessFile randomAccessFile = null;
		try {
			fileName = fileName.replace("/", "");
			rootPath = getRealFolderPath(rootPath);
			createFolder(rootPath);
			String filePath = rootPath + fileName;
			deleteFile(filePath);
			randomAccessFile = new RandomAccessFile(filePath, "rw");
			randomAccessFile.setLength(0);
			if (content != null) {
				randomAccessFile.write(content);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (randomAccessFile != null) {
				try {
					randomAccessFile.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * 创建二进制类型文件
	 * 
	 * @param content
	 *            二进制内容
	 * @param rootPath
	 *            目录
	 * @param fileFullName
	 *            文件名
	 */
	public static void createFile(byte[] content, String fileFullName) {
		fileFullName = getRealFileName(fileFullName);
		String rootPath = fileFullName.substring(0, fileFullName
				.lastIndexOf('/'));
		fileFullName = fileFullName.substring(fileFullName.lastIndexOf('/'));
		createFile(content, rootPath, fileFullName);
	}

	/**
	 * 读取二进制类型文件
	 * 
	 * @param rootPath
	 *            目录
	 * @param fileName
	 *            文件名
	 * @return 文件内容
	 */
	public static byte[] readBytesFile(String rootPath, String fileName) {
		File file = null;
		FileInputStream fis = null;
		try {
			fileName = fileName.replace("/", "");
			rootPath = getRealFolderPath(rootPath);
			String filePath = rootPath + fileName;
			file = new File(filePath);
			if (file.exists()) {
				fis = new FileInputStream(file);
				byte[] bytes = readInputStream(fis);
				return bytes;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			/**
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			**/
			IOUtils.closeQuietly(fis);
		}
		return new byte[0];
	}

	/**
	 * 读取输入流
	 * 
	 * @param is 输入流
	 * @return 二进制数据
	 */
	public static byte[] readInputStream(InputStream inputStream){
		/**
		List<Byte> bytes = new ArrayList<Byte>();
		try {
			int c;
			while ((c = inputStream.read()) != -1){
				bytes.add((byte)c);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] bytesResult = new byte[bytes.size()];
		for (int i = 0; i < bytesResult.length; i++) {
			bytesResult[i] = bytes.get(i);
		}
		return bytesResult;
		**/
		//~ 2010-02-26修改
		byte[] bytesResult = null;
		try {
			bytesResult = IOUtils.toByteArray(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytesResult;
	}

	/**
	 * 读取二进制类型文件<br>
	 * 返回结果是一个byte[]类型，可以转换为String
	 * 
	 * @param fileFullName
	 *            文件目录
	 * @return 文件内容
	 */
	public static byte[] readBytesFile(String fileFullName) {
		File file = null;
		FileInputStream fis = null;
		try {
			fileFullName = getRealFileName(fileFullName);
			file = new File(fileFullName);
			if (file.exists()) {
				fis = new FileInputStream(file);
				byte[] bytes = readInputStream(fis);
				return bytes;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			/**
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}**/
			IOUtils.closeQuietly(fis);
		}
		return new byte[0];
	}

//	public static void main(String[] args) {
//		createFile("你好", "d:\\dd\\fds\\fds\\fds\\fds\\fds\\", "data.txt");
//		System.out.println(readCharsFile("d:\\dd\\fds\\fds\\fds\\fds\\fds\\","data.txt"));
//		appendContentToFile("你好", "d:\\dd\\fds\\fds\\fds\\fds\\fds\\", "data.txt");
//		System.out.println(readCharsFile("E:\\xvxv\\Function\\dataaccess\\src\\main\\java\\cn\\rtdata\\dataaccess\\util\\file\\FileTools.java"));
//		byte[] content = readBytesFile("d:\\dd\\fds\\fds\\fds\\fds\\fds\\", "img.jpg");
//		createFile(content, "d:\\dd\\\\fds\\fds\\fds\\fds\\fds\\", "img2.jpg");
//		appendContentToFile(content, "d:\\dd\\\\fds\\fds\\fds\\fds\\fds\\", "img2.jpg");
//		String a = "E:\\xvxv\\Fck22\\build";
//		System.out.println(getParentDirectory(a, 8));
//		try {
//			FileInputStream inputStream = new FileInputStream("D:\\test\\file1.zip");
//			FileOutputStream outputStream = new FileOutputStream("D:\\test\\file2.zip");
//			long st = new Date().getTime();
//			copyStream(outputStream, inputStream);
//			System.out.println(new Date().getTime()-st);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//	}
}
