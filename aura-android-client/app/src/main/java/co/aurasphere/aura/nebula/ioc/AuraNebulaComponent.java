package co.aurasphere.aura.nebula.ioc;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Donato on 23/05/2016.
 */
@Singleton
@Component(modules = {AuraNebulaModule.class})
public interface AuraNebulaComponent {

}
