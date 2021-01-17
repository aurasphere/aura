package co.aurasphere.aura.nebula.dashboard.common.dto;

import co.aurasphere.aura.common.dto.base.AuraBaseRequest;
import co.aurasphere.aura.nebula.dashboard.common.entities.Todo;

/**
 * Todo base request used for CRUD DB operations.
 * @author Donato Rimenti
 * @date 05/mag/2016
 */
public class BaseTodoRequest extends AuraBaseRequest{
	
	private static final long serialVersionUID = 1L;
	
	private Todo todo;

	public Todo getTodo() {
		return todo;
	}

	public void setTodo(Todo todo) {
		this.todo = todo;
	}

}
