package com.hongqiang.shop.common.utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Comment;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class ExcelView extends AbstractExcelView {
	private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private String filename;
	private String sheetName;
	private String[] properties;
	private String[] titles;
	private Integer[] widths;
	private Converter[] converters;
	private Collection<?> data;
	private String[] contents;

	static {
		DateConverter localDateConverter = new DateConverter();
		localDateConverter.setPattern(DATE_PATTERN);
		ConvertUtils.register(localDateConverter, Date.class);
	}

	public ExcelView(String filename, String sheetName, String[] properties,
			String[] titles, Integer[] widths, Converter[] converters,
			Collection<?> data, String[] contents) {
		this.filename = filename;
		this.sheetName = sheetName;
		this.properties = properties;
		this.titles = titles;
		this.widths = widths;
		this.converters = converters;
		this.data = data;
		this.contents = contents;
	}

	public ExcelView(String[] properties, String[] titles, Collection<?> data,
			String[] contents) {
		this.properties = properties;
		this.titles = titles;
		this.data = data;
		this.contents = contents;
	}

	public ExcelView(String[] properties, String[] titles, Collection<?> data) {
		this.properties = properties;
		this.titles = titles;
		this.data = data;
	}

	public ExcelView(String[] properties, Collection<?> data) {
		this.properties = properties;
		this.data = data;
	}

	public void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		Assert.notEmpty(this.properties);
		HSSFSheet localHSSFSheet;
		if (StringUtils.isNotEmpty(this.sheetName))
			localHSSFSheet = workbook.createSheet(this.sheetName);
		else
			localHSSFSheet = workbook.createSheet();
		int i = 0;
		Object localObject1;
		Object localObject2;
		Object localObject3;
		Object localObject4;
		Object localObject5;
		if ((this.titles != null) && (this.titles.length > 0)) {
			localObject1 = localHSSFSheet.createRow(i);
			((HSSFRow) localObject1).setHeight((short) 400);
			for (int j = 0; j < this.properties.length; j++) {
				localObject2 = ((HSSFRow) localObject1).createCell(j);
				HSSFCellStyle localHSSFCellStyle = workbook.createCellStyle();
				localHSSFCellStyle.setFillForegroundColor((short) 31);
				localHSSFCellStyle.setFillPattern((short) 1);
				localHSSFCellStyle.setAlignment((short) 2);
				localHSSFCellStyle.setVerticalAlignment((short) 1);
				localObject3 = workbook.createFont();
				((HSSFFont) localObject3).setFontHeightInPoints((short) 11);
				((HSSFFont) localObject3).setBoldweight((short) 700);
				localHSSFCellStyle.setFont((HSSFFont) localObject3);
				((HSSFCell) localObject2).setCellStyle(localHSSFCellStyle);
				if (j == 0) {
					localObject4 = localHSSFSheet.createDrawingPatriarch();
					localObject5 = ((HSSFPatriarch) localObject4)
							.createComment(new HSSFClientAnchor((short) 0,
									(short) 0, (short) 0, (short) 0, (short) 1,
									(short) 1, (short) 4, (short) 4));
					((HSSFComment) localObject5)
							.setString(new HSSFRichTextString(
									"Powered By SHOP-HQ"));
					((HSSFCell) localObject2)
							.setCellComment((Comment) localObject5);
				}
				if ((this.titles.length > j) && (this.titles[j] != null))
					((HSSFCell) localObject2).setCellValue(this.titles[j]);
				else
					((HSSFCell) localObject2).setCellValue(this.properties[j]);
				if ((this.widths != null) && (this.widths.length > j)
						&& (this.widths[j] != null))
					localHSSFSheet.setColumnWidth(j, this.widths[j].intValue());
				else
					localHSSFSheet.autoSizeColumn(j);
			}
			i++;
		}
		if (this.data != null) {
			Iterator<?> localIterator = this.data.iterator();
			while (localIterator.hasNext()) {
				localObject1 = localIterator.next();
				localObject2 = localHSSFSheet.createRow(i);
				for (int n = 0; n < this.properties.length; n++) {
					localObject3 = ((HSSFRow) localObject2).createCell(n);
					if ((this.converters != null)
							&& (this.converters.length > n)
							&& (this.converters[n] != null)) {
						localObject4 = PropertyUtils.getPropertyType(
								localObject1, this.properties[n]);
						ConvertUtils.register(this.converters[n],
								(Class<?>) localObject4);
						((HSSFCell) localObject3).setCellValue(BeanUtils
								.getProperty(localObject1, this.properties[n]));
						ConvertUtils.deregister((Class<?>) localObject4);
						if (localObject4.equals(Date.class)) {
							localObject5 = new DateConverter();
							((DateConverter) localObject5)
									.setPattern(DATE_PATTERN);
							ConvertUtils.register((Converter) localObject5,
									Date.class);
						}
					} else {
						((HSSFCell) localObject3).setCellValue(BeanUtils
								.getProperty(localObject1, this.properties[n]));
					}
					if ((i != 0) && (i != 1))
						continue;
					if ((this.widths != null) && (this.widths.length > n)
							&& (this.widths[n] != null))
						localHSSFSheet.setColumnWidth(n,
								this.widths[n].intValue());
					else
						localHSSFSheet.autoSizeColumn(n);
				}
				i++;
			}
		}
		if ((this.contents != null) && (this.contents.length > 0)) {
			i++;
			for (String localString : this.contents) {
				localObject3 = localHSSFSheet.createRow(i);
				localObject4 = ((HSSFRow) localObject3).createCell(0);
				localObject5 = workbook.createCellStyle();
				HSSFFont localHSSFFont = workbook.createFont();
				localHSSFFont.setColor((short) 23);
				((HSSFCellStyle) localObject5).setFont(localHSSFFont);
				((HSSFCell) localObject4)
						.setCellStyle((HSSFCellStyle) localObject5);
				((HSSFCell) localObject4).setCellValue(localString);
				i++;
			}
		}
		response.setContentType("application/force-download");
		if (StringUtils.isNotEmpty(this.filename))
			try {
				response.setHeader(
						"Content-disposition",
						"attachment; filename="
								+ URLEncoder.encode(this.filename, "UTF-8"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		else
			response.setHeader("Content-disposition", "attachment");
	}

	public String getFileName() {
		return this.filename;
	}

	public void setFileName(String filename) {
		this.filename = filename;
	}

	public String getSheetName() {
		return this.sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String[] getProperties() {
		return this.properties;
	}

	public void setProperties(String[] properties) {
		this.properties = properties;
	}

	public String[] getTitles() {
		return this.titles;
	}

	public void setTitles(String[] titles) {
		this.titles = titles;
	}

	public Integer[] getWidths() {
		return this.widths;
	}

	public void setWidths(Integer[] widths) {
		this.widths = widths;
	}

	public Converter[] getConverters() {
		return this.converters;
	}

	public void setConverters(Converter[] converters) {
		this.converters = converters;
	}

	public Collection<?> getData() {
		return this.data;
	}

	public void setData(Collection<?> data) {
		this.data = data;
	}

	public String[] getContents() {
		return this.contents;
	}

	public void setContents(String[] contents) {
		this.contents = contents;
	}
}