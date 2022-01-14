package presentation;

import business.Poll;
import business.PollManager;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.*;

import business.Choice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DownloadResultsServlet extends HttpServlet {


    private final int MAX_DOWNLOAD_SIZE = 2096;
    
    /**
     * Will first 
     *
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
	System.out.println("Downloading ... ");

	//Initializes needed variables
	PollManager PM = new PollManager();
	String pollID = (String)request.getSession().getAttribute("pollID");
	HashMap<String, HashMap<String, String>> pollInfo = PM.getPoll(pollID);
	HashMap<String, Integer> results = PM.pollResults(pollID);
	String fileName = pollInfo.get(pollID).get("PollName") + "-" + PM.getReleasedTime(pollID)+"."+request.getParameter("file_format");
	String fileContents = "";
	ArrayList<HashMap<String, String>> choices = PM.getChoices(pollID);
	int t = 0;
	
	String fileFormat = request.getParameter("file_format");

	switch(fileFormat){
	    /*
	      saving xml in the following sort of format:
	      <POLL>
	        <NAME></NAME>
	        <QUESTION></QUESTION>
		<CHOICE>
		  <OPTION></OPTION>
		  <DESCRIPTION></DESCRIPTION>
		  <VOTES></VOTES>
		</CHOICE>
		<TOTALVOTES></TOTALVOTES>
	      </POLL>
	    */
	case "xml":{
	    fileContents += "<POLL>\n"+
		"\t<QUESTION>\n"+
		  "\t\t"+pollInfo.get(pollID).get("Question")+"\n"+
		"\t</QUESTION>\n";
	    for (int i = 0; i < choices.size(); i++){
		int votes = 0;
		
		if (choices.get(i).get("Votes") != null)
		    votes = Integer.parseInt(choices.get(i).get("Votes"));
		t += votes;
		fileContents += "\t<CHOICE>\n";
		fileContents += "\t\t<OPTION>"+
		    choices.get(i).get("Option")+
		    "</OPTION>\n";
		fileContents += "\t\t<DESCRIPTION>"+
		    choices.get(i).get("Description")+
		    "</DESCRIPTION>\n";
		fileContents += "\t\t<VOTES>"+
		    votes+
		    "</VOTES>\n";
		fileContents +="\t</CHOICE>\n";
	    }
	    fileContents += "\t<TOTALVOTES>"+t+"</TOTALVOTES>\n";
	    fileContents += "</POLL>";
	}break;
	case "json":{
	    /*
	      saving json in the following sort of format:
	      {
	      "PollName" : name:str,
	      "Question" : question:str,
	      "choices" : {
	        Option:str : {
		  "Description" : description:str,
		  "Votes" : votes:int
		},
		...
	      },
	      "TotalVotes" : votes:int
	      }
	    */
	    fileContents += "{ \n"+
		"\"PollName\" : \""+pollInfo.get(pollID).get("PollName")+"\",\n"+
		"\"Question\" : \""+pollInfo.get(pollID).get("Question")+"\",\n";
	    fileContents += "\"Choices\": {\n";
	    for (int i = 0; i < choices.size(); i++){
		int votes = 0;
		if (choices.get(i).get("Votes") != null)
		    votes = Integer.parseInt(choices.get(i).get("Votes"));
		t += votes;
		
		fileContents += "\t\""+choices.get(i).get("Option")+"\" : {\n";
		fileContents += "\t\t \"Description\" : \""+choices.get(i).get("Description")+ "\",\n";
		fileContents += "\t\t\"Votes\" : "+ votes +"\n\t}";
		if (i+1 < choices.size())
		    fileContents += ",\n";
		else
		    fileContents += "\n";
	    }
	    fileContents += "},\n"+
		"\"TotalVotes\" : "+t
		+"\n}";
	}break;
	default:{ //Text Files
	    //Sets up header of the file to be downloaded
	    fileContents += "Poll: "+pollInfo.get(pollID).get("PollName")+"\n";
	    fileContents += "--------------------------------------\n\n";
	    fileContents += "Question: "+pollInfo.get(pollID).get("Question")+"\n";
	    for (int i = 0; i < choices.size(); i++){
		int votes = 0;
		if (choices.get(i).get("Votes") != null)
		    votes = Integer.parseInt(choices.get(i).get("Votes"));
		t += votes;
		
		fileContents += "Option "+i+": "+choices.get(i).get("Option")+"\n";
		fileContents += "Description: "+choices.get(i).get("Description")+ "\n";
		fileContents += "Votes: "+ votes +"\n\n";
				
		
	    }

	fileContents += "Total Votes: " + t;
	

	}
	    
	}
	/*
	 * For each question in the poll, it will add it to the file string to be
	 * outputted
	 */
	System.out.println(fileName);

	// adds to the response type the file attatchment with its content
	response.setContentType("text/plain");
	response.setHeader("Content-disposition", "attachment; filename=\""+fileName+"\"");

	/*
	 * Will set up an input and output stream, then take the String of prepared text
	 * and convert it to a byte stream and feed it throug hthe buffer. the output stream
	 * will then be fed through the response object to then be downloaded
	 */
	try {
	    InputStream in = new ByteArrayInputStream(fileContents.getBytes());
	    OutputStream out = response.getOutputStream();

	    byte[] buffer = new byte[MAX_DOWNLOAD_SIZE];

	    int bytesRead;

	    while ((bytesRead = in.read(buffer)) > 0){
		out.write(buffer, 0, bytesRead);
	    }
	    // flushes the streams
	    try{

		out.flush();
		in.close();
		out.close();
	    }catch(IOException e){
	    
	    }
	}
	catch(IOException e){
	    
	}
    }
}
