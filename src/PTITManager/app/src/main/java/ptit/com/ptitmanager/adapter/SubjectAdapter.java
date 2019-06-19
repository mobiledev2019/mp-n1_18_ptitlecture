package ptit.com.ptitmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import ptit.com.ptitmanager.R;
import ptit.com.ptitmanager.activity.GroupSubjectActivity;
import ptit.com.ptitmanager.activity.ImportExcelActivity;
import ptit.com.ptitmanager.object.MonHoc;
import ptit.com.ptitmanager.object.Subject;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {
    private Context context;
    private List<MonHoc> monHocList;
    private LayoutInflater inflater;

    public SubjectAdapter(Context context, List<MonHoc> monHocList) {
        this.context = context;
        this.monHocList = monHocList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_subject, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final MonHoc monHoc = monHocList.get(position);
        viewHolder.tvSubjectName.setText(monHoc.getTenMonHoc());
        viewHolder.btAddExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImportExcelActivity.class);
                intent.putExtra("MonHocID", monHoc.getID());
                context.startActivity(intent);
            }
        });
        viewHolder.rltLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GroupSubjectActivity.class);
                intent.putExtra("MonHocID", monHoc.getID());
                intent.putExtra("TenMonHoc", monHoc.getTenMonHoc());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return monHocList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvSubjectName;
        Button btAddExcel;
        RelativeLayout rltLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubjectName = itemView.findViewById(R.id.tvSubjectsName);
            btAddExcel = itemView.findViewById(R.id.btAddExcel);
            rltLayout = itemView.findViewById(R.id.relativeLayoutSubject);
        }
    }
}
