package com.testApp.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.baselibrary.base.BaseApplication;
import com.baselibrary.custom.CEditText;
import com.baselibrary.dao.db.DBUtil;
import com.baselibrary.dao.db.DbCallBack;
import com.baselibrary.listener.OnceClickListener;
import com.baselibrary.pojo.Manager;
import com.baselibrary.util.SPUtil;
import com.baselibrary.util.SoftInputKeyboardUtils;
import com.baselibrary.util.ToastUtils;
import com.baselibrary.util.dialogUtil.AppDialog;
import com.orhanobut.logger.Logger;
import com.testApp.R;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import static com.baselibrary.util.SPUtil.putHasManagerPwd;


/**
 * Created By pq
 * on 2019/9/30
 */
public class AskDialog {

    //修改可注册的最大管理员数量
    @SuppressLint("ResourceAsColor")
    public static void reviseMaxManagerNum(@NonNull Activity activity) {
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.revise_max_manager_num_view, null);
        //AppCompatTextView managerSetTitle = dialogView.findViewById(R.id.managerSetTitle);
        AppCompatImageView dismissBtn = dialogView.findViewById(R.id.dismissBtn);
        //AppCompatTextView managerTip = dialogView.findViewById(R.id.managerTip);
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

    //确认管理员密码
    public static void verifyManagerPwd(@NonNull Activity activity, ManagerPwdVerifyCallBack callBack) {
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.ask_manager_dialog_view, null);
        AppCompatTextView managerSetTitle = dialogView.findViewById(R.id.managerSetTitle);
        AppCompatButton nextBtn = dialogView.findViewById(R.id.nextBtn);
        CEditText inputPw = dialogView.findViewById(R.id.inputPw);
        AppCompatImageView dismissBtn = dialogView.findViewById(R.id.dismissBtn);
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
                    Thread.sleep(500);
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

    // TODO: 2019/10/8 逻辑还需要完善
    //展示设置管理员密码和启用人脸识别
    public static void showManagerDialog(@NonNull Activity activity, PositiveCallBack callBack) {
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.ask_manager_dialog_view, null);
        AppCompatTextView managerSetTitle = dialogView.findViewById(R.id.managerSetTitle);
        AppCompatTextView managerTip = dialogView.findViewById(R.id.managerTip);
        CEditText inputPw = dialogView.findViewById(R.id.inputPw);
        AppCompatButton nextBtn = dialogView.findViewById(R.id.nextBtn);
        LinearLayout btnParent = dialogView.findViewById(R.id.btnParent);
        LinearLayout openFaceAsk = dialogView.findViewById(R.id.openFaceAsk);
        AppCompatButton cancelBtn = dialogView.findViewById(R.id.cancelBtn);
        AppCompatButton positiveBtn = dialogView.findViewById(R.id.positiveBtn);
        AppCompatCheckBox cbOpenFace = dialogView.findViewById(R.id.cbOpenFace);
        AppCompatImageView dismissBtn = dialogView.findViewById(R.id.dismissBtn);
        managerSetTitle.setText(activity.getString(R.string.manager_set));
        nextBtn.setVisibility(View.VISIBLE);
        btnParent.setVisibility(View.GONE);
        Dialog dialog = AppDialog.gmDialog(activity, dialogView, false);
        final String[] pw1 = new String[2];
        nextBtn.setOnClickListener(new OnceClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (pw1[0] == null) {
                    String pw = inputPw.getText().toString().trim();
                    if (pw.length() < 6) {
                        ToastUtils.showSquareTvToast(
                                activity, activity.getString(com.pw.R.string.please_input_6_pw));
                        return;
                    }
                    pw1[0] = pw;
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
                        nextBtn.setVisibility(View.GONE);
                        btnParent.setVisibility(View.VISIBLE);
                        inputPw.setVisibility(View.GONE);
                        openFaceAsk.setVisibility(View.VISIBLE);
                        managerTip.setText(activity.getString(R.string.please_select_open_face));
                    } else {
                        ToastUtils.showSquareTvToast(
                                activity, activity.getString(R.string.pw_no_eq));
                        inputPw.getText().clear();
                    }
                }
            }
        });
        cbOpenFace.setOnCheckedChangeListener((compoundButton, b) -> SPUtil.putOpenFace(b));
        dismissBtn.setOnClickListener(new OnceClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                pw1[0] = null;
                pw1[1] = null;
                dialog.dismiss();
                SoftInputKeyboardUtils.hiddenKeyboard(inputPw);
            }
        });
        cancelBtn.setOnClickListener(new OnceClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                pw1[0] = null;
                pw1[1] = null;
                dialog.dismiss();
                SoftInputKeyboardUtils.hiddenKeyboard(inputPw);
            }
        });
        positiveBtn.setOnClickListener(new OnceClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (pw1[0].equals(pw1[1])) {
                    checkManagerPw(activity, pw1[0]);
                }
                if (callBack != null)
                    callBack.positiveCallBack();
                dialog.dismiss();
                SoftInputKeyboardUtils.hiddenKeyboard(inputPw);
            }
        });
    }

    //最多添加10位管理员
    private static void checkManagerPw(Activity activity, String pw) {
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
                            ToastUtils.showSingleToast(activity, activity.getString(R.string.max_manager_mun));
                            break;
                        } else {
                            if (i == result.size() - 1) {
                                saveManagerPwdToDB(activity, pw);
                            }
                        }
                    }
                } else {
                    saveManagerPwdToDB(activity, pw);
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
    private static void saveManagerPwdToDB(Activity activity, String managerPw) {
        Manager manager = new Manager();
        manager.setManage_pw(managerPw);
        DBUtil dbUtil = BaseApplication.getDbUtil();
        dbUtil.setDbCallBack(new DbCallBack<Manager>() {
            @Override
            public void onSuccess(Manager result) {
                ToastUtils.showSquareImgToast(activity,
                        activity.getString(R.string.manager_add_success),
                        ActivityCompat.getDrawable(activity, R.drawable.ic_emoje));

                putHasManagerPwd(true);
                Logger.d("管理员密码存储成功:" + result.toString());
            }

            @Override
            public void onSuccess(List<Manager> result) {

            }

            @Override
            public void onFailed() {
                ToastUtils.showSquareImgToast(activity,
                        activity.getString(R.string.manager_add_fail),
                        ActivityCompat.getDrawable(activity, R.drawable.cry_icon));
                putHasManagerPwd(false);
                Logger.d("管理员密码存储失败");
            }

            @Override
            public void onNotification(boolean result) {

            }
        }).insertAsyncSingle(manager);
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
        void positiveCallBack();
    }

    public interface ManagerPwdVerifyCallBack {
        void managerPwdVerifyCallBack(Boolean isVerify);
    }


}