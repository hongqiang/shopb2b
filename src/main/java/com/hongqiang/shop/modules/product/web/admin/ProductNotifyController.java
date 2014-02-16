package com.hongqiang.shop.modules.product.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.product.service.ProductNotifyService;
import com.hongqiang.shop.modules.product.service.ProductService;

@Controller("adminProductNotifyntroller")
@RequestMapping({"${adminPath}/product_notify"})
public class ProductNotifyController extends BaseController
{

  @Autowired
  private ProductNotifyService productNotifyService;

  @RequestMapping(value={"/send"}, method=RequestMethod.POST)
  @ResponseBody
  public Message send(Long[] ids)
  {
    int i = this.productNotifyService.send(ids);
    return Message.success("admin.productNotify.sentSuccess", new Object[] { Integer.valueOf(i) });
  }

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Boolean isMarketable, Boolean isOutOfStock, Boolean hasSent, Pageable pageable, ModelMap model)
  {
    model.addAttribute("isMarketable", isMarketable);
    model.addAttribute("isOutOfStock", isOutOfStock);
    model.addAttribute("hasSent", hasSent);
    model.addAttribute("page", this.productNotifyService.findPage(null, isMarketable, isOutOfStock, hasSent, pageable));
    return "/admin/product_notify/list";
  }

  @RequestMapping(value={"/delete"}, method=RequestMethod.POST)
  @ResponseBody
  public Message delete(Long[] ids)
  {
    this.productNotifyService.delete(ids);
    return ADMIN_SUCCESS;
  }
  
 @Autowired
 private ProductService productService;
 
  @RequestMapping(value={"/donoti"},method=RequestMethod.GET)
	public void doit(){
//		//test findid
//		ProductNotify productNotify = this.productNotifyService.find(1L);
//		System.out.println(productNotify.getEmail());
//		System.out.println("==============================================");
//		//test findpage
//		Pageable pageable = new Pageable(1,40);
//		Page<ProductNotify> page = this.productNotifyService.findPage(null, true, true,true,pageable);
//		if(page.getList().size()>0){
//			for (ProductNotify o : page.getList()) {
//				System.out.print(o.getEmail()+"\n");
//			}
//		}
//		else{
//			System.out.print("nothing\n");
//		}
//		System.out.println("==============================================");
//		//test exists
//		Product product = this.productService.find(1L);
//		boolean ig = this.productNotifyService.exists(product, "test");
//		System.out.print("ig="+ig+"\n");
//		//test save
//		ProductNotify pr=new ProductNotify();
//		pr.setEmail("hello@126.com");
//		pr.setProduct(product);
//		pr.setHasSent(true);
//		this.productNotifyService.save(pr);
//		//test send
//		Long [] longs = new Long[4];
//		for(int i=0;i<4;++i){
//			longs[i]=(long)i+1;
//		}
//		
//		
//		this.productNotifyService.send(longs);
//		//test delete
//		this.productNotifyService.delete(longs);
	}
}
