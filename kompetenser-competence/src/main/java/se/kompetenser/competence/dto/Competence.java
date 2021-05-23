package se.kompetenser.competence.dto;

public class Competence {
	
	private Integer competenceId;
	private String competenceName;
	private Integer createdBy;
	private Integer changedBy;
	private String description;
	private String descriptionUrl;
	public Integer getCompetenceId() {
		return competenceId;
	}
	public void setCompetenceId(Integer competenceId) {
		this.competenceId = competenceId;
	}
	public String getCompetenceName() {
		return competenceName;
	}
	public void setCompetenceName(String competenceName) {
		this.competenceName = competenceName;
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
