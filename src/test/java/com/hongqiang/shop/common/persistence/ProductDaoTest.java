package com.hongqiang.shop.common.persistence;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hongqiang.shop.common.test.SpringTransactionalContextTests;
import com.hongqiang.shop.modules.account.dao.PromotionDao;
import com.hongqiang.shop.modules.product.dao.BrandDao;
import com.hongqiang.shop.modules.product.dao.ProductCategoryDao;
import com.hongqiang.shop.modules.product.dao.ProductDao;
import com.hongqiang.shop.modules.product.dao.TagDao;
import com.hongqiang.shop.modules.user.dao.MemberDao;

public class ProductDaoTest  extends SpringTransactionalContextTests{
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private BrandDao brandDao;
	
	@Autowired
	private PromotionDao promotionDao;
	
	@Autowired
	private TagDao tagDao;
	
	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Autowired
	private MemberDao memberDao;
	
	@Test
	public void find(){
//		Product product = this.productDao.find(1L);
//		System.out.println("name="+product.getName()+",id="+product.getId()+",sn="+product.getSn());
//		
//		Product product2 = this.productDao.findBySn("2013041");
//		System.out.println("name="+product2.getName()+",id="+product2.getId()+",sn="+product2.getSn());
//		
//		
//	   boolean bool=this.productDao.snExists("2013041");
//	   System.out.println("bool="+bool);
//	   System.out.print("================================================\n");
//	   
//	   String keyword = "2013041";
//	   Boolean isGift = Boolean.FALSE;
////	   Integer count = Integer.valueOf(10);
//	   List<Product> products = this.productDao.search(keyword, isGift, null);
//	   for (Product product3 : products) {
//		   System.out.println("name="+product3.getName()+",id="+product3.getId()+",sn="+product3.getSn());
//	   }
//	   System.out.print("================================================\n");
//
//	   List<Product> ppList=this.productDao.findList(null, null,null,null);
//	   System.out.println("ppsize="+ppList.size());
//	   for (Product product3 : ppList) {
//		   System.out.println("name="+product3.getName()+",id="+product3.getId()+",sn="+product3.getSn());
//	   }
//	   System.out.print("================================================\n");
//
//	   Brand brand =this.brandDao.find(3L);
//	   Promotion promotion = this.promotionDao.find(1L);
//	   Iterable<Tag> tags = this.tagDao.findAll();
//	   List<Tag> tags2 = new ArrayList<Tag>();
//	   for(Tag tag : tags){
//		   tags2.add(tag);
//	   }
//	   ProductCategory productCategory = this.productCategoryDao.find(1L);
//	   Boolean isMarketable = Boolean.TRUE;
//	   Boolean isList=Boolean.TRUE;
//		Boolean isTop=Boolean.FALSE;
//		Boolean isGifts=Boolean.FALSE;
//		Boolean isOutOfStock=Boolean.FALSE;
//		Boolean isStockAlert=Boolean.FALSE;
//	
//	   List<Product> pps=this.productDao.findList( productCategory, brand,
//			   promotion,  tags2,null,null,null, isMarketable, isList,
//			 isTop, isGifts, isOutOfStock,isStockAlert, null, null,
//			 null, null);
//	   System.out.println("pps="+pps.size());
//	   for (Product product3 : pps) {
//		   System.out.println("name="+product3.getName()+",id="+product3.getId()+",sn="+product3.getSn());
//	   }
//	   System.out.print("================================================\n");
//
//	   Pageable pageable = new Pageable();
//	   pageable.setPageNumber(1);
//	   pageable.setPageSize(40);
//
//		Page<Product> page=this.productDao.findPage( productCategory, brand,
//	 promotion,  tags2, null, null, null, isMarketable, isList,
//		isTop, isGift, isOutOfStock,isStockAlert, null, pageable);
//		System.out.println("page="+page.getList().size());
//		 for (Product product3 : page.getList()) {
//			   System.out.println("name="+product3.getName()+",id="+product3.getId()+",sn="+product3.getSn());
//		   }
//	   System.out.print("================================================\n");
//	   Filter[] filters = null;
//	   long cc = this.productDao.count(filters);
//	   long c2= this.productDao.count();
//	   System.out.println("count =" + cc);
//	   System.out.println("count2 =" + c2);
//	   System.out.print("================================================\n");
//	   
//	   Member member = this.memberDao.find(1L);
//	   System.out.println("member =" + member.getName());
//	   Page<Product> pg = this.productDao.findPage(member, pageable);
//	   System.out.println("pg="+pg.getList().size());
//		 for (Product product3 : pg.getList()) {
//			   System.out.println("name="+product3.getName()+",id="+product3.getId()+",sn="+product3.getSn());
//		   }
//	   System.out.print("================================================\n");
//	   
//	   Page<Object> pg2 = this.productDao.findSalesPage(null,null, pageable);
//	   System.out.println("pg="+pg2.getList().size());
//		 for (Object pro : pg2.getList()) {
//			 if (pro instanceof Product) {
//				 Product product3 = (Product)pro;
//				 System.out.println("name="+product3.getName()+","
//				 		+ "id="+product3.getId()+",sn="+product3.getSn());
//			}
//			   
//		   }
	   System.out.print("================================================\n");
	}
}
