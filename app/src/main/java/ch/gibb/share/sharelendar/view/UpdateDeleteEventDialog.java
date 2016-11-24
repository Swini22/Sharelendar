package ch.gibb.share.sharelendar.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

import ch.gibb.share.sharelendar.R;
import ch.gibb.share.sharelendar.model.Event;
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
        View view = inflater.inflate(R.layout.dialog_updatedeleteevent, null);

        DatePicker datePicker = ((DatePicker) view.findViewById(R.id.date));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(event.getDate());

        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);

        datePicker.updateDate(year, month, day);

        EditText info = (EditText) view.findViewById(R.id.info);
        info.setText(event.getInformation());

        Button deleteEvent = (Button) view.findViewById(R.id.deleteButton);
        deleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent(event);
            }
        });

        builder.setView(view)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog dialog1 = UpdateDeleteEventDialog.this.getDialog();
                        Date date = new Date(((DatePicker) dialog1.findViewById(R.id.date)).getCalendarView().getDate());
                        String info = ((EditText) dialog1.findViewById(R.id.info)).getText().toString();
                        if (!info.equals("") && !date.equals(null)) {
                            event.setDate(date);
                            event.setInformation(info);
                            updateEvent(event);
                        }
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        UpdateDeleteEventDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
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

    private void deleteEvent(final Event event){
        new AsyncTask<Event , Void, Event>() {
            @Override
            protected Event doInBackground(Event... params) {
                Event newEvent = service.deleteEvent(event);
                return newEvent;
            }

            @Override
            protected void onPostExecute(Event result) {
                super.onPostExecute(result);
                eventoverViewActivity.getListItems().remove(result);
                eventoverViewActivity.updateEventOverView();
            }
        }.execute(event);
    }
}