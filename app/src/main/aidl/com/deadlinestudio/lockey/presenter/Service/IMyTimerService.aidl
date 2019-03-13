// IMyTimerService.aidl
package com.deadlinestudio.lockey.presenter.Service;

// Declare any non-default types here with import statements

interface IMyTimerService {
    long getTempTargetTime();
    long getTargetTime();
    long getTotalTime();
}
