package app.psa;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.http.client.ClientProtocolException;

import psa.navi.HelloServer;
import android.app.Application;
import android.app.Dialog;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

public class MyApplication extends Application{
	
	RegisterTask registerTask;
	private static HelloServer mHelloServer; 
	boolean hasFinished = true;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mHelloServer = new HelloServer("App");
		registerTask = new RegisterTask();
		registerTask.execute();
		Log.e("MyApplication", "start");
	}
	public static HelloServer getInstance(){
		return mHelloServer;
	}
	
	public static void serverStart(){
		try {
			if (! mHelloServer.isAlive()) {
				mHelloServer.start();	
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void serverStop(){
		if (mHelloServer.isAlive()) {
			mHelloServer.stop();	
		}
	}
	
	private String getLocalIpAddress() throws UnknownHostException {
        WifiManager wifiManager = (WifiManager) this.getSystemService(android.content.Context.WIFI_SERVICE );
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String tmp = String.valueOf(String.format("%d.%d.%d.%d",
                (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff)));
        Log.e("ip", tmp);
        return tmp;
       /** return InetAddress.getByName(String.format("%d.%d.%d.%d",
                        (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                        (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff)));**/
	}
	
	class RegisterTask extends AsyncTask<Void, Integer, Integer>
	{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			hasFinished = false;
		}
		
		@Override
		protected Integer doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			try {
				Http.Register(getLocalIpAddress(), "8080");
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 1;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			hasFinished = true;
			
		}
	}
}
