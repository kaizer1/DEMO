package com.UHF.scanlable;

import static com.UHF.scanlable.ScanMode.MSG_UPDATE_LISTVIEW;
import static com.UHF.scanlable.ScanMode.MSG_UPDATE_STOP;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.device.DeviceManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.os.Bundle;
import android.widget.Spinner;
import java.util.Timer;
import java.util.TimerTask;


import com.rfid.trans.ReadTag;
import com.rfid.trans.ReaderParameter;
import com.rfid.trans.TagCallback;

public class WebViewA extends Activity {


    private static final int MSG_UPDATE_TIME = 1;
	private static final int MSG_UPDATE_SPEED = 2;

        private WebView webView;
        Spinner spbaudRate;
        private int ReaderType=-1;
	    private int ModuleType=-1;
        Handler handler;
        private Timer timer;
        public long lastTime=0;
	    public int lastCount=0;
		String items[]=null;
	    boolean chk[]=null;
		public boolean isStopThread=false;

        MsgCallback callback = new MsgCallback();




    	@Override
	    protected void onCreate(Bundle savedInstanceState)  {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activi_webview);

            webView = findViewById(R.id.id_webview);
            CookieManager jejHFBE = CookieManager.getInstance();
            jejHFBE.setAcceptCookie(true);


            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setSupportZoom(false);
            webSettings.setAllowContentAccess(true);
            webSettings.setAllowFileAccess(true);
            webSettings.setAllowContentAccess(true);

            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setDefaultTextEncodingName("utf-8");
            webSettings.setDomStorageEnabled(true);
            webSettings.setDisplayZoomControls(false);
            webSettings.setPluginState(WebSettings.PluginState.ON);
            webSettings.setBuiltInZoomControls(true);


            webView.loadUrl("https://beloson.ru/display?channel=android");

            	Reader.rrlib.SetCallBack(callback);


