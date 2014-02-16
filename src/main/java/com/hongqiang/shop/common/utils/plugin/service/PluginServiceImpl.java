package com.hongqiang.shop.common.utils.plugin.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hongqiang.shop.common.utils.plugin.PaymentPlugin;
import com.hongqiang.shop.common.utils.plugin.StoragePlugin;


@Service
public class PluginServiceImpl
  implements PluginService
{

  @Resource
  private List<PaymentPlugin> paymentPluginList = new ArrayList<PaymentPlugin>();

  @Resource
  private List<StoragePlugin> storagePluginList = new ArrayList<StoragePlugin>();

  @Resource
  private Map<String, PaymentPlugin> paymentPluginMap = new HashMap<String, PaymentPlugin>();

  @Resource
  private Map<String, StoragePlugin> storagePluginMap = new HashMap<String, StoragePlugin>();

  public List<PaymentPlugin> getPaymentPlugins()
  {
    Collections.sort(this.paymentPluginList);
    return this.paymentPluginList;
  }

  public List<StoragePlugin> getStoragePlugins()
  {
    Collections.sort(this.storagePluginList);
    return this.storagePluginList;
  }

  public List<PaymentPlugin> getPaymentPlugins(boolean isEnabled)
  {
    List<PaymentPlugin> localArrayList = new ArrayList<PaymentPlugin>();
	for(PaymentPlugin localPaymentPlugin : this.paymentPluginList){
		if(localPaymentPlugin.getIsEnabled() == isEnabled){
			localArrayList.add(localPaymentPlugin);
		}
	}
    Collections.sort(localArrayList);
    return localArrayList;
  }

  public List<StoragePlugin> getStoragePlugins(boolean isEnabled)
  {
    List<StoragePlugin> localArrayList = new ArrayList<StoragePlugin>();
    for(StoragePlugin locaStoragePlugin : this.storagePluginList){
		if(locaStoragePlugin.getIsEnabled() == isEnabled){
			localArrayList.add(locaStoragePlugin);
		}
	}
    Collections.sort(localArrayList);
    return localArrayList;
  }

  public PaymentPlugin getPaymentPlugin(String id)
  {
    return (PaymentPlugin)this.paymentPluginMap.get(id);
  }

  public StoragePlugin getStoragePlugin(String id)
  {
    return (StoragePlugin)this.storagePluginMap.get(id);
  }
}