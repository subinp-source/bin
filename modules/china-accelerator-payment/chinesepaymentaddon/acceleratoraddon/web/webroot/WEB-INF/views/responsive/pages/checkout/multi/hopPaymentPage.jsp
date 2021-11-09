<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<spring:htmlEscape defaultHtmlEscape="true"/>
<spring:url value="/checkout/multi/summary/payRightNow/{/code}" var="payRightNowUrl" htmlEscape="false">
	<spring:param name="code"  value="${orderData.code}"/>
</spring:url>
<spring:url value="/checkout/multi/summary/checkPaymentResult/{/code}" var="resultUrl" htmlEscape="false">
	<spring:param name="code"  value="${orderData.code}"/>
</spring:url>

<div class="row">
	<div class="checkout-headline">
		<spring:theme code="checkout.multi.clicktopay" />
	</div>

	<div class="col-sm-5 col-md-4 col-lg-4">
		<div class="step-body hop-payment-border">
			<div class="checkout-shipping hop-payment-border">
				<div class="form-group">
					<label class="control-label "> 
						<spring:theme code="order.submit.order.code" />
					</label>
					<div class="controls">
						<spring:theme text="${orderData.code}" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label "> 
						<spring:theme code="order.submit.due.payment" />
					</label>
					<div class="controls">
						<format:price priceData="${orderData.totalPrice}" />
					</div>
				</div>
				<div class="row">
					<div class="col-lg-7 col-md-12 col-sm-12">
						<a id="payRightNowButton"
							class="btn btn-primary btn-block checkout-next"
							data-order-code="${fn:escapeXml(orderData.code)}"
							data-payment="${fn:escapeXml(orderData.chinesePaymentInfo.paymentProvider)}"
							data-href="${fn:escapeXml(payRightNowUrl)}" href="javascript:;"
							<c:if test="${payInNewWindow}">target="_blank"</c:if>
							disabled="disabled"> 
							<spring:theme code="checkout.multi.payrightnow" />
						</a>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="row pay-pop-container">
	    <div class="payPop col-lg-4 col-sm-6 col-md-6 col-xs-12">
	    	<p><spring:theme code="order.payment.remark" /></p>
	        <div class="payPopBtn clearfix">
	        	<a href="${fn:escapeXml(resultUrl)}" class="btn btn-primary btn-block checkout-next"><spring:theme code="order.payment.successfully" /></a><br/>
	            <a href="${fn:escapeXml(resultUrl)}" class="btn btn-primary btn-block checkout-next"><spring:theme code="order.payment.rencontre.problem" /></a>
	            <br><br>
	        </div>	
	    </div>
	</div>
	<div class="mask"></div>
</div>	

