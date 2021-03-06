package com.testApp.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.baselibrary.base.BaseApplication;
import com.baselibrary.constant.AppConstant;
import com.baselibrary.custom.CEditText;
import com.baselibrary.dao.db.DBUtil;
import com.baselibrary.dao.db.DbCallBack;
import com.baselibrary.dao.db.ManagerDao;
import com.baselibrary.dao.db.PwDao;
import com.baselibrary.listener.OnceClickListener;
import com.baselibrary.pojo.Face;
import com.baselibrary.pojo.Manager;
import com.baselibrary.pojo.User;
import com.baselibrary.pojo.Pw;
import com.baselibrary.util.SPUtil;
import com.baselibrary.util.SoftInputKeyboardUtils;
import com.baselibrary.util.ToastUtils;
import com.baselibrary.util.dialogUtil.AppDialog;
import com.baselibrary.util.glidUtils.GlideUtil;
import com.orhanobut.logger.Logger;
import com.testApp.R;
import com.testApp.callBack.CancelBtnClickListener;
import com.testApp.callBack.PositionBtnClickListener;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.baselibrary.util.SPUtil.putHasManagerPwd;


/**
 * Created By pq
 * on 2019/9/30
 */
public class AskDialog {

    /**
     * 删除manager的item dialog
     *
     * @param activity activity
     */
    public static void deleteItemDataDialog(@NonNull Activity activity, User user, Manager manager,
                                            String managerName,
                                            PositionBtnClickListener positionBtnClickListener) {
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.delete_item_dialog, null);
        AppCompatImageView userAvatar = dialogView.findViewById(R.id.userAvatar);
        AppCompatTextView title = dialogView.findViewById(R.id.title);
        AppCompatTextView userName = dialogView.findViewById(R.id.userName);
        AppCompatTextView userSex = dialogView.findViewById(R.id.userSex);
        AppCompatTextView userAge = dialogView.findViewById(R.id.userAge);
        AppCompatTextView userSection = dialogView.findViewById(R.id.userSection);
        AppCompatTextView userCompany = dialogView.findViewById(R.id.userCompany);
        AppCompatButton cancelBtn = dialogView.findViewById(R.id.cancelBtn);
        AppCompatButton positionBtn = dialogView.findViewById(R.id.positionBtn);
//        if (user != null) {
//            Face face = user.getFace();
//            String imagePath = null;
//            if (face != null) {
//                imagePath = face.getImagePath();
//            }
//            title.setText(activity.getString(R.string.delete_user));
//            GlideUtil.loadCircleImage(activity, R.drawable.ic_default_user_avatar,
//                    imagePath, userAvatar);
//            userName.setText(user.getName());
//            userSection.setText(user.getSection());
//            userSex.setText(user.getSex());
//            userAge.setText(user.getAge());
//            userCompany.setText(user.getOrganizName());
//        }
//        if (manager != null) {
//            userSex.setVisibility(View.GONE);
//            userAge.setVisibility(View.GONE);
//            userCompany.setVisibility(View.GONE);
//            userSection.setText(MessageFormat.format("密码:{0}", manager.getManage_pw()));
//            userName.setText(managerName);
//            title.setText(activity.getString(R.string.delete_manager));
//            GlideUtil.loadCircleImage(activity, R.drawable.ic_default_user_avatar,
//                    ActivityCompat.getDrawable(activity, R.drawable.ic_default_user_avatar), userAvatar);
//        }
        Dialog dialog = AppDialog.gmDialog(activity, dialogView, false);
        cancelBtn.setOnClickListener(new OnceClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                dialog.dismiss();
            }
        });
        positionBtn.setOnClickListener(new OnceClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (positionBtnClickListener != null)
                    positionBtnClickListener.positionClickListener(1);
                dialog.dismiss();
            }
        });
    }

    /**
     * 修改可注册的最大管理员数量
     *
     * @param activity activity
     */
    @SuppressLint("ResourceAsColor")
    public static void reviseMaxManagerNum(@NonNull Activity activity) {
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.revise_max_manager_num_view, null);
        AppCompatImageView dismissBtn = dialogView.findViewById(R.id.dismissBtn);
        AppCompatEditText maxManagerEt = dialogView.findViewById(R.id.maxManagerEt);
        AppCompatButton nextBtn = dialogView.findViewById(R.id.nextBtn);
        nextBtn.setText(activity.getString(R.string.positive));
        nextBtn.setVisibility(View.GONE);
        Dialog dialog = AppDialog.gmDialog(activity, dialogView, false);
        maxManagerEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = editable.length();
                if (length > 0) {
                    nextBtn.setVisibility(View.VISIBLE);
                } else {
                    nextBtn.setVisibility(View.GONE);
                }
            }
        });
        dismissBtn.setOnClickListener(new OnceClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                dialog.dismiss();
                SoftInputKeyboardUtils.hiddenKeyboard(maxManagerEt);
            }
        });
        nextBtn.setOnClickListener(new OnceClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                String maxManagerNum = maxManagerEt.getText().toString().trim();
                SPUtil.putMaxManagerNum(Integer.valueOf(maxManagerNum));
                ToastUtils.showSquareImgToast(activity, activity.getString(R.string.revise_success),
                        ActivityCompat.getDrawable(activity, R.drawable.ic_emoje));
                dialog.dismiss();
                SoftInputKeyboardUtils.hiddenKeyboard(maxManagerEt);
            }
        });
    }

    public static void showAskUserCenterDialog(@NonNull Activity activity, PositionBtnClickListener listener) {
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.ask_user_center_dialog_view,
                null);
        //AppCompatTextView tip = dialogView.findViewById(R.id.tip);
        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);
        AppCompatRadioButton fingerRadioButton = dialogView.findViewById(R.id.fingerRadioButton);
        AppCompatRadioButton faceRadioButton = dialogView.findViewById(R.id.faceRadioButton);
        AppCompatRadioButton cardRadioButton = dialogView.findViewById(R.id.cardRadioButton);
        AppCompatRadioButton pwRadioButton = dialogView.findViewById(R.id.pwRadioButton);
        AppCompatTextView cancelBtn = dialogView.findViewById(R.id.cancelBtn);
        AppCompatTextView positiveBtn = dialogView.findViewById(R.id.positiveBtn);
        final int[] flag = {0};
        radioGroup.setOnCheckedChangeListener((radioGroup1, i) -> {
            int checkedRadioButtonId = radioGroup1.getCheckedRadioButtonId();
            if (i == fingerRadioButton.getId()) {
                flag[0] = AppConstant.FINGER_MODEL;
            } else if (i == faceRadioButton.getId()) {
                flag[0] = AppConstant.FACE_MODEL;
            } else if (i == cardRadioButton.getId()) {
                flag[0] = AppConstant.ID_CARD_MODEL;
            } else if (i == pwRadioButton.getId()) {
                flag[0] = AppConstant.PW_MODEL;
            }
        });
        Dialog dialog = AppDialog.gmDialog(activity, dialogView, false);
        cancelBtn.setOnClickListener(new OnceClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                dialog.dismiss();
            }
        });
        positiveBtn.setOnClickListener(new OnceClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (listener != null)
                    listener.positionClickListener(flag[0]);
                dialog.dismiss();
            }
        });
    }

    public static void VerifyUserPwd(@NonNull Activity mContext, UserPwdVerifyCallBack callBack) {
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.ask_manager_dialog_view,
                null);
        AppCompatTextView managerSetTitle = dialogView.findViewById(R.id.managerSetTitle);
        AppCompatTextView managerTip = dialogView.findViewById(R.id.managerTip);
        managerTip.setText(mContext.getString(R.string.please_verify_pw));
        AppCompatButton nextBtn = dialogView.findViewById(R.id.nextBtn);
        CEditText inputPw = dialogView.findViewById(R.id.inputPw);
        AppCompatImageView dismissBtn = dialogView.findViewById(R.id.dismissBtn);
        managerSetTitle.setText(mContext.getString(R.string.verify_user_pwd));
        nextBtn.setText(mContext.getString(R.string.positive));
        Dialog dialog = AppDialog.gmDialog(mContext, dialogView, false);
        nextBtn.setOnClickListener(new OnceClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                String pw = inputPw.getText().toString().trim();
                if (pw.length() < 6) {
                    ToastUtils.showSquareTvToast(
                            mContext, mContext.getString(com.pw.R.string.please_input_6_pw));
                    return;
                }
                verifyUserPw(dialog, pw, callBack);
                try {
                    Thread.sleep(300);
                    SoftInputKeyboardUtils.hiddenKeyboard(inputPw);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        dismissBtn.setOnClickListener(new OnceClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                dialog.dismiss();
                SoftInputKeyboardUtils.hiddenKeyboard(inputPw);
            }
        });
    }


    /**
     * 确认管理员密码
     *
     * @param activity activity
     * @param callBack 接口回调
     */
    public static void verifyManagerPwd(@NonNull Activity activity, ManagerPwdVerifyCallBack callBack) {
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.ask_manager_dialog_view,
                null);
        AppCompatTextView managerSetTitle = dialogView.findViewById(R.id.managerSetTitle);
        AppCompatButton nextBtn = dialogView.findViewById(R.id.nextBtn);
        CEditText inputPw = dialogView.findViewById(R.id.inputPw);
        AppCompatImageView dismissBtn = dialogView.findViewById(R.id.dismissBtn);
        dismissBtn.setVisibility(View.VISIBLE);
        managerSetTitle.setText(activity.getString(R.string.verify_manager_pwd));
        nextBtn.setText(activity.getString(R.string.positive));
        Dialog dialog = AppDialog.gmDialog(activity, dialogView, false);
        nextBtn.setOnClickListener(new OnceClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                String pw = inputPw.getText().toString().trim();
                if (pw.length() < 6) {
                    ToastUtils.showSquareTvToast(
                            activity, activity.getString(com.pw.R.string.please_input_6_pw));
                    return;
                }
                verifyManagerPw(dialog, pw, callBack);
                try {
                    Thread.sleep(300);
                    SoftInputKeyboardUtils.hiddenKeyboard(inputPw);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        dismissBtn.setOnClickListener(new OnceClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                dialog.dismiss();
                SoftInputKeyboardUtils.hiddenKeyboard(inputPw);
            }
        });
    }

    /**
     * 询问用户是否保存当前用户信息
     *
     * @param activity activity
     */
    public static void showAskSaveDialog(@NonNull Activity activity, String content,
                                         PositionBtnClickListener positionClickListener,
                                         CancelBtnClickListener cancelClickListener) {
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.ask_user_save_register_msg, null);
        AppCompatTextView msgContent = dialogView.findViewById(R.id.msgContent);
        AppCompatButton cancelBtn = dialogView.findViewById(R.id.cancelBtn);
        AppCompatButton positiveBtn = dialogView.findViewById(R.id.positiveBtn);
        if (!TextUtils.isEmpty(content)) {
            msgContent.setText(content);
        }
        Dialog dialog = AppDialog.gmDialog(activity, dialogView, false);
        positiveBtn.setOnClickListener(new OnceClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (positionClickListener != null) {
                    positionClickListener.positionClickListener(1);
                    dialog.dismiss();
                }
            }
        });
        cancelBtn.setOnClickListener(new OnceClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (cancelClickListener != null) {
                    cancelClickListener.cancelClickListener();
                    dialog.dismiss();
                }
            }
        });

    }

    /**
     * 询问用户是否修改当前用户信息
     *
     * @param activity activity
     */
    public static void showAskUpdateDialog(@NonNull Activity activity, String content,
                                         PositionBtnClickListener positionClickListener,
                                         CancelBtnClickListener cancelClickListener) {
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.ask_user_save_register_msg, null);
        AppCompatTextView msgContent = dialogView.findViewById(R.id.msgContent);
        AppCompatButton cancelBtn = dialogView.findViewById(R.id.cancelBtn);
        AppCompatButton positiveBtn = dialogView.findViewById(R.id.positiveBtn);
        if (!TextUtils.isEmpty(content)) {
            msgContent.setText(content);
        }
        Dialog dialog = AppDialog.gmDialog(activity, dialogView, false);
        positiveBtn.setOnClickListener(new OnceClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (positionClickListener != null) {
                    positionClickListener.positionClickListener(1);
                    dialog.dismiss();
                }
            }
        });
        cancelBtn.setOnClickListener(new OnceClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (cancelClickListener != null) {
                    cancelClickListener.cancelClickListener();
                    dialog.dismiss();
                }
            }
        });

    }

    /**
     * 设置验证方式的Dialog
     *
     * @param activity activity
     */
    public static void showVerifyMethodSetDialog(@NonNull Activity activity) {
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.verify_method_set_dialog_view, null);
        AppCompatCheckBox cbPw = dialogView.findViewById(R.id.cbPw);
        AppCompatCheckBox cbFinger = dialogView.findViewById(R.id.cbFinger);
        AppCompatCheckBox cbFace = dialogView.findViewById(R.id.cbFace);
        AppCompatCheckBox cbCard = dialogView.findViewById(R.id.cbCard);
        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);
        AppCompatRadioButton radioBtn1 = dialogView.findViewById(R.id.radioBtn1);
        AppCompatRadioButton radioBtn2 = dialogView.findViewById(R.id.radioBtn2);
        AppCompatTextView cancelBtn = dialogView.findViewById(R.id.cancelBtn);
        AppCompatTextView positiveBtn = dialogView.findViewById(R.id.positiveBtn);
        AtomicBoolean openPw = new AtomicBoolean(false);
        AtomicBoolean openFinger = new AtomicBoolean(false);
        AtomicBoolean openFace = new AtomicBoolean(false);
        AtomicBoolean openCard = new AtomicBoolean(false);
        AtomicBoolean verifyLogic = new AtomicBoolean(false);
        openPw.set(SPUtil.getPwVerifyFlag());
        openCard.set(SPUtil.getCardVerifyFlag());
        openFinger.set(SPUtil.getFingerVerifyFlag());
        openFace.set(SPUtil.getFaceVerifyFlag());
        verifyLogic.set(SPUtil.getVerifyLogic());
        cbPw.setOnCheckedChangeListener((compoundButton, b) -> openPw.set(true));
        cbFinger.setOnCheckedChangeListener((compoundButton, b) -> openFinger.set(true));
        cbFace.setOnCheckedChangeListener((compoundButton, b) -> openFace.set(true));
        cbCard.setOnCheckedChangeListener((compoundButton, b) -> openCard.set(true));
        radioGroup.setOnCheckedChangeListener((radioGroup1, i) -> {
            if (radioBtn1.getId() == i) {
                verifyLogic.set(true);
            } else if (radioBtn2.getId() == i) {
                verifyLogic.set(false);
            }
        });
        Dialog dialog = AppDialog.gmDialog(activity, dialogView, false);
        cancelBtn.setOnClickListener(new OnceClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                dialog.dismiss();
            }
        });
        positiveBtn.setOnClickListener(new OnceClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                SPUtil.putPwVerifyFlag(openPw.get());
                SPUtil.putCardVerifyFlag(openCard.get());
                SPUtil.putFingerVerifyFlag(openFinger.get());
                SPUtil.putFaceVerifyFlag(openFace.get());
                SPUtil.putVerifyLogic(verifyLogic.get());
                dialog.dismiss();
            }
        });
    }

    static int lastLength = 0;

    /**
     * 展示设置管理员密码和启用人脸识别
     *
     * @param activity activity
     * @param callBack 回调接口
     */
    public static void showManagerDialog(@NonNull Activity activity, int flag, PositiveCallBack callBack) {
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.ask_manager_dialog_view, null);
        AppCompatTextView managerSetTitle = dialogView.findViewById(R.id.managerSetTitle);
        AppCompatTextView managerTip = dialogView.findViewById(R.id.managerTip);
        CEditText inputPw = dialogView.findViewById(R.id.inputPw);
        AppCompatButton nextBtn = dialogView.findViewById(R.id.nextBtn);
        LinearLayout btnParent = dialogView.findViewById(R.id.btnParent);
        LinearLayout openFaceAsk = dialogView.findViewById(R.id.openFaceAsk);
        AppCompatButton cancelBtn = dialogView.findViewById(R.id.cancelBtn);
        AppCompatButton positiveBtn = dialogView.findViewById(R.id.positiveBtn);
        AppCompatImageView dismissBtn = dialogView.findViewById(R.id.dismissBtn);
        managerSetTitle.setText(activity.getString(R.string.manager_set));
        nextBtn.setVisibility(View.VISIBLE);
        dismissBtn.setVisibility(View.VISIBLE);
        btnParent.setVisibility(View.GONE);
        if (flag == AppConstant.INIT_ADD_MANAGER) {
            dismissBtn.setVisibility(View.GONE);
        }
        Dialog dialog = AppDialog.gmDialog(activity, dialogView, false);
        final String[] pw1 = new String[2];
        nextBtn.setOnClickListener(new OnceClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onNoDoubleClick(View v) {
                if (nextBtn.getText().toString().trim().equals(activity.getString(R.string.previous))) {
                    pw1[0] = null;
                    pw1[1] = null;
                    inputPw.getText().clear();
                    managerTip.setText(activity.getString(R.string.please_input_pw_new));
                    nextBtn.setText(activity.getString(R.string.next));
                } else {
                    if (pw1[0] == null) {
                        String pw = inputPw.getText().toString().trim();
                        if (pw.length() < 6) {
                            ToastUtils.showSquareTvToast(
                                    activity, activity.getString(com.pw.R.string.please_input_6_pw));
                            return;
                        }
                        pw1[0] = pw;
                        if (flag == AppConstant.ADD_NEW_MANAGER) {
                            nextBtn.setText(activity.getString(R.string.save));
                        }
                        managerTip.setText(activity.getString(R.string.please_input_pw_agin));
                        inputPw.getText().clear();
                    } else {
                        String pw2 = inputPw.getText().toString().trim();
                        if (pw2.length() < 6) {
                            ToastUtils.showSquareTvToast(
                                    activity, activity.getString(com.pw.R.string.please_input_6_pw));
                            return;
                        }
                        pw1[1] = pw2;
                        if (pw1[0].equals(pw1[1])) {
                            if (flag == AppConstant.ADD_NEW_MANAGER) {
                                if (pw1[0].equals(pw1[1])) {
                                    checkManagerPw(activity, pw1[0], callBack);
                                }
                                SoftInputKeyboardUtils.hiddenKeyboard(inputPw);
                                dialog.dismiss();
                            } else if (flag == AppConstant.INIT_ADD_MANAGER) {
                                nextBtn.setVisibility(View.GONE);
                                inputPw.setVisibility(View.GONE);
                                openFaceAsk.setVisibility(View.VISIBLE);
                                btnParent.setVisibility(View.VISIBLE);
                                managerTip.setText(activity.getString(R.string.please_select_open_face));
                            }
                        } else {
                            ToastUtils.showSquareTvToast(
                                    activity, activity.getString(R.string.pw_no_eq));
                            nextBtn.setText(activity.getString(R.string.previous));
                            nextBtn.setTextColor(R.color.red_1);
                        }
                    }
                }
            }
        });
        dismissBtn.setOnClickListener(new OnceClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                pw1[0] = null;
                pw1[1] = null;
                SoftInputKeyboardUtils.hiddenKeyboard(inputPw);
                callBack.positiveCallBack(0, null);
                dialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new OnceClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (pw1[0].equals(pw1[1])) {
                    checkManagerPw(activity, pw1[0], callBack);
                }
                SoftInputKeyboardUtils.hiddenKeyboard(inputPw);
                SPUtil.putOpenFace(false);
                dialog.dismiss();
            }
        });
        positiveBtn.setOnClickListener(new OnceClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (pw1[0].equals(pw1[1])) {
                    checkManagerPw(activity, pw1[0], callBack);
                }
                callBack.activationCodeCallBack(AppConstant.FACE_ACTIVITY_CODE);
                openFaceAsk.setVisibility(View.GONE);
                btnParent.setVisibility(View.GONE);
                SPUtil.putOpenFace(true);
                dialog.dismiss();
            }
        });
    }

    //最多添加10位管理员
    private static void checkManagerPw(Activity activity, String pw, PositiveCallBack callBack) {
        DBUtil dbUtil = BaseApplication.getDbUtil();
        QueryBuilder<Manager> queryBuilder = dbUtil.getQueryBuilder(Manager.class);
        dbUtil.setDbCallBack(new DbCallBack<Manager>() {
            @Override
            public void onSuccess(Manager result) {

            }

            @Override
            public void onSuccess(List<Manager> result) {
                if (result.size() > 0) {
                    if (result.size() >= SPUtil.getMacManagerNum()) {
                        ToastUtils.showSingleToast(activity, activity.getString(R.string.max_manager_mun));
                        return;
                    }
                    for (int i = 0; i < result.size(); i++) {
                        String manage_pw = result.get(i).getManage_pw();
                        if (pw.equals(manage_pw)) {
                            ToastUtils.showSingleToast(activity, activity.getString(R.string.manage_no_had));
                            break;
                        } else {
                            if (i == result.size() - 1) {
                                saveManagerPwdToDB(activity, pw, callBack);
                            }
                        }
                    }
                } else {
                    saveManagerPwdToDB(activity, pw, callBack);
                }
            }

            @Override
            public void onFailed() {

            }

            @Override
            public void onNotification(boolean result) {

            }
        }).queryAsyncAll(Manager.class, queryBuilder);
    }

    //将管理员密码存储到数据库
    private static void saveManagerPwdToDB(Activity activity, String managerPw, PositiveCallBack callBack) {
        Manager manager = new Manager();
        manager.setManage_pw(managerPw);
        DBUtil dbUtil = BaseApplication.getDbUtil();
        dbUtil.insertOrReplace(manager);
        ToastUtils.showSquareImgToast(activity,
                activity.getString(R.string.manager_add_success),
                ActivityCompat.getDrawable(activity, R.drawable.ic_emoje));
        putHasManagerPwd(true);
        if (callBack != null) {
            callBack.positiveCallBack(1, manager);
        }
        Logger.d("管理员密码存储成功:");
    }

    //验证管理员密码
    private static void verifyUserPw(Dialog dialog, String pw, UserPwdVerifyCallBack callBack) {
        DBUtil dbUtil = BaseApplication.getDbUtil();
        WhereCondition whereCondition = PwDao.Properties.Password.eq(pw);
        dbUtil.setDbCallBack(new DbCallBack<Pw>() {
            @Override
            public void onSuccess(Pw result) {

            }

            @Override
            public void onSuccess(List<Pw> result) {
                if (result.size() > 0) {
                    callBack.userPwdVerifyCallBack(result.get(0).getPwId());

                } else {
                    callBack.userPwdVerifyCallBack(null);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailed() {

            }

            @Override
            public void onNotification(boolean result) {

            }
        }).queryAsync(Pw.class, whereCondition);
    }


    //验证管理员密码
    private static void verifyManagerPw(Dialog dialog, String pw, ManagerPwdVerifyCallBack callBack) {
        DBUtil dbUtil = BaseApplication.getDbUtil();
        QueryBuilder<Manager> queryBuilder = dbUtil.getQueryBuilder(Manager.class);
        dbUtil.setDbCallBack(new DbCallBack<Manager>() {
            @Override
            public void onSuccess(Manager result) {

            }

            @Override
            public void onSuccess(List<Manager> result) {
                if (result.size() > 0) {
                    for (int i = 0; i < result.size(); i++) {
                        String manage_pw = result.get(i).getManage_pw();
                        if (pw.equals(manage_pw)) {
                            callBack.managerPwdVerifyCallBack(true);
                            dialog.dismiss();
                            break;
                        } else {
                            if (i == result.size() - 1) {
                                callBack.managerPwdVerifyCallBack(false);
                                dialog.dismiss();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailed() {

            }

            @Override
            public void onNotification(boolean result) {

            }
        }).queryAsyncAll(Manager.class, queryBuilder);
    }

    public interface PositiveCallBack {
        void positiveCallBack(int flag, Manager manager);

        void activationCodeCallBack(String code);
    }

    public interface UserPwdVerifyCallBack {
        void userPwdVerifyCallBack(Long id);
    }

    public interface ManagerPwdVerifyCallBack {
        void managerPwdVerifyCallBack(Boolean isVerify);
    }


}
