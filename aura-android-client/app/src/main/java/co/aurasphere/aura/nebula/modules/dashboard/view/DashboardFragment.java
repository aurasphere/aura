package co.aurasphere.aura.nebula.modules.dashboard.view;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.aurasphere.aura.nebula.R;
import co.aurasphere.aura.nebula.dashboard.common.entities.Task;
import co.aurasphere.aura.nebula.modules.dashboard.controller.DashboardService;
import co.aurasphere.aura.nebula.modules.dashboard.ioc.DaggerDashboardComponent;
import co.aurasphere.aura.nebula.modules.dashboard.ioc.DashboardComponent;
import co.aurasphere.aura.nebula.modules.dashboard.model.DashboardCard;
import co.aurasphere.aura.nebula.modules.dashboard.model.DashboardCardListListener;
import co.aurasphere.aura.nebula.modules.dashboard.view.activity.DashboardTaskListActivity;
import co.aurasphere.aura.nebula.modules.dashboard.view.adapter.DashboardCardAdapter;
import co.aurasphere.aura.nebula.modules.dashboard.view.util.CardGestureCallback;
import co.aurasphere.aura.nebula.modules.dashboard.view.util.RecyclerViewEmptySupport;

/**
 * Created by Donato on 14/05/2016.
 */
public class DashboardFragment extends Fragment implements DashboardCardListListener {

    @Inject
    DashboardService dashboardController;

    @BindView(R.id.dashboard_card_list_swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.dashboard_card_list_recycler_view)
    RecyclerViewEmptySupport recyclerView;

    @BindView(R.id.dashboard_empty_card_list_view)
    View recyclerViewEmptyView;

    private DashboardCardAdapter cardAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // Autoinjection.
        DashboardComponent dashboardComponent = DaggerDashboardComponent.builder().build();
        dashboardComponent.inject(this);
        // Registers the observer and fetches data if the card list is empty.
        dashboardController.addCardListListener(this);
        dashboardController.fetchCards();

        // Instantiates the adapter.
        cardAdapter = new DashboardCardAdapter(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containerView, Bundle savedInstanceState){

        // View binding for injection.
        View view = inflater.inflate(R.layout.dashboard_card_list_layout, containerView, false);
        ButterKnife.bind(this, view);

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(cardAdapter);
        recyclerView.setEmptyView(recyclerViewEmptyView);

        // Swipe gesture for cards. TODO: sistema
        ItemTouchHelper.Callback callback = new CardGestureCallback(cardAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void onCardListChanged(List<DashboardCard> cardList) {
        // Adds data into the main view.
        cardAdapter.updateData(cardList);
        refreshLayout.setRefreshing(false);
    }

    @OnClick(R.id.dashboard_card_list_fab)
    void openDashboardWatchListActivity(){
        Intent intent = new Intent(getActivity(), DashboardTaskListActivity.class);
        startActivity(intent);
    }

    public void updateTask(DashboardCard card) {
        dashboardController.updateTask(card);
    }
}
