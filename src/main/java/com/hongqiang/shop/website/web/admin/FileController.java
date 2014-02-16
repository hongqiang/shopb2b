package com.hongqiang.shop.website.web.admin;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hongqiang.shop.common.utils.JsonUtils;
import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.website.entity.FileInfo;
import com.hongqiang.shop.website.service.FileService;

@Controller("adminFileController")
@RequestMapping({"${adminPath}/file"})
public class FileController extends BaseController
{

  @Autowired
  private FileService fileService;

  @RequestMapping(value={"/upload"}, method=RequestMethod.POST)
  public void upload(FileInfo.FileType fileType, MultipartFile file, HttpServletResponse response)
  {
    if (!this.fileService.isValid(fileType, file))
    {
      JsonUtils.toJson(response, "text/html; charset=UTF-8", Message.warn("admin.upload.invalid", new Object[0]));
    }
    else
    {
      String str = this.fileService.upload(fileType, file, false);
      if (str == null)
        JsonUtils.toJson(response, "text/html; charset=UTF-8", Message.warn("admin.upload.error", new Object[0]));
      HashMap<String, String> localHashMap = new HashMap<String, String>();
      localHashMap.put("url", str);
      JsonUtils.toJson(response, "text/html; charset=UTF-8", localHashMap);
    }
  }

  @RequestMapping(value={"/browser"}, method=RequestMethod.GET)
  @ResponseBody
  public List<FileInfo> browser(String path, FileInfo.FileType fileType, FileInfo.OrderType orderType)
  {
    return this.fileService.browser(path, fileType, orderType);
  }
}