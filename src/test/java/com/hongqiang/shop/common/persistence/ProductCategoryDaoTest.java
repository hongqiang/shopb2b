package com.hongqiang.shop.common.persistence;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hongqiang.shop.common.test.SpringTransactionalContextTests;
import com.hongqiang.shop.modules.product.dao.ProductCategoryDao;
import com.hongqiang.shop.modules.product.service.ProductCategoryService;

public class ProductCategoryDaoTest extends SpringTransactionalContextTests{
	@Autowired
	private ProductCategoryDao productCategoryDao;

	@Autowired
	private ProductCategoryService productCategoryService;
	
	public static byte[] getBytes(String filePath){  
        byte[] buffer = null;  
        try {  
            File file = new File(filePath);  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return buffer;  
    }  
	
	@Test
	public void find() throws IOException{
		System.out.println("hello");
	}
}
