package com.hongqiang.shop.common.utils.plugin.local;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.hongqiang.shop.common.utils.Setting;
import com.hongqiang.shop.common.utils.SettingUtils;
import com.hongqiang.shop.common.utils.plugin.StoragePlugin;
import com.hongqiang.shop.website.entity.FileInfo;

@Component("filePlugin")
public class FilePlugin extends StoragePlugin
  implements ServletContextAware
{
  private ServletContext servletContext;

  public void setServletContext(ServletContext servletContext)
  {
    this.servletContext = servletContext;
  }

  public String getName()
  {
    return "本地文件存储";
  }

  public String getVersion()
  {
    return "1.0";
  }

  public String getAuthor()
  {
    return "HONGQIANG_SHOP";
  }

  public String getSiteUrl()
  {
    return "http://www.shopxx.net";
  }

  public String getInstallUrl()
  {
    return null;
  }

  public String getUninstallUrl()
  {
    return null;
  }

  public String getSettingUrl()
  {
    return "file/setting.jhtml";
  }

  public void upload(String path, File file, String contentType)
  {
    File localFile = new File(this.servletContext.getRealPath(path));
    try
    {
      FileUtils.moveFile(file, localFile);
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
  }

  public String getUrl(String path)
  {
    Setting localSetting = SettingUtils.get();
    return localSetting.getSiteUrl() + path;
  }

  public List<FileInfo> browser(String path)
  {
    Setting localSetting = SettingUtils.get();
    List<FileInfo> localArrayList = new ArrayList<FileInfo>();
    File localFile1 = new File(this.servletContext.getRealPath(path));
    if ((localFile1.exists()) && (localFile1.isDirectory()))
      for (File localFile2 : localFile1.listFiles())
      {
        FileInfo localFileInfo = new FileInfo();
        localFileInfo.setName(localFile2.getName());
        localFileInfo.setUrl(localSetting.getSiteUrl() + path + localFile2.getName());
        localFileInfo.setIsDirectory(Boolean.valueOf(localFile2.isDirectory()));
        localFileInfo.setSize(Long.valueOf(localFile2.length()));
        localFileInfo.setLastModified(new Date(localFile2.lastModified()));
        localArrayList.add(localFileInfo);
      }
    return localArrayList;
  }
}