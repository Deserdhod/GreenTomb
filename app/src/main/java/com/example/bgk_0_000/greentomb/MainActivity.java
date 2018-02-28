package com.example.bgk_0_000.greentomb;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new DrawView(this));

        public void saveToFile(String fileName, String str){
            try{
                File file = new File(fileName);
                PrintStream printStream = new PrintStream(file);
                printStream.printf(str);
                printStream.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }

