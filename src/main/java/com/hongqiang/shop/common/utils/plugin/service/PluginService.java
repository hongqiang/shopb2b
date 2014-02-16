package com.hongqiang.shop.common.utils.plugin.service;

import java.util.List;

import com.hongqiang.shop.common.utils.plugin.PaymentPlugin;
import com.hongqiang.shop.common.utils.plugin.StoragePlugin;

public interface PluginService {
	public List<PaymentPlugin> getPaymentPlugins();

	public List<StoragePlugin> getStoragePlugins();

	public List<PaymentPlugin> getPaymentPlugins(boolean isEnabled);

	public List<StoragePlugin> getStoragePlugins(boolean isEnabled);

	public PaymentPlugin getPaymentPlugin(String id);

	public StoragePlugin getStoragePlugin(String id);
}