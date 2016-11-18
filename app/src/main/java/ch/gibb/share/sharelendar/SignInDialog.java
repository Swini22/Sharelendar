package ch.gibb.share.sharelendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;




public class SignInDialog extends DialogFragment {

    SharelendarService service = new SharelendarService();
    Admin admin;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_signin, null))
                .setPositiveButton("Anmelden", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...

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
        new AsyncTask<Admin, Void, Boolean>() {

            @Override
            protected boolean doInBackground(Void... params) {
                boolean result = service.checkAdmin(admin);
                return result;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                ClassoverViewActivity.admin = admin.getUsername();
            }
        }.execute();
    }
}