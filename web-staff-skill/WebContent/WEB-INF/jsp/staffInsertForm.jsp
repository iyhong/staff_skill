<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title></title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script>

	$(document).ready(function(){
	    $("#btn").click(function(){
	    	let sn1 = $("#sn1").val();
	        let sn2 = $("#sn2").val();
	        let graduateDay = $("#graduateDay").val();
	        if($("#name").val()==""){
	        	alert("�̸��� �Է��ϼ���!");
	        	$("#name").focus();
	        }else if(sn1.length<6){
	        	alert("������� 6�ڸ��� ��Ȯ�� �Է����ּ���");
	        	$("#sn1").focus();
	        }else if(sn2.length<7){
	        	alert("�ֹι�ȣ �� 7�ڸ��� ��Ȯ�� �Է��� �ּ���");
	        	$("#sn2").focus();
	        }else if(graduateDay==""){
	        	alert("�������� �Է��ϼ���");
	        }else{
	        	$("#insert").submit();
	        }
	    });
	});

</script>
</head>
<body>
	<h1>Staff Insert Form</h1>
	�̸�/�ֹι�ȣ/����/�з�/������ (�ʼ��Է�) ���(�����Է�)
	<form id="insert" action="<c:url value="/StaffInsertAction"/>" method="post">
	<table border="1" style="width:70%">
		<tr>
			<td>�̸�</td>
			<td><input id="name" type="text" name="name"></td>
			<td>�ֹι�ȣ</td>
			<td><input id="sn1" type="text" name="sn1" size="10px" maxlength="6">-<input id="sn2" type="password" name="sn2" size="10px" maxlength="7"></td>
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
					<input type="radio" name="schoolNo" value="${sc.no}" <c:if test="${sc.no==1}">checked</c:if>>${sc.graduate}
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
				<input id="graduateDay" type="date" name="graduateDay">			
			</td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<input id="btn" type="button" value="���">
				<input type="reset" value="�ʱ�ȭ">
			</td>
			
		</tr>
		
	</table>
</form>
	
</body>
</html>