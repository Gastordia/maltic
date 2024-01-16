import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;



public class Master {

    public static void main(String[] args) {
        try{
            Scanner scanner = new Scanner(System.in);
            ServerSocket ss = new ServerSocket(8080);    
            System.out.println("waiting for the connection desuuuuu");
            Socket s = ss.accept();
            System.out.println("connection established with the slave kun >///<");
            PrintWriter pw = new PrintWriter(s.getOutputStream(),true);
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

            while(true){
                System.out.print("enter a command pwease: ");
                String command= scanner.nextLine();
                String[] check = command.split(" ");

                if (check[0].equals("cd")){
                    pw.println(check[0]);
                    String path = check[1];
                    pw.println(path);
                    System.out.println("path changed successfully to the rep (˶ᵔ ᵕ ᵔ˶): "+path);

                }
                else if(check[0].equals("rm")){
                    pw.println(check[0]);
                    String removable=check[1];
                    pw.println(removable);
                    System.out.println("item deleted successfully (ᵔᗜᵔ)!");
                }
                else{
                    switch (command) {

                        case "ls":
                            pw.println(command);
                            String result = br.readLine();
                            result = result.replace(";", System.lineSeparator());
                            System.out.println(result);

                            break;
                        case "pwd":
                            pw.println(command);
                            String result1 = br.readLine();
                            System.out.println(result1);

                            break;
                        case "scrot":
                            pw.println(command);
                            Thread scoThread = new Thread(()->scrot());
                            scoThread.start();
                            
                            break;
                        case "bye":
                            pw.println(command);
                            String result2 = br.readLine();
                            System.out.println(result2);
                            System.exit(0);
                            break; 
                        case "":
                            break;
                    
                        default:
                            break;
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    } 
    public static void scrot(){
        try {
            ServerSocket sss= new ServerSocket(8081);
            Socket ts=sss.accept();
            
            InputStream inputstrem = ts.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputstrem.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            byte[] imageData = buffer.toByteArray();

            FileOutputStream outputStream = new FileOutputStream("received_image.png");
            outputStream.write(imageData);
            sss.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
