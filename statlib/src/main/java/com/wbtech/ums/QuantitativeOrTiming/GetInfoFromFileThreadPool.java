package com.wbtech.ums.QuantitativeOrTiming;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.wbtech.ums.dao.GetInfoFromFile;

import java.util.HashMap;

/**
 * Created by dupengfei on 15/8/13.
 * 定时会产生大量thread，此类用一个mHashMap去明确始终运行状态就一个thread，之前的给销毁掉
 */
public class GetInfoFromFileThreadPool {

    //map的键
    private static final String TAG = "Thread";

    private  static GetInfoFromFileThreadPool mGetInfoFromFileThreadPool;
    //存储当前活跃的thread
    private  HashMap<String ,GetInfoFromFile> mHashMap;
    //销毁thread的管理Handler
    private  Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            stopThread();

            return false;
        }
    });

    public GetInfoFromFileThreadPool(){
        mHashMap = new HashMap<String ,GetInfoFromFile>();
    }

    public static  synchronized GetInfoFromFileThreadPool getInstance(){

    if(mGetInfoFromFileThreadPool == null)mGetInfoFromFileThreadPool = new GetInfoFromFileThreadPool();
        return mGetInfoFromFileThreadPool;
    }

    /**
     * 在thread启动的时候吧thread 放入map里，如果上一个没有销毁则销毁
     * @param thread
     */
    public void putThread(GetInfoFromFile thread ){
        if(mHashMap == null) mHashMap = new HashMap<String ,GetInfoFromFile>();
        GetInfoFromFile getInfoFromFile = mHashMap.get(TAG);
        if(getInfoFromFile != null &&getInfoFromFile.isAlive())getInfoFromFile.interrupt();
        mHashMap.put(TAG,thread);
    }

     // 销毁map中的thread
    private  void stopThread(){
        Log.v("stopThread","begin");
        if(mHashMap == null)return;
        GetInfoFromFile getInfoFromFile = mHashMap.get(TAG);
        if(  getInfoFromFile == null)return;
        if(getInfoFromFile.isAlive())getInfoFromFile.interrupt();
        Log.v("stopThread","done");
    }

    /**
     * thread执行完毕的时候发送message
     */
    public void handlerForstopThread(){
        mHandler.sendEmptyMessage(0);
    }
}
