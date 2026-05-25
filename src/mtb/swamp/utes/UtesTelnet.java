package mtb.swamp.utes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class UtesTelnet {
	 public static void createClient() throws Exception
	    {
	        Socket soc=new Socket("127.0.0.1",23);
/*	        String LoginName;
	        String Password;
	        String Command;
	        
	        
	        DataInputStream din=new DataInputStream(soc.getInputStream());        
	        DataOutputStream dout=new DataOutputStream(soc.getOutputStream());
	        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

	        System.out.println("Welcome to Telnet Client");
	        System.out.println("Your Credential Please...");
	        System.out.print("Login Name :");

	        LoginName=br.readLine();
	        
	        System.out.print("Password :");
	        Password=br.readLine();
	        
	        dout.writeUTF(LoginName);
	        dout.writeUTF(Password);

	        if (din.readUTF().equals("ALLOWED"))
	        {
	            do
	            {
	            System.out.print("< Telnet Prompt >");
	            Command=br.readLine();            
	            dout.writeUTF(Command);
	            if(!Command.equals("quit"))
	            {
	                System.out.println(din.readUTF());        
	            }                
	            }while(!Command.equals("quit"));
	        }*/
	        soc.close();        
	    }
	 
	 
	 

	 static ServerSocket servSock=null;
	 static ArrayList<AcceptTelnetClient> clientArray=new ArrayList<AcceptTelnetClient>();
	 

    
	 
	 public static void createServer(String hellomsg) throws Exception
     {
		 createServer(hellomsg, 23);
     }
     
     public static void createServer(String hellomsg, int port) throws Exception
     {
         Callback def = new Callback() {
        	 public void doJob(String helloMsg, Socket cliSock, BufferedReader din, BufferedWriter dout) {
        		 try {
		        	 //BufferedReader din=new BufferedReader(new InputStreamReader(cliSock.getInputStream(),"US-ASCII"));
		        	 //BufferedWriter dout=new BufferedWriter(new OutputStreamWriter(cliSock.getOutputStream()));
		
		        	 //InputStreamReader din=new InputStreamReader(cliSock.getInputStream(),"US-ASCII");
		        	 //OutputStreamWriter dout=new OutputStreamWriter(cliSock.getOutputStream());
		
	
		    	     char[] buffer= new char[256];
		        	 din.read(buffer, 0, 255);
		        	 String command="";
			         dout.write(helloMsg+cliSock.getLocalAddress()+"\n\r");
			         dout.flush();
			         boolean allow=true;
			         while(allow) {
				         command=din.readLine();
				         System.out.println(command);
			             if(command.equals("quit")) {
			            	 allow=false;
			             }
			             else {
			                 dout.write("command : "+command+"\n\r");   
			                 dout.flush();
			             }                        
			         }
			         cliSock.close();
		         } 
        		 catch(Exception ex) {
		             ex.printStackTrace();
		         }

        	 }
         };
    	 
    	 
         createServer(hellomsg, port, def);

     }
     
     public static void createServer(String hellomsg, int port, Callback func) throws Exception
     {
         
         try {
        	 servSock=new ServerSocket(port);
          } catch(java.net.BindException be) {
             // debug/warning here
        	  throw new java.io.IOException("Failed to create the server socket");
          }
       
          WaitConnectionThread wc=new WaitConnectionThread(hellomsg, func);

     }
     
     public static void closeServer() throws Exception
     {
		 servSock.close();
     }
     
     
     static class WaitConnectionThread extends Thread
     {
    	 String helloMsg;
    	 Callback callbackFunc;
    	 WaitConnectionThread(String hellomsg, Callback func) throws Exception
 	     {
    		 helloMsg=hellomsg;
    		 callbackFunc=func;
 	         start();        
 	     }
    	 public void run()
    	 {
             while(true)
             {
                 Socket CSoc=null;
				try {
					CSoc = servSock.accept();
				} catch (IOException e) {
					// socket closed so we go out of here
					return;
				}
                 try {
                	 clientArray.add(new AcceptTelnetClient(helloMsg, CSoc, callbackFunc));
					//AcceptTelnetClient ob=new AcceptTelnetClient(CSoc);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
             }
    	 }
     }

     public static void closeClient(Socket clisock) {
		 for (int i=0;i<clientArray.size();i++){
			 if(clientArray.get(i).cliSock==clisock){
				 try {
					clisock.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					//exception si déjà fermé. on s'en fout
				}
				 clientArray.remove(i);
				 break;
			 }
			 
		 }

    	 
     }
	 
	 
	 public static void broadcastMsg(String msg){
		 for (int i=0;i<clientArray.size();i++){
			 try {
					clientArray.get(i).dout.write(msg);
					clientArray.get(i).dout.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				UtesTelnet.closeClient(clientArray.get(i).cliSock);
				//e.printStackTrace();
			}
			 
		 }
	 }
	 
	 public interface Callback{
		 void doJob(String helloMsg, Socket soc, BufferedReader din, BufferedWriter dout);
	}
}
	 



class AcceptTelnetClient extends Thread
{
    Socket cliSock;
    String helloMsg;
    UtesTelnet.Callback callbackFunc;
    BufferedReader din;
    BufferedWriter dout;


    AcceptTelnetClient(String msg, Socket CSoc, UtesTelnet.Callback func) throws Exception {
        cliSock=CSoc;
        helloMsg=msg;
        callbackFunc = func;
        System.out.println("Client Connected ..."+cliSock.getInetAddress()+":"+cliSock.getPort());
        start();        
    }
    public void run() {
   	try {
			din=new BufferedReader(new InputStreamReader(cliSock.getInputStream(),"US-ASCII"));
			dout=new BufferedWriter(new OutputStreamWriter(cliSock.getOutputStream()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

   	callbackFunc.doJob(helloMsg, cliSock, din, dout);
    } 
}
