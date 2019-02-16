package com.deadlinestudio.lockey.presenter.Controller;

import android.content.Context;
import android.util.Log;

import com.deadlinestudio.lockey.presenter.Activity.BaseActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class LogfileController extends BaseActivity {

    public LogfileController(){

    }
    //텍스트내용을 경로의 텍스트 파일에 쓰기
    public void WriteLogFile(Context cont,String filename, String contents, int flag){
        try{
            //파일 output stream 생성
            FileOutputStream fos;
            if(flag==1)
                fos = cont.openFileOutput(filename, MODE_APPEND);      //이어서 저장
            else
                fos = cont.openFileOutput(filename, MODE_PRIVATE);      //새로 저장

            //파일쓰기
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
            writer.write(contents);
            writer.flush();

            writer.close();
            fos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //경로의 텍스트 파일읽기
    public String ReadLogFile(Context cont,String filename){
        StringBuffer strBuffer = new StringBuffer();
        try{
            FileInputStream fis = cont.openFileInput(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line="";
            while((line=reader.readLine())!=null){
                strBuffer.append(line);
                Log.e("Log file : ",line);
            }
            reader.close();
            fis.close();
        }catch (FileNotFoundException e){
            return "nofile";
        }catch (IOException e){
            e.printStackTrace();
            return "nofile";
        }
        return strBuffer.toString();
    }
}
