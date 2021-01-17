package co.aurasphere.aura.nebula.modules.dashboard.rest.service;

import co.aurasphere.aura.common.dto.base.AuraBaseRequest;
import co.aurasphere.aura.common.dto.base.AuraBaseResponse;
import co.aurasphere.aura.nebula.dashboard.common.dto.BaseProjectRequest;
import co.aurasphere.aura.nebula.dashboard.common.dto.BaseTodoRequest;
import co.aurasphere.aura.nebula.dashboard.common.dto.GetProjectListResponse;
import co.aurasphere.aura.nebula.dashboard.common.dto.GetTodoListResponse;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Donato on 18/05/2016.
 */
public interface AuraDashboardRestInterface {

    @POST("todo/getTodoList")
    Call<GetTodoListResponse> getTodoList(@Body AuraBaseRequest request);

    @POST("projects/getProjectList")
    Call<GetProjectListResponse> getProjectList(@Body AuraBaseRequest request);

    @POST("todo/insertTodo")
    Call<AuraBaseResponse> insertTodo(@Body BaseTodoRequest request);

    @POST("todo/updateTodo")
    Call<AuraBaseResponse> updateTodo(@Body BaseTodoRequest request);

    @POST("todo/deleteTodo")
    Call<AuraBaseResponse> deleteTodo(@Body BaseTodoRequest request);

    @POST("projects/insertProject")
    Call<AuraBaseResponse> insertProject(@Body BaseProjectRequest request);

    @POST("projects/updateProject")
    Call<AuraBaseResponse> updateProject(@Body BaseProjectRequest request);

    @POST("projects/deleteProject")
    Call<AuraBaseResponse> deleteProject(@Body BaseProjectRequest request);

}
