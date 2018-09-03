package com.in.ac.srit.books;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.in.ac.srit.books.Room.Favorites;
import com.in.ac.srit.books.Room.FavoritesViewModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class BookDetail extends AppCompatActivity {
    public static final String BOOKDETAILURL="https://www.googleapis.com/books/v1/volumes/";
    String id;
    TextView descriptionTv,ratingTv,publisherTv;
    ImageView favorite,coverpage;
    ProgressBar progressBar;
    private  boolean state;
    FavoritesViewModel viewModel;
    String title,description,publisher,imageUrl,rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        favorite=findViewById(R.id.favorite_image);
        descriptionTv=findViewById(R.id.description);
        ratingTv=findViewById(R.id.rating);
        coverpage=findViewById(R.id.book_cover_page);
        publisherTv=findViewById(R.id.publisher);
        progressBar=findViewById(R.id.progress);
        AdView adView = findViewById(R.id.adView);
        id=getIntent().getStringExtra("ID");
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(getString(R.string.adds)).build();
        adView.loadAd(adRequest);
        viewModel= ViewModelProviders.of(this).get(FavoritesViewModel.class);
        checkBook();
      loadBookDetails();

    }

    private void loadBookDetails() {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue= Volley.newRequestQueue(this);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, BOOKDETAILURL + id,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                try {
                JSONObject volumeInfo=response.optJSONObject("volumeInfo");
                title=volumeInfo.optString("title");
                setTitle(title);
               publisher=volumeInfo.optString("publisher");
                description=volumeInfo.optString("description");
                rating=String.valueOf(volumeInfo.getDouble("averageRating"));
                JSONObject image=volumeInfo.optJSONObject("imageLinks");
                imageUrl=image.optString("thumbnail");
                publisherTv.setText(publisher);
                descriptionTv.setText(description);
                ratingTv.setText(String.valueOf(rating));
                    Picasso.with(BookDetail.this).load(imageUrl).into(coverpage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    public void toggleImage(View view) {
        if (state)
        {
            favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            delete();
            state=!state;
        }
        else
        {
            favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
            insert();state=!state;

        }
    }

    private void insert() {
        Favorites favorites=new Favorites(id,description,imageUrl,title,publisher,rating);
        viewModel.insertData(favorites);
    }

    private void delete() {
        Favorites favorites=new Favorites(id,description,imageUrl,title,publisher,rating);
        viewModel.deleteData(favorites);
    }

    public void checkBook()
    {
        Favorites fav=viewModel.chekingDb(id);
        if (fav==null)
        {
            state=false;
            favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
        else
        {
            state=true;
            favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return true;
    }
}
