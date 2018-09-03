package com.in.ac.srit.books;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.in.ac.srit.books.Room.Favorites;
import com.in.ac.srit.books.Room.FavoritesViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private static final String URL_LINK = "https://www.googleapis.com/books/v1/volumes?q=";
    public static final String MAXIMUMNOBOOKS = "&maxResults=20";


    EditText searchBookName;
    Button searchButton;
    ProgressBar resultLoadingProgressBar;
    RecyclerView dataRecyclerview;
    String bookName;
    LinearLayoutManager linearManager;
    List<BookData> bookDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchBookName=findViewById(R.id.bookname_edt_txt_box);
        searchButton=findViewById(R.id.search_the_book_button);
        resultLoadingProgressBar=findViewById(R.id.search_activity_progress_bar);
        dataRecyclerview=findViewById(R.id.results_recycler_view);
        linearManager=new LinearLayoutManager(this);
        resultLoadingProgressBar.setVisibility(View.GONE);
        dataRecyclerview.setLayoutManager(linearManager);
        bookDataList=new ArrayList<>();
        if (savedInstanceState!=null)
        {
            searchBookName.setVisibility(View.GONE);
            searchButton.setVisibility(View.GONE);
            bookDataList= (List<BookData>) savedInstanceState.getSerializable("LIST");
            int position=savedInstanceState.getInt("POSITION");
            dataRecyclerview.setAdapter(new BooksRecyclerAdapter(SearchActivity.this,bookDataList));
            dataRecyclerview.scrollToPosition(position);
        }
        else
        {
            searchBookName.setVisibility(View.VISIBLE);
            searchButton.setVisibility(View.VISIBLE);

        }

    }

    public void loadBooks(View view) {
        resultLoadingProgressBar.setVisibility(View.VISIBLE);
        bookName=searchBookName.getText().toString();
        String url=URL_LINK+bookName+MAXIMUMNOBOOKS;
        RequestQueue queue= Volley.newRequestQueue(this);
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (bookDataList!=null){
                    bookDataList.clear();
                }
                searchBookName.setVisibility(View.GONE);
                searchButton.setVisibility(View.GONE);
                resultLoadingProgressBar.setVisibility(View.GONE);
                dataRecyclerview.setVisibility(View.VISIBLE);
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("items");
                    for (int i=0; i<array.length();i++)
                    {
                        JSONObject object=array.optJSONObject(i);
                        String ids=object.optString("id");
                        JSONObject book=object.optJSONObject("volumeInfo");
                        JSONObject imageLinks=book.optJSONObject("imageLinks");
                        String title=book.optString("title");
                       String imageUrl=imageLinks.optString("thumbnail");
                        BookData bookData=new BookData(title,imageUrl,ids);
                        bookDataList.add(bookData);
                    }

                    dataRecyclerview.setAdapter(new BooksRecyclerAdapter(SearchActivity.this,bookDataList));

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.search)
        {
            dataRecyclerview.setVisibility(View.GONE);
        }
        else if (id==R.id.favorites)
        {
            loadFavorites();

        }
        return true;
    }

    private void loadFavorites() {
       resultLoadingProgressBar.setVisibility(View.GONE);
       searchButton.setVisibility(View.GONE);
       searchBookName.setVisibility(View.GONE);
        FavoritesViewModel viewModel= ViewModelProviders.of(this).get(FavoritesViewModel.class);
        viewModel.getList().observe(this, new Observer<List<Favorites>>() {
            @Override
            public void onChanged(@Nullable List<Favorites> favorites) {
                if (bookDataList!=null)
                {
                    bookDataList.clear();
                }
                if (favorites.size()>0){
                   for (int i=0;i<favorites.size();i++)
                   {
                       String title=favorites.get(i).getTitle();
                       String imagUrl=favorites.get(i).getImageurl();
                       String id=favorites.get(i).getId();
                       BookData data=new BookData(title,imagUrl,id);
                       bookDataList.add(data);

                   }
                    dataRecyclerview.setAdapter(new BooksRecyclerAdapter(SearchActivity.this,bookDataList));

                }
                else
                {
                    searchButton.setVisibility(View.VISIBLE);
                    searchBookName.setVisibility(View.VISIBLE);
                    Toast.makeText(SearchActivity.this, "There are no favorites to Load", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (bookDataList!=null)
        {
            int postion=linearManager.findFirstCompletelyVisibleItemPosition();
            outState.putInt("POSITION",postion);
        }
        outState.putSerializable("LIST", (Serializable) bookDataList);
    }
}
