package com.zopsmart.updatehelper.interfaces;

import com.zopsmart.updatehelper.pojo.UpdateConfig;

public interface UpdateListener {
    void error(Throwable e);
    void success(UpdateConfig updateConfig);
}
