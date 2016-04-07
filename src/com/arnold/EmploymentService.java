/**
 * 
 * This class houses the REST web service that receives a request from the front end, get the employee information from the Firebase database and 
 * transfer the information to the Mortgage broker services.
 * 
 * @author arnold
 */

package com.arnold;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


@Path("/request")
public class EmploymentService {
	
	//declare variables
	Connection conn;
	JSONObject resultJson;
	JSONObject responseJson;
	EmploymentInformation empInfo;
	//public static int statusCode = 0;
	
	
	
	/**
	 * This is the 'request' web service that communicates with the employer portal and the Mortgage Broker web services.
	 * It receives json formated data from the employer portal containing the employee id and the mortgage id.
	 * It then uses the employee id to get access to employee employment information stored in Firebase.
	 * Finally, it sends the result (with the mortgage id) in json format to the Mortgage Broker
	 * 
	 * @param incomingData
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEmploymentInformation(InputStream incomingData) throws IOException, JSONException{
		
		
		//variable to hold incoming data
		StringBuilder result = new StringBuilder();
		BufferedReader bReader = new BufferedReader(new InputStreamReader(incomingData));
		
		
		String line = null;
		
		while( (line = bReader.readLine()) != null){
			
			result.append(line);
			
		}
		
		//convert string builder to JSON object
		try {
			resultJson = new JSONObject(result.toString());
			//System.out.println(resultJson.getInt("studentId"));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//initialize firebase object to user data location
		Firebase ref =  new Firebase("https://employement.firebaseio.com/" + resultJson.getString("empID"));
		
		//attach a listener to read data once from Firebase
		ref.addListenerForSingleValueEvent(new ValueEventListener(){
			
			//int statusCode = 0;

			@Override
			public void onCancelled(FirebaseError arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onDataChange(DataSnapshot arg0) {
				// TODO Auto-generated method stub
				
				
				//save information into our object
				empInfo = arg0.getValue(EmploymentInformation.class);
		

				//convert object to JSON object
				try {
					
					//convert to json object
					responseJson = new JSONObject(empInfo.toString());
					
					//include mortgage id in the responseJson
					responseJson.put("Mort_id", resultJson.get("mortID"));
					
					
					//make a call to MBR
					URL url = new URL("http://ec2-54-175-127-10.compute-1.amazonaws.com:3000/broker_emp");
					
					//set connection parameters
					try {
						URLConnection connection = url.openConnection();
						connection.setDoOutput(true);
						connection.setRequestProperty("Content-Type", "application/json");
						connection.setConnectTimeout(50000);
						connection.setReadTimeout(50000);
						
						//send data to web service
						OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
						out.write(responseJson.toString());
						out.close();
						
						BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						
						StringBuilder response = new StringBuilder();
						String line2 = null;
						
						while((line2 = in.readLine()) != null){
							
							response.append(line2);
						}
						
						System.out.println(response.toString());
						
						in.close();
						
						//statusCode = 200;
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						//statusCode = 503;
						System.out.println("Post Request to MBR Failed");
						e.printStackTrace();
					}
					
					
					
					
					
				} catch (JSONException | MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				//display JSON response
				System.out.println(responseJson.toString());
				
			}
			
				
		});
			
		
		
		System.out.println("Received Information: " + result.toString() + " status code: " + 0);
		
		
		return Response.status(200).entity("{\"created\": \"yes\"}").build();
		
	}

}
