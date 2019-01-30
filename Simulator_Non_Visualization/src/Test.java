import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Test {

	public static void main(String... args) {
		
		try {
			
			Socket s = new Socket("localhost", 2228);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			new Thread(new Runnable() {
				boolean runn = true;
				@SuppressWarnings("null")
				@Override
				public void run() {
					while(runn) {
						try {
							String ss = br.readLine();
							if(ss != null || !ss.isEmpty()) {
								System.out.println(ss);
							}
						}catch(Exception e) {
							this.runn = false;
						}
					}
				}
			}).run();
			
			
			Scanner scan = new Scanner(System.in);
			PrintWriter pw = new PrintWriter(s.getOutputStream());
			while(true) {
				
				if(scan.nextLine().contains("exit")) {
					scan.close();
					s.close();
					System.exit(0);
				}
				
				String ss = scan.nextLine();
				
				pw.println(ss);
				pw.flush();
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
