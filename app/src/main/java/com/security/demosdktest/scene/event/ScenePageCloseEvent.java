package com.security.demosdktest.scene.event;

import com.security.demosdktest.scene.event.model.ScenePageCloseModel;

/**
 * Created by nielev on 2018/5/31.
 */

public interface ScenePageCloseEvent {
    void onEvent(ScenePageCloseModel model);
}
