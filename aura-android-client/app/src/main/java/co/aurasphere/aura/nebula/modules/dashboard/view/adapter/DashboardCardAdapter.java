package co.aurasphere.aura.nebula.modules.dashboard.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.aurasphere.aura.nebula.R;
import co.aurasphere.aura.nebula.dashboard.common.entities.Task;
import co.aurasphere.aura.nebula.modules.dashboard.model.DashboardCard;
import co.aurasphere.aura.nebula.modules.dashboard.view.DashboardFragment;
import co.aurasphere.aura.nebula.modules.dashboard.view.activity.DashboardTaskDetailActivity;
import co.aurasphere.aura.nebula.modules.dashboard.view.activity.DashboardTaskListActivity;

/**
 * Created by Donato on 17/05/2016.
 */
public class DashboardCardAdapter extends DashboardBaseAdapter<DashboardCardAdapter.DashboardCardHolder> implements ItemGestureAdapter {

    private DashboardFragment parentView;

    public DashboardCardAdapter(Context context, DashboardFragment parentView) {
        super(context);
        this.parentView = parentView;
    }

    @Override
    public DashboardCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View v = layoutInflater.inflate(R.layout.dashboard_card_layout, parent, false);
        final DashboardCardHolder holder = new DashboardCardHolder(v);
        // Opens the detail when clicked.
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Intent intent = new Intent(context, DashboardTaskDetailActivity.class);
                intent.putExtra("Card", watchedTaskList.get(position));
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(DashboardCardHolder holder, int position) {
        holder.content.setText(wrapContent(watchedTaskList.get(position).getTask().getDescription()));
        holder.type.setText(watchedTaskList.get(position).getType().toString());
        Calendar deadline = watchedTaskList.get(position).getTask().getDeadline();
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
    public void onItemDismiss(int position) {
        // Update the watched status.
        DashboardCard card = watchedTaskList.get(position);
        card.getTask().setWatched(false);
        parentView.updateTask(card);

        watchedTaskList.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(watchedTaskList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(watchedTaskList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }
    @Override
    public int getItemCount() {
        return watchedTaskList.size();
    }


    public class DashboardCardHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.dashboard_card_content)
        TextView content;

        @BindView(R.id.dashboard_card_deadline_content)
        TextView deadline;

        @BindView(R.id.dashboard_card_category)
        TextView type;

        public DashboardCardHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
