import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.HashMap;


public class MainClass{
	private ServerSocket server;

	ArrayList<UserClass> user_list;

	HashMap[] problems = {
		new HashMap(){{
			put("question","1+2=?");
			put("answer","3");
			put("hint1","hint1:three");
			put("hint2","hint2:third");
		}},
		new HashMap(){{
			put("question","25+15=?");
			put("answer","40");
			put("hint1","hint1:forty");
			put("hint2","hint2:40");
		}},
		new HashMap(){{
			put("question","4+6=?");
			put("answer","10");
			put("hint1","hint1:ten");
			put("hint2","hint2:x");
		}}

	};
	int problemNumber = 0;
	String question = "";
	String answer = "";
	String hint1 = "";
	String hint2 = "";
	int score = 0;
	int incorrect = 0;

	int totalCorrect = 0;



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

				UserClass user = new UserClass(nickName,socket,score);
				user.start();
				user_list.add(user);
	
				if(user_list.size()==2){
					sendToClient("server : game start!!");
					question = (String)problems[0].get("question");
					answer = (String)problems[0].get("answer");
					hint1 = (String)problems[0].get("hint1");
					hint2 = (String)problems[0].get("hint2");
					problemNumber++;
					sendToClient("problem1 : "+question);
				}
				
	
			}catch(Exception e){e.printStackTrace();}

		}

	}


	class UserClass extends Thread{
		int score;
		String nickName;
		Socket socket;
		DataInputStream dis;
		DataOutputStream dos;

		public UserClass(String nickName, Socket socket,int score){
			try{
				this.nickName = nickName;
				this.socket = socket;
				this.score = score;
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

					if(msg.equals(answer)){
						score++;
						sendToClient(nickName+" Correct! score : "+score);
			
						if(problemNumber == 3){
							Quiz test = new Quiz(nickName,socket,score);
							test.start();

							String user0 = user_list.get(0).nickName;
							int user0_s = user_list.get(0).score;
							String user1 = user_list.get(1).nickName;
							int user1_s = user_list.get(1).score;
							sendToClient("[TEST2]"+user0+" score:"+user0_s);
							sendToClient("[TEST2]"+user1+" score:"+user1_s);
						
							if(user0_s<user1_s){
								sendToClient(user1+" win!!");
							}		
							else if(user0_s==user1_s){
								sendToClient("Draw!!");
							}
							else{
								sendToClient(user0+"win!!");
							}
							problemNumber = 0;
						}

						//give new question	to users
						question = (String)problems[problemNumber].get("question");
						answer = (String)problems[problemNumber].get("answer");
						hint1 = (String)problems[problemNumber].get("hint1");
						hint2 = (String)problems[problemNumber].get("hint2");
						problemNumber++;
						sendToClient("answer | "+question);
						incorrect = 0;
					}
					else{
						sendToClient(nickName+" wrong answer!");
						incorrect++;
			
						if(incorrect == 2){
							sendToClient("try again! "+hint1);
						}
						else if(incorrect == 4){
							sendToClient("try again! "+hint2);
						}
			
						else if(incorrect == 10){
							sendToClient("No one can guess");
							//give new question	to users
							question = (String)problems[problemNumber].get("question");
							answer = (String)problems[problemNumber].get("answer");
							hint1 = (String)problems[problemNumber].get("hint1");
							hint2 = (String)problems[problemNumber].get("hint2");
							problemNumber++;
							sendToClient("answer | "+question);
							incorrect = 0;
						}
					}
				}
	
			}catch(Exception e){e.printStackTrace();}

		}


	}	


	class Quiz extends Thread{
		String nickName;
		Socket socket;
		int score;
	
		public Quiz(String nickName, Socket socket,int score){
			try{
				this.nickName = nickName;
				this.socket = socket;
				this.score = score;
			}catch(Exception e){e.printStackTrace();}
		}
		
		public void run(){
			try{
				sendToClient("[Test]"+nickName+" score : "+score);
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
