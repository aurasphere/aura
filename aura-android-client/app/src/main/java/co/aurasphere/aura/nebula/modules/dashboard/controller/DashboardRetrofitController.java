package co.aurasphere.aura.nebula.modules.dashboard.controller;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import co.aurasphere.aura.common.dto.base.AuraBaseResponse;
import co.aurasphere.aura.nebula.dashboard.common.dto.BaseProjectRequest;
import co.aurasphere.aura.nebula.dashboard.common.dto.BaseTodoRequest;
import co.aurasphere.aura.nebula.dashboard.common.dto.GetProjectListResponse;
import co.aurasphere.aura.nebula.dashboard.common.dto.GetTodoListResponse;
import co.aurasphere.aura.nebula.dashboard.common.entities.Project;
import co.aurasphere.aura.nebula.dashboard.common.entities.Todo;
import co.aurasphere.aura.nebula.dto.AuraNebulaBaseRequest;
import co.aurasphere.aura.nebula.modules.dashboard.rest.service.AuraDashboardRestInterface;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * Created by Donato on 16/05/2016.
 */
public class DashboardRetrofitController {

    @Inject
    Retrofit retrofit;

    @Inject
    AuraDashboardRestInterface auraDashboardRestInterface;

    private final static String TAG = "DashboardRetrofit";

    public void fetchTodoList(final DashboardService callback) {
        Call<GetTodoListResponse> call = auraDashboardRestInterface.getTodoList(new AuraNebulaBaseRequest());
        call.enqueue(new Callback<GetTodoListResponse>() {
            @Override
            public void onResponse(Response<GetTodoListResponse> response, Retrofit retrofit) {
                List<Todo> todoList = response.body().getTodoList();
                callback.notifyFetchTodoListComplete(todoList);
            }
            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "Error fetching todoList: ", t);
                callback.notifyFetchTodoListComplete(new ArrayList<Todo>());
        }
        });
    }

    public void fetchProjectList(final DashboardService callback) {
        Call<GetProjectListResponse> call = auraDashboardRestInterface.getProjectList(new AuraNebulaBaseRequest());
        call.enqueue(new Callback<GetProjectListResponse>() {
            @Override
            public void onResponse(Response<GetProjectListResponse> response, Retrofit retrofit) {
                List<Project> projectList = response.body().getProjectList();
                callback.notifyFetchProjectListComplete(projectList);
            }
            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "Error fetching projectList: ", t);
                callback.notifyFetchProjectListComplete(new ArrayList<Project>());
            }
        });
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public void setAuraDashboardRestInterface(AuraDashboardRestInterface auraDashboardRestInterface) {
        this.auraDashboardRestInterface = auraDashboardRestInterface;
    }

    public void addTodo(Todo todo, final DashboardService callback) {
        AuraBaseResponse response;
        BaseTodoRequest request = new BaseTodoRequest();
        request.setTodo(todo);
        Call<AuraBaseResponse> call = auraDashboardRestInterface.insertTodo(request);
        call.enqueue(new Callback<AuraBaseResponse>() {
            @Override
            public void onResponse(Response<AuraBaseResponse> response, Retrofit retrofit) {
                callback.notifyAddTaskComplete(response.body().isOutcome());
            }
            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "Exception during insertTodo: ", t);
                callback.notifyAddTaskComplete(false);
            }
        });
    }

    public void addProject(Project project, final DashboardService callback) {
        AuraBaseResponse response;
        BaseProjectRequest request = new BaseProjectRequest();
        request.setProject(project);
        Call<AuraBaseResponse> call = auraDashboardRestInterface.insertProject(request);
        call.enqueue(new Callback<AuraBaseResponse>() {
            @Override
            public void onResponse(Response<AuraBaseResponse> response, Retrofit retrofit) {
                callback.notifyAddTaskComplete(response.body().isOutcome());
            }
            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "Exception during insertProject: ", t);
                callback.notifyAddTaskComplete(false);
            }
        });
    }

    public void updateProject(Project project) {
        AuraBaseResponse response;
        BaseProjectRequest request = new BaseProjectRequest();
        request.setProject(project);
        Call<AuraBaseResponse> call = auraDashboardRestInterface.updateProject(request);
        call.enqueue(new Callback<AuraBaseResponse>() {
            @Override
            public void onResponse(Response<AuraBaseResponse> response, Retrofit retrofit) {
                Log.d(TAG, "Succesfully updated Project.");
            }
            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "Exception during updateProject: ", t);
            }
        });
    }

    public void updateTodo(Todo todo) {
        AuraBaseResponse response;
        BaseTodoRequest request = new BaseTodoRequest();
        request.setTodo(todo);
        Call<AuraBaseResponse> call = auraDashboardRestInterface.updateTodo(request);
        call.enqueue(new Callback<AuraBaseResponse>() {
            @Override
            public void onResponse(Response<AuraBaseResponse> response, Retrofit retrofit) {
                Log.d(TAG, "Succesfully updated Todo.");
            }
            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "Exception during updateTodo: ", t);
            }
        });
    }
}
