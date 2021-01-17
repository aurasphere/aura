package co.aurasphere.aura.nebula.modules.dashboard.view.adapter;

import android.content.Context;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import co.aurasphere.aura.nebula.modules.dashboard.model.DashboardCard;

/**
 * Created by Donato on 28/05/2016.
 */
public abstract class DashboardBaseAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T>{

    private static final int CONTENT_MAX_LENGTH = 80;

    protected List<DashboardCard> dashboardCards;

    protected List<DashboardCard> watchedTaskList;

    protected Context context;

    public DashboardBaseAdapter(Context context){
        this.context = context;
        this.dashboardCards = new ArrayList<DashboardCard>();
        this.watchedTaskList = new ArrayList<DashboardCard>();
    }

    @UiThread
    public void updateData(List<DashboardCard> list){
        if(dashboardCards != null) {
            dashboardCards.clear();
            dashboardCards.addAll(list);
            updateWatchedTaskList(list);
        } else {
            dashboardCards = list;
        }
        notifyDataSetChanged();
    }

    private void updateWatchedTaskList(List<DashboardCard> cardList) {
        watchedTaskList.clear();
        for(DashboardCard c: cardList) {
            if(c.getTask().isWatched()) {
                watchedTaskList.add(c);
            }
        }
    }

    public String wrapContent(String content){
        if (content.length() > CONTENT_MAX_LENGTH) {
            content = content.substring(0, CONTENT_MAX_LENGTH) + "...";
        }
        return content;
    }
}
