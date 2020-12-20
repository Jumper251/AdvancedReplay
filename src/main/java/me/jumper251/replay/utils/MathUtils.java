package me.jumper251.replay.utils;

import java.util.List;

import java.util.Random;


public class MathUtils {

	public static int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
	public static double round(double number, double amount){				
		return Math.round(number * amount)/amount;
		
	}
	
	public static double getAverageDouble(List<Double> list){
		if(list == null) return -1;
		if(list.size() == 0) return -1;
		double avg = 0;
		for(double val : list){
			avg += val;
		}
		avg /= list.size();
		
		return round(avg, 100);
	}
	
	public static int getAverageInt(List<Integer> list){
		if(list == null) return -1;
		if(list.size() == 0) return -1;
		int avg = 0;
		for(int val : list){
			avg += val;
		}
		avg /= list.size();
		
		return avg;
	}
	
	  public static boolean isInt(String string)  {
	    try
	    {
	      Integer.parseInt(string);
	      return true; } catch (Exception ex) {
	    }
	    return false;
	  }

	  public static boolean isDouble(String string) {
	    try
	    {
	      Double.parseDouble(string);
	      return true; } catch (Exception ex) {
	    }
	    return false;
	  }
	  
}
