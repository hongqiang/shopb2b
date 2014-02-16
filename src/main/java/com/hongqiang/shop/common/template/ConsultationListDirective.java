package com.hongqiang.shop.common.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hongqiang.shop.common.utils.FreeMarkers;
import com.hongqiang.shop.modules.content.service.ConsultationService;
import com.hongqiang.shop.modules.entity.Brand;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Product;
import com.hongqiang.shop.modules.product.service.ProductService;
import com.hongqiang.shop.modules.user.service.MemberService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("consultationListDirective")
public class ConsultationListDirective extends BaseDirective
{
  private static final String MEMBER_ID = "memberId";
  private static final String PRODUCT_ID = "productId";
  private static final String CONSULTATIONS = "consultations";

  @Autowired
  private ConsultationService consultationService;

  @Autowired
  private MemberService memberService;

  @Autowired
  private ProductService productService;

  @SuppressWarnings({ "unchecked", "rawtypes" })
public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException
  {
    Long localLong1 = (Long)FreeMarkers.getParameter(MEMBER_ID, Long.class, params);
    Long localLong2 = (Long)FreeMarkers.getParameter(PRODUCT_ID, Long.class, params);
    Member localMember = (Member)this.memberService.find(localLong1);
    Product localProduct = (Product)this.productService.find(localLong2);
    boolean bool = setFreemarker(env, params);
    String str = getFreemarkerCacheRegion(env, params);
    Integer localInteger = getFreemarkerCount(params);
    List localList1 = getFreemarkerFilter(params, Brand.class, new String[0]);
    List localList2 = getFreemarkerOrder(params, new String[0]);
    Object localObject;
    if (((localLong1 != null) && (localMember == null)) || ((localLong2 != null) && (localProduct == null)))
      localObject = new ArrayList();
    else if (bool)
      localObject = this.consultationService.findList(localMember, localProduct, Boolean.valueOf(true), localInteger, localList1, localList2, str);
    else
      localObject = this.consultationService.findList(localMember, localProduct, Boolean.valueOf(true), localInteger, localList1, localList2);
    setFreemarker(CONSULTATIONS, localObject, env, body);
  }
}