import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class myftpServer {
	
	//-----------FTP Commands Methods-----------//
	
	public static String[] splCommand(String command) {
		return command.split(" ");
	}
	
	public static String mkdir(String folderName) {
		try {
			File file = new File(System.getProperty("user.dir"));
			File dir = new File(file.getAbsolutePath() + "/" + folderName);
			dir.mkdir();
			return "Folder created!";
			
		} catch (Exception e) {
			// TODO: handle exception
			return e.toString();
		}
	}
	
	public static String setCurPath(String folderName) {
		try {
			File dir = new File(folderName);
			System.setProperty("user.dir", dir.getAbsolutePath());
			File file = new File(System.getProperty("user.dir"));
			return file.getAbsolutePath();
		} catch (Exception e) {
			System.out.println(e.toString());
			// TODO: handle exception
			return "Error";
		}
	}
	
	public static String setPrePath() {
		try {
			File file = new File(System.getProperty("user.dir"));
			String path = file.getAbsoluteFile().getParentFile().getAbsolutePath();
			System.setProperty("user.dir", path);
			file = new File(System.getProperty("user.dir"));
			return file.getAbsolutePath();
		} catch (Exception e) {
			// TODO: handle exception
			return e.toString();
		}
	}
	
	public static String delete(String fName) {
		
		File file = new File(fName);
		
		if (file.delete()) {
			return "File deleted..!!";
		}
		else {
			return "Unsuccesesfull!!";
		}
	}
	
	public static File[] ls(File directory) {
		File[] files = directory.listFiles();
		return files;
	}
	
	public static File[] ls() {
		File dirr = new File(System.getProperty("user.dir"));
		File[] files = dirr.listFiles();
		return files;
	}
	
	public static String pwd(File file) {
		file = new File(System.getProperty("user.dir"));
		return file.getAbsolutePath();
	}
	
	public static String get(String filename, String clientpath) {
		Path f1 = FileSystems.getDefault().getPath(System.getProperty("user.dir") + "/" + filename);
		Path f2 = FileSystems.getDefault().getPath(clientpath + "/" + filename);
		
		try {
			Files.move(f1, f2);
			return("File moved successfully!");
		} catch (Exception e) {
			// TODO: handle exception
			return "Error!";
		}
	}
	
	public static String put(String filename, String clientpath) {
		Path f1 = FileSystems.getDefault().getPath(clientpath + "/" + filename);
		Path f2 = FileSystems.getDefault().getPath(System.getProperty("user.dir") + "/" + filename);
		
		try {
			Files.move(f1, f2);
			return("File moved successfully!");
		} catch (IOException e) {
			// TODO: handle exception
			return "Error!";
		}
	}
	
	//----------------FTP Server Commands call---------------//
	
	public static void main(String[] args) {
		
		// Take the port number and parse it in to integer.
		String pNo = args[0];
		int portNo = Integer.parseInt(pNo);
		
		System.out.println("Server is running..");
		
		try {
			
			// Initialize the Session
			ServerSocket Soc = new ServerSocket(portNo);
			Socket socket = Soc.accept();
			
			
			while(true) {
				InputStreamReader reader = new InputStreamReader(socket.getInputStream());
				BufferedReader buffer = new BufferedReader(reader);
				String inString = buffer.readLine();
				String clientPath = buffer.readLine();
				
				PrintStream printStream = new PrintStream(socket.getOutputStream());
				
				if (splCommand(inString)[0].equals("mkdir")) {
					// make directory
					printStream.println(mkdir(splCommand(inString)[1]));
				}
				
				if (splCommand(inString)[0].equals("cd")) {
					// back path
					if (!splCommand(inString)[1].equals("..")) {
						printStream.println(setCurPath(splCommand(inString)[1]));
					}
					else {
						printStream.println(setPrePath());
					}
				}
				
				if (splCommand(inString)[0].equals("delete")) {
					// Delete the file
					printStream.println(delete(splCommand(inString)[1]));
				}
				
				if (splCommand(inString)[0].equals("ls")) {
					File[] files;
					String path = "";
					
					if (splCommand(inString).length == 1) {
						files = ls();
						for (File f : files) {
							System.out.println(f.getName());
							path = path + " " + f.getName() + '\t';
						}
					}
					else {
						files = ls(new File(splCommand(inString)[1]));
						for (File file : files) {
							path = path + " " + file.getName() + '\t';
 						}
					}
					printStream.println(path);
					printStream.flush();
				}
				
				if (splCommand(inString)[0].equals("pwd")) {
					//System.out.println("directory path!");
					printStream.println(pwd(new File("")));
				}
				
				if (splCommand(inString)[0].equals("get")) {
					//System.out.println("File get!");
					printStream.println(get(splCommand(inString)[1], clientPath));
				}
				
				if (splCommand(inString)[0].equals("put")) {
					//System.out.println("File put!");
					printStream.println(put(splCommand(inString)[1], clientPath));
				}
				
				if (inString.equals("quit")) {
					
					//System.out.println("Connection terminated!");
					printStream.println("Connection terminated!!!");
					socket.close();
					break;
				}
				
				printStream.flush();
			}
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
	}

}
