package co.aurasphere.aura.nebula.dashboard.common.dto;

import co.aurasphere.aura.common.dto.base.AuraBaseRequest;
import co.aurasphere.aura.nebula.dashboard.common.entities.Project;

/**
 * Project base request used for CRUD DB operations.
 * @author Donato Rimenti
 * @date 29/mag/2016
 */
public class BaseProjectRequest extends AuraBaseRequest{
	
	private static final long serialVersionUID = 1L;
	
	private Project project;

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
