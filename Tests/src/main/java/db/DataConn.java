package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DataConn{
	Conn conn = null;
	Connection connection = null;

	private static final String POLLS_TABLE = "Polls";
	private static final String POLL_OPTIONS_TABLE = "PollOptions";
	private static final String USER_VOTES_TABLE = "UserVotes";
	private static final String USERS_TABLE = "Users";



    ////////////// Connectivity ////////////////
    public DataConn(){
		conn = new Conn();
		connection = Conn.getConnection();
    }

	public void closeConnection() throws SQLException {
		conn.closeConnection();
	}



    ////////////// Polls Table Operations ////////////////

    /**
     * retrieve a unique poll by the pollID and puts the poll info in a HashMap of key String and value HashMap<String, String>
     * the key is the pollID
     * the value is a HashMap that contains (PollName, Question, ManagerPinID, PollStatus, ReleaseTime)
     * @param pollID
     * @return a HashMap of key String and value HashMap<String, String>
     * @throws SQLException
     */
    public HashMap<String, HashMap<String, String>> getPollByID(String pollID) throws SQLException {

	String query = "SELECT * FROM "+ POLLS_TABLE +" WHERE PollID=?";
	PreparedStatement ps = connection.prepareStatement(query);
	ps.setString(1, pollID);
	ResultSet resultSet = ps.executeQuery();
	HashMap<String, HashMap<String, String>> result = resultSetToHashMap(resultSet);
	// Close all the connections
	ps.close();
	return result;
    }

    /**
     * return poll status given the poll ID
     * @param pollID
     * @return
     * @throws SQLException
     */
    public String getPollStatusByID(String pollID) throws SQLException {

	String query = "SELECT * FROM "+ POLLS_TABLE +" WHERE PollID=?";
	PreparedStatement ps = connection.prepareStatement(query);
	ps.setString(1, pollID);
	ResultSet resultSet = ps.executeQuery();
	HashMap<String, HashMap<String, String>> result = resultSetToHashMap(resultSet);

	
	String statusStr = result.get(pollID).get("PollStatus");
	
	// Close all the connections
	ps.close();
	return statusStr;
    }

    /**
     * retrieve a list of polls created by the managerID and puts the polls in a HashMap of key String and value HashMap<String, String>
     * the key is the pollID
     * the value is a HashMap that contains (PollName, Question, ManagerPinID, PollStatus, ReleaseTime)
     * @param managerID
     * @return a HashMap of key String and value HashMap<String, String>
     * @throws SQLException
     */
    public HashMap<String, HashMap<String, String>> getListOfPollsByManagerID(String managerID) throws SQLException {

	String query = "SELECT * FROM "+ POLLS_TABLE +" WHERE ManagerPinID=?";
	PreparedStatement ps = connection.prepareStatement(query);
	ps.setString(1, managerID);
	ResultSet resultSet = ps.executeQuery();
	HashMap<String, HashMap<String, String>> result = resultSetToHashMap(resultSet);
	// Close all the connections
	ps.close();
	return result;
    }

	public HashMap<String, HashMap<String, String>> getActivePolls() throws SQLException {

		String query = "SELECT * FROM "+ POLLS_TABLE +" WHERE PollStatus='RUNNING' OR PollStatus='RELEASED' ";
		PreparedStatement ps = connection.prepareStatement(query);
		ResultSet resultSet = ps.executeQuery();
		HashMap<String, HashMap<String, String>> result = resultSetToHashMap(resultSet);
		// Close all the connections
		ps.close();
		return result;
	}

    /**
     * insert into Poll Table a new row
     * @param pollID
     * @param pollName
     * @param question
     * @param managerID
     * @param status
     * @param Options
     * @throws SQLException
     */
    public void insertPoll(String pollID, String pollName, String question, String managerID, String status, String createTime,HashMap<String, String> Options) throws SQLException {
	String inPoll = "INSERT INTO " + POLLS_TABLE + " (PollID, PollName, Question, managerPinID, PollStatus, CreateTime) VALUES(?, ?, ?, ?, ?, ?)";

	PreparedStatement stP = connection.prepareStatement(inPoll);

	stP.setString(1, pollID);
	// Same for second parameter
	stP.setString(2, pollName);
	// Same for rest of the parameters
	stP.setString(3, question);
	stP.setString(4, managerID);
	stP.setString(5, status);
	stP.setString(6, createTime);
	stP.executeUpdate();

	// add choices
	insertPollOptions(pollID, Options);

	// Close all the connections
	stP.close();

    }

    /**
     * delete a row from the Poll Table
     * @param pollID
     * @throws SQLException
     */
    public void deletePoll (String pollID) throws SQLException {
	String inOps = "DELETE FROM " + POLLS_TABLE + " WHERE PollID=?";
	PreparedStatement stO = connection.prepareStatement(inOps);
	stO.setString(1,pollID);

	stO.executeUpdate();
    }

    /**
     * update the poll status in Poll Table
     * @param pollID
     * @param status
     * @throws SQLException
     */
    public void updatePollStatus(String pollID, String status) throws SQLException {

	String query = "UPDATE " + POLLS_TABLE + " SET PollStatus=? WHERE PollID=?";
	PreparedStatement ps = connection.prepareStatement(query);
	ps.setString(2, pollID);
	ps.setString(1, status.toUpperCase(Locale.ROOT));
	ps.executeUpdate();
	// Close all the connections
	ps.close();
    }

    /**
     * update poll name in Poll Table
     * @param pollID
     * @param name
     * @throws SQLException
     */
    public void updatePollName(String pollID, String name) throws SQLException {

	String query = "UPDATE " + POLLS_TABLE + " SET PollName=? WHERE PollID=?";
	PreparedStatement ps = connection.prepareStatement(query);
	ps.setString(2, pollID);
	ps.setString(1, name);
	ps.executeUpdate();
	// Close all the connections
	ps.close();
    }

    /**
     * update poll question in Poll table
     * @param pollID
     * @param question
     * @throws SQLException
     */
    public void updatePollQuestion(String pollID, String question) throws SQLException {

	String query = "UPDATE " + POLLS_TABLE + " SET Question=? WHERE PollID=?";
	PreparedStatement ps = connection.prepareStatement(query);
	ps.setString(2, pollID);
	ps.setString(1, question);
	ps.executeUpdate();
	// Close all the connections
	ps.close();
    }

    public void updatePollReleaseTime(String pollID, String releaseTime) throws SQLException {

	String query = "UPDATE " + POLLS_TABLE + " SET ReleaseTime=? WHERE PollID=?";
	PreparedStatement ps = connection.prepareStatement(query);
	ps.setString(2, pollID);
	ps.setString(1, releaseTime);
	ps.executeUpdate();
	// Close all the connections
	ps.close();
    }



    ////////////// PollOptions Table Operations ////////////////

    public ArrayList<String> getOptions (String pollId) throws SQLException {
	String inOps = "SELECT PollOption FROM " + POLL_OPTIONS_TABLE + " WHERE PollID=?";
	PreparedStatement stO = connection.prepareStatement(inOps);
	stO.setString(1, pollId);
	ResultSet resultSet = stO.executeQuery();

	ArrayList<String> options = new ArrayList<>();
	while (resultSet.next()) {
	    options.add(resultSet.getString(1));
	}
	return options;
    }
    /**
     * insert new options into PollOptions Table
     * @param pollID
     * @param choices
     * @throws SQLException
     */
    public void insertPollOptions (String pollID, HashMap<String, String> choices) throws SQLException {
	String inOps = "INSERT INTO " + POLL_OPTIONS_TABLE + " (PollID, PollOption, Description) VALUES (?, ?, ?)";
	PreparedStatement stO = connection.prepareStatement(inOps);
	for (Map.Entry<String, String> entry : choices.entrySet()) {
	    String key = entry.getKey();
	    String value = entry.getValue();
	    stO.setString(1, pollID);
	    stO.setString(2, key);
	    stO.setString(3,value);
	    stO.executeUpdate();
	}
	stO.close();
    }

    /**
     * update options in PollOptions Table given the pollId
     * @param pollID
     * @param choices
     * @throws SQLException
     */
    public void updatePollOptions (String pollID, HashMap<String, String> choices) throws SQLException {
	deleteOptions(pollID);

	String inOps = "INSERT INTO " + POLL_OPTIONS_TABLE + " (PollID, PollOption, Description) VALUES (?, ?, ?)";
	PreparedStatement stO = connection.prepareStatement(inOps);
	for (Map.Entry<String, String> entry : choices.entrySet()) {
	    String key = entry.getKey();
	    String value = entry.getValue();
	    stO.setString(1, pollID);
	    stO.setString(2, key);
	    stO.setString(3,value);
	    stO.executeUpdate();
	}
	stO.close();
    }

    /**
     * delete options from PollOptions Table given the pollId
     * @param pollID
     * @throws SQLException
     */
    public void deleteOptions (String pollID) throws SQLException {
	String inOps = "DELETE FROM " + POLL_OPTIONS_TABLE + " WHERE PollID=?";
	PreparedStatement stO = connection.prepareStatement(inOps);
	stO.setString(1,pollID);

	stO.executeUpdate();
	stO.close();
    }


    ////////////// UserVotes Table Operations ////////////////

    public void insertVote(String pollId, String option, String PIN) throws SQLException {
	String inOps = "INSERT INTO " + USER_VOTES_TABLE + " VALUE (?,?,?)";
	PreparedStatement stO = connection.prepareStatement(inOps);
	stO.setString(1, pollId);
	stO.setString(2, PIN);
	stO.setString(3, option);

	stO.executeUpdate();
	stO.close();
    }

    public int updateVote(String pollId, String option, String PIN) throws SQLException {
	String inOps = "UPDATE " + USER_VOTES_TABLE + " SET PollOption=? WHERE PollID=? AND PinID =?";
	PreparedStatement stO = connection.prepareStatement(inOps);
	stO.setString(2, pollId);
	stO.setString(3, PIN);
	stO.setString(1, option);

	int result = stO.executeUpdate();
	stO.close();

	return result;
    }

    /**
     * delete all votes in UserVotes Table given the pollID
     * @param pollID
     * @throws SQLException
     */
    public void deleteVotes (String pollID) throws SQLException {
	String inOps = "DELETE FROM " + USER_VOTES_TABLE + " WHERE PollID=?";
	PreparedStatement stO = connection.prepareStatement(inOps);
	stO.setString(1,pollID);

	stO.executeUpdate();
	stO.close();
    }
    ////////////// Joint info getters //////////////

    public HashMap<String, Integer> getResults (String pollId) throws SQLException {
	ArrayList<String> options = getOptions(pollId);

	HashMap<String, Integer> result = new HashMap<>();
	for (String option : options) {
	    String inOps = "SELECT COUNT(U.PinID)" +
		"FROM "+ POLL_OPTIONS_TABLE+" O " +
		"JOIN "+ USER_VOTES_TABLE+" U " +
		"ON O.PollID=U.PollID AND O.PollOption=U.PollOption " +
		"WHERE O.PollID=? AND O.PollOption=?";
	    PreparedStatement stO = connection.prepareStatement(inOps);
	    stO.setString(1, pollId);
	    stO.setString(2, option);

	    ResultSet resultSet = stO.executeQuery();
	    resultSet.next();
	    result.put(option, resultSet.getInt(1));
	}

	return result;
    }


    public String getPollInfo() throws SQLException{
	String createdQ = "SELECT count(status) " + POLLS_TABLE + " WHERE Status='CREATED'";
	String runningQ = "SELECT count(status) " + POLLS_TABLE + " WHERE Status='RUNNING'";
	String releasedQ = "SELECT count(status) " + POLLS_TABLE + " WHERE Status='RELEASED'";
	String closedQ = "SELECT count(status) " + POLLS_TABLE + " WHERE Status='CLOSED'";
	
	PreparedStatement createdPS = connection.prepareStatement(createdQ);
	PreparedStatement runningPS = connection.prepareStatement(runningQ);
	PreparedStatement releasedPS = connection.prepareStatement(releasedQ);
	PreparedStatement closedPS = connection.prepareStatement(closedQ);
	
	ResultSet created = createdPS.executeQuery();
	ResultSet running = runningPS.executeQuery();
	ResultSet released = releasedPS.executeQuery();
	ResultSet closed = closedPS.executeQuery();

	String response = "There are "+created+" polls currently in created state.\n"+
	    "There are "+running+" polls currently running.\n"+
	    "There are "+released+" polls currently released.\n"+
	    "There are "+closed+" polls currently closed.";
	return response;
	
    }

    
    public String getReleasedTime(String pollID) throws SQLException{
	String timeQuery = "SELECT ReleaseTime FROM " + POLLS_TABLE + " WHERE PollID=?";
	PreparedStatement timePS = connection.prepareStatement(timeQuery);

	timePS.setString(1, pollID);

	ResultSet rs = timePS.executeQuery();
	rs.next();
	String time = rs.getString(1);

	return time;
	
    }
    
    public String getPollName(String pollID) throws SQLException{
	String query = "SELECT PollName FROM " + POLLS_TABLE + " WHERE PollID=?";
	PreparedStatement stmt = connection.prepareStatement(query);

	stmt.setString(1, pollID);

	ResultSet rs = stmt.executeQuery();
	rs.next();

	String name = rs.getString(1);

	return name;
	
    }

        
    public ArrayList<HashMap<String, String>> getChoices(String pollID) throws SQLException{
	String query = "SELECT O.PollOption, O.Description, COUNT(U.PinID) FROM "
	    + POLL_OPTIONS_TABLE+ " O "+
	    "LEFT JOIN "+USER_VOTES_TABLE+" U "+
	    "ON O.PollID=U.PollID AND O.PollOption=U.PollOption "+
	    "WHERE O.PollID=? "+
	    "GROUP BY O.PollOption";
	PreparedStatement stmt = connection.prepareStatement(query);

	stmt.setString(1, pollID);
	
	ResultSet cs = stmt.executeQuery();

	ArrayList<HashMap<String, String>> choices = new ArrayList<>();
	while(cs.next()){
		HashMap<String, String> option = new HashMap<>();
		option.put("Option",cs.getString(1));
		option.put("Description",cs.getString(2));
		option.put("Votes",cs.getString(3));
		choices.add(option);
	}
	
	return choices;
	
    }
	////////////// Users Table Operations ////////////////
	public void insertUser (String userID, String firstName, String lastName, String email, String password, String token) throws SQLException {
		String inOps = "INSERT INTO " + USERS_TABLE + " (UserID, FName, LName, Email, HashedPassword, Token, AccStatus) VALUES (?, ?, ?, ?, ? ,?, ?)";
		PreparedStatement stO = connection.prepareStatement(inOps);
		stO.setString(1, userID);
		stO.setString(2, firstName);
		stO.setString(3, lastName);
		stO.setString(4, email);
		stO.setString(5, password);
		stO.setString(6, token);
		stO.setString(7, "NONVALIDATED");

		stO.executeUpdate();

		stO.close();
	}

	public void updateToken (String email, String token) throws SQLException {
		String inOps = "UPDATE " + USERS_TABLE + " SET Token=? WHERE Email=?";
		PreparedStatement stO = connection.prepareStatement(inOps);
		stO.setString(1, token);
		stO.setString(2, email);

		stO.executeUpdate();

		stO.close();
	}

	public String getPassword (String userID) throws SQLException {
		String query = "SELECT HashedPassword FROM " + USERS_TABLE +" WHERE UserID=?";
		PreparedStatement stmt = connection.prepareStatement(query);
		stmt.setString(1, userID);

		ResultSet rs = stmt.executeQuery();
		if (rs.next()==false){
			return null;
		} else {
			String hashedPW = rs.getString(1);
			return hashedPW;
		}
	}
	public boolean updatePassword (String userID, String newPassword) throws SQLException {
		String inOps = "UPDATE " + USERS_TABLE + " SET HashedPassword=? WHERE UserID=?";
		PreparedStatement stmt = connection.prepareStatement(inOps);
		stmt.setString(1, newPassword);
		stmt.setString(2, userID);
		int result = stmt.executeUpdate();
		stmt.close();

		if(result==1){
			return true;
		}
		return false;
	}

	public boolean updateValidate (boolean validated, String token) throws SQLException {
		String inOps = "UPDATE " + USERS_TABLE + " SET AccStatus=? WHERE Token=?";
		PreparedStatement stmt = connection.prepareStatement(inOps);
		if (validated) {
			stmt.setString(1, "VALIDATED");
		} else {
			stmt.setString(1, "NONVALIDATED");
		}
		stmt.setString(2, token);
		int result = stmt.executeUpdate();

		stmt.close();
		if(result==1){
			return true;
		}
		return false;
	}

	public String getUserIDByEmail (String email) throws SQLException {
		String query = "SELECT UserID FROM " + USERS_TABLE +" WHERE Email=?";
		PreparedStatement stmt = connection.prepareStatement(query);
		stmt.setString(1, email);

		ResultSet rs = stmt.executeQuery();
		if (rs.next()==false){
			return null;
		} else {
			String userID = rs.getString(1);
			return userID;
		}
	}

	public String getUserIDByToken (String token) throws SQLException {
		String query = "SELECT UserID FROM " + USERS_TABLE +" WHERE Token=?";
		PreparedStatement stmt = connection.prepareStatement(query);
		stmt.setString(1, token);

		ResultSet rs = stmt.executeQuery();
		if (rs.next()==false){
			return null;
		} else {
			String userID = rs.getString(1);
			return userID;
		}
	}

	public String getEmailByToken (String token) throws SQLException {
		String query = "SELECT Email FROM " + USERS_TABLE +" WHERE Token=?";
		PreparedStatement stmt = connection.prepareStatement(query);
		stmt.setString(1, token);

		ResultSet rs = stmt.executeQuery();
		if (rs.next()==false){
			return null;
		} else {
			String email = rs.getString(1);
			return email;
		}
	}

	public String getAccountStatusByToken (String token) throws SQLException {
		String query = "SELECT AccStatus FROM " + USERS_TABLE +" WHERE Token=?";
		PreparedStatement stmt = connection.prepareStatement(query);
		stmt.setString(1, token);

		ResultSet rs = stmt.executeQuery();
		if (rs.next()==false){
			return null;
		} else {
			String accStatus = rs.getString(1);
			return accStatus;
		}
	}

	public String getAccountStatusByUserID (String userID) throws SQLException {
		String query = "SELECT AccStatus FROM " + USERS_TABLE +" WHERE UserID=?";
		PreparedStatement stmt = connection.prepareStatement(query);
		stmt.setString(1, userID);

		ResultSet rs = stmt.executeQuery();
		if (rs.next()==false){
			return null;
		} else {
			String accStatus = rs.getString(1);
			return accStatus;
		}
	}

	public void deleteUser (String userID) throws SQLException {
		String query = "DELETE FROM " + USERS_TABLE +" WHERE UserID=?";
		PreparedStatement stmt = connection.prepareStatement(query);
		stmt.setString(1, userID);

		stmt.executeUpdate();

	}
    
    ////////////// Helper methods ////////////////
    /**
     * convert a ResultSet into a HashMap of polls
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private HashMap<String, HashMap<String, String>> resultSetToHashMap(ResultSet resultSet) throws SQLException {
	// return a map of (String - HashMap)
	// each key String is a poll ID
	// each value is a HashMap that contains (PollName, Question, ManagerPinID, PollStatus, ReleaseTime)

	HashMap<String, HashMap<String, String>> result = new HashMap<>();
	ResultSetMetaData rsmd = resultSet.getMetaData();
	int columnCount = rsmd.getColumnCount();

	while (resultSet.next()) {
	    HashMap<String, String> pollMap = new HashMap<>();

		for (int j = 2; j <= columnCount; j++ ) {
			pollMap.put(rsmd.getColumnName(j), resultSet.getString(j));
		}

	    result.put(resultSet.getString(1), pollMap);
	}

	return result;
    }
}
