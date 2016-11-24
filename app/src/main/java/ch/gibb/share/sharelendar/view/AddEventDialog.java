package ch.gibb.share.sharelendar.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Date;

import ch.gibb.share.sharelendar.R;
import ch.gibb.share.sharelendar.model.Event;
import ch.gibb.share.sharelendar.model.SchoolClass;
import ch.gibb.share.sharelendar.service.SharelendarService;


public class AddEventDialog extends DialogFragment {

    private SharelendarService service = new SharelendarService();
    private EventoverViewActivity eventoverViewActivity;
    private ArrayList<Event> listItems = new ArrayList<Event>();
    private ArrayAdapter<Event> adapter;
    private SchoolClass schoolClass;

    public AddEventDialog(EventoverViewActivity eventoverViewActivity, SchoolClass schoolClass) {
        this.eventoverViewActivity = eventoverViewActivity;
        this.schoolClass = schoolClass;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_addevent, null))
                .setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog dialog1 =  AddEventDialog.this.getDialog();
                        Date date = new Date(((DatePicker) dialog1.findViewById(R.id.date)).getCalendarView().getDate());
                        String info = ((EditText) dialog1.findViewById(R.id.info)).getText().toString();
                        if (!info.equals("") && !date.equals(null)) {
                            Event event = new Event();
                            event.setDate(date);
                            event.setInformation(info);
                            event.setSchoolClass(schoolClass);
                            createEvent(event);
                        }
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddEventDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void createEvent(final Event event){
        new AsyncTask<Event , Void, Event>() {
            @Override
            protected Event doInBackground(Event... params) {
                Event newEvent = service.createEvent(event);
                return newEvent;
            }


            @Override
            protected void onPostExecute(Event result) {
                super.onPostExecute(result);
                eventoverViewActivity.getListItems().add(result);
                eventoverViewActivity.updateEventOverView();
            }
        }.execute(event);
    }
}