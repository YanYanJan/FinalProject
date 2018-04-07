package com.example.yanyan.finalproject;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by yanyan on 4/6/18.
 */

public class newpostActivity extends AppCompatActivity {

    private Context mContext;
    DatabaseHelper myDb;
    EditText editbook, editauthor, edittags, editquote, editthoughts;
    Button submit, record , stop;
    ImageButton playback;
    MediaRecorder mAudioRecorder;
    String audiooutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newpost);

        myDb = new DatabaseHelper(this);

        editbook = findViewById(R.id.title_edit);
        editauthor = findViewById(R.id.author_edit);
        edittags = findViewById(R.id.tags_edit);
        editquote = findViewById(R.id.quote_edit);
        editthoughts = findViewById(R.id.review_edit);
        final RatingBar ratingBar = (RatingBar)findViewById(R.id.ratebar);
        submit = findViewById(R.id.submit);


        //audio recorder
        record = findViewById(R.id.startrecord);
        stop = findViewById(R.id.stoprecord);
        playback =findViewById(R.id.playrecord);
        stop.setEnabled(false);
        playback.setEnabled(false);

        audiooutput = Environment.getExternalStorageDirectory().getAbsolutePath()+"/recording.3gp";

        mAudioRecorder = new MediaRecorder();
        mAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mAudioRecorder.setOutputFile(audiooutput);

        record.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                try {
                    mAudioRecorder.prepare();
                    mAudioRecorder.start();
                }catch (IllegalStateException ise){

                } catch (IOException e) {
                    e.printStackTrace();
                }
                record.setEnabled(false);
                stop.setEnabled(true);

                Toast.makeText(getApplicationContext(),"Recording started", Toast.LENGTH_LONG).show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAudioRecorder.stop();
                mAudioRecorder.release();
                mAudioRecorder = null;
                record.setEnabled(true);
                stop.setEnabled(false);
                playback.setEnabled(true);
                Toast.makeText(getApplicationContext(),"Record complete", Toast.LENGTH_LONG).show();
            }
        });

        playback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayer mediaPlayer= new MediaPlayer();

                try{
                    mediaPlayer.setDataSource(audiooutput);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    Toast.makeText(getApplicationContext(),"Playing Audio", Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rate = String.valueOf(ratingBar.getRating());
                String book = editbook.getText().toString();
                String author = editauthor.getText().toString();
                String tags = edittags.getText().toString();
                String quote = editquote.getText().toString();
                String thoughts = editthoughts.getText().toString();
                String record = audiooutput;

                //get the date and time when submitted
                Date todayDate = new Date();
                DateFormat dateFormat = DateFormat.getDateTimeInstance();
                String datetime = dateFormat.format(todayDate);

                boolean checksub = thoughts.equals("")&&record.equals("");



//                boolean checksub = checksubmit(rate,book,author,thoughts,record);

                if (rate.equals("")||book.equals("")||author.equals("")||checksub){
                    Toast.makeText(newpostActivity.this, "Please complete the required information", Toast.LENGTH_LONG).show();
                }
                else{
                    myDb.insertInput(book, author, tags, quote, rate, thoughts,record, datetime);
                    Toast.makeText(newpostActivity.this, rate, Toast.LENGTH_LONG).show();

                }
            }
        });

    }


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
