<%@ taglib prefix="profiletag" tagdir="/WEB-INF/tags/addons/profiletagaddon/shared/profiletag" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<profiletag:profileTag/>

<script>
    var profiletag_context = {
            "categoryIds": [
                <c:forEach items="${categoryIdList}" var="item" varStatus="iterator">
                "${item}"
                <c:if test="${!iterator.last}">, </c:if>
                </c:forEach>
            ]
        }
    ;
</script>
