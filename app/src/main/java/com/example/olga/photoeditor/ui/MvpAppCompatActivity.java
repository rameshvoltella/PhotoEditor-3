package com.example.olga.photoeditor.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.arellomobile.mvp.MvpDelegate;

/**
 * Date: 05.08.16
 * Time: 10:08
 *
 * @author Olga
 */
public class MvpAppCompatActivity extends AppCompatActivity {

    private MvpDelegate<? extends MvpAppCompatActivity> mMvpDelegate;

    public MvpAppCompatActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getMvpDelegate().onCreate(savedInstanceState);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.getMvpDelegate().onDetach();
        this.getMvpDelegate().onDestroy();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.getMvpDelegate().onSaveInstanceState(outState);
    }

    protected void onStart() {
        super.onStart();
        this.getMvpDelegate().onAttach();
    }

    public MvpDelegate getMvpDelegate() {
        if (this.mMvpDelegate == null) {
            this.mMvpDelegate = new MvpDelegate(this);
        }

        return this.mMvpDelegate;
    }

}
