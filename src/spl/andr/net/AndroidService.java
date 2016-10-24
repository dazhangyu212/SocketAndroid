package spl.andr.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * Android 客户端
 * @author octopus
 *
 */
public class AndroidService {
	public static void main(String[] args) throws IOException {
		ServerSocket service = new ServerSocket(30000);
		while (true) {
			//等待客户端连接
			Socket socket = service.accept();
			new Thread(new AndroidRunnable(socket)).start();
			
		}
	}
}
