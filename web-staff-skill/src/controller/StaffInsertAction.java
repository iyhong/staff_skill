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

@WebServlet("/insert")
public class StaffInsertAction extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<Skill> skillList = Dao.selectSkill();
		ArrayList<School> schoolList = Dao.selectSchool();
		ArrayList<Religion> religionList = Dao.selectReligion();
		
		request.setAttribute("skillList", skillList);
		request.setAttribute("schoolList", schoolList);
		request.setAttribute("religionList", religionList);
		
		request.getRequestDispatcher("/WEB-INF/jsp/staffInsertForm.jsp").forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("euc-kr");
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
		staff.setName(name);
		staff.setSn(sn);
		staff.setReligion(religion);
		staff.setSchool(school);
		staff.setGraduateday(graduateDay);
		//System.out.println("skillNo[0]"+skillNo[0]);
		Dao.insertStaff(staff, skillNo);
		
		response.sendRedirect(request.getContextPath()+"/StaffSearchAction");

	}

}
