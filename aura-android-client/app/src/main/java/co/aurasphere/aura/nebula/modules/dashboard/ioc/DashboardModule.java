package co.aurasphere.aura.nebula.modules.dashboard.ioc;

import javax.inject.Singleton;

import co.aurasphere.aura.nebula.modules.dashboard.controller.DashboardService;
import co.aurasphere.aura.nebula.modules.dashboard.controller.DashboardRetrofitController;
import co.aurasphere.aura.nebula.modules.dashboard.rest.service.AuraDashboardRestInterface;
import dagger.Module;
import dagger.Provides;
import retrofit.Retrofit;

/**
 * Created by Donato on 23/05/2016.
 */
@Singleton
@Module
public class DashboardModule {

    @Provides @Singleton
    public DashboardRetrofitController getRetrofitController(AuraDashboardRestInterface auraDashboardRestInterface, Retrofit retrofit){
        DashboardRetrofitController dashboardRetrofitController = new DashboardRetrofitController();
        dashboardRetrofitController.setAuraDashboardRestInterface(auraDashboardRestInterface);
        dashboardRetrofitController.setRetrofit(retrofit);
        return dashboardRetrofitController;
    }

    @Provides @Singleton
    public AuraDashboardRestInterface getAuraDashboardRestInterface(Retrofit retrofit){
        return retrofit.create(AuraDashboardRestInterface.class);
    }

    @Provides @Singleton
    public DashboardService getDashboardController(DashboardRetrofitController dashboardRetrofitController){
        DashboardService dashboardController = new DashboardService();
        dashboardController.setDashboardRetrofitController(dashboardRetrofitController);
        return dashboardController;
    }
}
