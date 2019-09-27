package com.baselibrary.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import com.baselibrary.R;


/**
 * 背景音乐提供类
 */
public class AudioProvider implements SoundPool.OnLoadCompleteListener {

    private volatile static AudioProvider mInstance;

    // 声音池
    private SoundPool soundPool;
    // 删除成功
    private int soundID_deleteok;
    // 采集失败
    private int soundID_enrollfail;
    // 采集成功
    private int soundID_enrollok;
    // 请放手指
    private int soundID_inputfinger;
    // 请再次放手指
    private int soundID_inputagain;
    // 验证成功
    private int soundID_vetifyok;
    // 验证失败
    private int soundID_vetifyfail;
    // 放置身份证
    private int soundID_inputcard;
    // 身份证识别成功
    private int soundID_cardok;
    // 身份证识别失败
    private int soundID_cardfail;
    // 拿开手指
    private int soundID_fingerrelease;
    //操作超时
    private int soundID_timeout;
    //非相同手指、采集失败
    private int soundID_not_samefinger;
    //有相同id登记
    private int soundID_has_sameid;
    // 音量
    private float volume;
    //idcard ok
    private int soundID_idOk;
    //rfid卡
    private int rfid_card;

    private int not_same_finger;
    private int success;

    private Context mContext;
    //当前因音量值
    private float streamVolumeCurrent;
    //最大的音量值
    private float streamVolumeMax;
    private final AudioManager mgr;
    private int playID = -1;

    /**
     * 创建SoundPool ，注意 api 等级
     * https://blog.csdn.net/qq_28261343/article/details/82818868
     */
    private void createSoundPoolIfNeeded() {
        if (soundPool == null) {
            // 5.0 及 之后
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                AudioAttributes audioAttributes = null;
//                audioAttributes = new AudioAttributes.Builder()
//                        .setUsage(AudioAttributes.USAGE_MEDIA)
//                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                        .build();
//
//                soundPool = new SoundPool.Builder()
//                        .setMaxStreams(16)
//                        .setAudioAttributes(audioAttributes)
//                        .build();
//            } else { // 5.0 以前
                soundPool = new SoundPool(16, AudioManager.STREAM_SYSTEM, 0);  // 创建SoundPool
//            }
            soundPool.setOnLoadCompleteListener(this);  // 设置加载完成监听
        }
    }

