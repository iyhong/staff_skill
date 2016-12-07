package dto;

import java.util.Arrays;

public class Search {
	private String name;
	private String[] gender;
	private int[] religionNo;
	private int[] schoolNo;
	private int[] skillNo;
	private String graduateDayStart;
	private String graduateDayEnd;

	public Search() {
		super();
	}
	
	
	public Search(String name, String[] gender, int[] religionNo, int[] schoolNo, int[] skillNo, String graduateDayStart,
			String graduateDayEnd) {
		super();
		this.name = name;
		this.gender = gender;
		this.religionNo = religionNo;
		this.schoolNo = schoolNo;
		this.skillNo = skillNo;
		this.graduateDayStart = graduateDayStart;
		this.graduateDayEnd = graduateDayEnd;
	}
	
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String[] getGender() {
		return gender;
	}
	public void setGender(String[] gender) {
		this.gender = gender;
	}
	public int[] getReligionNo() {
		return religionNo;
	}
	public void setReligionNo(int[] religionNo) {
		this.religionNo = religionNo;
	}
	public int[] getSchoolNo() {
		return schoolNo;
	}
	public void setSchoolNo(int[] schoolNo) {
		this.schoolNo = schoolNo;
	}
	public int[] getSkillNo() {
		return skillNo;
	}
	public void setSkillNo(int[] skillNo) {
		this.skillNo = skillNo;
	}
	public String getGraduateDayStart() {
		return graduateDayStart;
	}
	public void setGraduateDayStart(String graduateDayStart) {
		this.graduateDayStart = graduateDayStart;
	}
	public String getGraduateDayEnd() {
		return graduateDayEnd;
	}
	public void setGraduateDayEnd(String graduateDayEnd) {
		this.graduateDayEnd = graduateDayEnd;
	}


	@Override
	public String toString() {
		return "Search [name=" + name + ", gender=" + Arrays.toString(gender) + ", religionNo="
				+ Arrays.toString(religionNo) + ", schoolNo=" + Arrays.toString(schoolNo) + ", skillNo="
				+ Arrays.toString(skillNo) + ", graduateDayStart=" + graduateDayStart + ", graduateDayEnd="
				+ graduateDayEnd + "]";
	}

	
	
	
}
