package co.aurasphere.aura.nebula.modules.dashboard.ioc;

import javax.inject.Singleton;

import co.aurasphere.aura.nebula.ioc.AuraNebulaModule;
import co.aurasphere.aura.nebula.modules.dashboard.controller.DashboardService;
import co.aurasphere.aura.nebula.modules.dashboard.view.DashboardFragment;
import co.aurasphere.aura.nebula.modules.dashboard.view.activity.DashboardAddNewTaskActivity;
import co.aurasphere.aura.nebula.modules.dashboard.view.activity.DashboardTaskListActivity;
import co.aurasphere.aura.nebula.modules.dashboard.controller.DashboardRetrofitController;
import dagger.Component;

/**
 * Created by Donato on 25/05/2016.
 */
@Singleton
@Component(modules = {DashboardModule.class, AuraNebulaModule.class})
public interface DashboardComponent {
    void inject(DashboardFragment dashboardFragment);
    void inject(DashboardService dashboardController);
    void inject(DashboardRetrofitController dashboardRetrofitController);
    void inject(DashboardTaskListActivity dashboardTaskListActivity);
    void inject(DashboardAddNewTaskActivity dashboardAddNewTaskActivity);
}
