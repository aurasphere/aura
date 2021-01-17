package co.aurasphere.aura.nebula.dashboard.common.dto;

import java.util.List;

import co.aurasphere.aura.common.dto.base.AuraBaseResponse;
import co.aurasphere.aura.nebula.dashboard.common.entities.Todo;

public class GetTodoListResponse extends AuraBaseResponse{
	
	private static final long serialVersionUID = 1L;
	
	private List<Todo> todoList;

	public List<Todo> getTodoList() {
		return todoList;
	}

	public void setTodoList(List<Todo> todoList) {
		this.todoList = todoList;
	}

}
