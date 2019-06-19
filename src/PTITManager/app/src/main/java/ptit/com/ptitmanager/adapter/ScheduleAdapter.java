package ptit.com.ptitmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ptit.com.ptitmanager.R;
import ptit.com.ptitmanager.activity.StudentActivity;
import ptit.com.ptitmanager.object.Subject;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder>{
    private Context context;
    private List<Subject> subjectList;
    private LayoutInflater inflater;

    public ScheduleAdapter(Context context, List<Subject> subjectList) {
        this.context = context;
        this.subjectList = subjectList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = inflater.inflate(R.layout.item_schedule, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Subject subject = subjectList.get(position);
        holder.tvSubjectName.setText(subject.getSubjectName());
        holder.tvClassRoom.setText(subject.getClassRoom());
        holder.tvStudyTime.setText(subject.getStudyTime());
        holder.tvSTTNhom.setText(subject.getSttNhom());
        if (Integer.parseInt(subject.getTypeSubject()) == 1){
            holder.constraintLayout.setBackgroundColor(Color.parseColor("#00FFFF"));
        }
        if (Integer.parseInt(subject.getTypeSubject()) == 0){
            holder.constraintLayout.setBackgroundColor(Color.parseColor("#FFCCFF"));
        }


        if (Integer.parseInt(subject.getStatus()) == 1){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();

                    if(position < 0){
                        return;
                    }
                    Intent intent = new Intent(context, StudentActivity.class);
                    intent.putExtra("LopHocID", subject.getLopHocID());
                    context.startActivity(intent);
                }
            });
        }else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Môn này đã quá giờ lên lớp", Toast.LENGTH_SHORT).show();
                }
            });

            holder.constraintLayout.setBackgroundColor(Color.parseColor("#BBBBBB"));
        }

    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvSubjectName;
        TextView tvClassRoom;
        TextView tvStudyTime;
        TextView tvSTTNhom;
        ConstraintLayout constraintLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubjectName = itemView.findViewById(R.id.tvSubjects);
            tvClassRoom = itemView.findViewById(R.id.tvClassRoom);
            tvStudyTime = itemView.findViewById(R.id.tvStudyTime);
            tvSTTNhom = itemView.findViewById(R.id.tvStudyGroup);
            constraintLayout = itemView.findViewById(R.id.layoutSchedule);
        }
    }
}
