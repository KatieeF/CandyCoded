package com.codeschool.candycoded;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.codeschool.candycoded.CandyContract.CandyEntry;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //get the intent that created this activity
        Intent intent = DetailActivity.this.getIntent();

        if (intent != null && intent.hasExtra("position")) {
            int position = intent.getIntExtra("position", 0);

            CandyDbHelper candyDbHelper = new CandyDbHelper(this);
            SQLiteDatabase db = candyDbHelper.getWritableDatabase();
            //query all the candy
            Cursor cursor = db.rawQuery("SELECT * FROM candy", null);
            //move the cursor to the selected position
            cursor.moveToPosition(position);

            int columnIndex = cursor.getColumnIndexOrThrow(
                    CandyEntry.COLUMN_NAME_NAME);
            //create variable to store candy name passed from columnindex
            //also add in an exception to incase the column doesn't exist
            String candyName = cursor.getString(cursor.getColumnIndexOrThrow(CandyEntry.COLUMN_NAME_NAME));
            String candyPrice = cursor.getString(cursor.getColumnIndexOrThrow(CandyEntry.COLUMN_NAME_PRICE));
            String candyImage = cursor.getString(cursor.getColumnIndexOrThrow(CandyEntry.COLUMN_NAME_IMAGE));
            String candyDesc = cursor.getString(cursor.getColumnIndexOrThrow(CandyEntry.COLUMN_NAME_DESC));

            //programmatically set the title so it displays each candy name
            setTitle(candyName);
            //setTitle(R);
            //r.string.title_mytitle
            //make string to hold data

            //look up the data inside the intent
            TextView textView = (TextView) this.findViewById(R.id.text_view_name);
            textView.setText(candyName);

            TextView textViewPrice = (TextView) this.findViewById(R.id.text_view_price);
            textViewPrice.setText(candyPrice);

            TextView textViewDesc = (TextView) this.findViewById(R.id.text_view_description);
            textViewDesc.setText(candyDesc);

            ImageView imageView = (ImageView) this.findViewById(
                    R.id.image_view_candy);
            Picasso.with(this).load(candyImage).into(imageView);

            //check that the value is not null and has the vslue
            //      if(intent.hasExtra("position")) {
            //          (TextView) DetailActivity.this.findViewById(R.id.text_view_name).setText(candyName);
            //          (TextView) DetailActivity.this.findViewById(R.id.text_view_price).setText(candyName);
            //          (TextView) DetailActivity.this.findViewById(R.id.text_view_description).setText(candyName);
//
            //           Picasso.with(DetailActivity.this).load(candyImage).into(
            //                   (ImageView) DetailActivity.this.findViewById(R.id.image_view_candy));
            //        }

        }

    }
}


        //create a text view to show the candy name
        //find the textview by its id

       /* String candyImage = "";
        if(intent != null && intent.hasExtra("candy_image")) {
            candyImage = intent.getStringExtra("candy_image");
        }
        ImageView imageView = (ImageView)this.findViewById(R.id.image_view_candy);
       Picasso.with(this).load(candyImage).into(imageView);


        String candyPrice = "";
        if(intent != null && intent.hasExtra("candy_price")){
            candyPrice = intent.getStringExtra("candy_price");
        }
        TextView textViewPrice = (TextView)this.findViewById(R.id.text_view_price);
        textViewPrice.setText(candyPrice);


        String candyDesc = "";
        if(intent != null && intent.hasExtra("candy_desc")){
            candyDesc = intent.getStringExtra("candy_desc");
        }*/
      /*  TextView textViewDesc = (TextView)this.findViewById(R.id.text_view_description);
        textViewPrice.setText(candyDesc);
*/
        //Log.d("DetailActivity", "Intent Data: " + candyImage + ", " + candyPrice + candyDesc);
   // }
//}
