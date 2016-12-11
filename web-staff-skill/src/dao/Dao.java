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
	
	//��� ��� �������� (controller ���� ȣ���ϴ� �޼���)
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
	
	//��� �з����� �������� (controller ���� ȣ���ϴ� �޼���)
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
	
	//��� �������� �������� (controller ���� ȣ���ϴ� �޼���)
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
	
	//ȸ������ �Է��ϱ�  (controller ���� ȣ���ϴ� �޼���)
	public static int insertStaff(Staff staff, int[] skillNo){
		int rowCount = 0;
		System.out.println("insertStaff() Dao.java");
		try{
			conn = DBUtil.getConnection();
			System.out.println("skillNo.size()"+skillNo.length);
			
			//staff ���̺� �Է�
			pstmt = conn.prepareStatement("insert into staff (name,sn, GRADUATEDAY, SCHOOLNO, RELIGIONNO) values(?,?,?,?,?)");
			pstmt.setString(1, staff.getName());
			pstmt.setString(2, staff.getSn());
			pstmt.setString(3, staff.getGraduateday());
			pstmt.setInt(4, staff.getSchool().getNo());
			pstmt.setInt(5, staff.getReligion().getNo());
			pstmt.executeUpdate();
			System.out.println("staff �Է¼���");
			
			//staff ���̺� ��� �Էµ� no ������
			pstmt = conn.prepareStatement("select no from staff where sn=?");
			pstmt.setString(1, staff.getSn());
			rs = pstmt.executeQuery();
			System.out.println("rs:"+rs);
			int staffNo = 0;
			if(rs.next()){
				staffNo = rs.getInt("no");
				System.out.println("staffNo:"+staffNo);
			}
			
			//������ no������ staffskill ���̺� �� �Է�
			pstmt = conn.prepareStatement("insert into staffskill (staffno,skillno) values(?,?)");
			for(int i = 0 ; i<skillNo.length;i++){
				System.out.println("�ݺ���"+i);
				pstmt.setInt(1, staffNo);
				pstmt.setInt(2, skillNo[i]);
				pstmt.executeUpdate();
			}
			System.out.println("staffskill �Է¼���");
		} catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return rowCount;
	}
	
	//�Ѹ��� ȸ������ �������� (controller ���� ȣ���ϴ� �޼���)
	public static Staff oneStaff(Staff staff){
		System.out.println("Dao.java oneStaff() ����");
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
				
				//skill �������� ������
				//skill �� �� staff ���� ������ �̹Ƿ� ArrayList�� ��� ������������ staff�� ��´�.
				skillPstmt = conn.prepareStatement("select  staffskill.staffno , staffskill.skillno, skill.name from staffskill inner join skill on staffskill.skillno = skill.`no`  where staffskill.staffno=? ; ");
				skillPstmt.setInt(1, rs.getInt("no"));	//���� staff�� no ������ skill ���̺�� staffskill ���̺��� ������ �����´�
				skillRs = skillPstmt.executeQuery();
				skillList = new ArrayList<Skill>();
				
				//skill �� ������� �ϳ��� �ְ� �װ͵��� ArrayList�� ��´�.
				while(skillRs.next()){
					Skill skill = new Skill();
					skill.setNo(skillRs.getInt("skillno"));
					skill.setName(skillRs.getString("name"));
					skillList.add(skill);
				}
				
				//���� ArrayList �� staff �� ����
				staff.setSkillList(skillList);
				System.out.println(staff);
			}
			System.out.println("ȸ�� �Ѹ� ���� ������");
		} catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return staff;
	}
	
	//��ü staff ���� ���� (controller ���� ȣ���ϴ� �޼���)
	public static ArrayList<Staff> allStaff(){
		System.out.println("Dao.java allStaff() ����");
		ArrayList<Integer> noList = null;
		String sql = "";
		ArrayList<Staff> staffList = null;
		sql = "select st.`no`,st.name, st.sn, re.name as religionname,re.no as religionno, sc.graduate as schoolgraduate, sc.no as schoolno,graduateday from staff st inner join religion re on st.religionno = re.`no` inner join school sc on st.schoolno = sc.`no` where st.`no`=? order by st.name asc";
		noList = allSelect();
		staffList = getList(sql,noList);
		
		return staffList;
	}
	
	
	//��ȸ ����� ���� staff ��������(controller ���� ȣ���ϴ� �޼���)
	public static ArrayList<Staff> searchStaff(Search search){
		System.out.println("Dao.java searchStaff() ����");
		ArrayList<Integer> noList = new ArrayList<Integer>();
		ArrayList<Staff> staffList = null;
		String sql = "select st.`no`,st.name, substr(sn, 8,1)as sn, re.name as religionname,re.no as religionno, sc.graduate as schoolgraduate, sc.no as schoolno,graduateday from staff st inner join religion re on st.religionno = re.`no` inner join school sc on st.schoolno = sc.`no` where st.`no`=? order by st.name asc";
		
		System.out.println("searchStaff�� �ʱ� noList : "+noList);
		if(search.getName()==""&&search.getGender()==null&&search.getReligionNo()==0
				&&search.getSchoolNo()==null&&search.getSkillNo()==null
				&&search.getGraduateDayStart()==""&&search.getGraduateDayEnd()==""){
			System.out.println("�ƹ��͵� �Է¾�������...");
			noList = allSelect();
			System.out.println("searchStaff()�� ���ǹ�All noList : "+noList);
		}
		
				
		//���� Ȯ��
		if(search.getGender()!=null){
			System.out.println("gender Ȯ�� �б⹮");
			ArrayList<Integer> noListGender = genderSelect(search.getGender());
			System.out.println("searchStaff()�� ���ǹ� noList : "+noList);
			System.out.println("searchStaff()�� ���ǹ� noListGender : "+noListGender);
			//noList.addAll(noListGender);
			noList = duplicationValue(noList, noListGender);
			System.out.println("searchStaff()�� ���ǹ� noList : "+noList);

		}
		
		//���� Ȯ��
		if(search.getReligionNo()!=0){
			System.out.println("religion Ȯ�� �б⹮");
			ArrayList<Integer> noListReligion = religionSelect(search.getReligionNo());
			System.out.println("searchStaff()�� ���ǹ� noList : "+noList);
			System.out.println("searchStaff()�� ���ǹ� noListReligion : "+noListReligion);
			//noList.addAll(noListReligion);
			noList = duplicationValue(noList, noListReligion);
			System.out.println("searchStaff()�� ���ǹ� noList : "+noList);

		}
		
		//�з� Ȯ��
		if(search.getSchoolNo()!=null){
			System.out.println("school Ȯ�� �б⹮");
			ArrayList<Integer> noListSchool = schoolSelect(search.getSchoolNo());
			System.out.println("searchStaff()�� ���ǹ� noList : "+noList);
			System.out.println("searchStaff()�� ���ǹ� noListSchool : "+noListSchool);
			//noList.addAll(noListReligion);
			noList = duplicationValue(noList, noListSchool);
			System.out.println("searchStaff()�� ���ǹ� noList : "+noList);
		}
		
		//��ų Ȯ��
		if(search.getSkillNo()!=null){
			System.out.println("skill Ȯ�� �б⹮");
			ArrayList<Integer> noListSkill = skillSelect(search.getSkillNo());
			System.out.println("searchStaff()�� ���ǹ� noList : "+noList);
			System.out.println("searchStaff()�� ���ǹ� noListSkill : "+noListSkill);
			//noList.addAll(noListReligion);
			noList = duplicationValue(noList, noListSkill);
			System.out.println("searchStaff()�� ���ǹ� noList : "+noList);
		}
		
		//�̸� Ȯ��
		if(search.getName()!=null){
			System.out.println("name Ȯ�� �б⹮");
			ArrayList<Integer> noListName = nameSelect(search.getName());
			System.out.println("searchStaff()�� ���ǹ� noList : "+noList);
			System.out.println("searchStaff()�� ���ǹ� noListName : "+noListName);
			//noList.addAll(noListReligion);
			noList = duplicationValue(noList, noListName);
			System.out.println("searchStaff()�� ���ǹ� noList : "+noList);
		}
		
		//������ Ȯ��
		if(search.getGraduateDayStart()!=""||search.getGraduateDayEnd()!=""){
			System.out.println("graduateDay Ȯ�� �б⹮");
			ArrayList<Integer> noListGraduate = graduateSelect(search.getGraduateDayStart(),search.getGraduateDayEnd());
			System.out.println("searchStaff()�� ���ǹ� noList : "+noList);
			System.out.println("searchStaff()�� ���ǹ� noListGraduate : "+noListGraduate);
			//noList.addAll(noListReligion);
			noList = duplicationValue(noList, noListGraduate);
			System.out.println("searchStaff()�� ���ǹ� noList : "+noList);
		}
		
		
		System.out.println("searchStaff()�� ���� noList : "+noList);
		staffList = getList(sql,noList);
	
		return staffList;
	}
	
	
	//noList �޾Ƽ� ����Ʈ �������ִ� �޼���
	private static ArrayList<Staff> getList(String sql, ArrayList<Integer> noList){
		System.out.println("---Dao.java getList() ����---");
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
					
					//�ֹι�ȣ �ڸ� ���ڰ����ͼ� ��� 2�γ����� �������� '��' �ȶ�������'��'�� ����
					int genderNum = rs.getInt("sn");
					if(genderNum%2!=0){
						staff.setSn("��");
					}else{
						staff.setSn("��");
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
					
					//skill �������� ������
					//skill �� �� staff ���� ������ �̹Ƿ� ArrayList�� ��� ������������ staff�� ��´�.
					skillPstmt = conn.prepareStatement("select staffskill.staffno , staffskill.skillno, skill.name from staffskill inner join skill on staffskill.skillno = skill.`no`  where staffskill.staffno=? ; ");
					skillPstmt.setInt(1, rs.getInt("no"));	//���� staff�� no ������ skill ���̺�� staffskill ���̺��� ������ �����´�
					skillRs = skillPstmt.executeQuery();
					skillList = new ArrayList<Skill>();
					
					//skill �� ������� �ϳ��� �ְ� �װ͵��� ArrayList�� ��´�.
					while(skillRs.next()){
						Skill skill = new Skill();
						skill.setNo(skillRs.getInt("skillno"));
						skill.setName(skillRs.getString("name"));
						skillList.add(skill);
					}
					
					//���� ArrayList �� staff �� ����
					staff.setSkillList(skillList);
					staffList.add(staff);
					System.out.println("getList()�� "+ staff);
				}
			}
			System.out.println("��ü����Ʈ ������");
		} catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return staffList;
	}
	
	//��ü ȸ���� no ��������
	private static ArrayList<Integer> allSelect(){
		System.out.println("->allSelect() ����");
		ArrayList<Integer> noList = null;
		try{
			conn = DBUtil.getConnection();
			pstmt = conn.prepareStatement("select no from staff");
			rs = pstmt.executeQuery();
			noList = new ArrayList<Integer>();
			while(rs.next()){
				noList.add(rs.getInt(1));
			}
			System.out.println("allSelect�� noList : "+noList);
		} catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return noList;
	}
		
	//�̸��� ���� ȸ�� no �������� 
	private static ArrayList<Integer> nameSelect(String name){
		System.out.println("->nameSelect() ����");
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
	
	//������ ���� ȸ�� no ��������
	private static ArrayList<Integer> genderSelect(String[] gender){
		System.out.println("->genderSelect() ����");
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
			System.out.println("genderSelect()�� sql : select no from staff "+sqlAdd);
			rs = pstmt.executeQuery();
			noList = new ArrayList<Integer>();
			while(rs.next()){
				noList.add(rs.getInt(1));
			}
			System.out.println("genderSelect()�� noList : "+noList);
		} catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return noList;
	}
	
	//������ ���� ȸ�� no ��������
	private static ArrayList<Integer> religionSelect(int religionNo){
		System.out.println("->religionSelect() ����");
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
	
	//�з¿� ���� ȸ�� no ��������
	private static ArrayList<Integer> schoolSelect(int[] schoolNo){
		System.out.println("->schoolSelect() ����");
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
			System.out.println("schoolno ������ : select no from staff "+sqlAdd);
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
	
	//��ų������ ȸ�� no ��������
	private static ArrayList<Integer> skillSelect(int[] skillNo){
		System.out.println("->skillSelect() ����");
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
	
	
	//�����Ͽ� ���� ȸ�� no �������� 
	private static ArrayList<Integer> graduateSelect(String graduateDayStart, String graduateDayEnd){
		System.out.println("->graduateSelect() ����");
		ArrayList<Integer> noList = null;
		String sqlAdd = "";
		//������ã���� �տ��Է¾��ϰ� �ڸ��ϸ�
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
		
	
	//�迭 �ߺ��˻��ؼ� �ߺ��� ���� ��������
	private static ArrayList<Integer> duplicationValue(ArrayList<Integer> list, ArrayList<Integer> list2){
		ArrayList<Integer> resultList = new ArrayList<Integer>();
		
//      ù��° �迭�� ��������� �׳� �߰�
		if(list.size()==0){
			for(int i = 0;i<list2.size();i++){
				resultList.add(list2.get(i));
			}
		}
//      ù��° ����Ʈ�� �ι�°���� ũ�� �ι�°����Ʈ�� ������ŭ�ݺ��ؼ� 
//      ù��° ����Ʈ�� �ι�°����Ʈ�� ������ ������ �����ϴ��� Ȯ���� �����ϸ� �װ��� ���ο� ����Ʈ�� ����
		if(list.size()>list2.size()){
			for(int i = 0;i<list2.size();i++){
				if(list.contains(list2.get(i))){
					resultList.add(list2.get(i));
				}
			}
//      ù��° ����Ʈ�� �ι�°���� ũ�������� ù��°����Ʈ�� ������ŭ�ݺ��ؼ� 
//      �ι�°����Ʈ�� ù��° ����Ʈ�� ������ ������ �����ϴ��� Ȯ���� �����ϸ� �װ��� ���ο� ����Ʈ�� ����
		}else{
			for(int i = 0;i<list.size();i++){
				if(list2.contains(list.get(i))){
					resultList.add(list.get(i));
				}
			}
		}
		return resultList;
	}
	
	//��ü�����ϱ�
	public static void close(){
		if (rs != null)	try { rs.close();} catch (SQLException ex) {}
		if (pstmt != null) try { pstmt.close();	} catch (SQLException ex) {}
		if (conn != null) try {	conn.close(); } catch (SQLException ex) {}
	}
}
