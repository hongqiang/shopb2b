package com.hongqiang.shop.common.utils.plugin.remote;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Component;

import com.hongqiang.shop.common.utils.plugin.StoragePlugin;
import com.hongqiang.shop.website.entity.FileInfo;
import com.hongqiang.shop.website.entity.PluginConfig;

@Component("ftpPlugin")
public class FtpPlugin extends StoragePlugin
{
  public String getName()
  {
    return "FTP存储";
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
    return "ftp/install.jhtml";
  }

  public String getUninstallUrl()
  {
    return "ftp/uninstall.jhtml";
  }

  public String getSettingUrl()
  {
    return "ftp/setting.jhtml";
  }

  public void upload(String path, File file, String contentType)
  {
    PluginConfig localPluginConfig = getPluginConfig();
    if (localPluginConfig != null)
    {
      String str1 = localPluginConfig.getAttribute("host");
      Integer localInteger = Integer.valueOf(localPluginConfig.getAttribute("port"));
      String str2 = localPluginConfig.getAttribute("username");
      String str3 = localPluginConfig.getAttribute("password");
      FTPClient localFTPClient = new FTPClient();
      FileInputStream localFileInputStream = null;
      try
      {
        localFileInputStream = new FileInputStream(file);
        localFTPClient.connect(str1, localInteger.intValue());
        localFTPClient.login(str2, str3);
        localFTPClient.setFileTransferMode(10);
        localFTPClient.setFileType(2);
        localFTPClient.enterLocalPassiveMode();
        if (FTPReply.isPositiveCompletion(localFTPClient.getReplyCode()))
        {
          String str4 = StringUtils.substringBeforeLast(path, "/");
          String str5 = StringUtils.substringAfterLast(path, "/");
          if (!localFTPClient.changeWorkingDirectory(str4))
          {
            String[] arrayOfString1 = StringUtils.split(str4, "/");
            String str6 = "/";
            localFTPClient.changeWorkingDirectory(str6);
            for (String str7 : arrayOfString1)
            {
              str6 = str6 + str7 + "/";
              if (localFTPClient.changeWorkingDirectory(str6))
                continue;
              localFTPClient.makeDirectory(str7);
              localFTPClient.changeWorkingDirectory(str6);
            }
          }
          localFTPClient.storeFile(str5, localFileInputStream);
          localFTPClient.logout();
        }
      }
      catch (IOException localIOException4)
      {
        localIOException4.printStackTrace();
        IOUtils.closeQuietly(localFileInputStream);
        if (localFTPClient.isConnected())
          try
          {
            localFTPClient.disconnect();
          }
          catch (IOException localIOException1)
          {
          }
      }
      finally
      {
        IOUtils.closeQuietly(localFileInputStream);
        if (localFTPClient.isConnected())
          try
          {
            localFTPClient.disconnect();
          }
          catch (IOException localIOException2)
          {
          }
      }
    }
  }

  public String getUrl(String path)
  {
    PluginConfig localPluginConfig = getPluginConfig();
    if (localPluginConfig != null)
    {
      String str = localPluginConfig.getAttribute("urlPrefix");
      return str + path;
    }
    return null;
  }

  public List<FileInfo> browser(String path)
  {
    List<FileInfo> localArrayList = new ArrayList<FileInfo>();
    PluginConfig localPluginConfig = getPluginConfig();
    if (localPluginConfig != null)
    {
      String str1 = localPluginConfig.getAttribute("host");
      Integer localInteger = Integer.valueOf(localPluginConfig.getAttribute("port"));
      String str2 = localPluginConfig.getAttribute("username");
      String str3 = localPluginConfig.getAttribute("password");
      String str4 = localPluginConfig.getAttribute("urlPrefix");
      FTPClient localFTPClient = new FTPClient();
      try
      {
        localFTPClient.connect(str1, localInteger.intValue());
        localFTPClient.login(str2, str3);
        localFTPClient.setFileTransferMode(10);
        localFTPClient.setFileType(2);
        localFTPClient.enterLocalPassiveMode();
        if ((FTPReply.isPositiveCompletion(localFTPClient.getReplyCode())) && (localFTPClient.changeWorkingDirectory(path)))
          for (FTPFile localFTPFile : localFTPClient.listFiles())
          {
            FileInfo localFileInfo = new FileInfo();
            localFileInfo.setName(localFTPFile.getName());
            localFileInfo.setUrl(str4 + path + localFTPFile.getName());
            localFileInfo.setIsDirectory(Boolean.valueOf(localFTPFile.isDirectory()));
            localFileInfo.setSize(Long.valueOf(localFTPFile.getSize()));
            localFileInfo.setLastModified(localFTPFile.getTimestamp().getTime());
            localArrayList.add(localFileInfo);
          }
      }
      catch (IOException localIOException4)
      {
        localIOException4.printStackTrace();
        if (localFTPClient.isConnected())
          try
          {
            localFTPClient.disconnect();
          }
          catch (IOException localIOException1)
          {
          }
      }
      finally
      {
        if (localFTPClient.isConnected())
          try
          {
            localFTPClient.disconnect();
          }
          catch (IOException localIOException2)
          {
          }
      }
    }
    return localArrayList;
  }
}