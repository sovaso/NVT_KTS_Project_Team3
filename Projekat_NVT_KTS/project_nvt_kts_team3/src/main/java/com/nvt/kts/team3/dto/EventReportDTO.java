package com.nvt.kts.team3.dto;

import java.util.ArrayList;

public class EventReportDTO {
	private ArrayList<String> dailyLabels = new ArrayList<>();
	private ArrayList<Integer> dailyValues = new ArrayList<>();
	private ArrayList<String> weeklyLabels = new ArrayList<>();
	private ArrayList<Integer> weeklyValues = new ArrayList<>();
	private ArrayList<String> monthlyLabels = new ArrayList<>();
	private ArrayList<Integer> monthlyValues = new ArrayList<>();
	
	public EventReportDTO() {
		super();
	}
	public EventReportDTO(ArrayList<String> dailyLabels, ArrayList<Integer> dailyValues,
			ArrayList<String> weeklyLabels, ArrayList<Integer> weeklyValues, ArrayList<String> monthlyLabels,
			ArrayList<Integer> monthlyValues) {
		super();
		this.dailyLabels = dailyLabels;
		this.dailyValues = dailyValues;
		this.weeklyLabels = weeklyLabels;
		this.weeklyValues = weeklyValues;
		this.monthlyLabels = monthlyLabels;
		this.monthlyValues = monthlyValues;
	}
	public ArrayList<String> getDailyLabels() {
		return dailyLabels;
	}
	public void setDailyLabels(ArrayList<String> dailyLabels) {
		this.dailyLabels = dailyLabels;
	}
	public ArrayList<Integer> getDailyValues() {
		return dailyValues;
	}
	public void setDailyValues(ArrayList<Integer> dailyValues) {
		this.dailyValues = dailyValues;
	}
	public ArrayList<String> getWeeklyLabels() {
		return weeklyLabels;
	}
	public void setWeeklyLabels(ArrayList<String> weeklyLabels) {
		this.weeklyLabels = weeklyLabels;
	}
	public ArrayList<Integer> getWeeklyValues() {
		return weeklyValues;
	}
	public void setWeeklyValues(ArrayList<Integer> weeklyValues) {
		this.weeklyValues = weeklyValues;
	}
	public ArrayList<String> getMonthlyLabels() {
		return monthlyLabels;
	}
	public void setMonthlyLabels(ArrayList<String> monthlyLabels) {
		this.monthlyLabels = monthlyLabels;
	}
	public ArrayList<Integer> getMonthlyValues() {
		return monthlyValues;
	}
	public void setMonthlyValues(ArrayList<Integer> monthlyValues) {
		this.monthlyValues = monthlyValues;
	}


}
