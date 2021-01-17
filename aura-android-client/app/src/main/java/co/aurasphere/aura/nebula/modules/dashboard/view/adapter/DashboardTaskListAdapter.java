package co.aurasphere.aura.nebula.modules.dashboard.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.aurasphere.aura.nebula.R;
import co.aurasphere.aura.nebula.dashboard.common.entities.Task;
import co.aurasphere.aura.nebula.modules.dashboard.model.DashboardCard;
import co.aurasphere.aura.nebula.modules.dashboard.view.activity.DashboardTaskDetailActivity;
import co.aurasphere.aura.nebula.modules.dashboard.view.activity.DashboardTaskListActivity;

/**
 * Created by Donato on 28/05/2016.
 */
public class DashboardTaskListAdapter extends DashboardBaseAdapter<DashboardTaskListAdapter.DashboardTaskListHolder> {

    private DashboardTaskListActivity parentView;

    public DashboardTaskListAdapter(Context context, DashboardTaskListActivity parentView) {
        super(context);
        this.parentView = parentView;
    }

    @Override
    public DashboardTaskListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View v = layoutInflater.inflate(R.layout.dashboard_task_list_row_layout, parent, false);
        final DashboardTaskListHolder holder = new DashboardTaskListHolder(v);
        // Opens the detail when clicked.
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Intent intent = new Intent(context, DashboardTaskDetailActivity.class);
                intent.putExtra("Task", dashboardCards.get(position));
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final DashboardTaskListHolder holder, int position) {
        // Gets the current card.
        final DashboardCard card = dashboardCards.get(position);

        holder.content.setText(wrapContent(dashboardCards.get(position).getTask().getDescription()));
        holder.type.setText(dashboardCards.get(position).getType().toString());
        holder.watchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Swaps the value, changes the color and sends the new data to server.
                Task task = card.getTask();
                boolean watched = task.isWatched();
                if(watched){
                    task.setWatched(false);
                    watchedTaskList.remove(card);
                } else {
                    task.setWatched(true);
                    watchedTaskList.add(card);
                }
                parentView.updateTask(card);
                holder.changeWatchButton(watched);
            }
        });
        Calendar deadline = dashboardCards.get(position).getTask().getDeadline();
        String formattedDate;
        if(deadline == null){
            formattedDate = "-";
        } else {
            // TODO: injection.
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
            formattedDate = formatter.format(deadline.getTime());
        }
        holder.deadline.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return dashboardCards.size();
    }


    public class DashboardTaskListHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.dashboard_task_list_row_layout_content)
        TextView content;

        @BindView(R.id.dashboard_task_list_row_layout_deadline)
        TextView deadline;

        @BindView(R.id.dashboard_task_list_row_layout_task_category)
        TextView type;

        @BindView(R.id.dashboard_task_list_row_layout_watch_button)
        ImageButton watchButton;

        public DashboardTaskListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void changeWatchButton(boolean watched){
            // TODO: change button color
        }
    }
}
