package business;
import Exceptions.WrongStateException;
import com.mifmif.common.regex.Generex;
import db.DataConn;


import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PollManager extends VotingUser{
    String managerId = "";

    public PollManager() {
    }

    public PollManager(String managerId) {
        this.managerId = managerId;
    }

    /**
     * Create a new poll when pollStatus is null, otherwise throw WrongStateException
     * @param name
     * @param question
     * @param choices
     * @return pollID
     * @throws WrongStateException
     */
    public String createPoll(String managerId, String name, String question, List<String> choices, List<String> descriptions) {
        HashMap<String, String> options = choiceListToHashMap(choices, descriptions);
        String now = LocalDate.now().toString();
        String pollId = "";
        boolean duplicateID = false;
        do {
            // generate pollID
            pollId = generatePollId();
            try {
                DataConn dataConn = new DataConn();
                dataConn.insertPoll(pollId, name, question, managerId, "CREATED",now, options);
                duplicateID = false;
            } catch (SQLException e) {
                // if pollID is not unique
                if (e instanceof SQLIntegrityConstraintViolationException) {
                    duplicateID = true;
                }
                e.printStackTrace();
            }
        } while (duplicateID);
        return pollId;
    };

    /**
     * Update poll when pollStatus is either created or running, otherwise throw WrongStateException
     * @param name
     * @param question
     * @param choices
     * @throws WrongStateException
     */
    public void updatePoll(String pollId, String name, String question, List<String> choices, List<String> descriptions, boolean replaceChoice) throws  WrongStateException{
        Status status = getPollStatus(pollId);

        if (status == Status.created || status == Status.running){
            // clear result before update
            clearResults(pollId);

            try {
                DataConn dataConn = new DataConn();
                if (!name.isBlank())
                    dataConn.updatePollName(pollId, name);
                if (!question.isBlank())
                    dataConn.updatePollQuestion(pollId, question);
                if (choices != null) {
                    HashMap<String, String> options = choiceListToHashMap(choices, descriptions);
                    if (replaceChoice){
                        dataConn.updatePollOptions(pollId, options);
                    }
                    else {
                        dataConn.insertPollOptions(pollId,options);
                    }
                }

                dataConn.updatePollStatus(pollId, "CREATED");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        else
            throw new WrongStateException("Poll can't be updated because it's not in created or running state");
    };

    /**
     * Clear results when pollStatus is running or released, otherwise throw WrongStateException
     * @throws WrongStateException
     */
    public void clearPoll(String pollId) throws  WrongStateException{
        Status status = getPollStatus(pollId);
        if (status  == Status.running || status  == Status.released){
            clearResults(pollId);
            if (status  == Status.released) {
                try {
                    DataConn dataConn = new DataConn();
                    dataConn.updatePollStatus(pollId, "CREATED");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        else
            throw new WrongStateException("Poll can't be cleared because it's not in running or released state");
    };

    /**
     * close Poll when pollStatus is running, otherwise throw WrongStateException
     * @throws WrongStateException
     */
    public void closePoll(String pollID) throws  WrongStateException{
        Status status = getPollStatus(pollID);
        // we archive the poll info, aka only set status to CLOSED
        if (status  == Status.released) {
            try {
                DataConn dataConn = new DataConn();
                dataConn.updatePollStatus(pollID, "CLOSED");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        else
            throw new WrongStateException("Poll can't be closed because it's not in released state");
    };

    /**
     * run poll when status is created, otherwise throw WrongStateException
     * @throws WrongStateException
     */
    public void runPoll(String pollId) throws  WrongStateException{
        Status status = getPollStatus(pollId);
        if (status  == Status.created){
            try {
                DataConn dataConn = new DataConn();
                dataConn.updatePollStatus(pollId, "RUNNING");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        else
            throw new WrongStateException("Poll can't be run because it's not in created state");
    };

    /**
     * clear the Poll results
     * @throws WrongStateException
     */
    public void clearResults(String pollId){
        try {
            DataConn dataConn = new DataConn();
            dataConn.deleteVotes(pollId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     Sets the status of the current poll to being released only if it is
     in the running state. Otherwise it will send an exception
     */
    public void releasePoll(String pollId) throws  WrongStateException {
        Status status = getPollStatus(pollId);
        if (status  == Status.running){
            try {
                DataConn dataConn = new DataConn();
                dataConn.updatePollStatus(pollId, "RELEASED");
                dataConn.updatePollReleaseTime(pollId, LocalDate.now().toString());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        else
            throw new WrongStateException("The state is not in running state.");
    };
    /**
     Will set a poll currently in the released state into the created state.
     if not in the released state it will send an exception
     */
    public void unreleasePoll(String pollId) throws  WrongStateException {
        Status status = getPollStatus(pollId);
        if (status  == Status.released){
            try {
                DataConn dataConn = new DataConn();
                dataConn.updatePollStatus(pollId, "CREATED");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        else
            throw new WrongStateException("The state is not in the released state.");
    };

    public void deletePoll(String pollId) {
        try {
            DataConn dataConn = new DataConn();
            dataConn.deletePoll(pollId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public HashMap<String, HashMap<String, String>> getListOfPollsCreatedBySelf(String managerID){
        HashMap<String, HashMap<String, String>> polls = new HashMap<>();
        try {
            DataConn dataConn = new DataConn();
            polls = dataConn.getListOfPollsByManagerID(managerID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return polls;
    }


    private String generatePollId() {
        Generex generex = new Generex("[A-HJ-KM-NO-TV-Z0-9]{10}");
        String randomStr = generex.random();
        System.out.println("Generating random PollID: " + randomStr);
        return randomStr;
    }

    private HashMap<String, String> choiceListToHashMap (List<String> choices, List<String> descriptions) {
        HashMap<String, String> options = new HashMap<>();
        for (int i = 0; i<choices.size(); i++) {
            options.put(choices.get(i), descriptions.get(i));
        }
        return options;
    }
    

    // =========Methods to queryback formatted poll data======== //
    
    public String getPollInfo() {
	String info = "";
//	try {
//	    DataConn dataConn = new DataConn();
//	    info = dataConn.getPollInfo();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//	    info = "Error Connecting to DB.";
//        }
	return info;

    }

        
    public HashMap<String, HashMap<String, String>> getPoll(String pollId){
	HashMap<String, HashMap<String, String>> results  = null;
	try {
	    DataConn dataConn = new DataConn();
	    results = dataConn.getPollByID(pollId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
	return results;
    }
    
    public Status getPollStatus(String pollId){
	Status status = null;
	try {
	    DataConn dataConn = new DataConn();
	    String statusStr = dataConn.getPollStatusByID(pollId);
        switch(statusStr){
            case "CREATED":{status = status.created;}break;
            case "RUNNING":{status = status.running;}break;
            case "RELEASED":{status = status.released;}break;
            case "CLOSED":{status = status.closed;}break;
            default:
                status = null;
        }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
	return status;

    }
    
    public HashMap<String, Integer> pollResults(String pollId){
	HashMap<String, Integer> results  = null;
	try {
	    DataConn dataConn = new DataConn();
	    results = dataConn.getResults(pollId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
	return results;

    }
    
    public String getReleasedTime(String pollId){
	String time = "";
	try {
	    DataConn dataConn = new DataConn();
	    time = dataConn.getReleasedTime(pollId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
	return time;

    }
    
    public String getPollName(String pollId){
	String name = "";
	try {
	    DataConn dataConn = new DataConn();
	    name = dataConn.getPollName(pollId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
	return name;

    }

    public ArrayList<HashMap<String, String>> getChoices(String pollId){
        ArrayList<HashMap<String, String>> choices  = null;
	try {
	    DataConn dataConn = new DataConn();
	    choices = dataConn.getChoices(pollId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
	return choices;

    }
    
    // =========Methods to Update poll data======== //
    public void setPollStatus(String pollId, Status pollStatus){
	try {
	    DataConn dataConn = new DataConn();
	    dataConn.updatePollStatus(pollId, pollStatus.name());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
