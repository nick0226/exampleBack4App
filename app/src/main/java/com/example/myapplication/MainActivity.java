package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<ParseObject> mObject;
    private ProgressDialog mProgressDialog;
    private TextView mInfoTextView;
    private EditText enterTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new GetNameTask().execute();
      //mInfoTextView = (TextView) findViewById(R.id.textView);
        enterTxt = (EditText) findViewById(R.id.enterTXT);


    }

    public void onSendDataClick(View view) {
        ParseObject parseObject = new ParseObject("TestObject");
        parseObject.put("cat", enterTxt.getText().toString());
      //parseObject.put("cat", "Васька");
        parseObject.saveInBackground();
        Toast.makeText(this, "Ready", Toast.LENGTH_LONG).show();
    }

    //Для получения данных с облака
    public void onGetDataClick(View view) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TestObject");

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    mInfoTextView.setText(object.getString("cat"));
                } else {
                    Log.d("Log", e.toString());
                }
            }
        });
    }
    //

    public void updateObject(View view) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TestObject");

        // Retrieve the object by id
        query.getInBackground("hfXaktpEAD", new GetCallback<ParseObject>() {
            public void done(ParseObject entity, ParseException e) {
                if (e == null) {
                    // Update the fields we want to
                    entity.put("cat", enterTxt.getText().toString());
                    //entity.put("cat", "My new value");

                    // All other fields will remain the same
                    entity.saveInBackground();
                }
            }
        });
    }

    private class GetNameTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Parse.com ListView");
            mProgressDialog.setMessage("Загружаем...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Находим класс "Cats" на Parse.com (Back4App.com)
            ParseQuery<ParseObject> query = new ParseQuery<>("TestObject");
            query.orderByDescending("_created_at");
            try {
                mObject = query.find();
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            ListView listView = (ListView) findViewById(R.id.listView);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                    android.R.layout.simple_list_item_1);
            // Получим объект "name" из Parse.com
            for (ParseObject cats : mObject) {
                adapter.add((String) cats.get("cat"));
            }

            listView.setAdapter(adapter);
            mProgressDialog.dismiss();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Toast.makeText(getApplicationContext(),
                            mObject.get(position).getString("cat"),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}