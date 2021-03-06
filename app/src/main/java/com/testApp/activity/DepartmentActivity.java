package com.testApp.activity;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.baselibrary.base.BaseActivity;
import com.baselibrary.util.ToastUtils;
import com.testApp.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 部门列表页面
 */
public class DepartmentActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.backBtn)
    AppCompatImageView backBtn;
    @BindView(R.id.rightTv)
    AppCompatTextView rightTv;
    @BindView(R.id.rightIcon)
    AppCompatImageView rightIcon;
    @BindView(R.id.departmentList)
    RecyclerView departmentList;

    @Override
    protected Integer contentView() {
        return R.layout.activity_department;
    }

    @Override
    protected void initToolBar() {

    }

    @Override
    protected void initView() {
        backBtn.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.all_department));
        rightIcon.setVisibility(View.VISIBLE);
        rightIcon.setImageResource(R.drawable.ic_add);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View view) {

    }

    @OnClick({R.id.backBtn,R.id.rightIcon})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.backBtn) {
            finish();
        }else if (view.getId()==R.id.rightIcon){
            //新建一个部门
            ToastUtils.showShortToast(this,"新建一个部门");

        }
    }
}
