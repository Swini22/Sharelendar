package ch.gibb.share.sharelendar.view;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.gibb.share.sharelendar.R;
import ch.gibb.share.sharelendar.model.Event;
import ch.gibb.share.sharelendar.model.SchoolClass;
import ch.gibb.share.sharelendar.service.SharelendarService;

public class EventoverViewActivity extends AppCompatActivity {

    private SchoolClass schoolClass;
    private ArrayList<Event> listItems= new ArrayList<Event>();
    private ArrayAdapter<Event> adapter;
    private SharelendarService service = new SharelendarService();
    private EventoverViewActivity eventoverViewActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventover_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button addEvent = (Button) findViewById(R.id.addEventButton);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                AddEventDialog addEventDialog = new AddEventDialog(eventoverViewActivity, schoolClass);
                addEventDialog.show(fm, "");
            }
        });

        if (ClassoverViewActivity.admin == null) {
            addEvent.setVisibility(View.INVISIBLE);
        }

        Intent intent = getIntent();
        schoolClass = (SchoolClass) intent.getSerializableExtra("schoolClass");
        handleEventListListener();
    }

    private void handleEventListListener() {
        TextView eventuebersichtText = (TextView) findViewById(R.id.eventuebersichtText);
        eventuebersichtText.setText(eventuebersichtText.getText() + " " + schoolClass.getName());
        ListView eventList = (ListView) findViewById(R.id.eventListView);
        getDataEventDataAndFillInList(eventList);
        if (ClassoverViewActivity.admin != null) {
            eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Event event = (Event) parent.getItemAtPosition(position);
                    showUpdateDeleteDialog(event);
                }
            });
        }
    }

    private void getDataEventDataAndFillInList(ListView eventList) {
        AsyncTask<Void, Void, List<Event>> asyncTask = new AsyncTask<Void, Void, List<Event>>() {

            @Override
            protected List<Event> doInBackground(Void... params) {
                listItems.addAll(service.getAllEventsInSchoolClass(schoolClass.getId()));
                return listItems;
            }

            @Override
            protected void onPostExecute(List<Event> result) {
                super.onPostExecute(result);
                adapter.notifyDataSetChanged();
            }
        };

        adapter = new ArrayAdapter<Event>(this, android.R.layout.simple_list_item_1 , listItems);
        eventList.setAdapter(adapter);

        asyncTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_eventover_view, menu);
        return true;
    }

    public void updateEventOverView() {
        finish();
        startActivity(getIntent());
    }

    private void showUpdateDeleteDialog(Event event) {
        FragmentManager fm = getFragmentManager();
        UpdateDeleteEventDialog updateDeleteEventDialog = new UpdateDeleteEventDialog(eventoverViewActivity, event);
        updateDeleteEventDialog.show(fm, "");
    }


    public ArrayList<Event> getListItems() {
        return listItems;
    }
}
