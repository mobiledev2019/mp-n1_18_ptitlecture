package ptit.com.ptitmanager.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import ptit.com.ptitmanager.Interface.IDialogListener;

public class DialogCustom {
    private Context context;
    private String dialog;
    private String textButtonPositive;
    private String textButtonNegative;
    private String title;

    public DialogCustom(Context context, String dialog, String textButtonPositive, String textButtonNegative, String title) {
        this.context = context;
        this.dialog = dialog;
        this.textButtonPositive = textButtonPositive;
        this.textButtonNegative = textButtonNegative;
        this.title = title;
    }

    public void createDialog(final IDialogListener listener) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        dialogBuilder.setMessage(dialog)
                .setPositiveButton(textButtonPositive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.OnYesClicked();
                    }
                })
                .setNegativeButton(textButtonNegative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setTitle(title)
                .setCancelable(false)
                .show();
    }
}
