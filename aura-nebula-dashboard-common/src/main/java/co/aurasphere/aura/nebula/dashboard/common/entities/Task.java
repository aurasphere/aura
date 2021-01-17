package co.aurasphere.aura.nebula.dashboard.common.entities;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import co.aurasphere.aura.nebula.dashboard.common.enumeration.TaskPriority;

@MappedSuperclass
public class Task implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "deadline")
	private Calendar deadline;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "priority")
	private TaskPriority priority;
	
	@Column(name = "watched")
	private boolean watched;
	
	@Column(name = "watching_order")
	private int watchingOrder;
		
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Calendar getDeadline() {
		return deadline;
	}

	public void setDeadline(Calendar deadline) {
		this.deadline = deadline;
	}

	public TaskPriority getPriority() {
		return priority;
	}

	public void setPriority(TaskPriority priority) {
		this.priority = priority;
	}

	public boolean isWatched() {
		return watched;
	}

	public void setWatched(boolean watched) {
		this.watched = watched;
	}

	public int getWatchingOrder() {
		return watchingOrder;
	}

	public void setWatchingOrder(int watchingOrder) {
		this.watchingOrder = watchingOrder;
	}

}
