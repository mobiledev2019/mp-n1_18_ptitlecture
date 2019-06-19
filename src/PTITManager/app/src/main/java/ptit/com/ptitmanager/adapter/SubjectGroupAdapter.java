package ptit.com.ptitmanager.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ptit.com.ptitmanager.Interface.CallBackData;
import ptit.com.ptitmanager.R;
import ptit.com.ptitmanager.object.MonHoc;
import ptit.com.ptitmanager.object.NhomMonHoc;

import static android.support.constraint.Constraints.TAG;

public class SubjectGroupAdapter extends RecyclerView.Adapter<SubjectGroupAdapter.ViewHolder> {
    private Context context;
    private List<NhomMonHoc> nhomMonHocList;
    private String tenMonHoc;
    private LayoutInflater inflater;
    private FragmentManager fragmentManager;
    private CallBackData listener;

    public SubjectGroupAdapter(Context context, String tenMonHoc, List<NhomMonHoc> nhomMonHocList, FragmentManager fragmentManager, CallBackData listener) {
        this.context = context;
        this.tenMonHoc = tenMonHoc;
        this.nhomMonHocList = nhomMonHocList;
        this.fragmentManager = fragmentManager;
        this.listener = listener;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_subject_group, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final NhomMonHoc nhomMonHoc = nhomMonHocList.get(i);
        viewHolder.tvMonHoc.setText(tenMonHoc);
        viewHolder.tvNhomMonHoc.setText(nhomMonHoc.getSttNhom());
        viewHolder.rltLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(nhomMonHoc.getLinkNhom()));
                    context.startActivity(intent);
                } catch (Exception e) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://stackoverflow.com/")));
                }
            }
        });
        viewHolder.btAddFBGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String document = nhomMonHoc.getID();
                showDialogRemiderFavourite(nhomMonHoc.getLinkNhom(), document);
            }
        });
    }

    private void showDialogRemiderFavourite (final String linkFb, final String document) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_update_facebook);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);
        Button btSave = dialog.findViewById(R.id.btnSave);
        Button btClose = dialog.findViewById(R.id.btnClose);
        final EditText etLinkFB = dialog.findViewById(R.id.edtAddLinkFB);

        etLinkFB.setText(linkFb);
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + linkFb + ", " + etLinkFB.getText());
                if(!linkFb.equals(etLinkFB.getText().toString())){
                    applyTexts(etLinkFB.getText().toString(), document);
                    listener.called(this);

                    dialog.dismiss();
                }else {
                    dialog.dismiss();
                }
            }
        });

        dialog.create();
        dialog.show();


    }
    @Override
    public int getItemCount() {
        return nhomMonHocList.size();
    }

    public void applyTexts(String linkFB, String document) {
        Log.d(TAG, "applyTexts: " + document);
        Map<String, Object> update = new HashMap<>();
        update.put("link", linkFB);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Nhóm Môn Học").document(document);
        docRef.update("LinkNhom", linkFB);

    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMonHoc, tvNhomMonHoc;
        Button btAddFBGroup;
        RelativeLayout rltLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMonHoc = itemView.findViewById(R.id.tvMonHoc);
            tvNhomMonHoc = itemView.findViewById(R.id.tvNhomMonHoc);
            btAddFBGroup = itemView.findViewById(R.id.btAddFBGroup);
            rltLayout = itemView.findViewById(R.id.relativeLayoutSubjectGroup);
        }
    }
}
