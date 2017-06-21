<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript">
	
	window.onload = function(){
		fnNmprCndChange();
	}

	function fnList() {
		var form = $("form[id=frmDetail]");
		form.attr({"method":"post","action":"<c:url value='/cart/list/'/>"});
		form.submit();
	}

	function fnSearch(cl_code) {
		var form = $("form[id=frmDetail]");
		$("input:hidden[id=hidPage]").val(1);
		form.attr({"method":"post","action":"<c:url value='/cart/list/'/>"});
		form.submit();
	}

	function fnNmprCndChange(){
		var form = $("form[id=frmDetail]");
		$("input:text[id=txtPay]").val($("input:hidden[id=hidNmprCnd"+ $("input:radio[id=rdoNmprCnd]:checked").val() +"]").val());
	}
</script>
<div align="center">
	<form id="frmDetail" name="frmDetail" action="<c:url value='/cart/list/'/>">
	<input type="hidden" id="hidPage" name="hidPage" value="${hidPage}">
	<input type="hidden" id="hidCartSn" name="hidCartSn" value="${cart_sn}">
	<table width="1024px" border="1" cellspacing="0" cellpadding="0" height="100%" style="border-collapse:collapse; border:1px gray solid;">
		<tr height="100px">
			<td align="center" colspan="2">
				${result.GOODS_NM}
			</td>
		</tr>
		<tr height="150px">
			<td align="center" colspan="2">
				${result.GOODS_INTRCN}
			</td>
		</tr>
		<tr height="430px">
			<td align="center" colspan="2">
			<iframe src="<c:url value='/file/imageListIframe/'/>?file_code=${result.FILE_CODE}" width="100%" height="100%"></iframe>
			</td>
		</tr>
		<tr height="80px">
			<td align="center" colspan="2">
				<c:forEach var="list" items="${clList}" varStatus="status">
				<a href="javascript:fnSearch('${list.CL_CODE}');">#${list.CL_NM}</a>&nbsp;&nbsp;&nbsp;
				</c:forEach>
			</td>
		</tr>
		<tr height="200px">
			<td align="center">
				<c:forEach var="list" items="${schdulList}" varStatus="status">
				${fn:substring(list.BEGIN_DE,0,4)}. ${fn:substring(list.BEGIN_DE,4,6)}. ${fn:substring(list.BEGIN_DE,6,8)} ~ 
				${fn:substring(list.END_DE,0,4)}. ${fn:substring(list.END_DE,4,6)}. ${fn:substring(list.END_DE,6,8)}
				&nbsp;&nbsp;&nbsp;
				<a href="javascript:fnCalendarPopup('txtDate','${list.BEGIN_CAL_DE}','${list.END_CAL_DE}')">예약일 선택</a>
				<br><br>
				</c:forEach>
				
				<input type="text" name="txtDate" id="txtDate" style="width:250px;height:50px;text-align:center;font-size:25px;" value ="${fn:substring(result.TOUR_DE,0,4)}-${fn:substring(result.TOUR_DE,4,6)}-${fn:substring(result.TOUR_DE,6,8)}" readonly onfocus="this.blur()">
			</td>
			<td align="center">
				<c:forEach var="list" items="${nmprList}" varStatus="status">
				<input type="radio" id="rdoNmprCnd" name="rdoNmprCnd" value="${list.NMPR_SN}" <c:if test="${result.NMPR_SN eq list.NMPR_SN}">checked</c:if> onchange="fnNmprCndChange()"> ${list.NMPR_CND} : ₩ ${list.SETUP_AMOUNT}
				<input type="hidden" id="hidNmprCnd${list.NMPR_SN}" name="hidNmprCnd${list.NMPR_SN}" value="₩ ${list.SETUP_AMOUNT}">
				<br>
				</c:forEach>
				<br>
				<input type="text" name="txtPay" id="txtPay" style="width:250px;height:50px;text-align:center;font-size:25px;" value ="" readonly onfocus="this.blur()">
			</td>
		</tr>
	</table>
	</form>	
</div>

<div style="height:100px;" align="center">
	<p style="vertical-align:middle;">
		<a href="javascript:fnList()">장바구니 목록</a>&nbsp;&nbsp;&nbsp;
	</p>
</div>