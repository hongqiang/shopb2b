package com.hongqiang.shop.modules.user.web.admin;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.shipping.service.OrderService;

@Controller("adminSalesController")
@RequestMapping({"${adminPath}/sales"})
public class SalesController extends BaseController
{
public enum Type
{
  year, month;
}

  private static final int LINK_LENGTH = 12;

  @Autowired
  private OrderService orderService;

  @RequestMapping(value={"/view"}, method=RequestMethod.GET)
  public String view(Type type, Date beginDate, Date endDate, Model model)
  {
    if (type == null)
      type = Type.month;
    if (beginDate == null)
      beginDate = DateUtils.addMonths(new Date(), -11);
    if (endDate == null)
      endDate = new Date();
    LinkedHashMap<Date, BigDecimal> salesAmountMap = new LinkedHashMap<Date, BigDecimal>();
    LinkedHashMap<Date, Integer> salesVolumeMap = new LinkedHashMap<Date, Integer>();
    Calendar start = Calendar.getInstance();
    start.setTime(beginDate);
    int startYear = start.get(Calendar.YEAR);
    int startMonth = start.get(Calendar.MONTH);
    
    Calendar end = Calendar.getInstance();
    end.setTime(endDate);
    int endYear = end.get(Calendar.YEAR);
    int endMonth = end.get(Calendar.MONTH);
    
    for (int loopYear = startYear; loopYear <= endYear; loopYear++)
    {
      if (salesAmountMap.size() >= LINK_LENGTH)
        break;
      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.YEAR, loopYear);
      if (type == Type.year)
      {
        calendar.set(Calendar.MONTH, calendar.getActualMinimum(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        Date beginSalesDate = calendar.getTime();
        calendar.set(Calendar.MONTH, calendar.getActualMaximum(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
        Date endSalesDate = calendar.getTime();
        BigDecimal salesAmount = this.orderService.getSalesAmount(beginSalesDate, endSalesDate);
        Integer salesVolume = this.orderService.getSalesVolume(beginSalesDate, endSalesDate);
        salesAmountMap.put(beginSalesDate, salesAmount != null ? salesAmount : BigDecimal.ZERO);
        salesVolumeMap.put(beginSalesDate, Integer.valueOf(salesVolume != null ? salesVolume.intValue() : 0));
      }
      else
      {
    	  int  loopStartMonth = (loopYear == startYear) ? startMonth : calendar.getActualMinimum(Calendar.MONTH);
    	  int  loopEndMonth = (loopYear == endYear) ? endMonth : calendar.getActualMaximum(Calendar.MONTH);
        for (int loopMonth = loopStartMonth; loopMonth <= loopEndMonth; loopMonth++)
        {
          if (salesAmountMap.size() >= LINK_LENGTH)
            break;
          calendar.set(Calendar.MONTH, loopMonth);
          calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
          calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
          calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
          calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
          Date beginSalesDate = calendar.getTime();
          calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
          calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
          calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
          calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
          Date endSalesDate = calendar.getTime();
          BigDecimal salesAmount = this.orderService.getSalesAmount(beginSalesDate, endSalesDate);
          Integer salesVolume = this.orderService.getSalesVolume(beginSalesDate, endSalesDate);
          salesAmountMap.put(beginSalesDate, salesAmount != null ? salesAmount : BigDecimal.ZERO);
          salesVolumeMap.put(beginSalesDate, Integer.valueOf(salesVolume != null ? salesVolume.intValue() : 0));
        }
      }
    }
    model.addAttribute("types", Type.values());
    model.addAttribute("type", type);
    model.addAttribute("beginDate", beginDate);
    model.addAttribute("endDate", endDate);
    model.addAttribute("salesAmountMap", salesAmountMap);
    model.addAttribute("salesVolumeMap", salesVolumeMap);
    return "/admin/sales/view";
  }
}