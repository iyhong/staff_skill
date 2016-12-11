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
		System.out.println("StaffUpdateAction doGet() ����!!");
		int no = Integer.parseInt(request.getParameter("no"));
		System.out.println("StaffUpdateAction�� no : "+no);
		Staff staff = new Staff();
		staff.setNo(no);
		staff = Dao.oneStaff(staff);
		
		//sn���ڿ��� �� �� ������ ������ ��� request �� setting
		String sn = staff.getSn();
		String sn1 = sn.substring(0, 6);
		String sn2 = sn.substring(7);
		System.out.println("sn1 : "+sn1);
		System.out.println("sn2 : "+sn2);
		
		ArrayList<Religion> religionList = Dao.selectReligion();
		ArrayList<School> schoolList = Dao.selectSchool();
		ArrayList<Skill> skillList = Dao.selectSkill();
		
		//skill dto�� checked ���ڿ� ������Ƽ�� �������ִ� ��ų�鿡�� checked ���ڿ� ����
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
		//�̸��ޱ�
		String name = request.getParameter("name");
		System.out.println("name:"+name);
		
		//�ֹι�ȣ�ޱ�
		String sn1 = request.getParameter("sn1");
		String sn2 = request.getParameter("sn2");
		String sn = sn1 +"-"+ sn2;
		System.out.println("sn:"+sn);
		
		//������ȣ
		int religionNo = Integer.parseInt(request.getParameter("religionNo"));
		Religion religion = new Religion(religionNo);
		System.out.println(religion);
		
		//�з¹�ȣ
		int schoolNo = Integer.parseInt(request.getParameter("schoolNo"));
		School school = new School(schoolNo);
		System.out.println(school);
		
		//�����ȣ ������
		String[] skillStr = request.getParameterValues("skillNo");
		int[] skillNo =null;
		if(skillStr!=null){
			skillNo = new int[skillStr.length];
			for(int i = 0;i<skillStr.length;i++){
				skillNo[i] = Integer.parseInt(skillStr[i]);
				System.out.println("skillNo["+i+"]:"+skillNo[i]);
			}
		}
		
		//������
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
