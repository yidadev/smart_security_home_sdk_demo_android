package com.security.demosdktest.scene.event;

import com.security.demosdktest.scene.event.model.SceneUpdateConditionModel;

/**
 * create by nielev on 2019-10-29
 */
public interface SceneUpdateCondtionEvent {
    void onEvent(SceneUpdateConditionModel model);
}
