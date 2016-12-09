<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title></title>
</head>
<body>
<%-- <%@ include file="/WEB-INF/jsp/staffSearchForm.jsp" %> --%>
<jsp:include page="/WEB-INF/jsp/staffSearchForm.jsp" />
<br>��ȸ���<br>
	<table border="1" style="width:70%">
		<tr>
			<td>�̸�</td>
			<td>����</td>
			<td>���� </td>
			<td>�з�</td>
			<td>���</td>
			<td>������</td>
			<td>����</td>
			<td>����</td>
		</tr>
			<c:forEach items="${list}" var="staff">
				<tr>
					<td>${staff.name}</td>
					<td>${staff.sn}</td>
					<td>${staff.religion.name}</td>
					<td>${staff.school.graduate}</td>
					<td>
						<c:forEach items="${staff.skillList}" var="skillList">
							${skillList.name} 
						</c:forEach>
					</td>
					<td>${staff.graduateday}</td>
					
					<td><a href="<c:url value="/StaffUpdateAction?no=${staff.no}"/>">����</a></td>
					<td><a href="<c:url value="/StaffDeleteAction?no=${staff.no}"/>">����</a></td>
				</tr>
			</c:forEach>
			
		
	</table>
</body>
</html>