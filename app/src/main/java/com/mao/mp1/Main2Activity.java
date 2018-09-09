/**
 * Sources:
 * Random image display inspired by https://stackoverflow.com/questions/20549705/how-to-display-random-images-on-image-view
 * exit: https://stackoverflow.com/questions/2257963/how-to-show-a-dialog-to-confirm-that-the-user-wishes-to-exit-an-android-activity
 * contact: https://stackoverflow.com/questions/44626758/how-to-save-a-contact-in-phone-via-intent-or-something-using-android-studio
 */
package com.mao.mp1;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    final Random rand = new Random();
    final String[] array = {"Aayush Tyagi", "Abhinav Koppu", "Aditya Yadav", "Ajay Merchia", "Alice Zhao", "Amy Shen", "Anand Chandra", "Andres Medrano", "Angela Dong", "Anika Bagga", "Anmol Parande", "Austin Davis", "Ayush Kumar", "Brandon David", "Candice Ye", "Carol Wang", "Cody Hsieh", "Daniel Andrews", "Daniel Jing", "Eric Kong", "Ethan Wong", "Fang Shuo", "Izzie Lau", "Jaiveer Singh", "Japjot Singh", "Jeffery Zhang", "Joey Hejna", "Julie Deng", "Justin Kim", "Kaden Dippe", "Kanyes Thaker", "Kayli Jiang", "Kiana Go", "Leon Kwak", "Levi Walsh", "Louie Mcconnell", "Max Miranda", "Michelle Mao", "Mohit Katyal", "Mudabbir Khan", "Natasha Wong", "Nikhar Arora", "Noah Pepper", "Radhika Dhomse", "Sai Yandapalli", "Saman Virai", "Sarah Tang", "Sharie Wang", "Shiv Kushwah", "Shomil Jain", "Shreya Reddy", "Shubha Jagannatha", "Shubham Gupta", "Srujay Korlakunta", "Stephen Jayakar", "Suyash Gupta", "Tiger Chen", "Vaibhav Gattani", "Victor Sun", "Vidya Ravikumar", "Vineeth Yeevani", "Wilbur Shi", "William Lu", "Will Oakley", "Xin Yi Chen", "Young Lin"};
    final ArrayList<String> memberNames = new ArrayList<String>(Arrays.asList(array));
    final Class drawableClass = R.drawable.class;
    final Field[] fields = drawableClass.getFields(); //member images

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ImageView img = findViewById(R.id.randomPic);

        //set up exit ImageButton to quit game
        ImageButton exit = findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Main2Activity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Exiting Game")
                        .setMessage("Are you sure you want to end the game?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        repeated();
    }


    //Renames buttons and returns correct choice
    public int setButtons(String name, ArrayList<Button> choice_list) {
        int corr_choice = rand.nextInt(4); //rand int between 0 to 3
        //set random button's text to correct name, set others to random names
        choice_list.get(corr_choice).setText(name);
        for (Button b: choice_list) {
            if (b.getText() == name) {continue;}
            int rndInt = rand.nextInt(memberNames.size() - 1);
            if (memberNames.get(rndInt) == name) {rndInt = rand.nextInt(memberNames.size() - 1);}
            b.setText(memberNames.get(rndInt));
        }
        return corr_choice;
    }

    public void repeated() {
        //timer
        final CountDownTimer timer = new CountDownTimer(6000, 1000) {
            TextView timerText = findViewById(R.id.timerText);
            public void onTick(long millisUntilFinished) {
                timerText.setText("YOU STILL HAVE: " + millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                timerText.setText("TIMES UP");
                Toast.makeText(Main2Activity.this, "Wrong", Toast.LENGTH_SHORT).show();
                repeated();
            }
        }.start();

        ImageView img = findViewById(R.id.randomPic);

        //get random image and set in ImageView
        int rndInt = rand.nextInt(memberNames.size() - 1);
        String name = memberNames.get(rndInt);
        String nameimg = name.replaceAll("\\s", "").toLowerCase(); //remove spaces & lowercase
        int resID = getResources().getIdentifier(nameimg,
                "drawable", getPackageName());
        img.setImageResource(resID);

        //create list of buttons/choices (0th->choice1)
        ArrayList<Button> choice_list = new ArrayList<>();
        choice_list.add((Button) findViewById(R.id.choice1));
        choice_list.add((Button) findViewById(R.id.choice2));
        choice_list.add((Button) findViewById(R.id.choice3));
        choice_list.add((Button) findViewById(R.id.choice4));
        int correct_choice = setButtons(name, choice_list); //1->choice1

        //configure buttons based on correct_choice
        for (Button b : choice_list) {
            if (b.getText() == name) {
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timer.cancel();
                        Toast.makeText(Main2Activity.this, "CORRECT!", Toast.LENGTH_SHORT).show();
                        increment_score();
                        repeated();
                    }
                });
            } else {
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timer.cancel();
                        Toast.makeText(Main2Activity.this, "Wrong", Toast.LENGTH_SHORT).show();
                        repeated();
                    }
                });
            }

        }
        //configure contact so creates contact on tap
        final Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                startActivity(intent);
                repeated();
            }
        });
    }

    public void increment_score() {
        TextView score_num = findViewById(R.id.score_num);
        int score_int = Integer.parseInt(score_num.getText().toString());
        score_int++;
        score_num.setText(Integer.toString(score_int));
    }
}