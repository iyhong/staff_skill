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
import dto.Search;
import dto.Skill;
import dto.Staff;

@WebServlet("/StaffSearchAction")
public class StaffSearchAction extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<Skill> skillList = Dao.selectSkill();
		ArrayList<School> schoolList = Dao.selectSchool();
		ArrayList<Religion> religionList = Dao.selectReligion();
		
		request.setAttribute("skillList", skillList);
		request.setAttribute("schoolList", schoolList);
		request.setAttribute("religionList", religionList);
		
		
		ArrayList<Staff> list = Dao.allStaff();
		request.setAttribute("list", list);
		request.getRequestDispatcher("/WEB-INF/jsp/staffSearchList.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("StaffSearchAction doPost()");
		ArrayList<Skill> skillList = Dao.selectSkill();
		ArrayList<School> schoolList = Dao.selectSchool();
		ArrayList<Religion> religionList = Dao.selectReligion();
		
		request.setAttribute("skillList", skillList);
		request.setAttribute("schoolList", schoolList);
		request.setAttribute("religionList", religionList);
		
		request.setCharacterEncoding("euc-kr");
		
		Search search = new Search();
		
		//이름받기
		if(request.getParameter("name")!=null){
			String name = request.getParameter("name");
			System.out.println("name : "+name);
			search.setName(name);
		}
		//성별받기
		if(request.getParameter("gender")!=null){
			String[] gender = request.getParameterValues("gender");
			for(int i = 0 ; i<gender.length;i++){
				System.out.println("gender : " + gender[i]);
			}
			search.setGender(gender);
		}
		
		//종교받기
		if(request.getParameter("religionNo")!= null){
			String[] religionStr = request.getParameterValues("religionNo");
			int[] religion = new int[religionStr.length];
			for(int i = 0 ; i<religionStr.length;i++){
				religion[i] = Integer.parseInt(religionStr[i]);
				System.out.println("religion : " + religion[i]);
			}
			search.setReligionNo(religion);
		}
		
		//학력받기
		if(request.getParameter("schoolNo")!=null){
			String[] schoolStr = request.getParameterValues("schoolNo");
			int[] school = new int[schoolStr.length];
			for(int i = 0 ; i<schoolStr.length;i++){
				school[i] = Integer.parseInt(schoolStr[i]);
				System.out.println("school : " + school[i]);
			}
			search.setSchoolNo(school);
		}
		
		//기술받기
		if(request.getParameter("skillNo")!=null){
			String[] skillStr = request.getParameterValues("skillNo");
			int[] skill = new int[skillStr.length];
			for(int i = 0 ; i<skillStr.length;i++){
				skill[i] = Integer.parseInt(skillStr[i]);
				System.out.println("skill : " + skill[i]);
			}
			search.setSkillNo(skill);
		}
		//졸업일받기
		if(request.getParameter("graduateDayStart")!= null){
			String graduateDayStart = request.getParameter("graduateDayStart");
			System.out.println("graduateDayStart : "+graduateDayStart);
			search.setGraduateDayStart(graduateDayStart);
		}
		if(request.getParameter("graduateDayEnd")!=null){
			String graduateDayEnd = request.getParameter("graduateDayEnd");
			System.out.println("graduateDayEnd : "+graduateDayEnd);
			search.setGraduateDayEnd(graduateDayEnd);
		}
		System.out.println(search);
		ArrayList<Staff> list = Dao.searchStaff(search);
		request.setAttribute("list", list);
		request.getRequestDispatcher("/WEB-INF/jsp/staffSearchList.jsp").forward(request, response);
	}

}
