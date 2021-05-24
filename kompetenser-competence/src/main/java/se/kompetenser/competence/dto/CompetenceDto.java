package se.kompetenser.competence.dto;

public class CompetenceDto {
	
	private Integer competenceId;
	private String competence;
	private Integer createdBy;
	private Integer changedBy;
	private String description;
	private String descriptionUrl;

	public Integer getId() {
		return competenceId;
	}
	public void setId(Integer competenceId) {
		this.competenceId = competenceId;
	}
	public String getCompetence() {
		return competence;
	}
	public void setCompetence(String name) {
		this.competence = name;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Integer getChangedBy() {
		return changedBy;
	}
	public void setChangedBy(Integer changedBy) {
		this.changedBy = changedBy;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescriptionUrl() {
		return descriptionUrl;
	}
	public void setDescriptionUrl(String descriptionUrl) {
		this.descriptionUrl = descriptionUrl;
	}
	
}
