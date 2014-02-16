<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.coupon.add")} - Powered By HONGQIANG_SHOP</title>
<meta name="author" content="HONGQIANG_SHOP Team" />
<meta name="copyright" content="HONGQIANG_SHOP" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/editor/kindeditor.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $isExchange = $("#isExchange");
	var $point = $("#point");
	
	[@flash_message /]
	
	// 是否允许积分兑换
	$isExchange.click(function() {
		if ($(this).prop("checked")) {
			$point.prop("disabled", false);
		} else {
			$point.val("").prop("disabled", true);
		}
	});
	
	$.validator.addMethod("compareAmount", 
		function(value, element, param) {
			var parameterValue = $(param).val();
			if ($.trim(parameterValue) == "" || $.trim(value) == "") {
				return true;
			}
			try {
				var startPrice = parseFloat(parameterValue);
				var endPrice = parseFloat(value);
				return endPrice >= startPrice;
			} catch(e) {
				return false;
			}
		},
		"${message("admin.coupon.compareAmount")}"
	);
	
	$.validator.addMethod("divisor", 
		function(value, element, param) {
			var parameterValue = $(param).val();
			if ($.trim(parameterValue) == "" || $.trim(value) == "") {
				return true;
			}
			try {
				var amount = parseFloat(value);
				return parameterValue != "divide" || amount != 0;
			} catch(e) {
				return false;
			}
		},
		"${message("admin.coupon.divisor")}"
	);
	
	// 表单验证
	$inputForm.validate({
		rules: {
			name: "required",
			prefix: "required",
			startPrice: {
				min: 0,
				decimal: {
					integer: 12,
					fraction: ${setting.priceScale}
				}
			},
			endPrice: {
				min: 0,
				decimal: {
					integer: 12,
					fraction: ${setting.priceScale}
				},
				compareAmount: "#startPrice"
			},
			point: {
				required: true,
				digits: true
			},
			priceValue: {
				number: true,
				decimal: {
					integer: 12,
					fraction: ${setting.priceScale}
				},
				divisor: "#priceOperator"
			}
		}
	});
	
});
</script>
</head>
<body>
	<div class="path">
		<a href="${base}${adminPath}/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.coupon.add")}
	</div>
	<form id="inputForm" action="save.jhtml" method="post">
		<ul id="tab" class="tab">
			<li>
				<input type="button" value="${message("admin.coupon.base")}" />
			</li>
			<li>
				<input type="button" value="${message("Coupon.introduction")}" />
			</li>
		</ul>
		<div class="tabContent">
			<table class="input">
				<tr>
					<th>
						<span class="requiredField">*</span>${message("Coupon.name")}:
					</th>
					<td>
						<input type="text" name="name" class="text" maxlength="200" />
					</td>
				</tr>
				<tr>
					<th>
						<span class="requiredField">*</span>${message("Coupon.prefix")}:
					</th>
					<td>
						<input type="text" name="prefix" class="text" maxlength="200" />
					</td>
				</tr>
				<tr>
					<th>
						${message("Coupon.beginDate")}:
					</th>
					<td>
						<input type="text" id="beginDate" name="beginDate" class="text Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', maxDate: '#F{$dp.$D(\'endDate\')}'});" />
					</td>
				</tr>
				<tr>
					<th>
						${message("Coupon.endDate")}:
					</th>
					<td>
						<input type="text" id="endDate" name="endDate" class="text Wdate" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', minDate: '#F{$dp.$D(\'beginDate\')}'});" />
					</td>
				</tr>
				<tr>
					<th>
						${message("Coupon.startPrice")}:
					</th>
					<td>
						<input type="text" id="startPrice" name="startPrice" class="text" maxlength="16" />
					</td>
				</tr>
				<tr>
					<th>
						${message("Coupon.endPrice")}:
					</th>
					<td>
						<input type="text" name="endPrice" class="text" maxlength="16" />
					</td>
				</tr>
				<tr>
					<th>
						${message("admin.common.setting")}:
					</th>
					<td>
						<label>
							<input type="checkbox" name="isEnabled" value="true" checked="checked" />${message("Coupon.isEnabled")}
							<input type="hidden" name="_isEnabled" value="false" />
						</label>
						<label>
							<input type="checkbox" id="isExchange" name="isExchange" value="true" checked="checked" />${message("Coupon.isExchange")}
							<input type="hidden" name="_isExchange" value="false" />
						</label>
					</td>
				</tr>
				<tr>
					<th>
						<span class="requiredField">*</span>${message("Coupon.point")}:
					</th>
					<td>
						<input type="text" id="point" name="point" class="text" maxlength="9" />
					</td>
				</tr>
				<tr>
					<th>
						${message("admin.coupon.price")}:
					</th>
					<td>
						${message("admin.coupon.originalPrice")}
						<select id="priceOperator" name="priceOperator">
							[#list operators as operator]
								<option value="${operator}">${message("Coupon.Operator." + operator)}</option>
							[/#list]
						</select>
						<input type="text" name="priceValue" class="text" maxlength="16" style="width: 50px;" />
					</td>
				</tr>
			</table>
		</div>
		<div class="tabContent">
			<table class="input">
				<tr>
					<td>
						<textarea id="editor" name="introduction" class="editor" style="width: 100%;"></textarea>
					</td>
				</tr>
			</table>
		</div>
		<table class="input">
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