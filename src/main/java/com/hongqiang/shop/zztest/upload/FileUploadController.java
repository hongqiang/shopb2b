package com.hongqiang.shop.zztest.upload;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.hongqiang.shop.modules.entity.ProductImage;
import com.hongqiang.shop.modules.product.service.ProductImageService;

@Controller
public class FileUploadController {
	
	@Autowired
	private ProductImageService productImageService;
	
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String handleFormUpload(@RequestParam("name") String name,
			@RequestParam("file") MultipartFile file) {
		System.out.println("name=" + name);
		System.out.println("get here.");
		try {
			if (!file.isEmpty()) {
				System.out.println("and get here");
				byte[] bytes = file.getBytes();
				System.out.println("len= " + bytes.length);
				ProductImage productImage = new ProductImage();
				productImage.setFile(file);
				productImage.setTitle("hehe");
				productImage.setOrder(1);
				System.out.println("productImage.Title"+productImage.getTitle());
				System.out.println("productImage.Order"+productImage.getOrder());
				this.productImageService.build(productImage);
				System.out.println("productImage.Large"+productImage.getLarge());
				System.out.println("productImage.Source"+productImage.getSource());
				System.out.println("productImage.Title"+productImage.getTitle());
				System.out.println("build success");
				// store the bytes somewhere
				return "redirect:uploadSuccess";
			} else {
				System.out.println("no file upload");
				return "redirect:uploadFailure";
			}
		} catch (IOException e) {
			System.out.println("nothing");
		}
		System.out.println("over\n");
		return "redirect:list";
	}

	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public String handleFormUpload() {
		System.out.println("we get here.");
		return "modules/upload/upload";
	}
}
