package example.naimish.com.alarm.Activities;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import example.naimish.com.alarm.R;
import example.naimish.com.alarm.Recievers.AlarmStopReciever;

public class WakeActivity1 extends AppCompatActivity implements View.OnClickListener{

    int colors[]={R.color.blue,R.color.red,R.color.green,R.color.yellow,R.color.black,R.color.brown,R.color.pink,R.color.orange};
    String colornames[]={"blue","red","green","yellow","black","brown","pink","orange"};

    int buttonId[]={R.id.button,R.id.button1,R.id.button2,R.id.button3};
    CardView cards[]=new CardView[4];
    long id;
    int textId[]={R.id.color1,R.id.color2,R.id.color3,R.id.color4};
    TextView tv[]=new TextView[4];
    int problems,difficulty=1;
    int anspos;
    String card1Color,backgroundColor;
    RelativeLayout rl;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wake1);

        id=getIntent().getLongExtra("id",0);
        rl= (RelativeLayout) findViewById(R.id.background);

        for(int i=0;i<4;i++){
            cards[i]= (CardView) findViewById(buttonId[i]);
            tv[i]= (TextView) findViewById(textId[i]);
        }

        textView= (TextView) findViewById(R.id.textView2);
        problems=3;
        if(difficulty==1)
            easylevel();
        else if(difficulty==2) {
            mediumlevel();
        }

        for(int i=0;i<4;i++){
            cards[i].setOnClickListener(this);
        }



    }

    @Override
    public void onClick(View view) {
        CardView b = (CardView)view;
        RelativeLayout rel= (RelativeLayout) b.getChildAt(0);
        TextView t= (TextView) rel.getChildAt(0);
        Intent intent = new Intent(this, AlarmStopReciever.class);
        intent.putExtra("id",id);

        if(difficulty==1) {
            if (t.getText() == colornames[anspos]) {
                if (problems == 1) {

                    sendBroadcast(intent);

                    finish();
                }
                else {
                    problems--;
                    easylevel();
                    textView.setText("Problems : " + problems);
                }
            } else {
                Toast.makeText(WakeActivity1.this, "Try Again", Toast.LENGTH_SHORT).show();
                easylevel();
            }
        }

        else if(difficulty==2){
            if (t.getText().toString().equalsIgnoreCase(backgroundColor+" & "+card1Color)) {
                if (problems == 1) {
                    sendBroadcast(intent);
                    finish();
                }
                else {
                    problems--;
                    mediumlevel();
                    textView.setText("Problems : " + problems);
                }
            } else {
                Toast.makeText(WakeActivity1.this, "Try Again", Toast.LENGTH_SHORT).show();
                mediumlevel();
            }
        }
    }

    void easylevel(){

        ArrayList<Integer> colorslist = new ArrayList<>();
        for (int i=0; i<8; i++) {
            colorslist.add(colors[i]);
        }
        Collections.shuffle(colorslist);

        ArrayList<String> namelist = new ArrayList<>();
        for (int i=0; i<8; i++) {
            namelist.add(colornames[i]);
        }
        Collections.shuffle(namelist);

        cards[0].setBackgroundColor(getResources().getColor(colorslist.get(0)));
        tv[0].setText(namelist.get(0));

        cards[1].setBackgroundColor(getResources().getColor(colorslist.get(1)));
        tv[1].setText(namelist.get(1));

        cards[2].setBackgroundColor(getResources().getColor(colorslist.get(2)));
        tv[2].setText(namelist.get(2));

        cards[3].setBackgroundColor(getResources().getColor(colorslist.get(3)));
        tv[3].setText(namelist.get(3));

        Random random = new Random();
        String ans=namelist.get(random.nextInt(4));
        anspos= Arrays.asList(colornames).indexOf(ans);

       rl.setBackgroundColor(getResources().getColor(colors[anspos]));
    }

    void mediumlevel(){

        Random random = new Random();
        int i=0,A[]={100,101,102,103};
        while(i<4){
            int x=random.nextInt(8);
            int pos= -1;
            for(int j=0;j<4;j++){
                if(x==A[j])
                {
                    pos=1;
                    break;
                }
            }
            if(pos==-1){
                A[i]=x;
                i++;
            }
        }//generates 4 random position of array colors and put them in array A
        int randomBack=random.nextInt(8);

        cards[0].setCardBackgroundColor(getResources().getColor(colors[A[0]]));
        cards[1].setCardBackgroundColor(getResources().getColor(colors[A[1]]));
        cards[2].setCardBackgroundColor(getResources().getColor(colors[A[2]]));
        cards[3].setCardBackgroundColor(getResources().getColor(colors[A[3]]));

        tv[0].setText(colornames[random.nextInt(8)]+" & "+ colornames[random.nextInt(8)]);
        tv[1].setText(colornames[random.nextInt(8)]+" & "+ colornames[random.nextInt(8)]);
        tv[2].setText(colornames[random.nextInt(8)]+" & "+ colornames[random.nextInt(8)]);
        tv[3].setText(colornames[random.nextInt(8)]+" & "+ colornames[random.nextInt(8)]);



        card1Color=colornames[A[0]];
        backgroundColor=colornames[randomBack];

        tv[random.nextInt(4)].setText(backgroundColor+" & "+card1Color);

    }

    @Override
    public void onAttachedToWindow() {
        Window window = getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onAttachedToWindow();
    }

    @Override
    public void onBackPressed() {

    }


}
