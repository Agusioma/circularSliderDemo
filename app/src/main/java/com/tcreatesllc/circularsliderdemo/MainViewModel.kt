package com.tcreatesllc.circularsliderdemo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel() : ViewModel() {
    var tempValue: MutableLiveData<String> = MutableLiveData("16")
}