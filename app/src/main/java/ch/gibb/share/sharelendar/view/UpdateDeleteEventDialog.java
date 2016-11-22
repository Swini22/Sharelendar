package ch.gibb.share.sharelendar.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Date;

import ch.gibb.share.sharelendar.R;
import ch.gibb.share.sharelendar.model.Event;
import ch.gibb.share.sharelendar.model.SchoolClass;
import ch.gibb.share.sharelendar.service.SharelendarService;


public class UpdateDeleteEventDialog extends DialogFragment {

    private SharelendarService service = new SharelendarService();
    private EventoverViewActivity eventoverViewActivity;
    private Event event;

    public UpdateDeleteEventDialog(EventoverViewActivity eventoverViewActivity, Event event) {
        this.eventoverViewActivity = eventoverViewActivity;
        this.event = event;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_updatedeleteevent, null))
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog dialog1 =  UpdateDeleteEventDialog.this.getDialog();
                        event.setDate(new Date(((DatePicker) dialog1.findViewById(R.id.date)).getCalendarView().getDate()));
                        event.setInformation(((EditText) dialog1.findViewById(R.id.info)).getText().toString());
                        updateEvent(event);
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        UpdateDeleteEventDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();

//        Button deleteEvent = (Button) findViewById(R.id.deleteButton);
//        deleteEvent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                private void deleteEvent(final Event event){
//                    new AsyncTask<Void , Void, Event>() {
//                        @Override
//                        protected Void doInBackground(Event... params) {
//                            service.deleteEvent(event);
//                        }
//
//                        @Override
//                        protected void onPostExecute(Event result) {
//                            super.onPostExecute(result);
//                            eventoverViewActivity.updateEventOverView();
//                        }
//                    }.execute(event);
//                }
//            }
//        });
    }

    private void updateEvent(final Event event){
        new AsyncTask<Event , Void, Event>() {
            @Override
            protected Event doInBackground(Event... params) {
                Event newEvent = service.updateEvent(event);
                return newEvent;
            }


            @Override
            protected void onPostExecute(Event result) {
                super.onPostExecute(result);
                eventoverViewActivity.updateEventOverView();
            }
        }.execute(event);
    }
}