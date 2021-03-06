package ch.gibb.share.sharelendar.view;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ch.gibb.share.sharelendar.R;
import ch.gibb.share.sharelendar.model.SchoolClass;
import ch.gibb.share.sharelendar.service.SharelendarService;

public class ClassoverViewActivity extends AppCompatActivity {

    public static String admin;
    private ArrayList<SchoolClass> listItems= new ArrayList<SchoolClass>();
    private ArrayAdapter<SchoolClass> adapter;
    private SharelendarService service = new SharelendarService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classover_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        handleClassListListener();
    }

    private void handleClassListListener() {
        ListView classList = (ListView) findViewById(R.id.classList);
        getDataSchoolClassesAndFillInList(classList);

        classList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(ClassoverViewActivity.this, EventoverViewActivity.class);
                SchoolClass schoolClass = (SchoolClass)parent.getItemAtPosition(position);
                myIntent.putExtra("schoolClass", schoolClass);
                startActivity(myIntent);
            }
        });
    }

    private void getDataSchoolClassesAndFillInList(ListView classList) {
        AsyncTask<Void, Void, List<SchoolClass>> asyncTask = new AsyncTask<Void, Void, List<SchoolClass>>() {

            @Override
            protected List<SchoolClass> doInBackground(Void... params) {
                listItems.addAll(service.getAllSchoolClasses());
                return listItems;
            }

            @Override
            protected void onPostExecute(List<SchoolClass> result) {
                super.onPostExecute(result);
                adapter.notifyDataSetChanged();
            }
        };

        adapter = new ArrayAdapter<SchoolClass>(this,android.R.layout.simple_list_item_1 , listItems);
        classList.setAdapter(adapter);

        asyncTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(admin == null) {
            getMenuInflater().inflate(R.menu.menu_classover_view, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            showSignInDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSignInDialog() {
        FragmentManager fm = getFragmentManager();
        SignInDialog signInDialog = new SignInDialog(this);
        signInDialog.show(fm, "");
    }

    public void updateClassOverView() {
        finish();
        startActivity(getIntent());
    }
}
