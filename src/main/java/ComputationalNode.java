
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Hp
 */
class ComputationalClientHandler implements Runnable
{
    Socket s;
    public ComputationalClientHandler(Socket s)
    {
        this.s = s;
    }
    private static final int[] StreetSpeedLimit = {60,80,100,50,60,80,120,60,80,70,100,90,60,100,80,90,70,120,100,50};
    @Override
    public void run(){
        DataOutputStream dos;
        try ( //3.create I/O streams
                DataInputStream dis = new DataInputStream(s.getInputStream())) {
            dos = new DataOutputStream(s.getOutputStream());
            //4.perform IO with client
            //a. Ask for request
            dos.writeUTF("[COMPUTATIONAL SERVER] Hello Please Enter Your Request");
            dos.flush();
            String Request = dis.readUTF();
            //////////////////////////////////////
            System.out.println(Request);
            //apply checks
            if ("[SENSING CLIENT] Vehicle Speed Monitoring".equals(Request))
            {
                //b. if correct accnum request password
                dos.writeUTF("[COMPUTATIONAL SERVER] Speed Monitoring request, Please Send Vehicle Speed");
                dos.flush();
                int Speed = Integer.parseInt(dis.readUTF());
                //////////////////////////////////////
                System.out.println("[SENSING CLIENT] The Speed is " + Speed);
                if (Speed > 0){
                    dos.writeUTF("[COMPUTATIONAL SERVER] Please Send Street Number");
                    dos.flush();
                    int StNum = Integer.parseInt(dis.readUTF());
                    dos.writeUTF("bye");
                    dos.flush();
                    dos.close();
                    dis.close();
                    s.close();
                    //////////////////////////////////////
                    System.out.println("[SENSING CLIENT] The Street Number is " + StNum);
                    System.out.println("[COMPUTATIONAL SERVER] It's Speed Limit is " + StreetSpeedLimit[StNum]);
                    if(StNum < 20){
                        int Diff = StreetSpeedLimit[StNum]-Speed;
                        //2. Create I/O streams
                        try (Socket s2 = new Socket("127.0.0.1", 2000)) {
                            //2. Create I/O streams
                            DataInputStream dis2 = new DataInputStream(s2.getInputStream());
                            DataOutputStream dos2 = new DataOutputStream(s2.getOutputStream());

                            //3.perform IO with server
                            //a. receive server command & print to user
                            String DSsrvr_msg = dis2.readUTF();
                            //////////////////////////////////////
                            System.out.println(DSsrvr_msg);
                            if (DSsrvr_msg.equals("[DECISION MAKING SERVER] Hello Please Enter Your Request")){
                                dos2.writeUTF("[COMPUTATIONAL SERVER] Vehicle Speed Monitoring");
                                dos2.flush();
                                String DSDataRQ = dis2.readUTF();
                                //////////////////////////////////////
                                System.out.println(DSDataRQ);
                                if(DSDataRQ.equals("[DECISION MAKING SERVER] Speed Monitoring request, Please Send Speed Difference")){
                                    dos2.writeUTF(String.valueOf(Diff));
                                    dos2.flush();
                                    String DSDecision = dis2.readUTF();
                                    System.out.println(DSDecision);
                                }
                            }
                            //4.close connections
                            dis2.close();
                            dos2.close();
                            s2.close();
                        }
                    }
                }                                
            }
            System.out.println("[COMPUTATIONAL SERVER] Client Finished...");
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}

public class ComputationalNode
{
    public static void main(String[] args)
    {
        try
        {
            //1.open server socket
            ServerSocket sv = new ServerSocket(3000);
            System.out.println("[COMPUTATIONAL SERVER] Server Running...");
            while (true)
            {
                //2.accept connection
                Socket s = sv.accept();
                System.out.println("[COMPUTATIONAL SERVER] Client Accepted...");
                //3. open thread for this client (s)
                ComputationalClientHandler ch = new ComputationalClientHandler(s);
                Thread t = new Thread(ch);
                t.start();
            }
        } 
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
