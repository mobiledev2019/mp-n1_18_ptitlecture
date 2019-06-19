package ptit.com.ptitmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ptit.com.ptitmanager.R;
import ptit.com.ptitmanager.activity.ScheduleActivity;
import ptit.com.ptitmanager.object.StudySchedule;

import static android.content.Context.MODE_PRIVATE;

public class StudyScheduleAdapter extends RecyclerView.Adapter<StudyScheduleAdapter.ViewHolder> {
    private Context context;
    private List<StudySchedule> list;
    private LayoutInflater inflater;
    private SharedPreferences pre;
    private SharedPreferences.Editor editor;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public StudyScheduleAdapter(Context context, List<StudySchedule> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view =  inflater.inflate(R.layout.item_study_schedule, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final StudySchedule schedule = list.get(position);
        viewHolder.tvDay.setText(schedule.getDay());
        viewHolder.tvTotal.setText(schedule.getTvTotalPeriod());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvDay;
        TextView tvTotal;
        LinearLayout llLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tvDay);
            tvTotal = itemView.findViewById(R.id.tvTotalPeriod);
            llLayout = itemView.findViewById(R.id.ll_study_schedule);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
