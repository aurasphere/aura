package co.aurasphere.aura.nebula.modules.dashboard.model;

import java.io.Serializable;

import co.aurasphere.aura.nebula.dashboard.common.entities.Task;

/**
 * Created by Donato on 17/05/2016.
 */
public class DashboardCard implements Serializable {

    private DashboardTaskType type;

    private Task task;

    public DashboardTaskType getType() {
        return type;
    }

    public void setType(DashboardTaskType type) {
        this.type = type;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
