package test1;
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
import java.io.FileInputStream;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MainClass{
	
	static String[][] table = new String [6][101];
	private ServerSocket server;

	ArrayList<UserClass> user_list;

	String question = "";
	String answer = "";
	String hint1 = "";
	String hint2 = "";
	String hint3 = "";

	int score = 0;
	int incorrect = 0;
	String  winner;
	int w_score;
	int totalCorrect = 0;
	int problemNumber = 1;


	public static void main(String[] args)
	{
		
		XSSFRow row;
		XSSFCell cell;
			
		try {
			FileInputStream inputStream = new FileInputStream("C:\\Users\\DKU\\Desktop\\jinho\\Invention Open Source.xlsx");
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			int sheetCn = workbook.getNumberOfSheets();
				for(int cn = 0; cn < sheetCn; cn++){
				XSSFSheet sheet = workbook.getSheetAt(cn);
				
				int rows = sheet.getPhysicalNumberOfRows();
				
				
				int cells = sheet.getRow(cn).getPhysicalNumberOfCells(); //
				for (int r = 1; r < rows; r++) {
					row = sheet.getRow(r); 
					if (row != null) {
						for (int c = 0; c < cells; c++) {
							cell = row.getCell(c);
							if (cell != null) {
								String value = null;
								
								
								
								switch (cell.getCellType()) {
								case XSSFCell.CELL_TYPE_FORMULA:
									value = "" + cell.getCellFormula();
									break;
								case XSSFCell.CELL_TYPE_NUMERIC:
									value = "" + cell.getNumericCellValue();
									break;
								case XSSFCell.CELL_TYPE_STRING:
									value = "" + cell.getStringCellValue();
									break;
								case XSSFCell.CELL_TYPE_BLANK:
									value = "" +"[blank]";
									break;
								case XSSFCell.CELL_TYPE_ERROR:
									value = "" + cell.getErrorCellValue();
									break;
								default:
								}

								table[c][r] = value;

							} 
							else {

							}
						} 

					}
				} 
			}
			} catch (Exception e) {
			e.printStackTrace();
		}
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
				dos.writeUTF("[server] Welcome "+nickName+"!");
				sendToClient("[server] "+nickName+" is connected");
				UserClass user = new UserClass(nickName,socket,score);
				user.start();
				user_list.add(user);
				if(user_list.size()==2){
					sendToClient("[server] game start!!");
					question = table[2][1];
					answer = table[1][1];
					hint1 = table[3][1];
					hint2 = table[4][1];
					hint3 = table[5][1];
					problemNumber++;
					sendToClient("Question | "+question);
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
					sendToClient("["+nickName+"] "+msg);

					if(msg.equals(answer)){
						score++;
						sendToClient("[server] "+nickName+" Correct! score : "+score);
			
						//define first place
						w_score = 0;
						for(int i=0;i<user_list.size();i++){
							if(user_list.get(i).score > w_score){
								winner = user_list.get(i).nickName;
								w_score = user_list.get(i).score;
							}
						}
						
						sendToClient("First Place : "+winner+"(score : "+w_score+")");
						problemNumber++;
						if(problemNumber==100)
						{
							problemNumber = 1;
						}
						question = table[2][problemNumber];
						answer = table[1][problemNumber];
						hint1 = table[3][problemNumber];
						hint2 = table[4][problemNumber];
						hint3 = table[5][problemNumber];
						sendToClient("Question | "+question);
						incorrect = 0;
						}
					else{
						sendToClient("[server] "+nickName+" Wrong answer!");
						incorrect++;
			
						if(incorrect == 2){
							sendToClient("Hint | "+hint1);
						}
						else if(incorrect == 4){
							sendToClient("Hint | "+hint2);
						}
			
						else if(incorrect == 10){
							sendToClient("[server] No one can guess");
							problemNumber++;
							if(problemNumber==100)
							{
								problemNumber = 1;
							}
							question = table[2][problemNumber];
							answer = table[1][problemNumber];
							hint1 = table[3][problemNumber];
							hint2 = table[4][problemNumber];
							hint3 = table[5][problemNumber];
							sendToClient("Question | "+question);
							incorrect = 0;
						}
					}
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
