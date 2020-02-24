package com.finger.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;

import com.baselibrary.callBack.FingerVerifyResultListener;
import com.baselibrary.constant.AppConstant;
import com.baselibrary.pojo.Finger6;
import com.baselibrary.util.FingerListManager;
import com.finger.constant.FingerConstant;
import com.finger.fingerApi.FingerApi;
import com.orhanobut.logger.Logger;
import com.sd.tgfinger.tgApi.TGBApi;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class FingerService extends Service {

    public static final String ACTION = "com.finger.FingerService.action";
    public static final String CATEGORY = "android.intent.category.DEFAULT";

    private Timer timer;
    private MyTask myTask;
    private byte[] fingerData;
    private int fingerDataSize;

    public FingerService() {
    }

    private Messenger fingerUtilMessenger;
    private static Activity activity;
    private Boolean isLoop = false;
    private FingerVerifyResultListener fingerVerifyResultListener;

    private Messenger messenger = new Messenger(new FingerServiceHandler());

    @SuppressLint("HandlerLeak")
    private class FingerServiceHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == FingerConstant.SEND_CODE) {
                activity = (Activity) msg.obj;
                fingerUtilMessenger = msg.replyTo;
                sendMsg1();
                updateFingerArrayToByte();
                taskSchedule();
            } else if (msg.what == FingerConstant.PAUSE_VERIFY_CODE) {
                pauseFingerVerify();
            } else if (msg.what == FingerConstant.RESTART_VERIFY_CODE) {
                FingerService.this.isCancelVerify = false;
            } else if (msg.what == FingerConstant.ADD_FINGER_CODE) {
                Bundle bun = msg.getData();
                byte[] newSingleFingerData = bun.getByteArray(AppConstant.ADD_FINGER);
                byte[] newFingerDatas = new byte[AppConstant.FINGER6_DATA_SIZE * (fingerDataSize + 1)];
                if (newSingleFingerData != null) {
                    if (fingerDataSize > 0) {
                        System.arraycopy(fingerData, 0, newFingerDatas, 0, fingerData.length);
                        System.arraycopy(newSingleFingerData, 0, newFingerDatas, fingerData.length,
                                newSingleFingerData.length);
                        FingerService.this.fingerData = newFingerDatas;
                        FingerService.this.fingerDataSize = (fingerDataSize + 1);
                    } else {
                        FingerService.this.fingerData = newSingleFingerData;
                        FingerService.this.fingerDataSize = 1;
                    }
                }
                FingerService.this.isCancelVerify = false;
            } else if (msg.what == FingerConstant.DELETE_FINGER_CODE) {
                Bundle bundle = msg.getData();
                int position = bundle.getInt(AppConstant.DELETE_FINGER);
                deleteFinger(position);
            } else if (msg.what == FingerConstant.SEND_MSG_2) {
                fingerVerifyResultListener = (FingerVerifyResultListener) msg.obj;
            } else if (msg.what == FingerConstant.UP_DATE_FINGER) {
                Logger.d("更新指静脉数据  11：");
                updateFingerArrayToByte();
            }
        }
    }

    /**
     * 发送消息到FingerServiceUtil
     */
    private void sendMsg1() {
        if (fingerUtilMessenger != null) {
            try {
                Message message = new Message();
                message.what = FingerConstant.SEND_MSG_1;
                fingerUtilMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateFingerArrayToByte() {
        ArrayList<Finger6> finger6ArrayList = FingerListManager.getInstance().getFingerData();
        if (finger6ArrayList != null && finger6ArrayList.size() > 0) {
            int fingerSize = finger6ArrayList.size();
            byte[] fingerData = new byte[AppConstant.FINGER6_DATA_SIZE * fingerSize];
            for (int i = 0; i < fingerSize; i++) {
                byte[] finger6Feature = finger6ArrayList.get(i).getFinger6Feature();
                System.arraycopy(finger6Feature, 0, fingerData,
                        AppConstant.FINGER6_DATA_SIZE * i, AppConstant.FINGER6_DATA_SIZE);
            }
            Logger.d("更新指静脉数据  22：");
            FingerService.this.fingerData = fingerData;
            FingerService.this.fingerDataSize = fingerSize;
        } else {
            FingerService.this.fingerData = new byte[AppConstant.FINGER6_DATA_SIZE];
            FingerService.this.fingerDataSize = 1;
        }
    }

    private void deleteFinger(int position) {
        ArrayList<Finger6> finger6ArrayList = FingerListManager.getInstance().getFingerData();
        if (finger6ArrayList != null && finger6ArrayList.size() > 0) {
            finger6ArrayList.remove(position);
            updateFingerArrayToByte();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        timer = new Timer();
        myTask = new MyTask();
        return messenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseTimerTask();
        stopSelf();
    }

    private void taskSchedule() {
        FingerService.this.isCancelVerify = false;
        if (!isLoop) {
            Logger.d("启动FingerService  111");
            timer.schedule(myTask, 800, 700);
        }
    }

    private Boolean isCancelVerify = false;

    private void pauseFingerVerify() {
        isCancelVerify = true;
        if (activity != null) {
            TGBApi.getTGAPI().cancelRegisterGetImg(activity, msg -> {
                if (msg.getResult() == 1) {
                    Logger.d("取消抓图");
                }
            });
            FingerService.this.isLoop = false;
        }
    }

    private class MyTask extends TimerTask {

        @Override
        public void run() {
            Logger.d("  是否验证：" + isCancelVerify);
            if (!isCancelVerify) {
                Logger.d("执行指静脉验证: fingerSize:" + fingerDataSize);
                FingerService.this.isLoop = true;
                FingerApi.getInstance().verifyN(activity, fingerData, fingerDataSize, false,
                        msg -> {
                            if (fingerVerifyResultListener != null) {
                                Logger.d("指静脉验证结果:" + msg.getResult());
                                if (msg.getResult() == 1) {
                                    Integer index = msg.getIndex();
                                    Long fingerId = FingerListManager.getInstance()
                                            .getFingerData().get(index).getFingerId();
                                    fingerVerifyResultListener.fingerVerifyResult(1,
                                            msg.getTip(), msg.getScore(), index,
                                            fingerId, msg.getFingerData());
                                } else {
                                    fingerVerifyResultListener.fingerVerifyResult(msg.getResult(), msg.getTip()
                                            , 0, 0, 0L, null);
                                }
                            }
                        });
            }
        }
    }

    private void releaseTimerTask() {
        FingerService.this.isLoop = false;
        if (myTask != null) {
            myTask.cancel();
            myTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


}
