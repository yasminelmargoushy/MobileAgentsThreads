
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Hp
 */
public class SensingNode {
    static int Speed;
    static int StNum;
    public static void main(String[] args){
        Scanner sc= new Scanner(System.in);    //System.in is a standard input stream  
        System.out.println("Enter Speed: ");  
        Speed = sc.nextInt();
        System.out.println("Enter Street Number: ");  
        StNum = sc.nextInt();
        try (Socket s = new Socket("127.0.0.1", 3000)) {
            //2. Create I/O streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            //a. receive server command & print to user
            String DSsrvr_msg = dis.readUTF();
            if (DSsrvr_msg.equals("[COMPUTATIONAL SERVER] Hello Please Enter Your Request")){
                dos.writeUTF("[SENSING CLIENT] Vehicle Speed Monitoring");
                dos.flush();
                String CSDataRQ1 = dis.readUTF();
                if(CSDataRQ1.equals("[COMPUTATIONAL SERVER] Speed Monitoring request, Please Send Vehicle Speed")){
                    dos.writeUTF(String.valueOf(Speed));
                    dos.flush();
                    String CSDataRQ2 = dis.readUTF();
                    if(CSDataRQ2.equals("[COMPUTATIONAL SERVER] Please Send Street Number")){
                        dos.writeUTF(String.valueOf(StNum));
                        dos.flush();
                    }
                }
            }
            //4.close connections
            dis.close();
            dos.close();
            s.close();
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
