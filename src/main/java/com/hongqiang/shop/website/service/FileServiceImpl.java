package com.hongqiang.shop.website.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

import com.hongqiang.shop.common.utils.FreeMarkers;
import com.hongqiang.shop.common.utils.Setting;
import com.hongqiang.shop.common.utils.SettingUtils;
import com.hongqiang.shop.common.utils.plugin.StoragePlugin;
import com.hongqiang.shop.common.utils.plugin.service.PluginService;
import com.hongqiang.shop.website.entity.FileInfo;

@Service("fileServiceImpl")
public class FileServiceImpl implements FileService, ServletContextAware {

	class UpLoadAsync implements Runnable {
		private String path;
		private File file;
		private String contentType;
		private StoragePlugin storagePlugin;

		public UpLoadAsync(File file, StoragePlugin storagePlugin, String path,
				String contentType) {
			this.path = path;
			this.file = file;
			this.contentType = contentType;
			this.storagePlugin = storagePlugin;
		}

		public void run() {
			try {
				this.storagePlugin.upload(this.path, this.file,
						this.contentType);
			} finally {
				FileUtils.deleteQuietly(this.file);
			}
		}
	}

	class SizeComparator implements Comparator<FileInfo> {

		private SizeComparator(FileServiceImpl paramFileServiceImpl) {
		}

		public int compare(FileInfo fileInfos1, FileInfo fileInfos2) {
			return new CompareToBuilder()
					.append(!fileInfos1.getIsDirectory().booleanValue(),
							!fileInfos2.getIsDirectory().booleanValue())
					.append(fileInfos1.getSize(), fileInfos2.getSize())
					.toComparison();
		}
	}

	class TypeComparator implements Comparator<FileInfo> {
		private TypeComparator(FileServiceImpl paramFileServiceImpl) {
		}

		public int compare(FileInfo fileInfos1, FileInfo fileInfos2) {
			return new CompareToBuilder()
					.append(!fileInfos1.getIsDirectory().booleanValue(),
							!fileInfos2.getIsDirectory().booleanValue())
					.append(FilenameUtils.getExtension(fileInfos1.getName()),
							FilenameUtils.getExtension(fileInfos2.getName()))
					.toComparison();
		}
	}

	class NameComparator implements Comparator<FileInfo> {
		private NameComparator(FileServiceImpl paramFileServiceImpl) {
		}

		public int compare(FileInfo fileInfos1, FileInfo fileInfos2) {
			return new CompareToBuilder()
					.append(!fileInfos1.getIsDirectory().booleanValue(),
							!fileInfos2.getIsDirectory().booleanValue())
					.append(fileInfos1.getName(), fileInfos2.getName())
					.toComparison();
		}
	}

	private ServletContext servletContext;

	@Autowired
	private TaskExecutor taskExecutor;

	@Autowired
	private PluginService pluginService;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	private void asyncLoad(StoragePlugin storagePlugin, String path, File file,
			String contentType) {
		this.taskExecutor.execute(new UpLoadAsync(file, storagePlugin, path,
				contentType));
	}

	public boolean isValid(FileInfo.FileType fileType,
			MultipartFile multipartFile) {
		if (multipartFile == null)
			return false;
		Setting localSetting = SettingUtils.get();
		if ((localSetting.getUploadMaxSize() != null)
				&& (localSetting.getUploadMaxSize().intValue() != 0)
				&& (multipartFile.getSize() > localSetting.getUploadMaxSize()
						.intValue() * 1024L * 1024L))
			return false;
		String[] arrayOfString;
		if (fileType == FileInfo.FileType.flash)
			arrayOfString = localSetting.getUploadFlashExtensions();
		else if (fileType == FileInfo.FileType.media)
			arrayOfString = localSetting.getUploadMediaExtensions();
		else if (fileType == FileInfo.FileType.file)
			arrayOfString = localSetting.getUploadFileExtensions();
		else
			arrayOfString = localSetting.getUploadImageExtensions();
		if (!ArrayUtils.isEmpty(arrayOfString))
			return FilenameUtils.isExtension(
					multipartFile.getOriginalFilename(), arrayOfString);
		return false;
	}

