package co.aurasphere.aura.nebula.modules.monitor;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.aurasphere.aura.nebula.R;

/**
 * Created by Donato on 21/05/2016.
 */
public class MonitorFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.galaxia_monitor_layout, container, false);
    }
}