                	Log.i("MY", "UHFReadTagFragment.EtCountOfTags=" + "10");
			handler = new Handler() {
				@SuppressLint("HandlerLeak")
				@Override
				public void handleMessage(Message msg) {
					try{
						switch (msg.what) {
							case MSG_UPDATE_LISTVIEW:
								String result = msg.obj+"";
								String[] strs = result.split(",");
//								if(strs.length==2)
//								{
//								//	addEPCToList(strs[0], strs[1]);
//								 Log.d("df", " my values strs[0] = " + strs[0].toString() );
//                                  // new AsynchronousGet().run();
//								}
//								else
//								{
//									//addEPCToList(strs[0]+","+strs[1], strs[2]);
//								 Log.d("df", " my strs[0] + strs[1] .. = " + strs[0].toString() + " " + strs[1].toString());
//                                 // new AsynchronousGet().run();
//								}

								break;
							case MSG_UPDATE_TIME:
								String ReadTime = msg.obj+"";
								long CurTime = Integer.valueOf(ReadTime);
								long hour=CurTime/(60*60*1000);
								long min=(CurTime/1000 - (hour*60*60))/60;
								long sec=(CurTime/1000-hour*60*60-min*60);
								String strHour = String.valueOf(hour);
								if(strHour.length()<2) strHour="0"+strHour;
								String strmin = String.valueOf(min);
								if(strmin.length()<2) strmin="0"+strmin;
								String strsec = String.valueOf(sec);
								if(strsec.length()<2) strsec="0"+strsec;
								//tv_time.setText(strHour+":"+strmin+":"+strsec);
								break;
							case MSG_UPDATE_SPEED:
								String readSpeed = msg.obj+"";
								 //new AsynchronousGet().run();
								Log.d("df", " speed = " + readSpeed);
                                //tv_speed.setText(readSpeed);
								break;
							case MSG_UPDATE_STOP:
								if(timer != null){
									timer.cancel();
									timer = null;
									//BtInventory.setText(getString(R.string.btStoping));
								}
								//setViewEnabled(true);
								//BtInventory.setText(getString(R.string.btInventory));

//								if(ledlist.size()==0)
//								{
//									items=new String[tagList.size()];
//									chk=new boolean[tagList.size()];
//									for(int i=0;i<tagList.size();i++)
//									{
//										items[i]=tagList.get(i).get("tagUii");
//										chk[i]=false;
//									}
//								}

								break;
							default:
								break;
						}
					}catch(Exception ex)
					{ex.toString();}
				}
			};
		//}

     }


   @Override
	protected void onResume() {
		OtgUtils.setPOGOPINEnable(true);
		super.onResume();


			setOpenScan523(false);
		isStopThread =false;

//        	switch (Connect232.baud)
//		{
//			case 57600:
//                //spbaudRate.setSelection(0,true);
//				break;
//			case 115200:
//				//spbaudRate.setSelection(1,true);
//				break;
//		}
//		if(ModuleType==0)//953/963
//		{
//			ReadParam();
//			ReadInformation();
//			getRangeControll();
//		}
//		else if(ModuleType==1)//r2000
//		{
//			ReadParam();
//			ReadInformation();
//			ReadProfile();
//			ReadCheckAnt();
//		}
//		else if(ModuleType==2)//ex10
//		{
//			ReadParam();
//			ReadInformation();
//			ReadFocus();
//			ReadProfile();
//			ReadCheckAnt();
//		}
//		else if(ModuleType==3)//c6
//		{
//			ReadParam();
//			ReadInformation();
//			ReadProfile();
//		}


	}
	@Override
	protected void onPause() {
		super.onPause();
				setOpenScan523(true);
		stopInventory();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Reader.rrlib.DisConnect();
	}


	private void stopInventory(){
	//	if(chkled.isChecked())/

		//{
	//		Reader.rrlib.StopInventoryLed();
	//	}
		//else
			Reader.rrlib.StopRead();
	}


    	private void ReadFocus()
	{
		byte[]data = new byte[250];
		int[] len = new int[1];
		int fCmdRet = Reader.rrlib.GetCfgParameter((byte)8,data,len);
		if(fCmdRet==0 && len[0]==1)
		{
			//spTagfocus.setSelection(data[0],true);
			Log.d("df", " my data[0] in focus = " + data[0] );
            //Reader.writelog(getString(R.string.get_success),tvResult);
		}
		else
		{
			//Reader.writelog(getString(R.string.get_failed),tvResult);
		}
	}



    private void ReadParam()
	{
		ReaderParameter param = Reader.rrlib.GetInventoryPatameter();
		//sptidlen.setSelection(param.Length, true);
		//sptidaddr.setSelection(param.WordPtr, true);
		//spqvalue.setSelection(param.QValue,true);
		//sptime.setSelection(param.ScanTime,true);
		//spType.setSelection(param.IvtType,true);
		//spMem.setSelection(param.Memory-1,true);

		int sessionindex = param.Session;
		if(sessionindex==255) sessionindex=4;
		if(sessionindex==254) sessionindex=5;
		if(sessionindex==253) sessionindex=6;
		if(sessionindex==252) sessionindex=7;
		if(sessionindex==251) sessionindex=8;
		//spsession.setSelection(sessionindex,true);
		if(Reader.rrlib.ModuleType==2)
		{
			byte[]data = new byte[30];
			int[]len = new int[1];
			int fCmdRet = Reader.rrlib.GetCfgParameter((byte)7,data,len);
			if(fCmdRet==0 && len[0]==3)
			{
				//jgTime.setSelection( data[0],true);
				//spDwell.setSelection(data[1]-2,true);
                Log.d("df", " my data[0] = " + data[0]);
                Log.d("df", " my data[1]-2 = " + (data[1]-2));
            }
		}
		Log.d("dsf", " ScanMode.runtime+");
		//Reader.writelog(getString(R.string.get_success),tvResult);
	}

	private int ReaderCode=0;
	private int curband =0;
	private void ReadInformation()
	{
		byte[]Version=new byte[2];
		byte[]Power=new byte[1];
		byte[]band=new byte[1];
		byte[]MaxFre=new byte[1];
		byte[]MinFre=new byte[1];
		int result = Reader.rrlib.GetReaderInformation(Version, Power, band, MaxFre, MinFre);
		if(result==0)
		{
			String hvn = String.valueOf(Version[0]&255);
			if(hvn.length()==1)hvn="0"+hvn;
			String lvn = String.valueOf(Version[1]&255);
			if(lvn.length()==1)lvn="0"+lvn;
			ReaderCode = Reader.rrlib.GetReaderType();
			String ModuleInfo = "";
			if(ReaderCode==0x70 || ReaderCode==0x71 || ReaderCode==0x31) {
				byte[]Describe = new byte[16];
				result =  Reader.rrlib.GetModuleDescribe(Describe);
				String dscInfo = "";
				if(Describe[0]==0x00)
					dscInfo="S";
				else if(Describe[0]==0x01)
					dscInfo="Plus";
				else if(Describe[0]==0x02)
					dscInfo="Pro";
				ModuleInfo = hvn + "." + lvn + " (" + Integer.toHexString(ReaderCode)+"-"+dscInfo+ ")";
			}
			else
				ModuleInfo = hvn+"."+lvn+" ("+Integer.toHexString(ReaderCode)+")";

            Log.d("df", " tvVersion == " + ModuleInfo);
            //tvVersion.setText(ModuleInfo);
			//tvpowerdBm.setSelection(Power[0],true);
			curband = band[0];
			//SetFre(band[0]);
			int bandindex = band[0];
			if(bandindex ==8)
			{
				bandindex=bandindex-4;
			}
			else if(bandindex==0)
			{
				bandindex = 5;
			}
			else
			{
				bandindex=bandindex-1;
			}
			//spBand.setSelection(bandindex,true);
			//spminFrm.setSelection(MinFre[0],true);
			//spmaxFrm.setSelection(MaxFre[0],true);
			//sptime.setSelection(ScanTime[0]&255,true);
			//Reader.writelog(getString(R.string.get_success),tvResult);
		}
		else
		{
			//Reader.writelog(getString(R.string.get_failed),tvResult);
		}
	}


    	private void getRangeControll()
	{
		byte[]data = new byte[250];
		int[] len = new int[1];
		int fCmdRet = Reader.rrlib.GetCfgParameter((byte)16,data,len);
		if(fCmdRet==0 && len[0]==4)
		{
			//spRange.setSelection(data[3]&255,true);
			//Reader.writelog(getString(R.string.get_success),tvResult);
		}
		else
		{
			//Reader.writelog(getString(R.string.get_failed),tvResult);
		}
	}

	private void ReadProfile()
	{
		byte[]Profile= new byte[1];
		int result = Reader.rrlib.GetProfile(Profile);
		if(result==0)
		{
			int index = 0;
			if(ModuleType==2)
			{
				switch(Profile[0]&255)
				{
					case 11:
						index = 0;
						break;
					case 1:
						index = 1;
						break;
					case 15:
						index = 2;
						break;
					case 12:
						index = 3;
						break;
					case 3:
						index = 4;
						break;
					case 5:
						index = 5;
						break;
					case 7:
						index = 6;
						break;
					case 13:
						index = 7;
						break;
					case 50:
						index = 8;
						break;
					case 51:
						index = 9;
						break;
					case 52:
						index = 10;
						break;
					case 53:
						index = 11;
						break;
				}
				//spProfilr.setSelection(index,true);
			}

			else
			{
				//spProfilr.setSelection(Profile[0]&255,true);
			}
			//Reader.writelog(getString(R.string.set_success),tvResult);
		}
		else if(ModuleType==3)
		{
			//spProfilr.setSelection((Profile[0]&255)-0x10,true);
		}
		else
		{
			//eader.writelog(getString(R.string.set_failed),tvResult);
		}
	}


    	public class MsgCallback implements TagCallback {

		@Override
		public void tagCallback(ReadTag arg0) {

			// TODO Auto-generated method stub
			String epc="";
			String mem="";
			if(arg0.epcId!=null)
				epc = arg0.epcId.toUpperCase();
			if(arg0.memId!=null)
				mem = arg0.memId.toUpperCase();

			String rssi = String.valueOf(arg0.rssi);
			Message msg = handler.obtainMessage();

			msg.what = MSG_UPDATE_LISTVIEW;
			if(mem.length()==0)
				msg.obj =epc+","+rssi ;
			else
				msg.obj =epc+","+mem+","+rssi ;
			handler.sendMessage(msg);

			lastCount++;
			if(System.currentTimeMillis() - lastTime>=1000)
			{
				msg = handler.obtainMessage();
				msg.what = MSG_UPDATE_SPEED;
				msg.obj = ((lastCount*1000)/(System.currentTimeMillis() - lastTime)) + "";
				handler.sendMessage(msg);
				lastTime = System.currentTimeMillis();
				lastCount=0;
			}
		}

		@Override
		public void StopReadCallBack() {
			// TODO Auto-generated method stub

			Message msg = handler.obtainMessage();
			msg.what = MSG_UPDATE_STOP;
			msg.obj ="" ;
			handler.sendMessage(msg);
		}
	};

	private void setOpenScan523(boolean isopen) {
		try{
			DeviceManager mDeviceManager = new DeviceManager();
			if (mDeviceManager != null) {
				if (isopen) {
					//TODO 设置触发 523键值(手柄按钮) 扫描出光
					mDeviceManager.setSettingProperty("persist-persist.sys.rfid.key", "0-");
					mDeviceManager.setSettingProperty("persist-persist.sys.scan.key", "520-521-522-523-");//这里入参传入了哪些键值，在按下键值的的时候就会调起扫描头出光
				}else {
					//TODO 设置触发 523键值(手柄按钮) 不扫描出光
					mDeviceManager.setSettingProperty("persist-persist.sys.rfid.key", "0-");
					mDeviceManager.setSettingProperty("persist-persist.sys.scan.key", "520-521-522-");//这里入参传入了哪些键值，在按下键值的的时候就会调起扫描头出光
				}
			}
		}catch(Exception ex)
		{}
	}



    private void ReadCheckAnt()
	{
		byte[]AntCheck = new byte[1];
		int fCmdRet = Reader.rrlib.GetCheckAnt(AntCheck);
		if(fCmdRet==0)
		{
			//spAntCheck.setSelection(AntCheck[0],true);
			//Reader.writelog(getString(R.string.get_success),tvResult);
		}
		else
		{
			//Reader.writelog(getString(R.string.get_failed),tvResult);
		}
	}


}
