package example.naimish.com.alarm.Activities;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import example.naimish.com.alarm.Adapters.MyCustomAdapter;
import example.naimish.com.alarm.Pojo.ListItem;
import example.naimish.com.alarm.R;

public class SetAlarm extends AppCompatActivity{
    private MyCustomAdapter mAdapter;
    private ListView list;
    Button cancel, ok;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null,imagePath;  //to store audio and image path
    Boolean record=false; //flag for recording
    int pT,cT;  //to store picked time,current time,picked date,current date in Integer


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        list = (ListView) findViewById(R.id.listView);
        cancel = (Button) findViewById(R.id.cancel);
        ok = (Button) findViewById(R.id.ok);
        Calendar mcurrentTime = Calendar.getInstance();
        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        final int minute = mcurrentTime.get(Calendar.MINUTE);



        cT= Integer.parseInt(""+hour+minute);
        pT=cT;

        final String currentTime = hour + ":" + minute;

        mAdapter = new MyCustomAdapter(SetAlarm.this);
        list.setAdapter(mAdapter);
        list.setItemsCanFocus(true);

        mAdapter.addItem(new ListItem("Time", currentTime));
        mAdapter.addItem(new ListItem("Notes"));
        mAdapter.addItem(new ListItem("My label"));
        mAdapter.addItem(new ListItem("Image", "None"));
        mAdapter.addItem(new ListItem("Recording"));


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v1=list.getChildAt(0);
                TextView time= (TextView) v1.findViewById(R.id.subheading);


                v1= list.getChildAt(1);
                TableLayout mTable= (TableLayout) v1.findViewById(R.id.TableLayout);
                String notes="";
                for (int i = 0; i < mTable.getChildCount(); i++) {
                    TableRow mRow = (TableRow) mTable.getChildAt(i);
                    EditText text = (EditText) mRow.getChildAt(1);
                    try {
                        notes = notes + text.getText().toString() + "|";
                    }
                    catch (Exception e){
                        notes=notes+"No notes";
                    }
                }

                v1= list.getChildAt(2);
                EditText label= (EditText) v1.findViewById(R.id.label);


                if(pT==cT){
                    Toast.makeText(SetAlarm.this,"Pick a new Time",Toast.LENGTH_SHORT).show();
                }

                else if(pT<cT){
                    Toast.makeText(SetAlarm.this,"Selected Time already past ",Toast.LENGTH_SHORT).show();
                }

                else {
                    if(imagePath==null)
                    imagePath="No Image";

                    if(outputFile==null)
                    outputFile="No Audio";

                    Intent sendIntent = new Intent();
                    sendIntent.setPackage("com.whatsapp");
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, time.getText().toString()+"\n"+notes+"\n"+label.getText().toString()+"\n"+imagePath
                            +"\n"+outputFile);
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, "Share via"));
                  //  finish();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                final TextView subheading = (TextView) view.findViewById(R.id.subheading); //to manipulate subheading of items 0,1,4

                if (i == 0) {
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(SetAlarm.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            subheading.setText(selectedHour + ":" + selectedMinute);
                            pT= Integer.parseInt(""+selectedHour+selectedMinute);
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.show(); //updates subheading of item 0 with time

                }

                else if (i == 3) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1); //pick photo from gallery
                }
            }
        });



    }


    public void addNoteItem(View v){    //create fresh instance of row just like that present in xml mylist2.xml
        TableLayout tl=(TableLayout)findViewById(R.id.TableLayout);
        TableRow tr=new TableRow(this);

        final ImageButton ib= new ImageButton(this);
        ib.setBackgroundResource(R.drawable.close);
        ib.setFocusable(true);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNoteItem(ib);
            }
        });


        EditText et=new EditText(this);
        et.setHint("Note");
        et.setBackgroundColor(Color.TRANSPARENT);
        et.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        et.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        tr.addView(et);//add edittext to new row
        tr.addView(ib);//add imagebutton to new row
        tl.addView(tr);//add the new row to table

    }
    public void deleteNoteItem(View v){          // deleting a row of tablelayout
        View row = (View) v.getParent();
        ViewGroup container = ((ViewGroup)row.getParent());
        container.removeView(row);
        container.invalidate();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturned) {   //For getting image selected
        super.onActivityResult(requestCode, resultCode, imageReturned);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturned.getData();
                    imagePath = getRealPathFromURI(getApplicationContext(), selectedImage);
                    String name=imagePath.substring(imagePath.lastIndexOf('/')+1);
                    View v = list.getChildAt(3);
                    TextView subheading = (TextView) v.findViewById(R.id.subheading);
                    subheading.setText(name);

                }
                break;
        }
    }


    public String getRealPathFromURI(Context context, Uri contentUri) {  //to get the path of image selected
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    public void record_stop(View v){
        View row = (View) v.getParent();
        Button play= (Button) row.findViewById(R.id.play);
        ImageButton delete_recording= (ImageButton) row.findViewById(R.id.delete_recording);

        if(!record) {

            outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
            myAudioRecorder=new MediaRecorder();
            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            myAudioRecorder.setAudioEncodingBitRate(24000);
            myAudioRecorder.setAudioSamplingRate(44100);
            myAudioRecorder.setOutputFile(outputFile);
            try {
                myAudioRecorder.prepare();
                myAudioRecorder.start();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Toast.makeText(getApplicationContext(), "Recording", Toast.LENGTH_LONG).show();
            record=true;
        }
        else{
            play.setVisibility(View.VISIBLE);
            delete_recording.setVisibility(View.VISIBLE);
            myAudioRecorder.stop();
            myAudioRecorder.release();
            myAudioRecorder  = null;
            Toast.makeText(getApplicationContext(), "Recorded",Toast.LENGTH_LONG).show();
            record=false;
        }

    }

    public void delete_recording(View v){
        File file = new File(outputFile);
        file.delete();
        View row = (View) v.getParent();
        Button play= (Button) row.findViewById(R.id.play);
        play.setVisibility(View.GONE);
        v.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), "Recording deleted",Toast.LENGTH_LONG).show();
    }


    public void recordPlay(View v)throws IllegalArgumentException,SecurityException,IllegalStateException{
        MediaPlayer m = new MediaPlayer();

        try {
            m.setDataSource(outputFile);
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            m.prepare();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        m.start();
        Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
    }

}