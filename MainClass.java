import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import java.net.InetAddress;
import java.net.UnknownHostException;



public class MainClass{
	private ServerSocket server;

	ArrayList<UserClass> user_list;
	public static void main(String[] args)
	{
		new MainClass();
	}		


	public MainClass(){
		try{
			user_list=new ArrayList<UserClass>();
			server=new ServerSocket(30000);

			ConnectionThread thread= new ConnectionThread();
			thread.start();
		}catch(Exception e){e.printStackTrace();}
	}



	class ConnectionThread extends Thread{

		@Override
		public void run(){
			try{
				while(true){
					System.out.println("waiting connection of users");
					InetAddress local = InetAddress.getLocalHost();
					System.out.println(local.getHostAddress());
					Socket socket=server.accept();
					System.out.println("user connected");
					NickNameThread thread = new NickNameThread(socket);

					thread.start();
				}
			}catch(Exception e){e.printStackTrace();}
		}
	}


	class NickNameThread extends Thread{
		private Socket socket;

		public NickNameThread(Socket socket){
			this.socket=socket;	
		}		
		
		public void run(){
			try{
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				DataInputStream dis = new DataInputStream(is);
				DataOutputStream dos = new DataOutputStream(os);

				String nickName=dis.readUTF();

				dos.writeUTF("server : Welcome "+nickName+"!");
				sendToClient("server : "+nickName+" is connected");

				UserClass user = new UserClass(nickName,socket);
				user.start();
				user_list.add(user);
	
			}catch(Exception e){e.printStackTrace();}

		}

	}


	class UserClass extends Thread{
		String nickName;
		Socket socket;
		DataInputStream dis;
		DataOutputStream dos;

		public UserClass(String nickName, Socket socket){
			try{
				this.nickName = nickName;
				this.socket = socket;
				InputStream is=socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				dis = new DataInputStream(is);
				dos = new DataOutputStream(os);

			}catch(Exception e){ e.printStackTrace();}
		
		}

		public void run(){
			try{
				while(true){
					String msg = dis.readUTF();
					sendToClient(nickName+" : "+msg);
				
				}
	
			}catch(Exception e){e.printStackTrace();}

		}


	}	


	public synchronized void sendToClient(String msg){
		try{
			for(UserClass user : user_list){
				user.dos.writeUTF(msg);
			}

		}catch(Exception e){e.printStackTrace();}

	}


}
