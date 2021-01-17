package co.aurasphere.aura.nebula.modules.dashboard.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.aurasphere.aura.nebula.R;
import co.aurasphere.aura.nebula.modules.dashboard.controller.DashboardService;
import co.aurasphere.aura.nebula.modules.dashboard.ioc.DaggerDashboardComponent;
import co.aurasphere.aura.nebula.modules.dashboard.ioc.DashboardComponent;
import co.aurasphere.aura.nebula.modules.dashboard.model.DashboardCard;
import co.aurasphere.aura.nebula.modules.dashboard.model.DashboardCardListListener;
import co.aurasphere.aura.nebula.modules.dashboard.view.adapter.DashboardTaskListAdapter;
import co.aurasphere.aura.nebula.modules.dashboard.view.util.DividerItemDecoration;
import co.aurasphere.aura.nebula.modules.dashboard.view.util.RecyclerViewEmptySupport;

/**
 * Created by Donato on 27/05/2016.
 */
public class DashboardTaskListActivity extends AppCompatActivity implements DashboardCardListListener {

    @Inject
    DashboardService dashboardController;

    @BindView(R.id.dashboard_watch_list_swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.dashboard_watch_list_recycler_view)
    RecyclerViewEmptySupport recyclerView;

    @BindView(R.id.dashboard_empty_task_list_view)
    View dashboardListEmptyView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private DashboardTaskListAdapter taskListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Autoinjection.
        DashboardComponent dashboardComponent = DaggerDashboardComponent.builder().build();
        dashboardComponent.inject(this);

        // Registers the observer and fetches data if the card list is empty.
        dashboardController.addCardListListener(this);
        if (dashboardController.getDashboardCardList().isEmpty()) {
            dashboardController.fetchCards();
        }

        // View bindings.
        setContentView(R.layout.toolbar_main_layout);
        View content = LayoutInflater.from(this).inflate(R.layout.dashboard_task_list_layout, null);
        FrameLayout mainContentView = ButterKnife.findById(this, R.id.main_content);
        mainContentView.addView(content);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        // Instantiates the task list adapter.
        taskListAdapter = new DashboardTaskListAdapter(this, this);

        // Sets the refresh listener to load data again.
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dashboardController.fetchCards();
            }
        });

        // Shows the refreshing spinner until data are loaded.
        refreshLayout.setRefreshing(true);

        // Sets the adapter to the data in the recycler view.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskListAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.line_divider)));
        recyclerView.setEmptyView(dashboardListEmptyView);
    }

    @OnClick(R.id.dashboard_watch_list_fab)
    void openDashboardAddNewCardActivity() {
        Intent intent = new Intent(this, DashboardAddNewTaskActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCardListChanged(List<DashboardCard> cardList) {

        // Shows the empty view.
//        Log.d("DEBUG DASH", new Boolean(cardList.isEmpty()).toString());
//        if(cardList == null || cardList.isEmpty()){
//            Log.d("DEBUG TASH", "showing view:" +  dashboardListEmptyView);
//            recyclerView.setVisibility(View.GONE);
//            dashboardListEmptyView.setVisibility(View.VISIBLE);
//        } else {
//
//            Log.d("DEBUG TASH", "hiding view:" +  dashboardListEmptyView);
//            recyclerView.setVisibility(View.VISIBLE);
//            dashboardListEmptyView.setVisibility(View.GONE);
//        }

        // Adds data into the main view.
        taskListAdapter.updateData(cardList);
        refreshLayout.setRefreshing(false);
    }


    public void updateTask(DashboardCard card) {
        dashboardController.updateTask(card);
    }
}
