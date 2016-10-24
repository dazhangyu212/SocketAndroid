package spl.andr.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
/**
 * android客户端
 * @author octopus
 *
 */
public class AndroidRunnable implements Runnable {

	Socket socket = null;
	public AndroidRunnable(Socket socket) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
	}
	@Override
	public void run() {
		//向客户端输出hello world
		String line = "";
		InputStream is;
		OutputStream os;
		String str = "hello world";
		try {
			os = socket.getOutputStream();
			is = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			os.write(str.getBytes("utf-8"));
			os.flush();
			//半关闭socket
			socket.shutdownOutput();
			//获取客户端的信息
			while ((line = br.readLine())!= null) {
				System.out.println(line);
				
			}
			//关闭输入输出流
			os.close();
			br.close();
			is.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
