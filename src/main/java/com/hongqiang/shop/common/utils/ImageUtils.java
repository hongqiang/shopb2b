package com.hongqiang.shop.common.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.Assert;

public final class ImageUtils {
	private static final Color color = Color.white;

	public static void zoom(File srcFile, File destFile, int destWidth,
			int destHeight) {
		Assert.notNull(srcFile);
		Assert.notNull(destFile);
		Assert.state(destWidth > 0);
		Assert.state(destHeight > 0);

		Graphics2D graphics2D = null;
		ImageOutputStream imageOutputStream = null;
		ImageWriter localImageWriter = null;
		try {
			BufferedImage localBufferedImage1 = ImageIO.read(srcFile);
			int i = localBufferedImage1.getWidth();
			int j = localBufferedImage1.getHeight();
			int k = destWidth;
			int m = destHeight;
			if (j >= i)
				k = (int) Math.round(destHeight * 1.0D / j * i);
			else
				m = (int) Math.round(destWidth * 1.0D / i * j);
			BufferedImage localBufferedImage2 = new BufferedImage(destWidth,
					destHeight, 1);
			graphics2D = localBufferedImage2.createGraphics();
			graphics2D.setBackground(color);
			graphics2D.clearRect(0, 0, destWidth, destHeight);
			graphics2D.drawImage(
					localBufferedImage1.getScaledInstance(k, m, 4), destWidth
							/ 2 - k / 2, destHeight / 2 - m / 2, null);
			imageOutputStream = ImageIO.createImageOutputStream(destFile);
			localImageWriter = (ImageWriter) ImageIO
					.getImageWritersByFormatName(
							FilenameUtils.getExtension(destFile.getName()))
					.next();
			localImageWriter.setOutput(imageOutputStream);
			ImageWriteParam localImageWriteParam = localImageWriter
					.getDefaultWriteParam();
			localImageWriteParam.setCompressionMode(2);
			localImageWriteParam.setCompressionQuality(0.88F);
			localImageWriter.write(null, new IIOImage(localBufferedImage2,
					null, null), localImageWriteParam);
			imageOutputStream.flush();
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
			if (graphics2D != null)
				graphics2D.dispose();
			if (localImageWriter != null)
				localImageWriter.dispose();
			if (imageOutputStream == null)
				return;
			try {
				imageOutputStream.close();
			} catch (IOException localIOException1) {
			}
		} finally {
			if (graphics2D != null)
				graphics2D.dispose();
			if (localImageWriter != null)
				localImageWriter.dispose();
			if (imageOutputStream != null)
				try {
					imageOutputStream.close();
				} catch (IOException localIOException2) {
				}
		}
		try {
			imageOutputStream.close();
		} catch (IOException localIOException3) {
		}
	}

	public static void addWatermark(File srcFile, File destFile,
			File watermarkFile, Setting.WatermarkPosition watermarkPosition,
			int alpha) {
		Assert.notNull(srcFile);
		Assert.notNull(destFile);
		Assert.state(alpha >= 0);
		Assert.state(alpha <= 100);
		if ((watermarkFile == null) || (!watermarkFile.exists())
				|| (watermarkPosition == null)
				|| (watermarkPosition == Setting.WatermarkPosition.no)) {
			try {
				FileUtils.copyFile(srcFile, destFile);
			} catch (IOException localIOException) {
				localIOException.printStackTrace();
			}
			return;
		}
		Graphics2D graphics2D = null;
		ImageOutputStream imageOutputStream = null;
		ImageWriter imageWriter = null;
		try {
			BufferedImage localBufferedImage1 = ImageIO.read(srcFile);
			int i = localBufferedImage1.getWidth();
			int j = localBufferedImage1.getHeight();
			BufferedImage localBufferedImage2 = new BufferedImage(i, j, 1);
			graphics2D = localBufferedImage2.createGraphics();
			graphics2D.setBackground(color);
			graphics2D.clearRect(0, 0, i, j);
			graphics2D.drawImage(localBufferedImage1, 0, 0, null);
			graphics2D.setComposite(AlphaComposite.getInstance(10,
					alpha / 100.0F));
			BufferedImage localBufferedImage3 = ImageIO.read(watermarkFile);
			int k = localBufferedImage3.getWidth();
			int m = localBufferedImage3.getHeight();
			int n = i - k;
			int i1 = j - m;
			if (watermarkPosition == Setting.WatermarkPosition.topLeft) {
				n = 0;
				i1 = 0;
			} else if (watermarkPosition == Setting.WatermarkPosition.topRight) {
				n = i - k;
				i1 = 0;
			} else if (watermarkPosition == Setting.WatermarkPosition.center) {
				n = (i - k) / 2;
				i1 = (j - m) / 2;
			} else if (watermarkPosition == Setting.WatermarkPosition.bottomLeft) {
				n = 0;
				i1 = j - m;
			} else if (watermarkPosition == Setting.WatermarkPosition.bottomRight) {
				n = i - k;
				i1 = j - m;
			}
			graphics2D.drawImage(localBufferedImage3, n, i1, k, m, null);
			imageOutputStream = ImageIO.createImageOutputStream(destFile);
			imageWriter = (ImageWriter) ImageIO.getImageWritersByFormatName(
					FilenameUtils.getExtension(destFile.getName())).next();
			imageWriter.setOutput(imageOutputStream);
			ImageWriteParam localImageWriteParam = imageWriter
					.getDefaultWriteParam();
			localImageWriteParam.setCompressionMode(2);
			localImageWriteParam.setCompressionQuality(0.88F);
			imageWriter.write(null, new IIOImage(localBufferedImage2, null,
					null), localImageWriteParam);
			imageOutputStream.flush();
		} catch (IOException localIOException7) {
			localIOException7.printStackTrace();
			if (graphics2D != null)
				graphics2D.dispose();
			if (imageWriter != null)
				imageWriter.dispose();
			if (imageOutputStream == null)
				return;
			try {
				imageOutputStream.close();
			} catch (IOException localIOException2) {
			}
		} finally {
			if (graphics2D != null)
				graphics2D.dispose();
			if (imageWriter != null)
				imageWriter.dispose();
			if (imageOutputStream != null)
				try {
					imageOutputStream.close();
				} catch (IOException localIOException3) {
				}
		}
		try {
			imageOutputStream.close();
		} catch (IOException localIOException4) {
		}
	}

	public static void initialize() {
	}
}