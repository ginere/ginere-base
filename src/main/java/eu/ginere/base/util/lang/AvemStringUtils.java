package eu.ginere.base.util.lang;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class AvemStringUtils {

	public final static String HOURS_TOKEN="h";
	public final static String MINUTES_TOKEN="min";
	public final static String SECONDS_TOKEN="sec";
	public final static String MILLIS_TOKEN="ml";
	private static final String DAYS_TOKEN = "d";
	
	public static String fromLapInMillis(long timeInMillis){
		if (timeInMillis<0){
			return Long.toString(timeInMillis);
		} 
		NumberFormat TWO=new DecimalFormat("00");
		NumberFormat THREE=new DecimalFormat("000");
		
		long valSec=timeInMillis/1000;
		long millis=timeInMillis-(valSec*1000);
		
		if (valSec == 0 ){
			StringBuilder buffer=new StringBuilder();
			buffer.append(THREE.format(millis));
			buffer.append(MILLIS_TOKEN);
			
			return buffer.toString();
		}
		
		long valMin=valSec/60;
		long seconds=valSec-(valMin*60);
		
		if (valMin == 0 ){
			StringBuilder buffer=new StringBuilder();
			
			buffer.append(TWO.format(seconds));
			buffer.append(SECONDS_TOKEN);
			
			if (millis!=0){
				buffer.append(' ');
				
				buffer.append(THREE.format(millis));
				buffer.append(MILLIS_TOKEN);
			}
			
			return buffer.toString();
		}
		
		
		long valH=valMin/60;
		long minutes=valMin-(valH*60);
		
		if (valH == 0 ){
			StringBuilder buffer=new StringBuilder();
			
			buffer.append(TWO.format(minutes));
			buffer.append(MINUTES_TOKEN);
			
			if (seconds!=0){
				buffer.append(' ');
				
				buffer.append(TWO.format(seconds));
				buffer.append(SECONDS_TOKEN);
			}
			
			if (millis!=0){
				buffer.append(' ');
				
				buffer.append(THREE.format(millis));
				buffer.append(MILLIS_TOKEN);
			}
			
			return buffer.toString();
		}
		
		long valD=valH/24;
		long hours=valH-(valD*24);
		
		if (valD == 0 ){
			StringBuilder buffer=new StringBuilder();

			buffer.append(TWO.format(hours));
			buffer.append(HOURS_TOKEN);
			
			if (minutes!=0){
				buffer.append(' ');
				
				buffer.append(TWO.format(minutes));
				buffer.append(MINUTES_TOKEN);
			}
			
			if (seconds!=0){
				buffer.append(' ');
				
				buffer.append(TWO.format(seconds));
				buffer.append(SECONDS_TOKEN);
			}
			
			if (millis!=0){
				buffer.append(' ');
				
				buffer.append(THREE.format(millis));
				buffer.append(MILLIS_TOKEN);
			}

			return buffer.toString();
		}
		
		long days=valD;
		
			StringBuilder buffer=new StringBuilder();
			
			buffer.append(TWO.format(days));
			buffer.append(DAYS_TOKEN);
			
			if (hours!=0){
				buffer.append(' ');
				
				buffer.append(TWO.format(hours));
				buffer.append(HOURS_TOKEN);
			}
			
			if (minutes!=0){
				buffer.append(' ');
				
				buffer.append(TWO.format(minutes));
				buffer.append(MINUTES_TOKEN);
			}
			
			if (seconds!=0){
				buffer.append(' ');
				
				buffer.append(TWO.format(seconds));
				buffer.append(SECONDS_TOKEN);
			}
			
			if (millis!=0){
				buffer.append(' ');
				
				buffer.append(THREE.format(millis));
				buffer.append(MILLIS_TOKEN);
			}

			return buffer.toString();
	}
	
	public static String fromLapInSec(int valSec){
		NumberFormat TWO=new DecimalFormat("00");
						
		long valMin=valSec/60;
		long seconds=valSec-(valMin*60);
		
		if (valMin == 0 ){
			StringBuilder buffer=new StringBuilder();
			
			buffer.append(TWO.format(seconds));
			buffer.append(SECONDS_TOKEN);
			
			return buffer.toString();
		}
		
		
		long valH=valMin/60;
		long minutes=valMin-(valH*60);
		
		if (valH == 0 ){
			StringBuilder buffer=new StringBuilder();
			
			buffer.append(TWO.format(minutes));
			buffer.append(MINUTES_TOKEN);
			
			if (seconds!=0){
				buffer.append(' ');
				
				buffer.append(TWO.format(seconds));
				buffer.append(SECONDS_TOKEN);
			}
				
			return buffer.toString();
		}
		
		long valD=valH/24;
		long hours=valH-(valD*24);
		
		if (valD == 0 ){
			StringBuilder buffer=new StringBuilder();

			buffer.append(TWO.format(hours));
			buffer.append(HOURS_TOKEN);
			
			if (minutes!=0){
				buffer.append(' ');
				
				buffer.append(TWO.format(minutes));
				buffer.append(MINUTES_TOKEN);
			}
			
			if (seconds!=0){
				buffer.append(' ');
				
				buffer.append(TWO.format(seconds));
				buffer.append(SECONDS_TOKEN);
			}

			return buffer.toString();
		}
		
		long days=valD;
		
			StringBuilder buffer=new StringBuilder();
			
			buffer.append(TWO.format(days));
			buffer.append(DAYS_TOKEN);
			
			if (hours!=0){
				buffer.append(' ');
				
				buffer.append(TWO.format(hours));
				buffer.append(HOURS_TOKEN);
			}
			
			if (minutes!=0){
				buffer.append(' ');
				
				buffer.append(TWO.format(minutes));
				buffer.append(MINUTES_TOKEN);
			}
			
			if (seconds!=0){
				buffer.append(' ');
				
				buffer.append(TWO.format(seconds));
				buffer.append(SECONDS_TOKEN);
			}
			
			return buffer.toString();
	}
}
