package co.aurasphere.aura.nebula.modules.social.ioc;

import android.hardware.camera2.params.Face;

import javax.inject.Singleton;

import co.aurasphere.aura.nebula.ioc.AuraNebulaModule;
import co.aurasphere.aura.nebula.modules.social.FacebookController;
import dagger.Component;

/**
 * Created by Donato on 26/05/2016.
 */
@Singleton
@Component(modules = {AuraNebulaModule.class, SocialModule.class})
public interface SocialComponent {
    void inject(FacebookController controller);
}
