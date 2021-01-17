package co.aurasphere.aura.nebula.modules.social.util;

import android.widget.Filter;
import android.widget.Filterable;

import com.restfb.types.User;

/**
 * Created by Donato on 23/04/2016.
 */
public class FacebookUserWrapper {

    private User user;

    @Override
    public String toString(){
        return this.user.getName();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
