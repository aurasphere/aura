package co.aurasphere.aura.nebula.dashboard.common.dto;

import java.util.List;

import co.aurasphere.aura.nebula.dashboard.common.entities.Project;

public class GetProjectListResponse {
	
	private List<Project> projectList;

	public List<Project> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<Project> projectList) {
		this.projectList = projectList;
	}

}
