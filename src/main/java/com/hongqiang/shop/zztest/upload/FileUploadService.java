package com.hongqiang.shop.zztest.upload;

import java.io.File;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hongqiang.shop.common.utils.ImageUtils;
import com.hongqiang.shop.modules.entity.ProductImage;

public class FileUploadService {

	private static final String jpgImage = "jpg";
	private ServletContext servletContext;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public ServletContext getServletContext() {
		return this.servletContext;
	}

	public void build(ProductImage productImage) {
		MultipartFile localMultipartFile = productImage.getFile();// 得到图片文件
		if ((localMultipartFile != null) && (!localMultipartFile.isEmpty())) {
			String str2 = UUID.randomUUID().toString();
			String sourceName = str2
					+ "-source."
					+ FilenameUtils.getExtension(localMultipartFile
							.getOriginalFilename());
			String largeName = str2 + "-large." + jpgImage;
			String str = System.getProperty("java.io.tmpdir");
			File localFile = new File(str + "/upload_" + UUID.randomUUID()+ ".tmp");
			if (!localFile.getParentFile().exists())
				localFile.getParentFile().mkdirs();
			// Transfer the received file to the given destination file.
			int width = 100;
			int height = 200;
			File localFile2 = new File(str + "/upload_" + UUID.randomUUID()+ "." + jpgImage);
			try {
				localMultipartFile.transferTo(localFile);
				ImageUtils.zoom(localFile, localFile2, width, height);
				File oriFile = new File(
						this.servletContext.getRealPath(sourceName));
				FileUtils.moveFile(localFile, oriFile);

				File localFileChange = new File(
						this.servletContext.getRealPath(largeName));
				FileUtils.moveFile(localFile2, localFileChange);
			} catch (Exception localException1) {
				localException1.printStackTrace();
			} finally {
				FileUtils.deleteQuietly(localFile);// 原始图像
				FileUtils.deleteQuietly(localFile2);
			}
		}
	}
}
