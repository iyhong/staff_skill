package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Dao;
import dto.Religion;
import dto.School;
import dto.Skill;
import dto.Staff;

@WebServlet("/StaffUpdateAction")
public class StaffUpdateAction extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("StaffUpdateAction doGet() 진입!!");
		int no = Integer.parseInt(request.getParameter("no"));
		System.out.println("StaffUpdateAction의 no : "+no);
		Staff staff = new Staff();
		staff.setNo(no);
		staff = Dao.oneStaff(staff);
		
		//sn문자열을 앞 뒤 나누어 변수에 담고 request 에 setting
		String sn = staff.getSn();
		String sn1 = sn.substring(0, 6);
		String sn2 = sn.substring(7);
		System.out.println("sn1 : "+sn1);
		System.out.println("sn2 : "+sn2);
		
		ArrayList<Religion> religionList = Dao.selectReligion();
		ArrayList<School> schoolList = Dao.selectSchool();
		ArrayList<Skill> skillList = Dao.selectSkill();
		
		//skill dto에 checked 문자열 프로퍼티에 가지고있는 스킬들에만 checked 문자열 셋팅
		for(int i = 0;i<staff.getSkillList().size();i++){
			//System.out.println(staff.getSkillList().get(i).getNo());
			int checkedNum = staff.getSkillList().get(i).getNo();
			skillList.get(checkedNum-1).setChecked("checked");
		}
		System.out.println(skillList);
		
		request.setAttribute("skillList", skillList);
		request.setAttribute("schoolList", schoolList);
		request.setAttribute("religionList", religionList);
		request.setAttribute("staff", staff);
		request.setAttribute("sn1", sn1);
		request.setAttribute("sn2", sn2);
		request.getRequestDispatcher("/WEB-INF/jsp/staffUpdateForm.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("euc-kr");
		int no = Integer.parseInt(request.getParameter("no"));
		System.out.println("no:"+no);
		//이름받기
		String name = request.getParameter("name");
		System.out.println("name:"+name);
		
		//주민번호받기
		String sn1 = request.getParameter("sn1");
		String sn2 = request.getParameter("sn2");
		String sn = sn1 +"-"+ sn2;
		System.out.println("sn:"+sn);
		
		//종교번호
		int religionNo = Integer.parseInt(request.getParameter("religionNo"));
		Religion religion = new Religion(religionNo);
		System.out.println(religion);
		
		//학력번호
		int schoolNo = Integer.parseInt(request.getParameter("schoolNo"));
		School school = new School(schoolNo);
		System.out.println(school);
		
		//기술번호 여러값
		String[] skillStr = request.getParameterValues("skillNo");
		int[] skillNo =null;
		if(skillStr!=null){
			skillNo = new int[skillStr.length];
			for(int i = 0;i<skillStr.length;i++){
				skillNo[i] = Integer.parseInt(skillStr[i]);
				System.out.println("skillNo["+i+"]:"+skillNo[i]);
			}
		}
		
		//졸업일
		String graduateDay = request.getParameter("graduateDay");
		System.out.println("graduateDay:"+graduateDay);
		
		Staff staff = new Staff();
		staff.setNo(no);
		staff.setName(name);
		staff.setSn(sn);
		staff.setReligion(religion);
		staff.setSchool(school);
		staff.setGraduateday(graduateDay);
		System.out.println("UpdateAction staff : "+staff);
		//Dao.insertStaff(staff, skillNo);
		Dao.updatedStaff(staff, skillNo);
		response.sendRedirect(request.getContextPath()+"/StaffSearchAction");
	}

}
