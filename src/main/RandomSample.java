package main;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

import org.mozilla.javascript.*;


public class RandomSample {
	
	private static final String JS_VAR_GRAPH = "__DOC__";

	public static void main(String[] args) throws Exception {
		
		Scanner scanner = new Scanner(new File("tests/json-array.json"), "UTF-8");
		String fileContent = scanner.useDelimiter("\\Z").next();
		scanner.close();

		Context cx = Context.enter();
		Scriptable scope = cx.initStandardObjects();
		cx.evaluateString(scope, JS_VAR_GRAPH + " = " + fileContent, "", 1, null);
		
		NativeArray graph = (NativeArray)scope.get(JS_VAR_GRAPH, scope);
		NativeObject nodeDefinition = (NativeObject)graph.get(0);
		String nodeRepetition[] = nodeDefinition.get("type").toString().split(" ");
		String nodeRepetitionType = nodeRepetition[0];
		String nodeRepetitionParameter = nodeRepetition.length >= 2 ? nodeRepetition[1] : null;
		
		NativeArray nodeList = (NativeArray)nodeDefinition.get("data");
		System.out.println(nodeRepetitionType);
		System.out.println(nodeRepetitionParameter);

		for(int i = 0; i < nodeList.size(); i++) {
			NativeObject node = (NativeObject)nodeList.get(i);
			
			for(Entry<Object, Object> entry : node.entrySet()) {
				
				System.out.println(entry.getValue().getClass());
				
				String value = entry.getValue() == null ? "null" : entry.getValue().toString().trim();
				String key = entry.getKey().toString().trim();
				
				//JavaScript function detected...
				if(value.startsWith("function")) {
					System.out.println("XXXXX " + value);
					Object fo = cx.evaluateString(scope, "f = " + value, "", 1, null);
		            Function f = (Function)fo;
		            Object result = f.call(cx, scope, null, null);
		            System.out.println(key + ":" + Context.toString(result));
				}
				
				//Build-in function detected...
				if(value.matches("\\{\\{[^\\}]*\\}\\}")) {
					String functionParts[] = value.substring(2, value.length()-2).split(" ");
					String function = functionParts[0];
					String parameters[] = Arrays.copyOfRange(functionParts, 1, functionParts.length);
					System.out.println(key + ":" + callUserDefinedFunction(function, parameters));
				}
				
				System.out.println(key + ":" + value);
			}
			
		}
		
		Context.exit();
	}

	private static String callUserDefinedFunction(String function, String[] parameters) throws ParseException {
		switch(function) {
			case "integer":
				return randomInteger(Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]));
			case "random":
				return random(parameters);
			case "gender":
				return randomGender(parameters.length > 0 ? Double.parseDouble(parameters[0]) : -1);
			case "boolean":
				return randomBoolean(parameters.length > 0 ? Double.parseDouble(parameters[0]) : -1);
			case "date":
				return randomDate(parameters[0], parameters[1], parameters.length > 2 ? parameters[2] : null);
			default: 
				return String.format("The function '%s' is not defined!", function);
		}
	}
	
	public static String randomInteger(int min, int max) {
	    Random rand = new Random();
	    return Integer.toString(rand.nextInt((max - min) + 1) + min);
	}
	
	public static String random(String list[]) {
		int i = Integer.parseInt(randomInteger(0, list.length - 1));
		return list[i];
	}
	
	public static String randomGender(double chanceToBecomeMale) {
		Random rand = new Random();
		if(chanceToBecomeMale == -1) {
			return random(new String[] {"male", "female"});
		}
		
		return rand.nextDouble() <= chanceToBecomeMale ? "male" : "female"; 
	}
	
	public static String randomBoolean(double chanceToBecomeTrue) {
		Random rand = new Random();
		if(chanceToBecomeTrue == -1) {
			return Boolean.toString(rand.nextBoolean());
		}
		return Boolean.toString(rand.nextDouble() <= chanceToBecomeTrue);
	}
	
	public static String randomDate(String min, String max, String format) throws ParseException  {

		Date minDate = new SimpleDateFormat(format, Locale.ENGLISH).parse(min);
		Date maxDate = new SimpleDateFormat(format, Locale.ENGLISH).parse(max);
		
		long MILLIS_PER_DAY = 1000*60*60*24;
        GregorianCalendar s = new GregorianCalendar();
        s.setTimeInMillis(minDate.getTime());
        GregorianCalendar e = new GregorianCalendar();
        e.setTimeInMillis(maxDate.getTime());
        
        // Get difference in milliseconds
        long endL = e.getTimeInMillis() + e.getTimeZone().getOffset(e.getTimeInMillis());
        long startL = s.getTimeInMillis() + s.getTimeZone().getOffset(s.getTimeInMillis());
        long dayDiff = (endL - startL) / MILLIS_PER_DAY;
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(minDate);
        cal.add(Calendar.DATE, new Random().nextInt((int)dayDiff));
        
        SimpleDateFormat df = new SimpleDateFormat(format == null ? "yyyy-MM-dd" : format, Locale.ENGLISH);
		return df.format(cal.getTime());
	}


}

