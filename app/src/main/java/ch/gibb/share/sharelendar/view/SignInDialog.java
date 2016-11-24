package ch.gibb.share.sharelendar.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

import org.json.JSONObject;

import ch.gibb.share.sharelendar.model.Admin;
import ch.gibb.share.sharelendar.R;
import ch.gibb.share.sharelendar.service.SharelendarService;


public class SignInDialog extends DialogFragment {

    SharelendarService service = new SharelendarService();
    ClassoverViewActivity classoverViewActivity;

    public SignInDialog(ClassoverViewActivity classoverViewActivity) {
        this.classoverViewActivity = classoverViewActivity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_signin, null))
                .setPositiveButton("Anmelden", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog dialog1 = SignInDialog.this.getDialog();
                        String username = ((EditText) dialog1.findViewById(R.id.username)).getText().toString();
                        String password = ((EditText) dialog1.findViewById(R.id.password)).getText().toString();
                        if (!username.equals("") && !password.equals("")) {
                            Admin admin = new Admin();
                            admin.setUsername(username);
                            admin.setPassword(password);
                            checkAdmin(admin);
                        }
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SignInDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void checkAdmin(final Admin admin){
        new AsyncTask<Admin , Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Admin... params) {
                boolean result = service.checkAdmin(admin);
                return result;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (result){
                    ClassoverViewActivity.admin = admin.getUsername();
                    classoverViewActivity.updateClassOverView();
                }
            }
        }.execute(admin);
    }
}