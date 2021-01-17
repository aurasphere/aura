package co.aurasphere.aura.nebula.dashboard.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import co.aurasphere.aura.common.dto.base.AuraBaseRequest;
import co.aurasphere.aura.common.dto.base.AuraBaseResponse;
import co.aurasphere.aura.nebula.dashboard.common.dto.BaseTodoRequest;
import co.aurasphere.aura.nebula.dashboard.common.dto.GetTodoListResponse;
import co.aurasphere.aura.nebula.dashboard.common.entities.Todo;
import co.aurasphere.aura.nebula.dashboard.dao.TodoDao;

@Controller
@RequestMapping(value = "/todo")
public class TodoServices {

	@Autowired
	private TodoDao todoDao;

	@RequestMapping(value = "/getTodoList", method = RequestMethod.POST)
	public @ResponseBody GetTodoListResponse getTodoList(@RequestBody AuraBaseRequest request){
		GetTodoListResponse response = new GetTodoListResponse();
		List<Todo> todoList = todoDao.readAll();
		response.setTodoList(todoList);
		return response;
	}
	
	@RequestMapping(value = "/deleteTodo", method = RequestMethod.POST)
	public @ResponseBody AuraBaseResponse deleteTodo(@RequestBody BaseTodoRequest request){
		todoDao.delete(request.getTodo());
		return new AuraBaseResponse();
	}
	
	@RequestMapping(value = "/insertTodo", method = RequestMethod.POST)
	public @ResponseBody AuraBaseResponse insertTodo(@RequestBody BaseTodoRequest request){
		todoDao.create(request.getTodo());
		return new AuraBaseResponse();
	}
	
	@RequestMapping(value = "/updateTodo", method = RequestMethod.POST)
	public @ResponseBody AuraBaseResponse updateTodo(@RequestBody BaseTodoRequest request){
		todoDao.update(request.getTodo());
		return new AuraBaseResponse();
	}
}
