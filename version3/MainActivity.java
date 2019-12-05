package com.example.quiz_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    boolean isConnect = false;
    EditText edit1;
    Button btn1;
    LinearLayout container;
    ScrollView scroll;
    ProgressDialog pro;
    TextView toptext;
    TextView toptext2;
    TextView toptext3;
    TextView toptext4;
    TextView firstplace;
    ImageView image_guess;

    boolean isRunning=false;

    Socket member_socket;

    String user_nickname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edit1 = findViewById(R.id.editText);
        btn1 = findViewById(R.id.button);
        container = findViewById(R.id.container);
        scroll = findViewById(R.id.scroll);
        toptext = findViewById(R.id.TopText);
        toptext2 = findViewById(R.id.TopText2);
        toptext3 = findViewById(R.id.TopText3);
        toptext4 = findViewById(R.id.TopText4);
        firstplace = findViewById(R.id.FirstPlace);
        image_guess = findViewById(R.id.Guess);
    }


    public void btnMethod(View v){
        if(!isConnect){
            String nickName = edit1.getText().toString();
            if(nickName.length()>0 && nickName != null){
                image_guess.setVisibility(View.INVISIBLE);
                pro = ProgressDialog.show(this, null, "connecting...");
                ConnectionThread thread = new ConnectionThread();
                thread.start();
            }

            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                image_guess.setVisibility(View.VISIBLE);
                builder.setMessage("Input your NickName");
                builder.setPositiveButton("Check",null);
                builder.show();
            }
        }
        else{
            String msg = edit1.getText().toString();

            SendToServerThread thread = new SendToServerThread(member_socket,msg);
            thread.start();
        }
    }


    class ConnectionThread extends Thread{
        @Override
        public void run(){
            try{
                //192.168.162.128
                final Socket socket = new Socket("172.20.40.59",30000);
                member_socket=socket;

                String nickName = edit1.getText().toString();
                user_nickname=nickName;

                OutputStream os = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);

                dos.writeUTF(nickName);

                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        pro.dismiss();
                        edit1.setText("");
                        edit1.setHint("Input Message");
                        btn1.setText("전송");

                        isConnect = true;
                        isRunning = true;

                        MessageThread thread = new MessageThread(socket);

                        thread.start();
                    }
                });
            }catch(Exception e){e.printStackTrace();}
        }
    }

    class MessageThread extends Thread{
        Socket socket;
        DataInputStream dis;

        public MessageThread(Socket socket){
            try{
                this.socket = socket;
                InputStream is = socket.getInputStream();
                dis = new DataInputStream(is);
            }catch(Exception e){e.printStackTrace();}
        }

        @Override
        public void run(){
            try{
                while(isRunning){
                    final String msg = dis.readUTF();
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run(){
                            TextView tv = new TextView(MainActivity.this);
                            tv.setTextColor(Color.BLACK);
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);

                            if(msg.startsWith(user_nickname)){
                                if(msg.contains("Hint1")){
                                    toptext.setText(msg);
                                }
                                if(msg.contains("Hint2")){
                                    toptext2.setText(msg);
                                }
                                if(msg.contains("Hint3")){
                                    toptext3.setText(msg);
                                }
                                if(msg.contains("Hint4")){
                                    toptext4.setText(msg);
                                }
                                if(msg.contains("First")){
                                    firstplace.setText(msg);
                                }

                                if(msg.contains("Correct!")){
                                    toptext.setText("");
                                    toptext2.setText("");
                                    toptext3.setText("");
                                    toptext4.setText("");
                                }
                                if(msg.contains("server")){
                                    tv.setBackgroundResource(R.drawable.me);
                                }
                                else{
                                    tv.setBackgroundResource(R.drawable.you);
                                }
                            }
                            else{
                                if(msg.contains("Hint1")){
                                    toptext.setText(msg);
                                }
                                if(msg.contains("Hint2")){
                                    toptext2.setText(msg);
                                }
                                if(msg.contains("Hint3")){
                                    toptext3.setText(msg);
                                }
                                if(msg.contains("Hint4")){
                                    toptext4.setText(msg);
                                }
                                if(msg.contains("First")){
                                    firstplace.setText(msg);
                                }

                                if(msg.contains("Correct!")){
                                    toptext.setText("");
                                    toptext2.setText("");
                                    toptext3.setText("");
                                    toptext4.setText("");
                                }
                                if(msg.contains("server")){
                                    tv.setBackgroundResource(R.drawable.me);
                                }
                                else{
                                    tv.setBackgroundResource(R.drawable.you);
                                }
                            }

                            if(msg.contains("Hint") || msg.contains("First")){
                                //not print msg
                            }
                            else {
                                tv.setText(msg);

                                container.addView(tv);

                                scroll.fullScroll(View.FOCUS_DOWN);
                            }
                        }
                    });
                }
            }catch (Exception e){e.printStackTrace();}
        }
    }

    class SendToServerThread extends Thread{
        Socket socket;
        String msg;
        DataOutputStream dos;

        public SendToServerThread(Socket socket, String msg){
            try{
                this.socket=socket;
                this.msg=msg;
                OutputStream os = socket.getOutputStream();
                dos = new DataOutputStream(os);
            }catch(Exception e){e.printStackTrace();}
        }

        @Override
        public void run(){
            try{
                dos.writeUTF(msg);
                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        edit1.setText("");
                    }
                });
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        try{
            member_socket.close();
            isRunning=false;

        }catch (Exception e){e.printStackTrace();}
    }

}
