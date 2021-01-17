package co.aurasphere.aura.nebula;

import android.app.Application;
import android.content.Context;

import co.aurasphere.aura.nebula.ioc.AuraNebulaComponent;
import co.aurasphere.aura.nebula.ioc.DaggerAuraNebulaComponent;

/**
 * Created by Donato on 26/05/2016.
 */
public class AuraNebula extends Application {

    private AuraNebulaComponent dependencyInjector;

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        dependencyInjector = DaggerAuraNebulaComponent.builder().build();
    }

    public AuraNebulaComponent getDependencyInjector() {
        return dependencyInjector;
    }

    public static Context getContext() {
        return context;
    }
}
