package com.tpg.movierx;

import android.annotation.*;
import android.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import butterknife.*;
import butterknife.ButterKnife;

/**
 * All activities should extend from this
 * <p>
 * Created by ciprian.grigor on 04/11/15.
 */
public abstract class BaseActivity extends RxAppCompatActivity {

    @Bind(R.id.toolbar)
    @Nullable
    Toolbar toolbar;

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        bindViews();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        bindViews();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        bindViews();
    }

    protected void bindViews() {
        ButterKnife.bind(this);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }
}