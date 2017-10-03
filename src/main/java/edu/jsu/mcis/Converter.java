package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import au.com.bytecode.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
	
    /*
        Consider a CSV file like the following:
        
        ID,Total,Assignment 1,Assignment 2,Exam 1
        111278,611,146,128,337
        111352,867,227,228,412
        111373,461,96,90,275
        111305,835,220,217,398
        111399,898,226,229,443
        111160,454,77,125,252
        111276,579,130,111,338
        111241,973,236,237,500
        
        The corresponding JSON file would be as follows (note the curly braces):
        
        {
            "colHeaders":["Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160","111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }  
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString){
		CSVReader reader = new CSVReader(new StringReader(csvString));
		List<String[]> data = new ArrayList();
		try{
			data = reader.readAll();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		String jsonString = "{\n    \"colHeaders\":[";
		int colHeaders = data.get(0).length;
		for(int i=0; i<colHeaders; i++){
			jsonString += "\"" + data.get(0)[i] + "\"";
			if(i < colHeaders-1){
				jsonString += ",";
			}
		}
		jsonString += "],\n    ";
		
		jsonString += "\"rowHeaders\":[";
		int rowHeaders = data.size();
		for(int i=1; i<rowHeaders; i++){
			jsonString += "\"" + data.get(i)[0] + "\"";
			if(i < rowHeaders-1){
				jsonString += ",";
			}
		}
		jsonString += "],\n    ";
		
		jsonString += "\"data\":[";
		for(int i=1; i<rowHeaders; i++){
			jsonString += "[";
			for(int j=1; j<colHeaders; j++){
				jsonString += data.get(i)[j];
				if(j < colHeaders-1){
					jsonString += ",";
				}
			}
			if(i < rowHeaders-1){
				jsonString += "],\n            ";
			}
		}
		jsonString += "]\n    ]\n}";
		
        return jsonString;
    }
    
    public static String jsonToCsv(String jsonString){
        JSONParser parser = new JSONParser();
		String csvString = "";
		
		try{
			Object obj = parser.parse(jsonString);
			JSONObject jsonObject = (JSONObject) obj;
			
			JSONArray headers = (JSONArray) jsonObject.get("colHeaders");
			for(int i=0; i<headers.size(); i++){
				csvString += "\"" + headers.get(i) + "\"";
				if(i < headers.size()-1){
					csvString += ",";
				}
			}
			csvString += "\n";
			
			JSONArray ids = (JSONArray) jsonObject.get("rowHeaders");
			
			JSONArray data = (JSONArray) jsonObject.get("data");
			for(int i=0; i<ids.size(); i++){
				csvString += "\"" + ids.get(i) + "\",";
				JSONArray grades = (JSONArray) data.get(i);
				for(int j=0; j<grades.size(); j++){
					csvString += "\"" + grades.get(j) + "\"";
					if(j < grades.size()-1){
						csvString += ",";
					}
				}
				csvString += "\n";
			}
		}
		catch(ParseException e){
			e.printStackTrace();
		}
		
		return csvString;
    }
}