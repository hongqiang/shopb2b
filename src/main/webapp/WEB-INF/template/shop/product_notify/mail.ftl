<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("shop.productNotify.mailTitle")} - Powered By HONGQIANG_SHOP</title>
<meta name="author" content="HONGQIANG_SHOP Team" />
<meta name="copyright" content="HONGQIANG_SHOP" />
</head>
<body>
	<p>
		${message("shop.productNotify.welcome")}:
	</p>
	<p>
		${message("shop.productNotify.content", productNotify.product.name)}
	</p>
	<p>
		<a href="${setting.siteUrl}${productNotify.product.path}">${setting.siteUrl}${productNotify.product.path}</a>
	</p>
</body>
</html>