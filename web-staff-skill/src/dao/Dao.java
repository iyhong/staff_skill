package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dto.Religion;
import dto.School;
import dto.Search;
import dto.Skill;
import dto.Staff;

public class Dao {
	static Connection conn = null;
	static PreparedStatement pstmt;
	static ResultSet rs;
	static ArrayList<Integer> noList = null;

	
	//모든 기술 가져오기
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
	
	//모든 학력정보 가져오기
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
	
	//모든 종교정보 가져오기
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
	
	//회원정보 입력하기 
	public static int insertStaff(Staff staff, int[] skillNo){
		int rowCount = 0;
		System.out.println("insertStaff() Dao.java");
		try{
			conn = DBUtil.getConnection();
			System.out.println("skillNo.size()"+skillNo.length);
			
			//staff 테이블에 입력
			pstmt = conn.prepareStatement("insert into staff (name,sn, GRADUATEDAY, SCHOOLNO, RELIGIONNO) values(?,?,?,?,?)");
			pstmt.setString(1, staff.getName());
			pstmt.setString(2, staff.getSn());
			pstmt.setString(3, staff.getGraduateday());
			pstmt.setInt(4, staff.getSchool().getNo());
			pstmt.setInt(5, staff.getReligion().getNo());
			pstmt.executeUpdate();
			System.out.println("staff 입력성공");
			
			//staff 테이블에 방금 입력된 no 가져옴
			pstmt = conn.prepareStatement("select no from staff where sn=?");
			pstmt.setString(1, staff.getSn());
			rs = pstmt.executeQuery();
			System.out.println("rs:"+rs);
			int staffNo = 0;
			if(rs.next()){
				staffNo = rs.getInt("no");
				System.out.println("staffNo:"+staffNo);
			}
			
			//가져온 no값으로 staffskill 테이블에 값 입력
			pstmt = conn.prepareStatement("insert into staffskill (staffno,skillno) values(?,?)");
			for(int i = 0 ; i<skillNo.length;i++){
				System.out.println("반복문"+i);
				pstmt.setInt(1, staffNo);
				pstmt.setInt(2, skillNo[i]);
				pstmt.executeUpdate();
			}
			System.out.println("staffskill 입력성공");
		} catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		
		return rowCount;
		
	}
	
	//전체 staff 가져오기
	public static ArrayList<Staff> allStaff(){
		System.out.println("Dao.java allStaff() 진입");
		String sql = "";
		ArrayList<Staff> staffList = null;
		sql = "select st.`no`,st.name,substring(sn, 8,1)as sn,re.name as religionno , sc.graduate as schoolno,graduateday from staff st inner join religion re on st.religionno = re.`no` inner join school sc on st.schoolno = sc.`no` order by st.name asc";
		
		staffList = getList(sql);
		
		return staffList;
	}
	
	//전체 회원의 no 가져오기
	private static ArrayList<Integer> allSelect(){
		try{
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement("select no from staff");
			rs = pstmt.executeQuery();
			noList = new ArrayList<Integer>();
			while(rs.next()){
				noList.add(rs.getInt(1));
			}
		} catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return noList;
	}
	
	//성별에 따른 회원 no 가져오기
	private static ArrayList<Integer> genderSelect(String[] gender){
		int genderNo = 0;
		String sqlAdd = "";
		if(gender.length==1){
			if(gender[0].equals("m")){
				sqlAdd += "where substring(sn, 8,1)=1 or substring(sn, 8,1)=3";
			}else{
				sqlAdd += "where substring(sn, 8,1)=2 or substring(sn, 8,1)=4";
			}
		}else{
			sqlAdd += "";
		}
		try{
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement("select no from staff "+sqlAdd);
			pstmt.setInt(1, genderNo);
			rs = pstmt.executeQuery();
			noList = new ArrayList<Integer>();
			while(rs.next()){
				noList.add(rs.getInt(1));
			}
		} catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return noList;
	}
	
	//조회 결과에 따른 staff 가져오기
	public static ArrayList<Staff> searchStaff(Search search){
		System.out.println("Dao.java searchStaff() 진입");
		ArrayList<Staff> staffList = null;
		String sql = "select st.`no`, st.name,substring(sn, 8,1)as sn,re.name as religionno , sc.graduate as schoolno,graduateday from staff st inner join religion re on st.religionno = re.`no` inner join school sc on st.schoolno = sc.`no` where st.`no`='?' order by st.name asc;";
		noList = new ArrayList<Integer>();
		//뭔가 하나라도 선택해서 값을 받았으면 where 단어를 추가해준다
		if(search.getName()!=""||search.getGender()!=null||search.getReligionNo()!=0
				||search.getSchoolNo()!=null||search.getSkillNo()!=null
				||search.getGraduateDayStart()!=""||search.getGraduateDayEnd()!=""){
		}
		
		//성별 확인
		if(search.getGender()!=null){
			System.out.println("gnder 확인 분기문");
			noList = genderSelect(search.getGender());
		}
		
		//종교 확인
		if(search.getReligionNo()!=0){
			System.out.println("religion 확인 분기문");
			int religion = search.getReligionNo();
		}
		
		//학력 확인
		if(search.getSchoolNo()!=null){
			System.out.println("school 확인 분기문");
			int[] schoolList = search.getSchoolNo();
		}
		
		ArrayList<Integer> noList = null;
		
		staffList = getList(sql,noList);
	
		return staffList;
	}
	
