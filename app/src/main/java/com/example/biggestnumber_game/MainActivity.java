package com.example.biggestnumber_game;

import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {

    public int counter = 0;
    public int level =1;
    TextView[] textViewArray = new TextView[8];
    TextView leveltextview;
    TextView counttextview;
    TextView timetextview;
    int max;
    int butarr[] = new int[10];
    int lowrangearr[] = new int[10];
    int highrangearr[] = new int[10];
    CountDownTimer countDownTimer;
    MediaPlayer mp=null;
    String path="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SOME VARIABLE DECLARATIONS

        timetextview = findViewById(R.id.timeview);

        mp=MediaPlayer.create(this,R.raw.lose_game);
        path="R.raw.out";

        textViewArray[0] =  findViewById(R.id.button1);
        textViewArray[1] =  findViewById(R.id.button2);
        textViewArray[2] =  findViewById(R.id.button3);
        textViewArray[3] =  findViewById(R.id.button4);
        textViewArray[4] =  findViewById(R.id.button5);
        textViewArray[5] =  findViewById(R.id.button6);
        textViewArray[6] =  findViewById(R.id.button7);
        textViewArray[7] =  findViewById(R.id.button8);

        lowrangearr[0]=0;
        lowrangearr[1]=0;
        lowrangearr[2]=0;
        lowrangearr[3]=0;
        lowrangearr[4]=-40;
        lowrangearr[5]=-60;
        lowrangearr[6]=-100;
        lowrangearr[7]=-100;
        lowrangearr[8]=-100;
        lowrangearr[9]=-100;

        highrangearr[0]=10;
        highrangearr[1]=20;
        highrangearr[2]=30;
        highrangearr[3]=40;
        highrangearr[4]=40;
        highrangearr[5]=60;
        highrangearr[6]=100;
        highrangearr[7]=100;
        highrangearr[8]=100;
        highrangearr[9]=100;

        butarr[0]=2;
        butarr[1]=3;
        butarr[2]=3;
        butarr[3]=4;
        butarr[4]=4;
        butarr[5]=4;
        butarr[6]=4;
        butarr[7]=5;
        butarr[8]=6;
        butarr[9]=8;

        //READ THE BEST POINT
        String alltext="";
        try{
            Scanner scan = new Scanner(
                    openFileInput("out.txt")
        );

            while (scan.hasNextLine())
            {
                String line = scan.nextLine();
                alltext+=line;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        //IF THIS IS NOT THE FIRST TIME PLAYING THIS GAME
            if (alltext!="")
            {
                level = Integer.parseInt(alltext);
                if(butarr[0] != butarr[level-1])
                {
                    for(int i=butarr[0];i<butarr[level-1];i++)
                    {
                        textViewArray[i].setVisibility(View.VISIBLE);
                    }

                }

            }

            //IF THERE IS NOTHING WRITTEN IN THE OUT.TXT FILE
            else
            {
                level=1;
                counter=0;
                try{
                    PrintStream output = new PrintStream(
                            openFileOutput("out.txt",MODE_PRIVATE)
                    );
                    output.println(level);
                    output.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }


        leveltextview =  findViewById(R.id.levelview);
        counttextview =  findViewById(R.id.countview);

        textViewArray[0].setVisibility(View.VISIBLE);
        textViewArray[1].setVisibility(View.VISIBLE);

        produce();


    }

    private void produce() {

        Random generate = new Random();
        boolean check=false;

        //PRINT THE CURRENT LEVEL AND SCORE
        counttextview.setText(Integer.toString(counter + ((level-1))*5));
        leveltextview.setText(Integer.toString(level));

        max=-200;
        long Sec=(long) (10000 - ((level-1)*1000));

        int temp[] = new int[butarr[level-1]];

        String alltext="";
        try{
            Scanner scan = new Scanner(
                    openFileInput("out.txt")
            );
            while (scan.hasNextLine())
            {
                String line = scan.nextLine();
                alltext+=line;
            }


        }
        catch (Exception e){
            e.printStackTrace();
        }

        //PRODUCE RANDOM NUMBERS AND SHOW IN THE BUTTON
        for(int i=0;i<butarr[level-1]; i++)
        {
            temp[i] = generate.nextInt(highrangearr[level-1] - lowrangearr[level-1] -1) +1 +lowrangearr[level-1];
            if(i>0)
            {
                do {
                    for(int h=0;h<i;h++)
                    {
                        if(temp[h]==temp[i])
                        {
                            check=true;
                            temp[i] = generate.nextInt(highrangearr[level-1] - lowrangearr[level-1] -1) +1 +lowrangearr[level-1];
                            break;
                        }
                        else
                        {
                            check=false;
                        }
                    }

                }while(check==true);
            }
            textViewArray[i].setText(Integer.toString(temp[i]));
            textViewArray[i].setBackgroundColor(Color.argb(150, generate.nextInt(151), generate.nextInt(151), generate.nextInt(151)));
        }

        //THE COUNTDOWN FOR BONUS
        countDownTimer = new CountDownTimer(Sec , 1000) {

            public void onTick(long millisUntilFinished) {
                timetextview.setText(" " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                counter=0;
                level=1;
                try{
                    PrintStream output = new PrintStream(
                            openFileOutput("out.txt",MODE_PRIVATE)
                    );
                    output.println(level);
                    output.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                mp.start();

                //WHEN TIME IS UP
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                new CountDownTimer(5000, 1000) {
                    public void onFinish() {
                        // When timer is finished
                        System.exit(0);
                        // Execute your code here
                    }

                    public void onTick(long millisUntilFinished) {
                        // millisUntilFinished    The amount of time until finished.
                        builder.setTitle("TIME IS UP!!");
                        builder.setMessage("Exits in " + millisUntilFinished/1000 + " seconds");
                        AlertDialog dialog =builder.create();
                        dialog.show();
                    }
                }.start();

                AlertDialog dialog =builder.create();
                dialog.show();

            }

        }.start();

        //FIND THE MAXIMUM BUTTON
        for(int i=0;i<temp.length;i++)
        {
            if(temp[i]>max)
                max=temp[i];
        }
    }

    //FUNCTION TO COMPARE BUTTONS
    public void compare(View view) {

        countDownTimer.cancel();

        Button theButton = (Button) view;

        //IF THE PRESSED BUTTON IS THE MAXIMUM
        if (Integer.parseInt((String) theButton.getText())==max) {

            if(counter!=5)
                counter++;
            else
            {
                if(level==10)
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("YOU WON!!");
                    builder.setMessage("Congratulations!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            new CountDownTimer(5000, 1000) {
                                public void onFinish() {

                                    // When timer is finished
                                    counter=0;
                                    level=1;
                                    try{
                                        PrintStream output = new PrintStream(
                                                openFileOutput("out.txt",MODE_PRIVATE)
                                        );
                                        output.println(level);
                                        output.close();
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    System.exit(0);
                                    // Execute your code here
                                }

                                public void onTick(long millisUntilFinished) {
                                    // millisUntilFinished    The amount of time until finished.
                                    builder.setMessage("Exits in " + millisUntilFinished/1000 + " seconds");
                                    AlertDialog dialog =builder.create();
                                    dialog.show();
                                }
                            }.start();
                        }
                    });
                    AlertDialog dialog =builder.create();
                    dialog.show();


                }
                else
                {
                    counter=0;
                    ++level;
                    try{
                        PrintStream output = new PrintStream(
                                openFileOutput("out.txt",MODE_PRIVATE)
                        );
                        output.println(level);
                        output.close();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
                if(butarr[level-1] != butarr[level-2])
                {
                    for(int i=butarr[level-2];i<butarr[level-1];i++)
                    {
                        textViewArray[i].setVisibility(View.VISIBLE);
                    }

                }
            }

            produce();
        }

        //IF WRONG BUTTON IS PRESSED
        else
        {
            counter=0;
            level=1;

            try{
                PrintStream output = new PrintStream(
                        openFileOutput("out.txt",MODE_PRIVATE)
                );
                output.println(level);
                output.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            max=-200;

            mp.start();
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    new CountDownTimer(5000, 1000) {
                        public void onFinish() {
                            // When timer is finished
                            System.exit(0);
                            // Execute your code here
                        }

                        public void onTick(long millisUntilFinished) {
                            // millisUntilFinished    The amount of time until finished.
                            builder.setTitle("YOU LOSE!!");
                            builder.setMessage("Exits in " + millisUntilFinished/1000 + " seconds");
                            AlertDialog dialog =builder.create();
                            dialog.show();
                        }
                    }.start();

            AlertDialog dialog =builder.create();
            dialog.show();
        }
    }
}