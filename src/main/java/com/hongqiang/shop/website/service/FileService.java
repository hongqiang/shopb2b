package com.hongqiang.shop.website.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hongqiang.shop.website.entity.FileInfo;

public abstract interface FileService {
	public abstract boolean isValid(FileInfo.FileType paramFileType,
			MultipartFile paramMultipartFile);

	public abstract String upload(FileInfo.FileType paramFileType,
			MultipartFile paramMultipartFile, boolean paramBoolean);

	public abstract String upload(FileInfo.FileType paramFileType,
			MultipartFile paramMultipartFile);

	public abstract String uploadLocal(FileInfo.FileType paramFileType,
			MultipartFile paramMultipartFile);

	public abstract List<FileInfo> browser(String paramString,
			FileInfo.FileType paramFileType, FileInfo.OrderType paramOrderType);
}