package com.codeschool.candycoded;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.codeschool.candycoded.CandyContract.CandyEntry;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private Candy[] candies;

    private CandyDbHelper candyDbHelper = new CandyDbHelper(this);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //query for all the candies and use a cursor instead of arrayAdapter
        //as it holds the SQL returned as a string
       SQLiteDatabase db = candyDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM candy", null);

        //Create the adapter from the results in the cursor
        final CandyCursorAdapter adapter = new CandyCursorAdapter(this,cursor);

        //create a TextView variable called textView
        //and find the value of text_view_title using r.id (resources file)
        TextView textView = (TextView)this.findViewById(R.id.text_view_title);
        //set the TextView via the xml string products_title
        textView.setText(R.string.products_title);

        /*//create an array to hold strings
        final ArrayList<String>candy_list = new ArrayList<String>();
        candy_list.add("Tropical Wave");

        //create the array adapter
        //it will be of type string
        //the layout is the xml file with the id of text_view_candy
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.list_item_candy,
                R.id.text_view_candy,
                //array list of candies
                candy_list
        );*/

    //    final CandyCursorAdapter adapter = new CandyCursorAdapter(this, cursor);
        ListView listView =(ListView)this.findViewById(R.id.list_view_candy);

        listView.setAdapter(adapter);
        //context of where it will run (mainactivity)
        Context context = this;

        /*create toast*/
        String text = "Hello!";
        int duration = Toast.LENGTH_SHORT;
        //what the toast should display
        Toast toast = Toast.makeText(context, text, duration);
        //show the toast
       toast.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
               // Toast toast = Toast.makeText(MainActivity.this, ""+i, Toast.LENGTH_SHORT);
               // toast.show();

                /*create an intent that tells the detail class to start*/
                Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
                //use the candy list and reference the position of the candy item with int i
            //    detailIntent.putExtra("candy_name", candy_list.get(i));

                /*instead of passing each candy property to the detail activity,
                pass i and then query the database in the Detail Activity*/
                detailIntent.putExtra("position",i);
         //       detailIntent.putExtra("candy_name", candies[i].name);
         //       detailIntent.putExtra("candy_image", candies[i].image);
          //      detailIntent.putExtra("candy_price", candies[i].price);
         //       detailIntent.putExtra("candy_desc", candies[i].description);
                startActivity(detailIntent);
            }

        });
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://s3.amazonaws.com/courseware.codeschool.com/super_sweet_android_time/API/CandyCoded.json", new TextHttpResponseHandler(){
            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                Log.e("AsyncHttpClient", "response = " + response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                Log.d("AsyncHttpClient", "response = " + response);
                Gson gson = new GsonBuilder().create();
                //using gson library converts json into gson
                //uses json string 'response' and the class 'Candy'
                //square brakcets determine that we have an array
                candies = gson.fromJson(response, Candy[].class);

                //clear the adapter
        //        adapter.clear();
         //       for(Candy candy : candies) {
                    //add the candy name to the adapter using a for loop to loop over each candy in the array
         //           adapter.add(candy.name);
                    //method to save candies to db
                    addCandiesToDatabase(candies);

                    SQLiteDatabase db = candyDbHelper.getWritableDatabase();
                    Cursor cursor = db.rawQuery("SELECT * FROM candy", null);
                    //update the cursor with the latest database enteries
                    adapter.changeCursor(cursor);
                }
        //    }


         });

    }
    //function to pass the candy array and it will allow us to add to db
    public void addCandiesToDatabase(Candy[] candies){
        //Create a variable and get access to the database
        SQLiteDatabase db = candyDbHelper.getWritableDatabase();

        if(candies == null) return;
        else db.delete(CandyEntry.TABLE_NAME,null,null);
        //loop to read candy in candies array
        for(Candy candy: candies){
            //insert values into the db with ContentValue
            //ContentValues ensures data is inserted in the right format
            //uses validation
            ContentValues values = new ContentValues();
            //put the column name and value into the ContentValues object using the put method
            values.put(CandyEntry.COLUMN_NAME_NAME, candy.name);
            values.put(CandyEntry.COLUMN_NAME_PRICE, candy.price);
            values.put(CandyEntry.COLUMN_NAME_DESC, candy.description);
            values.put(CandyEntry.COLUMN_NAME_IMAGE, candy.image);
            //insert into the db
            db.insert(CandyEntry.TABLE_NAME, null,values);

        }
    }








}
