package io.github.zhangsen.dao.mongodb.demo.entity;

import java.util.ArrayList;
import java.util.HashMap;

public class DailyModel {
    private HashMap<Integer,Long> dailyNum = new HashMap<>();

    private ArrayList<Integer> numArr = new ArrayList<>();

    private long lastTime;

    public HashMap<Integer, Long> getDailyNum() {
        return dailyNum;
    }

    public ArrayList<Integer> getNumArr() {
        return numArr;
    }

    public void setNumArr(ArrayList<Integer> numArr) {
        this.numArr = numArr;
    }

    public void setDailyNum(HashMap<Integer, Long> dailyNum) {
        this.dailyNum = dailyNum;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }
}
