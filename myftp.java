import java.util.*;

//import jdk.internal.jline.internal.InputStreamReader;

import java.net.*;
import java.io.*;

public class myftp {
	public static String input() throws Exception{
		System.out.println("mytftp> ");
		InputStreamReader reader = new InputStreamReader(System.in);
		BufferedReader buf = new BufferedReader(reader);
		return buf.readLine();
	}

	public static void main(String[] args) throws Exception{
		String localhost = args[0];
		String pNo = args[1];
		int portNo = Integer.parseInt(pNo);
		
		Socket cli_Soc = new Socket(localhost, portNo);
		PrintStream printStream = new PrintStream(cli_Soc.getOutputStream());
		
		//Take the commands
		while (true) {
			String command = input();
			printStream.println(command);
			printStream.println(new File(System.getProperty("user.dir")));
			printStream.flush();
			
			InputStreamReader reader = new InputStreamReader(cli_Soc.getInputStream());
			BufferedReader buffer = new BufferedReader(reader);
			String inStr = "";
			inStr = buffer.readLine();
			
			System.out.println(inStr);
			
		}
	}
}
