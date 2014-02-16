<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("shop.member.deposit.recharge")}[#if systemShowPowered] - Powered By HONGQIANG_SHOP[/#if]</title>
<meta name="author" content="HONGQIANG_SHOP Team" />
<meta name="copyright" content="HONGQIANG_SHOP" />
<link href="${base}/resources/shop/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/shop/css/member.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/shop/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/shop/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/shop/js/common.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	
	[@flash_message /]
	
	// 表单验证
	$inputForm.validate({
		rules: {
			amount: {
				required: true,
				positive: true,
				decimal: {
					integer: 12,
					fraction: ${setting.priceScale}
				}
			}
		}
	});

});
</script>
</head>
<body>
	[#assign current = "depositRecharge" /]
	[#include "/shop/include/header.ftl" /]
	<div class="container member">
		[#include "/shop/member/include/navigation.ftl" /]
		<div class="span18 last">
			<div class="input deposit">
				<div class="title">${message("shop.member.deposit.recharge")}</div>
				<form id="inputForm" action="recharge.jhtml" method="post" target="_blank">
					<table class="input">
						<tr>
							<th>
								${message("shop.member.deposit.balance")}: 
							</th>
							<td>
								${currency(member.balance, true, true)}
							</td>
						</tr>
						<tr>
							<th>
								${message("shop.member.deposit.amount")}: 
							</th>
							<td>
								<input type="text" name="amount" class="text" maxlength="16" />
							</td>
						</tr>
						<tr>
							<th>
								${message("shop.member.deposit.paymentPlugin")}:
							</th>
							<td>
								<div class="paymentPlugin clearfix">
									[#if paymentPlugins??]
										[#list paymentPlugins as paymentPlugin]
											<div>
												<input type="radio" id="${paymentPlugin.id}" name="paymentPluginId" value="${paymentPlugin.id}"[#if paymentPlugin == defaultPaymentPlugin] checked="checked"[/#if] />
												<label for="${paymentPlugin.id}">
													[#if paymentPlugin.logo??]
														<em title="${paymentPlugin.paymentName}" style="background-image: url(${paymentPlugin.logo});">&nbsp;</em>
													[#else]
														<em>${paymentPlugin.paymentName}</em>
													[/#if]
												</label>
											</div>
										[/#list]
									[/#if]
								</div>
							</td>
						</tr>
						<tr>
							<th>
								&nbsp;
							</th>
							<td>
								<input type="submit" class="button" value="${message("shop.member.submit")}" />
								<input type="button" class="button" value="${message("shop.member.back")}" onclick="location.href='list.jhtml'" />
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
	[#include "/shop/include/footer.ftl" /]
</body>
</html>