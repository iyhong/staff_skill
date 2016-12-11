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
	        	alert("이름을 입력하세요!");
	        	$("#name").focus();
	        }else if(sn1.length<6){
	        	alert("생년월일 6자리를 정확히 입력해주세요");
	        	$("#sn1").focus();
	        }else if(sn2.length<7){
	        	alert("주민번호 뒤 7자리를 정확히 입력해 주세요");
	        	$("#sn2").focus();
	        }else if(graduateDay==""){
	        	alert("졸업일을 입력하세요");
	        }else{
	        	$("#insert").submit();
	        }
	    });
	});

</script>
</head>
<body>
	<h1>Staff Insert Form</h1>
	이름/주민번호/종교/학력/졸업일 (필수입력) 기술(선택입력)
	<form id="insert" action="<c:url value="/StaffInsertAction"/>" method="post">
	<table border="1" style="width:70%">
		<tr>
			<td>이름</td>
			<td><input id="name" type="text" name="name"></td>
			<td>주민번호</td>
			<td><input id="sn1" type="text" name="sn1" size="10px" maxlength="6">-<input id="sn2" type="password" name="sn2" size="10px" maxlength="7"></td>
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
					<input type="radio" name="schoolNo" value="${sc.no}" <c:if test="${sc.no==1}">checked</c:if>>${sc.graduate}
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
				<input id="graduateDay" type="date" name="graduateDay">			
			</td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<input id="btn" type="button" value="등록">
				<input type="reset" value="초기화">
			</td>
			
		</tr>
		
	</table>
</form>
	
</body>
</html>