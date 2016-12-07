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

@WebServlet("/StaffInsertAction")
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
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
