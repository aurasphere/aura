package co.aurasphere.aura.nebula.dashboard.common.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "todo")
public class Todo extends Task {
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "project")
	private Project project;
	
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
