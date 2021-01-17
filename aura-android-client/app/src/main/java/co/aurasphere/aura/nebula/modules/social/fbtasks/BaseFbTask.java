package co.aurasphere.aura.nebula.modules.social.fbtasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.restfb.FacebookClient;

/**
 * Created by Donato on 20/04/2016.
 */
public abstract class BaseFbTask extends AsyncTask<Object, Void, String>{

    protected Context context;

    protected FacebookClient postClient;

}
