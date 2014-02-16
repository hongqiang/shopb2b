<div class="span6">
	<div class="info">
		<div class="top"></div>
		<div class="content">
			<p>${message("shop.member.navigation.welcome", member.username)}</p>
			<p>
				${message("shop.member.navigation.memberRank")}:
				<span class="red">${member.memberRank.name}</span>
			</p>
		</div>
		<div class="bottom"></div>
	</div>
	<div class="menu">
		<div class="title">
			<a href="${base}${memberPath}/index.jhtml">${message("shop.member.navigation.title")}</a>
		</div>
		<div class="content">
			<dl>
				<dt>${message("shop.member.navigation.order")}</dt>
				<dd>
					<a href="${base}${memberPath}/order/list.jhtml"[#if current == "orderList"] class="current"[/#if]>${message("shop.member.order.list")}</a>
				</dd>
				<dd>
					<a href="${base}${memberPath}/coupon_code/list.jhtml"[#if current == "couponCodeList"] class="current"[/#if]>${message("shop.member.couponCode.list")}</a>
				</dd>
				<dd>
					<a href="${base}${memberPath}/coupon_code/exchange.jhtml"[#if current == "couponCodeExchange"] class="current"[/#if]>${message("shop.member.couponCode.exchange")}</a>
				</dd>
			</dl>
			<dl>
				<dt>${message("shop.member.navigation.favorite")}</dt>
				<dd>
					<a href="${base}${memberPath}/favorite/list.jhtml"[#if current == "favoriteList"] class="current"[/#if]>${message("shop.member.favorite.list")}</a>
				</dd>
				<dd>
					<a href="${base}${memberPath}/product_notify/list.jhtml"[#if current == "productNotifyList"] class="current"[/#if]>${message("shop.member.productNotify.list")}</a>
				</dd>
				<dd>
					<a href="${base}${memberPath}/review/list.jhtml"[#if current == "reviewList"] class="current"[/#if]>${message("shop.member.review.list")}</a>
				</dd>
				<dd>
					<a href="${base}${memberPath}/consultation/list.jhtml"[#if current == "consultationList"] class="current"[/#if]>${message("shop.member.consultation.list")}</a>
				</dd>
			</dl>
			<dl>
				<dt>${message("shop.member.navigation.message")}</dt>
				<dd>
					<a href="${base}${memberPath}/message/send.jhtml"[#if current == "messageSend"] class="current"[/#if]>${message("shop.member.message.send")}</a>
				</dd>
				<dd>
					<a href="${base}${memberPath}/message/list.jhtml"[#if current == "messageList"] class="current"[/#if]>${message("shop.member.message.list")}</a>
				</dd>
				<dd>
					<a href="${base}${memberPath}/message/draft.jhtml"[#if current == "messageDraft"] class="current"[/#if]>${message("shop.member.message.draft")}</a>
				</dd>
			</dl>
			<dl>
				<dt>${message("shop.member.navigation.profile")}</dt>
				<dd>
					<a href="${base}${memberPath}/profile/edit.jhtml"[#if current == "profileEdit"] class="current"[/#if]>${message("shop.member.profile.edit")}</a>
				</dd>
				<dd>
					<a href="${base}${memberPath}/password/edit.jhtml"[#if current == "passwordEdit"] class="current"[/#if]>${message("shop.member.password.edit")}</a>
				</dd>
				<dd>
					<a href="${base}${memberPath}/receiver/list.jhtml"[#if current == "receiverList"] class="current"[/#if]>${message("shop.member.receiver.list")}</a>
				</dd>
			</dl>
			<dl>
				<dt>${message("shop.member.navigation.deposit")}</dt>
				<dd>
					<a href="${base}${memberPath}/deposit/recharge.jhtml"[#if current == "depositRecharge"] class="current"[/#if]>${message("shop.member.deposit.recharge")}</a>
				</dd>
				<dd>
					<a href="${base}${memberPath}/deposit/list.jhtml"[#if current == "depositList"] class="current"[/#if]>${message("shop.member.deposit.list")}</a>
				</dd>
			</dl>
		</div>
		<div class="bottom"></div>
	</div>
</div>