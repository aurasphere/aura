package co.aurasphere.aura.nebula.modules.dashboard.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import javax.inject.Inject;

import co.aurasphere.aura.nebula.dashboard.common.entities.Project;
import co.aurasphere.aura.nebula.dashboard.common.entities.Todo;
import co.aurasphere.aura.nebula.modules.dashboard.model.DashboardCard;
import co.aurasphere.aura.nebula.modules.dashboard.model.DashboardCardListListener;
import co.aurasphere.aura.nebula.modules.dashboard.model.DashboardTaskType;
import co.aurasphere.aura.nebula.modules.dashboard.view.DashboardFragment;
import co.aurasphere.aura.nebula.modules.dashboard.view.activity.DashboardAddNewTaskActivity;
import co.aurasphere.aura.nebula.modules.dashboard.view.adapter.DashboardBaseAdapter;

/**
 * Created by Donato on 24/05/2016.
 */
public class DashboardService {

    private List<DashboardCard> dashboardCardList = new ArrayList<DashboardCard>();

    private List<DashboardCardListListener> dashboardCardListListeners = new ArrayList<DashboardCardListListener>();

    private DashboardAddNewTaskActivity addTaskCallback;

    @Inject
    DashboardRetrofitController dashboardRetrofitController;

    public void fetchCards(){
        dashboardCardList.clear();
        dashboardRetrofitController.fetchTodoList(this);
        dashboardRetrofitController.fetchProjectList(this);
    }

    public void notifyFetchTodoListComplete(List<Todo> todoList){
        generateTodoCardList(todoList);
        callback();
    }

    public void notifyFetchProjectListComplete(List<Project> projectList) {
        generateProjectCardList(projectList);
        callback();
    }

    private void callback() {
      for(DashboardCardListListener listener : dashboardCardListListeners){
          listener.onCardListChanged(this.dashboardCardList);
      }
    }

    private void generateTodoCardList(List<Todo> todoList) {
        DashboardCard dashboardCard;
        for (Todo t : todoList) {
            dashboardCard = new DashboardCard();
            dashboardCard.setType(DashboardTaskType.TODO);
            dashboardCard.setTask(t);
            dashboardCardList.add(dashboardCard);
        }
    }

    private void generateProjectCardList(List<Project> projectList) {
        DashboardCard dashboardCard;
        for (Project p : projectList) {
            dashboardCard = new DashboardCard();
            dashboardCard.setType(DashboardTaskType.PROJECT);
            dashboardCard.setTask(p);
            dashboardCardList.add(dashboardCard);
        }
    }

    public List<DashboardCard> getDashboardCardList() {
        return dashboardCardList;
    }

    public void setDashboardRetrofitController(DashboardRetrofitController dashboardRetrofitController) {
        this.dashboardRetrofitController = dashboardRetrofitController;
    }

    public void addTodo(Todo todo, DashboardAddNewTaskActivity callback){
        this.addTaskCallback = callback;
        dashboardRetrofitController.addTodo(todo, this);
    }

    public void notifyAddTaskComplete(boolean outcome){
        addTaskCallback.onAddTaskComplete(outcome);
    }

    public void addCardListListener(DashboardCardListListener listener) {
        this.dashboardCardListListeners.add(listener);
    }

    public void addProject(Project project, DashboardAddNewTaskActivity callback) {
        this.addTaskCallback = callback;
        dashboardRetrofitController.addProject(project, this);
    }

    public void updateTask(DashboardCard card) {
        switch(card.getType()){
            case PROJECT:
                Project project = (Project) card.getTask();
                dashboardRetrofitController.updateProject(project);
                break;
            case TODO:
                Todo todo = (Todo) card.getTask();
                dashboardRetrofitController.updateTodo(todo);
                break;
        }
    }
}