	//리스트 가져오는 메서드
	private static ArrayList<Staff> getList(String sql){
		System.out.println("Dao.java getList() 진입");
		Staff staff;
		ArrayList<Staff> staffList = null;
		PreparedStatement skillPstmt;
		ResultSet skillRs;
		ArrayList<Skill> skillList = null;
		
		try{
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			staffList = new ArrayList<Staff>();
			while(rs.next()){
				staff = new Staff();
				System.out.println(staff);
				staff.setName(rs.getString("name"));
				
				//주민번호 잘른 숫자가져와서 담고 2로나누어 떨이지면 '여' 안떨어지면'남'을 셋팅
				int genderNum = Integer.parseInt(rs.getString("sn"));
				if(genderNum%2!=0){
					staff.setSn("남");
				}else{
					staff.setSn("여");
				}
				
				staff.setGraduateday(rs.getString("graduateday"));
				School school = new School();
				school.setGraduate(rs.getString("schoolno"));;
				staff.setSchool(school);
				Religion religion = new Religion();
				religion.setName(rs.getString("religionno"));
				staff.setReligion(religion);
				
				//skill 가져오는 쿼리문
				//skill 이 한 staff 마다 여러개 이므로 ArrayList에 담아 담은참조값을 staff에 담는다.
				skillPstmt = conn.prepareStatement("select  staffskill.staffno , staffskill.skillno, skill.name from staffskill inner join skill on staffskill.skillno = skill.`no`  where staffskill.staffno=? ; ");
				skillPstmt.setInt(1, rs.getInt("no"));	//현재 staff의 no 값으로 skill 테이블과 staffskill 테이블을 조인해 가져온다
				skillRs = skillPstmt.executeQuery();
				skillList = new ArrayList<Skill>();
				
				//skill 에 결과값을 하나씩 넣고 그것들을 ArrayList에 담는다.
				while(skillRs.next()){
					Skill skill = new Skill();
					skill.setName(skillRs.getString("name"));
					skillList.add(skill);
				}
				
				//담은 ArrayList 를 staff 에 담음
				staff.setSkillList(skillList);
				staffList.add(staff);
				System.out.println(staff);
			}
			System.out.println("전체리스트 가져옴");
		} catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return staffList;
	}
	
	//noList 받아서 리스트 가져와주는 메서드
	private static ArrayList<Staff> getList(String sql, ArrayList<Integer> noList){
		System.out.println("Dao.java getList() 진입");
		Staff staff;
		ArrayList<Staff> staffList = null;
		PreparedStatement skillPstmt;
		ResultSet skillRs;
		ArrayList<Skill> skillList = null;
		
		try{
			conn = DBUtil.getConnection();
			for(int i = 0;i<noList.size();i++){
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, noList.get(i));
				rs = pstmt.executeQuery();
				staffList = new ArrayList<Staff>();
				while(rs.next()){
					staff = new Staff();
					System.out.println(staff);
					staff.setName(rs.getString("name"));
					
					//주민번호 잘른 숫자가져와서 담고 2로나누어 떨이지면 '여' 안떨어지면'남'을 셋팅
					int genderNum = Integer.parseInt(rs.getString("sn"));
					if(genderNum%2!=0){
						staff.setSn("남");
					}else{
						staff.setSn("여");
					}
					
					staff.setGraduateday(rs.getString("graduateday"));
					School school = new School();
					school.setGraduate(rs.getString("schoolno"));;
					staff.setSchool(school);
					Religion religion = new Religion();
					religion.setName(rs.getString("religionno"));
					staff.setReligion(religion);
					
					//skill 가져오는 쿼리문
					//skill 이 한 staff 마다 여러개 이므로 ArrayList에 담아 담은참조값을 staff에 담는다.
					skillPstmt = conn.prepareStatement("select  staffskill.staffno , staffskill.skillno, skill.name from staffskill inner join skill on staffskill.skillno = skill.`no`  where staffskill.staffno=? ; ");
					skillPstmt.setInt(1, rs.getInt("no"));	//현재 staff의 no 값으로 skill 테이블과 staffskill 테이블을 조인해 가져온다
					skillRs = skillPstmt.executeQuery();
					skillList = new ArrayList<Skill>();
					
					//skill 에 결과값을 하나씩 넣고 그것들을 ArrayList에 담는다.
					while(skillRs.next()){
						Skill skill = new Skill();
						skill.setName(skillRs.getString("name"));
						skillList.add(skill);
					}
					
					//담은 ArrayList 를 staff 에 담음
					staff.setSkillList(skillList);
					staffList.add(staff);
					System.out.println(staff);
				}
				System.out.println("전체리스트 가져옴");
			}
		} catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return staffList;
	}
	
	
	//객체종료하기
	public static void close(){
		if (rs != null)	try { rs.close();} catch (SQLException ex) {}
		if (pstmt != null) try { pstmt.close();	} catch (SQLException ex) {}
		if (conn != null) try {	conn.close(); } catch (SQLException ex) {}
	}
}
