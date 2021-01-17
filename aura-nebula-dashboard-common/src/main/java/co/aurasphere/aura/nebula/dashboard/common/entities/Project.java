package co.aurasphere.aura.nebula.dashboard.common.entities;

import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import co.aurasphere.aura.nebula.dashboard.common.enumeration.ProjectCategory;
import co.aurasphere.aura.nebula.dashboard.common.enumeration.ProjectStatus;

@Entity
@Table(name = "project")
//@AttributeOverride(name = "id", column = @Column(name = "project_id"))
public class Project extends Task {
	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "id")
//	private int id;
//	
//	@Column(name = "title")
//	private String title;
//	
//	@Column(name = "description")
//	private String description;
//	
//	@Column(name = "deadline")
//	private Calendar deadline;
//	
//	@Enumerated(EnumType.STRING)
//	@Column(name = "priority")
//	private TaskPriority priority;
//	
//	@Column(name = "watched")
//	private boolean watched;
//	
//	@Column(name = "watching_order")
//	private int watchingOrder;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private ProjectStatus status;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "category")
	private ProjectCategory category;
	
	@Column(name = "last_update")
	private Calendar lastUpdate;
	

//	@JoinTable(name="todo", joinColumns = @JoinColumn(name="id"), inverseJoinColumns=@JoinColumn(name="goals"))
//	@JoinColumn(table = "todo", name = "id")
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	@Column(name = "goals")
	private List<Todo> goals;
//
//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}
//
//	public String getTitle() {
//		return title;
//	}
//
//	public void setTitle(String title) {
//		this.title = title;
//	}
//
//	public String getDescription() {
//		return description;
//	}
//
//	public void setDescription(String description) {
//		this.description = description;
//	}
//
//	public Calendar getDeadline() {
//		return deadline;
//	}
//
//	public void setDeadline(Calendar deadline) {
//		this.deadline = deadline;
//	}
//
//	public TaskPriority getPriority() {
//		return priority;
//	}
//
//	public void setPriority(TaskPriority priority) {
//		this.priority = priority;
//	}
//
//	public boolean isWatched() {
//		return watched;
//	}
//
//	public void setWatched(boolean watched) {
//		this.watched = watched;
//	}
//
//	public int getWatchingOrder() {
//		return watchingOrder;
//	}
//
//	public void setWatchingOrder(int watchingOrder) {
//		this.watchingOrder = watchingOrder;
//	}
//
	public ProjectStatus getStatus() {
		return status;
	}

	public void setStatus(ProjectStatus status) {
		this.status = status;
	}

	public ProjectCategory getCategory() {
		return category;
	}

	public void setCategory(ProjectCategory category) {
		this.category = category;
	}

	public Calendar getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Calendar lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public List<Todo> getGoals() {
		return goals;
	}

	public void setGoals(List<Todo> goals) {
		this.goals = goals;
	}

}
