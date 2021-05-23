package se.kompetenser.competence.dto;

import java.time.LocalDate;

public class EmployeeDto {
	
	private Long employmentId;
	private LocalDate employmentDate;
	private LocalDate employmentEndDate;
	private Long userAccountId;
	private Long companyId;
	public Long getEmploymentId() {
		return employmentId;
	}
	public void setEmploymentId(Long employmentId) {
		this.employmentId = employmentId;
	}
	public LocalDate getEmploymentDate() {
		return employmentDate;
	}
	public void setEmploymentDate(LocalDate employmentDate) {
		this.employmentDate = employmentDate;
	}
	public LocalDate getEmploymentEndDate() {
		return employmentEndDate;
	}
	public void setEmploymentEndDate(LocalDate employmentEndDate) {
		this.employmentEndDate = employmentEndDate;
	}
	public Long getUserAccountId() {
		return userAccountId;
	}
	public void setUserAccountId(Long userAccountId) {
		this.userAccountId = userAccountId;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	

}
