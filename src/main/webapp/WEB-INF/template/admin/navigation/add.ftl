<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.navigation.add")} - Powered By HONGQIANG_SHOP</title>
<meta name="author" content="HONGQIANG_SHOP Team" />
<meta name="copyright" content="HONGQIANG_SHOP" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $systemUrl = $("#systemUrl");
	var $url = $("#url");
	
	[@flash_message /]

	// 将选择的系统内容地址填充至链接地址中
	$systemUrl.change(function() {
		$url.val($systemUrl.val());
	});
	
	// 链接地址内容修改时,系统内容选择框修改为不选择任何项目
	$url.keypress(function() {
		$systemUrl.val("");
	});
	
	// 表单验证
	$inputForm.validate({
		rules: {
			name: "required",
			url: "required",
			order: "digits"
		}
	});
	
});
</script>
</head>
<body>
	<div class="path">
		<a href="${base}${adminPath}/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.navigation.add")}
	</div>
	<form id="inputForm" action="save.jhtml" method="post">
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Navigation.name")}:
				</th>
				<td>
					<input type="text" id="name" name="name" class="text" maxlength="200" />	 
				</td>
			</tr>
			<tr>
				<th>
					${message("admin.navigation.systemUrl")}:
				</th>
				<td>
					<select id="systemUrl">
						<option value="">------------</option>
						<option value="${base}${adminPath}/">${message("admin.navigation.home")}</option>
						<option value="${base}${adminPath}/product_category.jhtml">${message("admin.navigation.productCategory")}</option>
						<option value="${base}${adminPath}/friend_link.jhtml">${message("admin.navigation.friendLink")}</option>
						<option value="${base}${adminPath}/member/index.jhtml">${message("admin.navigation.member")}</option>
						[#list articleCategoryTree as articleCategory]
							<option value="${base}${articleCategory.path}">
								[#if articleCategory.grade != 0]
									[#list 1..articleCategory.grade as i]
										&nbsp;&nbsp;
									[/#list]
								[/#if]
								${articleCategory.name}
							</option>
						[/#list]
						[#list productCategoryTree as productCategory]
							<option value="${base}${productCategory.path}">
								[#if productCategory.grade != 0]
									[#list 1..productCategory.grade as i]
										&nbsp;&nbsp;
									[/#list]
								[/#if]
								${productCategory.name}
							</option>
						[/#list]
					</select>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Navigation.url")}:
				</th>
				<td>
					<input type="text" id="url" name="url" class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Navigation.position")}:
				</th>
				<td>
					<select name="position">
						[#list positions as position]
							<option value="${position}"[#if position == "middle"] selected="selected"[/#if]>${message("Navigation.Position." + position)}</option>
						[/#list]
					</select>
				</td>
			</tr>
			<tr>
				<th>
					${message("admin.common.setting")}:
				</th>
				<td>
					<label>
						<input type="checkbox" name="isBlankTarget" value="true" />${message("Navigation.isBlankTarget")}
						<input type="hidden" name="_isBlankTarget" value="false" />
					</label>
				</td>
			</tr>
			<tr>
				<th>
					${message("admin.common.order")}:
				</th>
				<td>
					<input type="text" name="order" class="text" maxlength="9" />
				</td>
			</tr>
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="submit" class="button" value="${message("admin.common.submit")}" />
					<input type="button" id="backButton" class="button" value="${message("admin.common.back")}" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>