    // 构造函数
    public AudioProvider(Context context) {
        mContext = context;
//        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        // 使用音乐池播放短小的音乐
        createSoundPoolIfNeeded();
        // 取得当前音量大小
        mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (mgr != null) {
            mgr.loadSoundEffects();
            // 当前音量
            streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_SYSTEM);
            streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
            // 最大音量
            volume = streamVolumeCurrent / streamVolumeMax;
//                volume = streamVolumeCurrent;
            //  Log.d("===哈哈哈", "   初始化 音量值：" + volume);
        }
    }

    public static AudioProvider getInstance(Context context) {
        if (mInstance == null) {
            synchronized (AudioProvider.class) {
                if (mInstance == null)
                    mInstance = new AudioProvider(context);
            }
        }
        return mInstance;
    }

    /**
     * 暂停播放背景音乐和回复播放背景音乐
     * https://blog.csdn.net/sodino/article/details/10055659
     *
     * @param bMute
     * @return
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public boolean muteAudioFocus(boolean bMute) {
        if (mContext == null) {
            Log.d("ANDROID_LAB", "context is null.");
            return false;
        }
//        if () {
//            // 2.1以下的版本不支持下面的API：requestAudioFocus和abandonAudioFocus
//            Log.d("ANDROID_LAB", "Android 2.1 and below can not stop music");
//            return false;
//        }
        boolean bool = false;
//        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (bMute) {
            int result = mgr.requestAudioFocus(null, AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            bool = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        } else {
            int result = mgr.abandonAudioFocus(null);
            bool = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        }
        Log.d("ANDROID_LAB", "pauseMusic bMute=" + bMute + " result=" + bool);
        return bool;
    }

    //获取当前的音量
    public float getCurrentVolume() {
        return streamVolumeCurrent;
    }

    //获取最大的音量值
    public float getStreamVolumeMax() {
        return streamVolumeMax;
    }

    //增大声音
    public boolean increaceVolume() {
        if (streamVolumeCurrent < streamVolumeMax) {
            streamVolumeCurrent++;
            volume = streamVolumeCurrent / streamVolumeMax;
            releaseAndCreatePool();
            soundPool.load(mContext, R.raw.success, 1);
            return true;
        } else {
            return false;
        }
    }

    //减小声音
    public boolean decreaseVolume() {
        if (streamVolumeCurrent <= 0) {
            return false;
        } else {
            streamVolumeCurrent--;
            volume = streamVolumeCurrent / streamVolumeMax;
            releaseAndCreatePool();
            soundPool.load(mContext, R.raw.success, 1);
            return true;
        }
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    // 播放采集失败，非同一手指
    public void not_same_finger() {
        releaseAndCreatePool();
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.not_same_finger, 1);
        }

    }

    public void stopPlay(int prePlayID) {
        if (soundPool != null && prePlayID != -1) {
            soundPool.stop(prePlayID);
        }
    }

    private void releaseAndCreatePool() {
        stopPlay(playID);
//        if (soundPool != null) {
//            soundPool.release();
//            soundPool = null;
//        }
//        createSoundPoolIfNeeded();
    }

    // 播放叮叮声
    public void success() {
        releaseAndCreatePool();
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.success, 1);
        }
    }

    // 播放采集成功
    public void play_enroll_ok() {
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.enrollok, 1);
        }
    }

    // 播放采集失败
    public void play_enroll_fail() {
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.enrollfail, 1);
        }
    }

    // 播放认证成功
    public void play_vetify_ok() {
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.vetifyok, 1);
        }
    }

    // 播放认证失败
    public void play_vetify_fail() {
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.vetifyfail, 1);
        }
    }

    // 播放请放手指语音
    public void play_input_finger() {
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.inputfinger, 1);
        }
    }

    // 播放请再次放手指语音
    public void play_input_again() {
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.inputagain, 1);
        }
    }

    // 播放IDCARD识别成功
    public void play_id_ok() {
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.cardok1, 1);
        }
    }

    // 播放身份证识别成功
    public void play_card_ok() {
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.cardok, 1);
        }
    }

    // 播放身份证识别失败
    public void play_card_fail() {
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.cardfail, 1);
        }
    }

    // 播放请将身份证放在指定区域
    public void play_input_card() {
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.inputcard, 1);
        }
    }

    //请放开手指
    public void play_release_finger() {
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.release_finger, 1);
        }
    }

    // 操作超时
    public void play_time_out() {
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.time_out, 1);
        }
    }

    // 不是同一根手指
    public void play_not_same_finger() {
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.not_same_finger, 1);
        }
    }

    // 有相同ID登记
    public void play_has_same_id() {
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.has_same_id, 1);
        }
    }

    // 请刷卡（rfid）
    public void play_rfid_card() {
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.rfid_card, 1);
        }
    }

    /**
     * 给声音池添加声音（请谨慎调用，注意调用次数不要超过256）
     *
     * @param raw
     * @return
     */
    public int load(int raw) {
        return soundPool.load(mContext, raw, 1);
    }


    //滴
    public void play_di() {
        releaseAndCreatePool();
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.waw1, 1);
        }
    }

    //滴滴
    public void play_didi() {
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.waw2, 1);
        }
    }

    //登记成功
    public void play_checkInSuccess() {
        releaseAndCreatePool();
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.waw3, 1);
        }
    }

    // 登记失败
    public void play_checkInFail() {
        releaseAndCreatePool();
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.waw4, 1);
        }
    }

    // 请再放一次
    public void play_inputAgain() {
        releaseAndCreatePool();
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.waw5, 1);
        }
    }

    // 请正确放入手指
    public void play_inputCorrect() {
        releaseAndCreatePool();
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.waw6, 1);
        }
    }

    // 请自然轻放手指
    public void play_inputDownGently() {
        releaseAndCreatePool();
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.waw7, 1);
        }
    }

    // 验证成功
    public void play_verifySuccess() {
        releaseAndCreatePool();
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.waw8, 1);
        }
    }

    // 验证失败
    public void play_verifyFail() {
        releaseAndCreatePool();
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.waw9, 1);
        }
    }

    // 请重试
    public void play_retryFeature() {
        releaseAndCreatePool();
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.waw10, 1);
        }
    }

    // 删除成功
    public void play_deleteSuccess() {
        releaseAndCreatePool();
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.waw11, 1);
        }
    }

    // 删除失败
    public void play_deleteFail() {
        releaseAndCreatePool();
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.waw12, 1);
        }
    }

    // 指静脉已满
    public void play_featureFull() {
        releaseAndCreatePool();
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.waw13, 1);
        }
    }

    // 登记重复
    public void play_registerRepeat() {
        releaseAndCreatePool();
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.waw14, 1);
        }
    }

    // 请抬起手指
    public void play_upLiftFinger() {
        releaseAndCreatePool();
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.waw15, 1);
        }
    }

    // 网线未连接
    public void play_notConnectNet() {
        releaseAndCreatePool();
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.waw16, 1);
        }
    }

    // 服务器未连接
    public void play_noConnectServer() {
        releaseAndCreatePool();
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.waw17, 1);
        }
    }

    // 请勿重复认证
    public void play_doNotAuthRepeat() {
        releaseAndCreatePool();
        if (soundPool != null) {
            soundPool.load(mContext, R.raw.waw18, 1);
        }
    }


    /**
     * 播放自定义声音
     *
     * @param sound 声音在声音池中的id 即load方法的返回值
     */
    public void play(int sound) {
        releaseAndCreatePool();
        if (soundPool != null) {
            playID = soundPool.play(sound, volume, volume, 1, 0, 1);
        }
    }

    // 释放媒体资源
    public void release() {
        try {
            soundPool.release();
            soundPool = null;
            mInstance = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int i, int i1) {
        if (soundPool != null) {
            playID = soundPool.play(i, volume, volume, 1, 0, 1);
        }
    }
}