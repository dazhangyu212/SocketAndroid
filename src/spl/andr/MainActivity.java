package spl.andr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	Socket socket = null;
	String buffer = "";
	TextView response_text ;
	Button send;
	EditText input;
	String getInput;
    /** Called when the activity is first created. */
	Handler myHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				Bundle bundle = msg.getData();
				response_text.append("server:"+bundle.getString("msg")+"\n");
			}
		};
	};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// 启动线程 向服务器发送和接受信息
				getInput = input.getText().toString().trim();
				response_text.append("client:"+getInput+"\n");
				new MyThread(getInput).start();
				
			}
		});
    }
    private void initView() {
		// TODO Auto-generated method stub
    	response_text = (TextView) findViewById(R.id.response_text);
    	send = (Button) findViewById(R.id.send);
    	input = (EditText) findViewById(R.id.input);
    	// 方法1 Android获得屏幕的宽和高    
        WindowManager windowManager = getWindowManager();    
        Display display = windowManager.getDefaultDisplay();    
        int screenWidth = screenWidth = display.getWidth();    
        int screenHeight = screenHeight = display.getHeight();    
            
        // 方法2   
        DisplayMetrics dm = new DisplayMetrics();  
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        float width=dm.widthPixels*dm.density;   
        float height=dm.heightPixels*dm.density; 
    }
    class MyThread extends Thread{
    	String txt;
    	public MyThread(String input) {
			//初始化线程
    		txt = input;
		}
    	@Override
    	public void run() {
    		// 定义消息
    		Message msg = new Message();
    		msg.what = 1;
    		Bundle bundle = new Bundle();
    		bundle.clear();
    		//连接服务器 并设置连接超时为5秒
    		socket = new Socket();
    		try {
    			//要看清楚网址信息,如果是手机,连接WIFI要用无线局域网分配的地址
    			//如果是虚拟机,网址则是要用有线以太网的地址
				socket.connect(new InetSocketAddress("192.168.40.227", 30000), 5000);
				//获取输入输出流
				OutputStream os = socket.getOutputStream();
				BufferedReader br = new BufferedReader(
						new InputStreamReader(socket.getInputStream())
						);
				//读取发来服务器信息
				String line = null;
				buffer = "";
				while ((line = br.readLine())!= null) {
					buffer = line + buffer+"你好" + txt;
				}
				// 把数据写入到OuputStream对象中  
				os.write(("android客户端"+txt).getBytes("utf-8"));
				//向服务器发送信息
				// 发送读取的数据到服务端 
				os.flush();
				bundle.putString("msg", buffer.toString());
				msg.setData(bundle);
				//发送消息,修改UI线程中的组件
				myHandler.sendMessage(msg);
				//关闭各种输入输出流
				br.close();
				os.close();
				socket.close();
    		}catch (SocketTimeoutException e) {
    			//连接超时 在UI界面显示消息
				bundle.putString("msg", "服务器连接失败!请检查网络");
				msg.setData(bundle);
				//发送消息 修改UI线程中的组件
				myHandler.sendMessage(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    	}
    }
}