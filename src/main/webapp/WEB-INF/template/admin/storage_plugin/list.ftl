<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.storagePlugin.list")} - Powered By HONGQIANG_SHOP</title>
<meta name="author" content="HONGQIANG_SHOP Team" />
<meta name="copyright" content="HONGQIANG_SHOP" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
<script type="text/javascript">
$().ready(function() {

	[@flash_message /]

});
</script>
</head>
<body>
	<div class="path">
		<a href="${base}${adminPath}/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.storagePlugin.list")} <span>(${message("admin.page.total", storagePlugins?size)})</span>
	</div>
	<form id="listForm" action="list.jhtml" method="get">
		<div class="bar">
			<div class="buttonWrap">
				<a href="javascript:;" id="refreshButton" class="iconButton">
					<span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
				</a>
			</div>
		</div>
		<table id="listTable" class="list">
			<tr>
				<th>
					<span>${message("StoragePlugin.name")}</span>
				</th>
				<th>
					<span>${message("StoragePlugin.version")}</span>
				</th>
				<th>
					<span>${message("StoragePlugin.author")}</span>
				</th>
				<th>
					<span>${message("StoragePlugin.isEnabled")}</span>
				</th>
				<th>
					<span>${message("admin.common.handle")}</span>
				</th>
			</tr>
			[#list storagePlugins as storagePlugin]
				<tr>
					<td>
						[#if storagePlugin.siteUrl??]
							<a href="${storagePlugin.siteUrl}" target="_blank">${storagePlugin.name}</a>
						[#else]
							${storagePlugin.name}
						[/#if]
					</td>
					<td>
						${storagePlugin.version!'-'}
					</td>
					<td>
						${storagePlugin.author!'-'}
					</td>
					<td>
						<span class="${storagePlugin.isEnabled?string("true", "false")}Icon">&nbsp;</span>
					</td>
					<td>
						[#if storagePlugin.isInstalled]
							[#if storagePlugin.settingUrl??]
								<a href="${storagePlugin.settingUrl}">[${message("admin.storagePlugin.setting")}]</a>
							[/#if]
							[#if storagePlugin.uninstallUrl??]
								<a href="${storagePlugin.uninstallUrl}">[${message("admin.storagePlugin.uninstall")}]</a>
							[/#if]
						[#else]
							[#if storagePlugin.installUrl??]
								<a href="${storagePlugin.installUrl}">[${message("admin.storagePlugin.install")}]</a>
							[/#if]
						[/#if]
					</td>
				</tr>
			[/#list]
		</table>
	</form>
</body>
</html>