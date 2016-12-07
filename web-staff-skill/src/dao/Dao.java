package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dto.Religion;
import dto.School;
import dto.Skill;

public class Dao {
	static Connection conn = null;
	static PreparedStatement pstmt;
	static ResultSet rs;
	
	public static ArrayList<Skill> selectSkill(){
		Skill skill = null;
		ArrayList<Skill> skillList = null;
		try{
			conn = DBUtil.getConnection();
			System.out.println("conn : "+conn);
			pstmt = conn.prepareStatement("select no,name from skill");
			rs = pstmt.executeQuery();
			skillList = new ArrayList<Skill>();
			while(rs.next()){
				skill = new Skill(rs.getInt("no"),rs.getString("name"));
				System.out.println(skill);
				skillList.add(skill);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return skillList;
	}
	public static ArrayList<School> selectSchool(){
		School school = null;
		ArrayList<School> schoolList = null;
		try{
			conn = DBUtil.getConnection();
			System.out.println("conn : "+conn);
			pstmt = conn.prepareStatement("select no,graduate from school");
			rs = pstmt.executeQuery();
			schoolList = new ArrayList<School>();
			while(rs.next()){
				school = new School(rs.getInt("no"),rs.getString("graduate"));
				System.out.println(school);
				schoolList.add(school);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return schoolList;
	}
	public static ArrayList<Religion> selectReligion(){
		Religion religion = null;
		ArrayList<Religion> religionlList = null;
		try{
			conn = DBUtil.getConnection();
			System.out.println("conn : "+conn);
			pstmt = conn.prepareStatement("select no,name from religion");
			rs = pstmt.executeQuery();
			religionlList = new ArrayList<Religion>();
			while(rs.next()){
				religion = new Religion(rs.getInt("no"),rs.getString("name"));
				System.out.println(religion);
				religionlList.add(religion);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return religionlList;
	}
	
	
	public static void close(){
		if (rs != null)	try { rs.close();} catch (SQLException ex) {}
		if (pstmt != null) try { pstmt.close();	} catch (SQLException ex) {}
		if (conn != null) try {	conn.close(); } catch (SQLException ex) {}
	}
}
