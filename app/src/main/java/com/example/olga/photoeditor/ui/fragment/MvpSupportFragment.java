package com.example.olga.photoeditor.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.arellomobile.mvp.MvpDelegate;
import com.arellomobile.mvp.MvpFragment;

/**
 * Date: 05.08.16
 * Time: 10:53
 *
 * @author Olga
 */
public class MvpSupportFragment extends Fragment {
    private Bundle mTemporaryBundle = null;
    private MvpDelegate<? extends MvpFragment> mMvpDelegate;

    public MvpSupportFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getMvpDelegate().onCreate(savedInstanceState);
    }

    public void onStart() {
        super.onStart();
        this.getMvpDelegate().onAttach();
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.getMvpDelegate().onDetach();
    }

    public void onDestroy() {
        super.onDestroy();
        this.getMvpDelegate().onDestroy();
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.getMvpDelegate().onSaveInstanceState(outState);
    }

    public MvpDelegate getMvpDelegate() {
        if(this.mMvpDelegate == null) {
            this.mMvpDelegate = new MvpDelegate(this);
        }

        return this.mMvpDelegate;
    }
}
