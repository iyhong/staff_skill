package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import dto.Religion;
import dto.School;
import dto.Search;
import dto.Skill;
import dto.Staff;

public class Dao {
	static Connection conn = null;
	static PreparedStatement pstmt;
	static ResultSet rs;
	
	//모든 기술 가져오기 (controller 에서 호출하는 메서드)
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
	
	//모든 학력정보 가져오기 (controller 에서 호출하는 메서드)
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
	
	//모든 종교정보 가져오기 (controller 에서 호출하는 메서드)
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
	
	//회원정보 입력하기  (controller 에서 호출하는 메서드)
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
	
	//한명의 회원정보 가져오기 (controller 에서 호출하는 메서드)
	public static Staff oneStaff(Staff staff){
		System.out.println("Dao.java oneStaff() 진입");
		String sql = "select st.`no`,st.name, st.sn, re.name as religionname,re.no as religionno, sc.graduate as schoolgraduate, sc.no as schoolno,graduateday from staff st inner join religion re on st.religionno = re.`no` inner join school sc on st.schoolno = sc.`no` where st.`no`=? order by st.name asc";
		PreparedStatement skillPstmt;
		ResultSet skillRs;
		ArrayList<Skill> skillList = null;
		try{
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, staff.getNo());
			rs = pstmt.executeQuery();
			while(rs.next()){
				System.out.println(staff);
				staff.setName(rs.getString("name"));
				staff.setSn(rs.getString("sn"));
				staff.setGraduateday(rs.getString("graduateday"));
				School school = new School();
				school.setNo(rs.getInt("schoolno"));
				school.setGraduate(rs.getString("schoolgraduate"));;
				staff.setSchool(school);
				Religion religion = new Religion();
				religion.setNo(rs.getInt("religionno"));
				religion.setName(rs.getString("religionname"));
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
					skill.setNo(skillRs.getInt("skillno"));
					skill.setName(skillRs.getString("name"));
					skillList.add(skill);
				}
				
				//담은 ArrayList 를 staff 에 담음
				staff.setSkillList(skillList);
				System.out.println(staff);
			}
			System.out.println("회원 한명 정보 가져옴");
		} catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return staff;
	}
	
	//전체 staff 가져 오기 (controller 에서 호출하는 메서드)
	public static ArrayList<Staff> allStaff(){
		System.out.println("Dao.java allStaff() 진입");
		ArrayList<Integer> noList = null;
		String sql = "";
		ArrayList<Staff> staffList = null;
		sql = "select st.`no`,st.name, st.sn, re.name as religionname,re.no as religionno, sc.graduate as schoolgraduate, sc.no as schoolno,graduateday from staff st inner join religion re on st.religionno = re.`no` inner join school sc on st.schoolno = sc.`no` where st.`no`=? order by st.name asc";
		noList = allSelect();
		staffList = getList(sql,noList);
		
		return staffList;
	}
	
	
	//조회 결과에 따른 staff 가져오기(controller 에서 호출하는 메서드)
	public static ArrayList<Staff> searchStaff(Search search){
		System.out.println("Dao.java searchStaff() 진입");
		ArrayList<Integer> noList = new ArrayList<Integer>();
		ArrayList<Staff> staffList = null;
		String sql = "select st.`no`,st.name, substr(sn, 8,1)as sn, re.name as religionname,re.no as religionno, sc.graduate as schoolgraduate, sc.no as schoolno,graduateday from staff st inner join religion re on st.religionno = re.`no` inner join school sc on st.schoolno = sc.`no` where st.`no`=? order by st.name asc";
		
		System.out.println("searchStaff의 초기 noList : "+noList);
		if(search.getName()==""&&search.getGender()==null&&search.getReligionNo()==0
				&&search.getSchoolNo()==null&&search.getSkillNo()==null
				&&search.getGraduateDayStart()==""&&search.getGraduateDayEnd()==""){
			System.out.println("아무것도 입력안했을때...");
			noList = allSelect();
			System.out.println("searchStaff()의 조건문All noList : "+noList);
		}
		
				
		//성별 확인
		if(search.getGender()!=null){
			System.out.println("gender 확인 분기문");
			ArrayList<Integer> noListGender = genderSelect(search.getGender());
			System.out.println("searchStaff()의 조건문 noList : "+noList);
			System.out.println("searchStaff()의 조건문 noListGender : "+noListGender);
			//noList.addAll(noListGender);
			noList = duplicationValue(noList, noListGender);
			System.out.println("searchStaff()의 조건문 noList : "+noList);

		}
		
		//종교 확인
		if(search.getReligionNo()!=0){
			System.out.println("religion 확인 분기문");
			ArrayList<Integer> noListReligion = religionSelect(search.getReligionNo());
			System.out.println("searchStaff()의 조건문 noList : "+noList);
			System.out.println("searchStaff()의 조건문 noListReligion : "+noListReligion);
			//noList.addAll(noListReligion);
			noList = duplicationValue(noList, noListReligion);
			System.out.println("searchStaff()의 조건문 noList : "+noList);

		}
		
		//학력 확인
		if(search.getSchoolNo()!=null){
			System.out.println("school 확인 분기문");
			ArrayList<Integer> noListSchool = schoolSelect(search.getSchoolNo());
			System.out.println("searchStaff()의 조건문 noList : "+noList);
			System.out.println("searchStaff()의 조건문 noListSchool : "+noListSchool);
			//noList.addAll(noListReligion);
			noList = duplicationValue(noList, noListSchool);
			System.out.println("searchStaff()의 조건문 noList : "+noList);
		}
		
		//스킬 확인
		if(search.getSkillNo()!=null){
			System.out.println("skill 확인 분기문");
			ArrayList<Integer> noListSkill = skillSelect(search.getSkillNo());
			System.out.println("searchStaff()의 조건문 noList : "+noList);
			System.out.println("searchStaff()의 조건문 noListSkill : "+noListSkill);
			//noList.addAll(noListReligion);
			noList = duplicationValue(noList, noListSkill);
			System.out.println("searchStaff()의 조건문 noList : "+noList);
		}
		
		//이름 확인
		if(search.getName()!=null){
			System.out.println("name 확인 분기문");
			ArrayList<Integer> noListName = nameSelect(search.getName());
			System.out.println("searchStaff()의 조건문 noList : "+noList);
			System.out.println("searchStaff()의 조건문 noListName : "+noListName);
			//noList.addAll(noListReligion);
			noList = duplicationValue(noList, noListName);
			System.out.println("searchStaff()의 조건문 noList : "+noList);
		}
		
		//졸업일 확인
		if(search.getGraduateDayStart()!=""||search.getGraduateDayEnd()!=""){
			System.out.println("graduateDay 확인 분기문");
			ArrayList<Integer> noListGraduate = graduateSelect(search.getGraduateDayStart(),search.getGraduateDayEnd());
			System.out.println("searchStaff()의 조건문 noList : "+noList);
			System.out.println("searchStaff()의 조건문 noListGraduate : "+noListGraduate);
			//noList.addAll(noListReligion);
			noList = duplicationValue(noList, noListGraduate);
			System.out.println("searchStaff()의 조건문 noList : "+noList);
		}
		
		
		System.out.println("searchStaff()의 최종 noList : "+noList);
		staffList = getList(sql,noList);
	
		return staffList;
	}
	
	
	//noList 받아서 리스트 가져와주는 메서드
	private static ArrayList<Staff> getList(String sql, ArrayList<Integer> noList){
		System.out.println("---Dao.java getList() 진입---");
		Staff staff;
		ArrayList<Staff> staffList = null;
		PreparedStatement skillPstmt;
		ResultSet skillRs;
		ArrayList<Skill> skillList = null;
		
		try{
			conn = DBUtil.getConnection();
			System.out.println("noList : "+noList);
			staffList = new ArrayList<Staff>();
			System.out.println("sql : "+sql);
			for(int i = 0;i<noList.size();i++){
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, noList.get(i));
				rs = pstmt.executeQuery();
				while(rs.next()){
					staff = new Staff();
					System.out.println(staff);
					staff.setNo(rs.getInt("no"));
					staff.setName(rs.getString("name"));
					
					//주민번호 자른 숫자가져와서 담고 2로나누어 떨이지면 '여' 안떨어지면'남'을 셋팅
					int genderNum = rs.getInt("sn");
					if(genderNum%2!=0){
						staff.setSn("남");
					}else{
						staff.setSn("여");
					}
					
					staff.setGraduateday(rs.getString("graduateday"));
					School school = new School();
					school.setNo(rs.getInt("schoolno"));
					school.setGraduate(rs.getString("schoolgraduate"));;
					staff.setSchool(school);
					Religion religion = new Religion();
					religion.setNo(rs.getInt("religionno"));
					religion.setName(rs.getString("religionname"));
					staff.setReligion(religion);
					
					//skill 가져오는 쿼리문
					//skill 이 한 staff 마다 여러개 이므로 ArrayList에 담아 담은참조값을 staff에 담는다.
					skillPstmt = conn.prepareStatement("select staffskill.staffno , staffskill.skillno, skill.name from staffskill inner join skill on staffskill.skillno = skill.`no`  where staffskill.staffno=? ; ");
					skillPstmt.setInt(1, rs.getInt("no"));	//현재 staff의 no 값으로 skill 테이블과 staffskill 테이블을 조인해 가져온다
					skillRs = skillPstmt.executeQuery();
					skillList = new ArrayList<Skill>();
					
					//skill 에 결과값을 하나씩 넣고 그것들을 ArrayList에 담는다.
					while(skillRs.next()){
						Skill skill = new Skill();
						skill.setNo(skillRs.getInt("skillno"));
						skill.setName(skillRs.getString("name"));
						skillList.add(skill);
					}
					
					//담은 ArrayList 를 staff 에 담음
					staff.setSkillList(skillList);
					staffList.add(staff);
					System.out.println("getList()의 "+ staff);
				}
			}
			System.out.println("전체리스트 가져옴");
		} catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return staffList;
	}
	
	//전체 회원의 no 가져오기
	private static ArrayList<Integer> allSelect(){
		System.out.println("->allSelect() 진입");
		ArrayList<Integer> noList = null;
		try{
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement("select no from staff");
			rs = pstmt.executeQuery();
			noList = new ArrayList<Integer>();
			while(rs.next()){
				noList.add(rs.getInt(1));
			}
			System.out.println("allSelect의 noList : "+noList);
		} catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return noList;
	}
		
	//이름에 따른 회원 no 가져오기 
	private static ArrayList<Integer> nameSelect(String name){
		System.out.println("->nameSelect() 진입");
		ArrayList<Integer> noList = null;
		
		try{
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement("select no, name from staff where name like '%"+name+"%'");
			System.out.println("");
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
		System.out.println("->genderSelect() 진입");
		ArrayList<Integer> noList = null;
		String sqlAdd = "";
		if(gender.length==1){
			if(gender[0].equals("m")){
				sqlAdd += "where substr(sn, 8,1)=1 or substr(sn, 8,1)=3";
			}else{
				sqlAdd += "where substr(sn, 8,1)=2 or substr(sn, 8,1)=4";
			}
		}else{
			sqlAdd += "";
		}
		try{
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement("select no from staff "+sqlAdd);
			System.out.println("genderSelect()의 sql : select no from staff "+sqlAdd);
			rs = pstmt.executeQuery();
			noList = new ArrayList<Integer>();
			while(rs.next()){
				noList.add(rs.getInt(1));
			}
			System.out.println("genderSelect()의 noList : "+noList);
		} catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return noList;
	}
	
	//종교에 따른 회원 no 가져오기
	private static ArrayList<Integer> religionSelect(int religionNo){
		System.out.println("->religionSelect() 진입");
		ArrayList<Integer> noList = null;
		try{
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement("select no from staff where religionno=?");
			pstmt.setInt(1, religionNo);
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
	
	//학력에 따른 회원 no 가져오기
	private static ArrayList<Integer> schoolSelect(int[] schoolNo){
		System.out.println("->schoolSelect() 진입");
		ArrayList<Integer> noList = null;
		String sqlAdd = "where ";
		
		for(int i=0;i<schoolNo.length;i++){
			sqlAdd += "schoolno="+schoolNo[i];
			if(i!=schoolNo.length-1){
				sqlAdd += " or ";
			}
		}
		try{
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement("select no from staff "+sqlAdd);
			System.out.println("schoolno 쿼리문 : select no from staff "+sqlAdd);
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
	
	//스킬에따른 회원 no 가져오기
	private static ArrayList<Integer> skillSelect(int[] skillNo){
		System.out.println("->skillSelect() 진입");
		ArrayList<Integer> noList = null;
		String sqlAdd = "where ";
		for(int i=0;i<skillNo.length;i++){
			sqlAdd += "staffskill.skillno='"+skillNo[i]+"'";
			if(i!=skillNo.length-1){
				sqlAdd += " or ";
			}
		}
		try{
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement("select DISTINCT staff.`no` from staff inner join staffskill on staff.`no` = staffskill.staffno "+sqlAdd);
			System.out.println("select DISTINCT staff.`no` from staff inner join staffskill on staff.`no` = staffskill.staffno "+sqlAdd);
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
	
	
	//졸업일에 따른 회원 no 가져오기 
	private static ArrayList<Integer> graduateSelect(String graduateDayStart, String graduateDayEnd){
		System.out.println("->graduateSelect() 진입");
		ArrayList<Integer> noList = null;
		String sqlAdd = "";
		//졸업일찾을때 앞에입력안하고 뒤만하면
		if(!graduateDayStart.equals("")&&!graduateDayEnd.equals("")){
			sqlAdd += "where graduateday between '"+graduateDayStart+"' and '"+graduateDayEnd+"'";
		}else if(!graduateDayStart.equals("")){
			sqlAdd += "where graduateday > '"+graduateDayStart+"'";
		}else{
			sqlAdd += "where graduateday < '"+graduateDayEnd+"'";
		}
		try{
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement("select no, name, graduateday from staff "+sqlAdd);
			System.out.println("");
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
		
	
	//배열 중복검사해서 중복된 값만 가져오기
	private static ArrayList<Integer> duplicationValue(ArrayList<Integer> list, ArrayList<Integer> list2){
		ArrayList<Integer> resultList = new ArrayList<Integer>();
		
//      첫번째 배열이 비어있으면 그냥 추가
		if(list.size()==0){
			for(int i = 0;i<list2.size();i++){
				resultList.add(list2.get(i));
			}
		}
//      첫번째 리스트가 두번째보다 크면 두밴째리스트의 개수만큼반복해서 
//      첫번째 리스트가 두번째리스트의 각각의 값들을 포함하는지 확인후 포함하면 그값을 새로운 리스트에 담음
		if(list.size()>list2.size()){
			for(int i = 0;i<list2.size();i++){
				if(list.contains(list2.get(i))){
					resultList.add(list2.get(i));
				}
			}
//      첫번째 리스트가 두밴째보다 크지않으면 첫번째리스트의 개수만큼반복해서 
//      두번째리스트가 첫번째 리스트의 각각의 값들을 포함하는지 확인후 포함하면 그값을 새로운 리스트에 담음
		}else{
			for(int i = 0;i<list.size();i++){
				if(list2.contains(list.get(i))){
					resultList.add(list.get(i));
				}
			}
		}
		return resultList;
	}
	
	//객체종료하기
	public static void close(){
		if (rs != null)	try { rs.close();} catch (SQLException ex) {}
		if (pstmt != null) try { pstmt.close();	} catch (SQLException ex) {}
		if (conn != null) try {	conn.close(); } catch (SQLException ex) {}
	}
}
