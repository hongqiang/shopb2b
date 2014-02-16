package com.hongqiang.shop.modules.product.service;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

import com.hongqiang.shop.common.utils.FreeMarkers;
import com.hongqiang.shop.common.utils.ImageUtils;
import com.hongqiang.shop.common.utils.Setting;
import com.hongqiang.shop.common.utils.SettingUtils;
import com.hongqiang.shop.common.utils.plugin.StoragePlugin;
import com.hongqiang.shop.modules.entity.ProductImage;

@Service("productImageServiceImpl")
public class ProductImageServiceImpl implements ProductImageService,
		ServletContextAware {
	class ImageInnerServiceImpl implements Runnable {
		private String sourceFilePath;
		private String largeFilePath;
		private String mediumFilePath;
		private String smallFilePath;
		private String contentType;
		private File sourceFile;
		private ProductImageServiceImpl productImageService;

		public ImageInnerServiceImpl(
				ProductImageServiceImpl productImageService, File sourceFile,
				String sourceFilePath, String contentType,
				String largeFilePath, String mediumFilePath,
				String smallFilePath) {
			this.productImageService = productImageService;
			this.largeFilePath = largeFilePath;
			this.sourceFilePath = sourceFilePath;
			this.mediumFilePath = mediumFilePath;
			this.smallFilePath = smallFilePath;
			this.contentType = contentType;
			this.sourceFile = sourceFile;
		}

		public void run() {
			Collections.sort(productImageService.storagePluginList);
			Iterator<StoragePlugin> localIterator = productImageService.storagePluginList
					.iterator();
			while (localIterator.hasNext()) {
				StoragePlugin localStoragePlugin = (StoragePlugin) localIterator
						.next();
				if (!localStoragePlugin.getIsEnabled())
					continue;
				Setting localSetting = SettingUtils.get();
				String str = System.getProperty("java.io.tmpdir");
				File localFile1 = new File(
						productImageService.servletContext
								.getRealPath(localSetting.getWatermarkImage()));// 得到水印图片文件
				File localFile2 = new File(str + "/upload_" + UUID.randomUUID()
						+ "." + jpgImage);
				File localFile3 = new File(str + "/upload_" + UUID.randomUUID()
						+ "." + jpgImage);
				File localFile4 = new File(str + "/upload_" + UUID.randomUUID()
						+ "." + jpgImage);
				File localFile2AddWater =  new File(str + "/upload_" + UUID.randomUUID()
						+ "." + jpgImage);
				File localFile3AddWater =  new File(str + "/upload_" + UUID.randomUUID()
						+ "." + jpgImage);
				try {
					ImageUtils.zoom(this.sourceFile, localFile2, localSetting
							.getLargeProductImageWidth().intValue(),
							localSetting.getLargeProductImageHeight()
									.intValue());
					ImageUtils.addWatermark(localFile2, localFile2AddWater, localFile1,
							localSetting.getWatermarkPosition(), localSetting
									.getWatermarkAlpha().intValue());
					ImageUtils.zoom(this.sourceFile, localFile3, localSetting
							.getMediumProductImageWidth().intValue(),
							localSetting.getMediumProductImageHeight()
									.intValue());
					ImageUtils.addWatermark(localFile3, localFile3AddWater, localFile1,
							localSetting.getWatermarkPosition(), localSetting
									.getWatermarkAlpha().intValue());
					ImageUtils.zoom(this.sourceFile, localFile4, localSetting
							.getThumbnailProductImageWidth().intValue(),
							localSetting.getThumbnailProductImageHeight()
									.intValue());

					localStoragePlugin.upload(this.sourceFilePath,
							this.sourceFile, this.contentType);// 原始图像的路径
					localStoragePlugin.upload(this.largeFilePath, localFile2AddWater,
							jpegImage);// localFile2的路径
					localStoragePlugin.upload(this.mediumFilePath, localFile3AddWater,
							jpegImage);// localFile3的路径
					localStoragePlugin.upload(this.smallFilePath, localFile4,
							jpegImage);// localFile4的路径
				} finally {
					FileUtils.deleteQuietly(this.sourceFile);// 原始图像
					FileUtils.deleteQuietly(localFile2AddWater);
					FileUtils.deleteQuietly(localFile3AddWater);
					FileUtils.deleteQuietly(localFile4);
				}
				break;
			}
		}
	}

	private static final String jpgImage = "jpg";
	private static final String jpegImage = "image/jpeg";
	private ServletContext servletContext;

	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;

	@Resource
	private List<StoragePlugin> storagePluginList;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public ServletContext getServletContext() {
		return this.servletContext;
	}

	private void storeImages(String paramString1, String paramString2,
			String paramString3, String paramString4, File paramFile,
			String paramString5) {
		try {
			this.taskExecutor.execute(new ImageInnerServiceImpl(this,
					paramFile, paramString1, paramString5, paramString2,
					paramString3, paramString4));
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	public void build(ProductImage productImage) {
		MultipartFile localMultipartFile = productImage.getFile();
		if ((localMultipartFile != null) && (!localMultipartFile.isEmpty()))
			try {
				Setting localSetting = SettingUtils.get();
				HashMap<String, String> localHashMap = new HashMap<String, String>();
				localHashMap.put("uuid", UUID.randomUUID().toString());
				String str1 = FreeMarkers.renderString(
						localSetting.getImageUploadPath(), localHashMap);
				String str2 = UUID.randomUUID().toString();
				String str3 = str1
						+ str2
						+ "-source."
						+ FilenameUtils.getExtension(localMultipartFile
								.getOriginalFilename());
				String str4 = str1 + str2 + "-large." + jpgImage;
				String str5 = str1 + str2 + "-medium." + jpgImage;
				String str6 = str1 + str2 + "-thumbnail." + jpgImage;
				Collections.sort(this.storagePluginList);
				Iterator<StoragePlugin> localIterator = this.storagePluginList
						.iterator();
				while (localIterator.hasNext()) {
					StoragePlugin localStoragePlugin = (StoragePlugin) localIterator
							.next();
					if (!localStoragePlugin.getIsEnabled())
						continue;
					File localFile = new File(
							System.getProperty("java.io.tmpdir") + "/upload_"
									+ UUID.randomUUID() + ".tmp");
					if (!localFile.getParentFile().exists())
						localFile.getParentFile().mkdirs();
					// Transfer the received file to the given destination file.
					localMultipartFile.transferTo(localFile);
					storeImages(str3, str4, str5, str6, localFile,
							localMultipartFile.getContentType());
					productImage.setSource(localStoragePlugin.getUrl(str3));
					productImage.setLarge(localStoragePlugin.getUrl(str4));
					productImage.setMedium(localStoragePlugin.getUrl(str5));
					productImage.setThumbnail(localStoragePlugin.getUrl(str6));
				}
			} catch (Exception localException1) {
				localException1.printStackTrace();
			}
	}
}