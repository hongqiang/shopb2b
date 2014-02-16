package com.hongqiang.shop.common.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hongqiang.shop.common.utils.FreeMarkers;
import com.hongqiang.shop.modules.content.service.ReviewService;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Review;
import com.hongqiang.shop.modules.user.service.MemberService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("reviewListDirective")
public class ReviewListDirective extends BaseDirective
{
  private static final String MEMBER_ID = "memberId";
  private static final String PRODUCT_ID = "productId";
  private static final String TYPE = "type";
  private static final String REVIEWS = "reviews";

  @Autowired
  private ReviewService reviewService;

  @Autowired
  private MemberService memberService;

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)throws TemplateException, IOException
  {
    Long localLong1 = (Long)FreeMarkers.getParameter(MEMBER_ID, Long.class, params);
    Long localLong2 = (Long)FreeMarkers.getParameter(PRODUCT_ID, Long.class, params);
    Review.Type localType = (Review.Type)FreeMarkers.getParameter(TYPE, Review.Type.class, params);
    Member localMember = (Member)this.memberService.find(localLong1);
    Object localObject;

    if (((localLong1 != null) && (localMember == null)) || ((localLong2 != null)))
    {
      localObject = new ArrayList();
    }
    else
    {
      boolean bool = setFreemarker(env, params);
      String str = getFreemarkerCacheRegion(env, params);
      Integer localInteger = getFreemarkerCount(params);
      List localList1 = getFreemarkerFilter(params, Review.class, new String[0]);
      List localList2 = getFreemarkerOrder(params, new String[0]);
      if (bool)
        localObject = this.reviewService.findList(localMember, localLong2, localType, Boolean.valueOf(true), localInteger, localList1, localList2, str);
      else
        localObject = this.reviewService.findList(localMember, localLong2, localType, Boolean.valueOf(true), localInteger, localList1, localList2);
    }
    setFreemarker(REVIEWS, localObject, env, body);
  }
}