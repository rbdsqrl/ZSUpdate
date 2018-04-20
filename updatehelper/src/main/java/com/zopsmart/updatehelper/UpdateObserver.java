package com.zopsmart.updatehelper;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class UpdateObserver<UpdateConfig> implements Observer<UpdateConfig> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onComplete() {

    }
}
