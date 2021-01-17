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
import co.aurasphere.aura.nebula.dashboard.common.dto.BaseProjectRequest;
import co.aurasphere.aura.nebula.dashboard.common.dto.GetProjectListResponse;
import co.aurasphere.aura.nebula.dashboard.common.entities.Project;
import co.aurasphere.aura.nebula.dashboard.dao.ProjectDao;

@Controller
@RequestMapping(value = "/projects")
public class ProjectServices {
	
	@Autowired
	private ProjectDao projectDao;

	@RequestMapping(value = "/getProjectList", method = RequestMethod.POST)
	public @ResponseBody GetProjectListResponse getProjectList(@RequestBody AuraBaseRequest request){
		List<Project> projects = projectDao.readAll();
		GetProjectListResponse response = new GetProjectListResponse();
		response.setProjectList(projects);
		return response;
	}
	
	@RequestMapping(value = "/deleteProject", method = RequestMethod.POST)
	public @ResponseBody AuraBaseResponse deleteProject(@RequestBody BaseProjectRequest request){
		projectDao.delete(request.getProject());
		return new AuraBaseResponse();
	}
	
	@RequestMapping(value = "/insertProject", method = RequestMethod.POST)
	public @ResponseBody AuraBaseResponse insertProject(@RequestBody BaseProjectRequest request){
		projectDao.create(request.getProject());
		return new AuraBaseResponse();
	}
	
	@RequestMapping(value = "/updateProject", method = RequestMethod.POST)
	public @ResponseBody AuraBaseResponse updateProject(@RequestBody BaseProjectRequest request){
		projectDao.update(request.getProject());
		return new AuraBaseResponse();
	}
}
