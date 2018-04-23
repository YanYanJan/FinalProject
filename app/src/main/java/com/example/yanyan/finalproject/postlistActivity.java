package com.example.yanyan.finalproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by yanyan on 4/7/18.
 */

public class postlistActivity extends AppCompatActivity {

    private Context mContext;
    FloatingActionButton create;
    Post selectedPost;
    ImageButton contactbtn;
    PostAdapter adapter;
    ArrayList<Post> postlist;

    private Spinner sortSpinner;

    SearchView searchView;

    DatabaseHelper myDB;
    ListView mpostlist;
    private static final String TAG = "postlistActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.Home);
        setContentView(R.layout.post_list);

        mContext = this;
        contactbtn = findViewById(R.id.contact);
        create = findViewById(R.id.create);
        sortSpinner =findViewById(R.id.sort);

        mpostlist = findViewById(R.id.post_list);
        myDB = new DatabaseHelper(this);
        postlist = new ArrayList<>();


        //generate the listview

//        Log.d(TAG,"populateListView: Displaying data in the ListView");
//        //get the data dan append to a list
//        //ArrayList<Post> currentlist= new ArrayList<Post>();
//        Cursor data = myDB.getAllData();
//        while (data.moveToNext()){
//            String book = data.getString(1);
//            String author =data.getString(2);//tag
//            String hashtag =data.getString(3);//rate
//            String quote =data.getString(4);//author
//            String rate = data.getString(5);//quote
//            String thoughts =data.getString(6);
//            String record = data.getString(7);
//            String Bookcover = data.getString(8);
//            String datetime =data.getString(9);
//            Post post = new Post(book, author,hashtag, quote, rate, thoughts, record ,Bookcover,datetime);
//            postlist.add(post);
//        }
//        Collections.reverse(postlist);


        //set the spinners of sorting
        String[] sortalg = new String[]{"Sort by", "Newly Added","Highest Rate"};
        ArrayAdapter<String> sortadpter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sortalg);
        sortSpinner.setAdapter(sortadpter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sortby =sortSpinner.getSelectedItem().toString();
                if (sortby.equals("Highest Rate")){
                    populateListview(sorthighest());
                }
                else{
                    populateListview(getPostlist());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // click the list_item
        mpostlist.setOnItemClickListener( new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id){
                selectedPost = postlist.get(position);

                Intent detailIntent = new Intent(mContext, PostDetailActivity.class);
                detailIntent.putExtra("title", selectedPost.gettitle());
                detailIntent.putExtra("author", selectedPost.getauthor());
                detailIntent.putExtra("quote", selectedPost.getquote());
                detailIntent.putExtra("rate", selectedPost.getrate());
                detailIntent.putExtra("tag", selectedPost.gethashtag());
                detailIntent.putExtra("review", selectedPost.getReviews());
                detailIntent.putExtra("record", selectedPost.getAudiopath());
                detailIntent.putExtra("datetime", selectedPost.getDatetime());
                detailIntent.putExtra("bookcover",selectedPost.getBookcover());

                startActivity(detailIntent);

            }
        });


        //create new posts
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createIntent = new Intent(mContext, BookListActivity.class);
                startActivity(createIntent);
            }


        });

        //contact us by writing an email

        contactbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.putExtra(Intent.EXTRA_EMAIL, "yany@oxy.edu");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
//                startActivity(intent);
                startActivity(Intent.createChooser(intent, "ChoseEmailClient"));

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                String newText = s.toLowerCase();
                ArrayList<Post> newList = new ArrayList<>();
                for (Post p : postlist) {
                    String title = p.gettitle().toLowerCase();
                    if (title.contains(newText))
                        newList.add(p);
                }

                populateListview(newList);

                return false;

            }
        });
        return true;
    }


    public ArrayList<Post> getPostlist(){
        Log.d(TAG,"populateListView: Displaying data in the ListView");
        //get the data dan append to a list
        ArrayList<Post> currentlist= new ArrayList<Post>();
        Cursor data = myDB.getAllData();
        while (data.moveToNext()){
            String book = data.getString(1);
            String author =data.getString(2);//tag
            String hashtag =data.getString(3);//rate
            String quote =data.getString(4);//author
            String rate = data.getString(5);//quote
            String thoughts =data.getString(6);
            String record = data.getString(7);
            String Bookcover = data.getString(8);
            String datetime =data.getString(9);
            Post post = new Post(book, author,hashtag, quote, rate, thoughts, record ,Bookcover,datetime);
            currentlist.add(post);
        }
        Collections.reverse(currentlist);
        postlist = currentlist;
        return postlist;
    }


    public ArrayList<Post> sorthighest(){
        //ArrayList<Post> postlist_ratehigh = postlist;
        Collections.sort(postlist, new Comparator<Post>() {
            @Override
            public int compare(Post p1, Post p2) {
                return Double.compare(p1.ratedouble, p2.ratedouble);
            }
        });
        Collections.reverse(postlist);
        return postlist;
    }

    private void populateListview( ArrayList<Post> postlist){
        adapter = new PostAdapter(this,postlist);
        mpostlist.setAdapter(adapter);
    }




}
