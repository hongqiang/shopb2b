package com.hongqiang.shop.modules.user.web.admin;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.product.service.ProductService;

@Controller("adminSalesRankingController")
@RequestMapping({"${adminPath}/sales_ranking"})
public class SalesRankingController extends BaseController
{

  @Autowired
  private ProductService productService;

  @RequestMapping(value={"/list"}, method=RequestMethod.GET)
  public String list(Date beginDate, Date endDate, Pageable pageable, Model model)
  {
    model.addAttribute("beginDate", beginDate);
    model.addAttribute("endDate", endDate);
    model.addAttribute("page", this.productService.findSalesPage(beginDate, endDate, pageable));
    return "/admin/sales_ranking/list";
  }
}