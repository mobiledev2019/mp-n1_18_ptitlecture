package ptit.com.ptitmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ptit.com.ptitmanager.R;
import ptit.com.ptitmanager.activity.ProfileActivity;
import ptit.com.ptitmanager.object.Student;

import static android.support.constraint.Constraints.TAG;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private Context context;
    private List<Student> students;
    private LayoutInflater inflater;

    public StudentAdapter(Context context, List<Student> students) {
        this.context = context;
        this.students = students;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_list_student, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        final Student student = students.get(position);
        Glide.with(context).load(student).centerInside().placeholder(student.getImage()).into(viewHolder.ivAvatar);
        viewHolder.tvMaSV.setText(student.getMaSV());
        viewHolder.tvFullName.setText(student.getFullName());
        Log.d(TAG, "onBindViewHolder: " + student.getStatus());
        if (student.getStatus() == 1){
            viewHolder.btnCheck.setText("ON");
        }else {
            viewHolder.btnCheck.setText("OFF");
        }
        viewHolder.btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.btnCheck.getText().equals("ON")){
                    student.setDiemCC("" + (Integer.parseInt(student.getDiemCC())-1));
                    viewHolder.btnCheck.setText("OFF");
                    student.setStatus(0);
                }else {
                    student.setDiemCC("" + (Integer.parseInt(student.getDiemCC())+1));
                    viewHolder.btnCheck.setText("ON");
                    student.setStatus(1);
                }
                Log.d(TAG, "onClick: " + student.getDiemCC());
            }
        });

        viewHolder.ctnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("HoTen", student.getFullName());
                intent.putExtra("MaSV", student.getMaSV());
                intent.putExtra("DiemCC", student.getDiemCC());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return students.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvMaSV;
        TextView tvFullName;
        Button btnCheck;
        ConstraintLayout ctnLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.profile_image);
            tvMaSV = itemView.findViewById(R.id.tvMaSV);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            ctnLayout = itemView.findViewById(R.id.ctnLayout);
            btnCheck = itemView.findViewById(R.id.btnCheck);
        }
    }
}
