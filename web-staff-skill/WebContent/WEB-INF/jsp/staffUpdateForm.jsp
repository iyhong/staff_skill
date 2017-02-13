<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>
	<h1>Staff Updated Form</h1>
	
	<form action="<c:url value="/update?no=${staff.no}"/>" method="post">
	<table border="1" style="width:70%">
		<tr>
			<td>이름</td>
			<td><input type="text" name="name" value="${staff.name}"></td>
			<td>주민번호</td>
			<td><input type="text" name="sn1" size="10px" value="${sn1}">-<input type="password" name="sn2" size="10px" value="${sn2}"></td>
			<td>종교</td>
			<td>
				<select name="religionNo">
					<c:forEach items="${religionList}" var="re">
						<option value="${re.no}" <c:if test="${re.name==staff.religion.name}">selected</c:if>>${re.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td>학력</td>
			<td>
				<c:forEach items="${schoolList}" var="sc">
					<input type="radio" name="schoolNo" value="${sc.no}" <c:if test="${sc.graduate==staff.school.graduate}">checked</c:if>>${sc.graduate}
				</c:forEach>
			</td>
			<td>기술</td>
			<td colspan="3">
				<c:forEach items="${skillList}" var="sk">
					<input type="checkbox" name="skillNo" value="${sk.no}" ${sk.checked}>${sk.name}
				</c:forEach>
			</td>
		</tr>
		<tr>
			<td>졸업일</td>
			<td colspan="5">
				<input type="date" name="graduateDay" value="${staff.graduateday}">			
			</td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<input type="submit" value="수정">
				<input type="reset" value="초기화">
			</td>
			
		</tr>
		
	</table>
</form>
</body>
</html>