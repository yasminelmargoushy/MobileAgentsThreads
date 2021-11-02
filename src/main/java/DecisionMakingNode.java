
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


class DecisionMakingClientHandler implements Runnable
{
    Socket s;
    public DecisionMakingClientHandler(Socket s)
    {
        this.s = s;
    }

    @Override
    public void run(){
        DataOutputStream dos;
        try ( //3.create I/O streams
                DataInputStream dis = new DataInputStream(s.getInputStream())) {
            dos = new DataOutputStream(s.getOutputStream());
            //4.perform IO with client

            //a. Ask for request
            dos.writeUTF("[DECISION MAKING SERVER] Hello Please Enter Your Request");
            dos.flush();
            String Request = dis.readUTF();
            ////////////////////////////////////////////
            System.out.println(Request);
            //apply checks
            if ("[COMPUTATIONAL SERVER] Vehicle Speed Monitoring".equals(Request))
            {
                //b. if correct accnum request password
                dos.writeUTF("[DECISION MAKING SERVER] Speed Monitoring request, Please Send Speed Difference");
                dos.flush();
                int Diff = Integer.parseInt(dis.readUTF());
                ///////////////////////////////////////
                System.out.println("[COMPUTATIONAL SERVER] The Speed Difference is " + Diff);
                if (Diff < 0){
                    dos.writeUTF("[DECISION MAKING SERVER] Above Speed Limit, Slow Down!");
                    dos.flush();
                }
                else
                {
                    dos.writeUTF("[DECISION MAKING SERVER] Within Speed Limit.");
                    dos.flush();
                }
                dos.close();
                s.close();
                dis.close();
            }
            System.out.println("[DECISION MAKING SERVER] Client Finished...");
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}

public class DecisionMakingNode
{
    public static void main(String[] args)
    {
        try
        {
            //1.open server socket
            ServerSocket sv = new ServerSocket(2000);
            System.out.println("[DECISION MAKING SERVER] Running...");
            while (true)
            {
                //2.accept connection
                Socket s = sv.accept();
                System.out.println("[DECISION MAKING SERVER] Client Accepted...");
                //3. open thread for this client (s)
                DecisionMakingClientHandler ch = new DecisionMakingClientHandler(s);
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
