package co.aurasphere.aura.nebula.modules.dashboard.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.ButterKnife;
import co.aurasphere.aura.nebula.R;

/**
 * Created by Donato on 30/05/2016.
 */
public class DashboardTaskDetailActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // View bindings.
        setContentView(R.layout.toolbar_main_layout);
        View content = LayoutInflater.from(this).inflate(R.layout.dashboard_task_detail_layout, null);
        FrameLayout mainContentView = ButterKnife.findById(this, R.id.main_content);
        mainContentView.addView(content);
        ButterKnife.bind(this);
    }
}
