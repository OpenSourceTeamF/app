package com.example.chat_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


    }


    public void btnMethod(View v){
        if(!isConnect){
            String nickName = edit1.getText().toString();
            if(nickName.length()>0 && nickName != null){
                pro = ProgressDialog.show(this, null, "connecting...");

                ConnectionThread thread = new ConnectionThread();
                thread.start();
            }

            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                final Socket socket = new Socket("192.168.162.128",30000);
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
                                tv.setBackgroundResource(R.drawable.me);
                            }
                            else{
                                tv.setBackgroundResource(R.drawable.you);
                            }
                            tv.setText(msg);

                            container.addView(tv);

                            scroll.fullScroll(View.FOCUS_DOWN);
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
