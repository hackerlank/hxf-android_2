package com.goodhappiness.utils;


import com.goodhappiness.dao.Observer;
import com.goodhappiness.dao.Subject;

import java.util.ArrayList;

public class PushNetStateUtils implements Subject {
	boolean isConnect;
	ArrayList<Observer> activityList;

	public PushNetStateUtils() {
		super();
		activityList = new ArrayList<>();
		isConnect = true;
	}

	@Override
	public void addObserver(Observer observer) {
		if (!(activityList.contains(observer)))
			activityList.add(observer);
	}

	@Override
	public void deleteObserver(Observer observer) {
		if (activityList.contains(observer))
			activityList.remove(observer);
	}

	@Override
	public void notifyObservers() {
		for (Observer observer : activityList) {
			observer.onNetStateChange(isConnect);
		}
	}

	public void giveNewMess(boolean b) {
		isConnect = b;
	}
}