	public String upload(FileInfo.FileType fileType,
			MultipartFile multipartFile, boolean async) {
		if (multipartFile == null)
			return null;
		Setting localSetting = SettingUtils.get();
		String str1;
		if (fileType == FileInfo.FileType.flash)
			str1 = localSetting.getFlashUploadPath();
		else if (fileType == FileInfo.FileType.media)
			str1 = localSetting.getMediaUploadPath();
		else if (fileType == FileInfo.FileType.file)
			str1 = localSetting.getFileUploadPath();
		else
			str1 = localSetting.getImageUploadPath();
		try {
			HashMap<String, String> localHashMap = new HashMap<String, String>();
			localHashMap.put("uuid", UUID.randomUUID().toString());
			String str2 = FreeMarkers.renderString(str1, localHashMap);
			String str3 = str2
					+ UUID.randomUUID()
					+ "."
					+ FilenameUtils.getExtension(multipartFile
							.getOriginalFilename());
			Iterator<StoragePlugin> localIterator = this.pluginService
					.getStoragePlugins(true).iterator();
			if (localIterator.hasNext()) {
				StoragePlugin localStoragePlugin = (StoragePlugin) localIterator
						.next();
				File localFile = new File(System.getProperty("java.io.tmpdir")
						+ "/upload_" + UUID.randomUUID() + ".tmp");
				if (!localFile.getParentFile().exists())
					localFile.getParentFile().mkdirs();
				multipartFile.transferTo(localFile);
				if (async)
					asyncLoad(localStoragePlugin, str3, localFile,
							multipartFile.getContentType());
				else
					try {
						localStoragePlugin.upload(str3, localFile,
								multipartFile.getContentType());
					} finally {
						FileUtils.deleteQuietly(localFile);
					}
				return localStoragePlugin.getUrl(str3);
			}
		} catch (Exception localException1) {
			localException1.printStackTrace();
		}
		return null;
	}

	public String upload(FileInfo.FileType fileType, MultipartFile multipartFile) {
		return upload(fileType, multipartFile, false);
	}

	public String uploadLocal(FileInfo.FileType fileType,
			MultipartFile multipartFile) {
		if (multipartFile == null)
			return null;
		Setting localSetting = SettingUtils.get();
		String str1;
		if (fileType == FileInfo.FileType.flash)
			str1 = localSetting.getFlashUploadPath();
		else if (fileType == FileInfo.FileType.media)
			str1 = localSetting.getMediaUploadPath();
		else if (fileType == FileInfo.FileType.file)
			str1 = localSetting.getFileUploadPath();
		else
			str1 = localSetting.getImageUploadPath();
		try {
			HashMap<String, String> localHashMap = new HashMap<String, String>();
			localHashMap.put("uuid", UUID.randomUUID().toString());
			String str2 = FreeMarkers.renderString(str1, localHashMap);
			String str3 = str2
					+ UUID.randomUUID()
					+ "."
					+ FilenameUtils.getExtension(multipartFile
							.getOriginalFilename());
			File localFile = new File(this.servletContext.getRealPath(str3));
			if (!localFile.getParentFile().exists())
				localFile.getParentFile().mkdirs();
			multipartFile.transferTo(localFile);
			return str3;
		} catch (Exception localException1) {
			localException1.printStackTrace();
		}
		return null;
	}

	public List<FileInfo> browser(String path, FileInfo.FileType fileType,
			FileInfo.OrderType orderType) {
		if (path != null) {
			if (!path.startsWith("/"))
				path = "/" + path;
			if (!path.endsWith("/"))
				path = path + "/";
		} else {
			path = "/";
		}
		Setting localSetting = SettingUtils.get();
		String str1;
		if (fileType == FileInfo.FileType.flash)
			str1 = localSetting.getFlashUploadPath();
		else if (fileType == FileInfo.FileType.media)
			str1 = localSetting.getMediaUploadPath();
		else if (fileType == FileInfo.FileType.file)
			str1 = localSetting.getFileUploadPath();
		else
			str1 = localSetting.getImageUploadPath();
		String str2 = StringUtils.substringBefore(str1, "${");
		str2 = StringUtils.substringBeforeLast(str2, "/") + path;
		List<FileInfo> files = new ArrayList<FileInfo>();
		if (str2.indexOf("..") >= 0)
			return files;
		Iterator<StoragePlugin> localIterator = this.pluginService
				.getStoragePlugins(true).iterator();
		if (localIterator.hasNext()) {
			StoragePlugin localStoragePlugin = (StoragePlugin) localIterator
					.next();
			files = localStoragePlugin.browser(str2);
		}
		if (orderType == FileInfo.OrderType.size)
			Collections.sort(files, new SizeComparator(this));
		else if (orderType == FileInfo.OrderType.type)
			Collections.sort(files, new TypeComparator(this));
		else
			Collections.sort(files, new NameComparator(this));
		return files;
	}
}