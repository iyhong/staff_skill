<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title></title>
</head>
<body>
<a href="<c:url value="/StaffInsertAction"/>">등록</a>
<form action="<c:url value="/StaffSearchAction"/>" method="post">
	<table border="1" style="width:70%">
		<tr>
			<td>이름</td>
			<td><input type="text" name="name"></td>
			<td>성별</td>
			<td><input type="checkbox" name="gender" value="m">남<input type="checkbox" name="gender" value="w">여</td>
			<td>종교</td>
			<td>
				<select name="religionNo">
					<c:forEach items="${religionList}" var="re">
						<option value="${re.no}" >${re.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td>학력</td>
			<td>
				<c:forEach items="${schoolList}" var="sc">
					<input type="checkbox" name="schoolNo" value="${sc.no}">${sc.graduate}
				</c:forEach>
			</td>
			<td>기술</td>
			<td colspan="3">
				<c:forEach items="${skillList}" var="sk">
					<input type="checkbox" name="skillNo" value="${sk.no}">${sk.name}
				</c:forEach>
				
			</td>
		</tr>
		<tr>
			<td>졸업일</td>
			<td colspan="5">
				<input type="date" name="graduateDayStart">~<input type="date" name="graduateDayEnd">
			</td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<input type="submit" value="조회">
			</td>
		</tr>
	</table>
</form>


</body>
</html>