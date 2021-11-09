<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cssConf" uri="/WEB-INF/tld/addons/ysapproductconfigaddon/sapproductconfig.tld"%>

<c:set var="variantOverview" value="${overviewUiData.overviewMode eq 'VARIANT_OVERVIEW'}" />
<c:set var="quotationVariantOverview" value="${overviewUiData.overviewMode eq 'QUOTATION_VARIANT_OVERVIEW'}" />
<c:set var="orderVariantOverview" value="${overviewUiData.overviewMode eq 'ORDER_VARIANT_OVERVIEW'}" />
<c:set var="savedCartVariantOverview" value="${overviewUiData.overviewMode eq 'SAVED_CART_VARIANT_OVERVIEW'}" />

<c:set var="anyVariantOverview"
	value="${variantOverview or quotationVariantOverview or orderVariantOverview or savedCartVariantOverview}" />

<c:set var="imageVisible"
	value="${(not empty config and !config.hideImageGallery) or (not empty overviewUiData and anyVariantOverview)}" />

<c:choose>
	<c:when test="${imageVisible}">
		<c:set value="open" var="chevronClass" />
	</c:when>
	<c:otherwise>
		<spring:url value="close" var="chevronClass" />
	</c:otherwise>
</c:choose>

<div class="product-details">
	<div id="productName" class="name product-details-glyphicon-chevron-${chevronClass}">${product.name}
		<span class="sku">ID ${product.code}</span>
	</div>
	<c:if test="${cssConf:isExpertModeEnabled()}">
		<div id="cpqDebugInfo" class="cpq-debug-info">
			<div>
				<label class="cpq-debug-info-name" for="cpqKbKeyName"><spring:theme code="sapproductconfig.debuginfo.kbName"
						text="KBName (Default)" /></label>
				<div class="cpq-debug-info-value" id="cpqKbKeyName">${config.kbKey.kbName}</div>
				<div>|</div>
			</div>
			<div>
				<label class="cpq-debug-info-name" for="cpqKbKeyLogsys"><spring:theme code="sapproductconfig.debuginfo.kbLogsys"
						text="KBLogsys (Default)" /></label>
				<div class="cpq-debug-info-value" id="cpqKbKeyLogsys">${config.kbKey.kbLogsys}</div>
				<div>|</div>
			</div>
			<div>
				<label class="cpq-debug-info-name" for="cpqKbKeyVersion"><spring:theme code="sapproductconfig.debuginfo.kbVersion"
						text="KBVersion (Default)" /></label>
				<div class="cpq-debug-info-value" id="cpqKbKeyVersion">${config.kbKey.kbVersion}</div>
				<div>|</div>
			</div>
			<div>
				<label class="cpq-debug-info-name" for="cpqKbBuildNumber"><spring:theme
						code="sapproductconfig.debuginfo.kbBuildNumber" text="KBBuildNumber (Default)" /></label>
				<div class="cpq-debug-info-value" id="cpqKbBuildNumber">${config.kbBuildNumber}</div>
			</div>
		</div>
	</c:if>
</div>


