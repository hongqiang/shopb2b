<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.sales.view")} - Powered By HONGQIANG_SHOP</title>
<meta name="author" content="HONGQIANG_SHOP Team" />
<meta name="copyright" content="HONGQIANG_SHOP" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/fusionCharts/FusionCharts.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $type = $("#type");
	var $beginDate = $("#beginDate");
	var $endDate = $("#endDate");
	[#if type == "year"]
		[#assign dateFormat = "yyyy" /]
	[#else]
		[#assign dateFormat = "yyyy-MM" /]
	[/#if]
	[@compress single_line = true]
		var salesAmountData = '<graph caption="${message("admin.sales.amount")}" subcaption="${beginDate?string(dateFormat)} ~ ${endDate?string(dateFormat)}" decimalPrecision="0" formatNumberScale="0" numberPrefix="${setting.currencySign}" chartRightMargin="30" showValues="0" yAxisMaxValue="1000" baseFontSize="12">
		[#list salesAmountMap.keySet() as key]
			<set name="${key?string(dateFormat)}" value="${salesAmountMap.get(key)}" hoverText="${key?string(dateFormat)}" \/>
		[/#list]
		<\/graph>';
		var salesVolumeData = '<graph caption="${message("admin.sales.volume")}" subcaption="${beginDate?string(dateFormat)} ~ ${endDate?string(dateFormat)}" decimalPrecision="0" formatNumberScale="0" chartRightMargin="30" showValues="0" yAxisMaxValue="100" baseFontSize="12">
		[#list salesVolumeMap.keySet() as key]
			<set name="${key?string(dateFormat)}" value="${salesVolumeMap.get(key)}" hoverText="${key?string(dateFormat)}" \/>
		[/#list]
		<\/graph>';
	[/@compress]
	
	[@flash_message /]
	
	var salesAmountChart = new FusionCharts("${base}/resources/admin/fusionCharts/Charts/FCF_Line.swf", "salesAmountChart", "800", "300");
	salesAmountChart.addParam("wmode", "Opaque");
	salesAmountChart.setDataXML(salesAmountData);
	salesAmountChart.render("salesAmountChart");
	
	var salesVolumeChart = new FusionCharts("${base}/resources/admin/fusionCharts/Charts/FCF_Line.swf", "salesVolumeChart", "800", "300");
	salesVolumeChart.addParam("wmode", "Opaque");
	salesVolumeChart.setDataXML(salesVolumeData);
	salesVolumeChart.render("salesVolumeChart");
	
	// 统计类型
	$type.change(function() {
		$beginDate.val("");
		$endDate.val("");
	});
	
	// 表单验证
	$inputForm.validate({
		rules: {
			beginDate: "required",
			endDate: "required"
		}
	});
	
});
</script>
</head>
<body>
	<div class="path">
		<a href="${base}${adminPath}/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.sales.view")}
	</div>
	<form id="inputForm" action="view.jhtml" method="get">
		<table class="input">
			<tr>
				<td colspan="2">
					<div id="salesAmountChart" style="height: 320px;"></div>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<div id="salesVolumeChart" style="height: 320px;"></div>
				</td>
			</tr>
			<tr>
				<th>
					${message("admin.sales.type")}:
				</th>
				<td>
					<select id="type" name="type">
						[#list types as salesType]
							<option value="${salesType}"[#if salesType == type] selected="selected"[/#if]>${message("admin.sales." + salesType)}</option>
						[/#list]
					</select>
				</td>
			</tr>
			<tr>
				<th>
					${message("admin.sales.beginDate")}:
				</th>
				<td>
					<input type="text" id="beginDate" name="beginDate" class="text Wdate" value="${beginDate?string(dateFormat)}" onfocus="WdatePicker({dateFmt: $dp.$('type').value == 'year' ? 'yyyy' : 'yyyy-MM', maxDate: '#F{$dp.$D(\'endDate\')}'});" />
				</td>
			</tr>
			<tr>
				<th>
					${message("admin.sales.endDate")}:
				</th>
				<td>
					<input type="text" id="endDate" name="endDate" class="text Wdate" value="${endDate?string(dateFormat)}" onfocus="WdatePicker({dateFmt: $dp.$('type').value == 'year' ? 'yyyy' : 'yyyy-MM', minDate: '#F{$dp.$D(\'beginDate\')}'});" />
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