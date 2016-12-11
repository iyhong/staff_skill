<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title></title>
</head>
<body>
	<h1>Staff Insert Form</h1>
	�̸�/�ֹι�ȣ/����/�з�/������ (�ʼ��Է�) ���(�����Է�)
	<form action="<c:url value="/StaffInsertAction"/>" method="post">
	<table border="1" style="width:70%">
		<tr>
			<td>�̸�</td>
			<td><input type="text" name="name"></td>
			<td>�ֹι�ȣ</td>
			<td><input type="text" name="sn1" size="10px">-<input type="password" name="sn2" size="10px"></td>
			<td>����</td>
			<td>
				<select name="religionNo">
					<c:forEach items="${religionList}" var="re">
						<option value="${re.no}" >${re.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td>�з�</td>
			<td>
				<c:forEach items="${schoolList}" var="sc">
					<input type="radio" name="schoolNo" value="${sc.no}">${sc.graduate}
				</c:forEach>
			</td>
			<td>���</td>
			<td colspan="3">
				<c:forEach items="${skillList}" var="sk">
					<input type="checkbox" name="skillNo" value="${sk.no}">${sk.name}
				</c:forEach>
			</td>
		</tr>
		<tr>
			<td>������</td>
			<td colspan="5">
				<input type="date" name="graduateDay">			
			</td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<input type="submit" value="���">
				<input type="reset" value="�ʱ�ȭ">
			</td>
			
		</tr>
		
	</table>
</form>
	
</body>
</html>