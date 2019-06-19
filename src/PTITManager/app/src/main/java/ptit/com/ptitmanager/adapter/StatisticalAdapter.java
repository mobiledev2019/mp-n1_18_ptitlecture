package ptit.com.ptitmanager.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ptit.com.ptitmanager.R;
import ptit.com.ptitmanager.object.Statistic;

public class StatisticalAdapter extends RecyclerView.Adapter<StatisticalAdapter.ViewHolder>{
    private Context context;
    private List<Statistic> list;
    private LayoutInflater inflater;

    public StatisticalAdapter(Context context, List<Statistic> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_time_statistical, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Statistic statistic = list.get(position);
        viewHolder.thu.setText(statistic.getThu());
        viewHolder.day.setText(statistic.getDay());
        viewHolder.month.setText(statistic.getMonth());
        viewHolder.year.setText(statistic.getYear());
        viewHolder.hour.setText(statistic.getHour());
        viewHolder.minute.setText(statistic.getMinute());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView thu;
        TextView day;
        TextView month;
        TextView year;
        TextView hour;
        TextView minute;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thu = itemView.findViewById(R.id.tvThu);
            day = itemView.findViewById(R.id.tvDate);
            month = itemView.findViewById(R.id.tvMonth);
            year = itemView.findViewById(R.id.tvYear);
            hour = itemView.findViewById(R.id.tvHour);
            minute = itemView.findViewById(R.id.tvMinute);
        }
    }
}
