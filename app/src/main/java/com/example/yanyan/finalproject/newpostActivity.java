package com.example.yanyan.finalproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yanyan on 4/6/18.
 */

public class newpostActivity extends AppCompatActivity {

    private Context mContext;
    private static final String TAG = newpostActivity.class.getSimpleName();
    DatabaseHelper myDb;
    EditText edittags, editquote, editthoughts;
    EditText editbook, editauthor;
    Button submit, record , stop;
    ImageButton playback;
    MediaRecorder mAudioRecorder;
    String audiooutput;
    String bookcoverURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.NewPost);
        setContentView(R.layout.newpost);

        myDb = new DatabaseHelper(this);

        editbook = findViewById(R.id.title_edit);
        editauthor = findViewById(R.id.author_edit);
        edittags = findViewById(R.id.tags_edit);
        editquote = findViewById(R.id.quote_edit);
        editthoughts = findViewById(R.id.review_edit);
        final RatingBar ratingBar = (RatingBar)findViewById(R.id.ratebar);
        submit = findViewById(R.id.submit);

        //load the book information from the previous searchview,get as a book

        Boolean fetch = this.getIntent().getExtras().getBoolean("fetch");

        if (fetch) {
            Book book = (Book) getIntent().getSerializableExtra("book");
            editbook.setText(book.getTitle());
            editauthor.setText(book.getAuthor());
            bookcoverURL = book.getCoverUrl();
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rate = String.valueOf(ratingBar.getRating());
                String book = editbook.getText().toString();
                String author = editauthor.getText().toString();
                String hashtag = edittags.getText().toString();
                String quote = editquote.getText().toString();
                String thoughts = editthoughts.getText().toString();
                String Bookcover = bookcoverURL;
                String record = audiooutput;

                //get the date and time when submitted
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String datetime = df.format(c.getTime());
                //Toast.makeText(newpostActivity.this, datetime, Toast.LENGTH_LONG).show();


                Post post = new Post(book, author,hashtag, quote, rate, thoughts, record ,Bookcover,datetime);



                //boolean checksub = thoughts.equals("")&&record.equals("");

                if (rate.isEmpty()||book.isEmpty()||author.isEmpty()||thoughts.isEmpty()){
                    Toast.makeText(newpostActivity.this, "Please complete the required information", Toast.LENGTH_LONG).show();
                }
                else{
                    //String book, String author, String tags, String quote, String rate, String thoughts,String record,String datetime
                    if (myDb.insertData(book, author,hashtag, quote, rate, thoughts, record ,Bookcover,datetime)){
                        Toast.makeText(newpostActivity.this, "New Post Submitted!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(newpostActivity.this,postlistActivity.class);
                        startActivity(intent);


                    }

                }


            }


        });

        //audio recorder
        record = findViewById(R.id.startrecord);
        stop = findViewById(R.id.stoprecord);
        playback =findViewById(R.id.playrecord);
        stop.setEnabled(false);
        playback.setEnabled(false);

        audiooutput = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+System.currentTimeMillis()+"/recording.3gp";
        mAudioRecorder = new MediaRecorder();
        mAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mAudioRecorder.setOutputFile(audiooutput);

//        record.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                try {
////                    audiooutput = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/"+System.currentTimeMillis()+"/recording.3gp";
////                    mAudioRecorder = new MediaRecorder();
////                    mAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
////                    mAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
////                    mAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
////                    mAudioRecorder.setOutputFile(audiooutput);
//                    mAudioRecorder.prepare();
//                    mAudioRecorder.start();
//                }catch (IllegalStateException ise){
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                record.setEnabled(false);
//                stop.setEnabled(true);
//
//                Toast.makeText(getApplicationContext(),"Recording started", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        stop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mAudioRecorder.stop();
//                mAudioRecorder.release();
//                record.setEnabled(true);
//                stop.setEnabled(false);
//                playback.setEnabled(true);
//                Toast.makeText(getApplicationContext(),"Record complete", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        playback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MediaPlayer mediaPlayer= new MediaPlayer();
//
//                try{
//                    mediaPlayer.setDataSource(audiooutput);
//                    mediaPlayer.prepare();
//                    mediaPlayer.start();
//                    Toast.makeText(getApplicationContext(),"Playing Audio", Toast.LENGTH_LONG).show();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });


    }

//    @SuppressLint("SdCardPath")
//    private String getFilename() {
//        File file = new File("/sdcard", "MyFile");
//
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//
//        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp3");
//    }



//    public boolean checksubmit(String rate, String book,String author,String thoughts,String record){
//        if (rate.equals(null)||book.equals(null)||author.equals(null)){
//            return false;
//        }
////        else if(!record.equals(null)||!thoughts.equals(null)){
////            return true;
////        }
//        else{
//            return true;
//        }
//    }

}
