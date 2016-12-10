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
		
		request.setAttribute("skillList", skillList);
		request.setAttribute("schoolList", schoolList);
		request.setAttribute("religionList", religionList);
		request.setAttribute("staff", staff);
		request.setAttribute("sn1", sn1);
		request.setAttribute("sn2", sn2);
		request.getRequestDispatcher("/WEB-INF/jsp/staffUpdateForm.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
