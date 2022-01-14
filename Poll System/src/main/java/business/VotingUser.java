package business;

import Exceptions.WrongStateException;
import db.DataConn;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;

public class VotingUser {
    protected Poll poll;

    public VotingUser() {
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public HashMap<String, HashMap<String, String>> getPoll(String pollId) {
        HashMap<String, HashMap<String, String>> poll = new HashMap<>();
        try {
            DataConn dataConn = new DataConn();
            poll = dataConn.getPollByID(pollId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return poll;
    }

    public HashMap<String, HashMap<String, String>> getActivePolls() {
        HashMap<String, HashMap<String, String>> poll = new HashMap<>();
        try {
            DataConn dataConn = new DataConn();
            poll = dataConn.getActivePolls();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return poll;
    }

    /**
     will submit a vote object into the ballot
     */
    public String vote(String pollId, String PIN, String choice){
        String result = PIN;
        // if no PIN is provided, generate unique pin and insert vote
        if (PIN.isBlank()) {
            boolean duplicatePIN = false;
            do {
                String pinGenerated = generatePIN();
                try {
                    DataConn dataConn = new DataConn();
                    dataConn.insertVote(pollId, choice, pinGenerated);
                    duplicatePIN = false;
                    result = pinGenerated;
                } catch (SQLException throwables) {
                    // if PIN is already given to someone else
                    if (throwables instanceof SQLIntegrityConstraintViolationException) {
                        duplicatePIN = true;
                    }
                    throwables.printStackTrace();
                }
            } while (duplicatePIN);
        } else {
            // a pin is provided -> update vote
            try {
                DataConn dataConn = new DataConn();
                int updateSuccess = dataConn.updateVote(pollId, choice, PIN);
                if (updateSuccess<1) {
                    result = "not found";
                } else {
                    result = "replaced";
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return result;
    };

    /**
     Will request from the ballet the poll results
     */
    public HashMap<String, Integer> getPollResults(String pollID) throws WrongStateException{
        Status status = getPollStatus(pollID);
        if (status == Status.released){
            HashMap<String, Integer> votesCount = new HashMap<>();
            try {
                DataConn dataConn = new DataConn();
                votesCount = dataConn.getResults(pollID);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            return votesCount;
        }else{
            throw new WrongStateException("Poll must be released first.");
        }
    };
    /**
     Will write the poll results to a file, then download it through the browser
     TODO: for Alek
     */
    public void downloadPollDetails(String pollId) throws WrongStateException{
        System.out.println("Doin a download!!");

        String fileName = poll.getName() + "-" + poll.getReleasedTime().toString()+".txt";
        HashMap<String, Integer> results = getPollResults(pollId);
        System.out.println(results);
        System.out.println(fileName);
    };

    protected Status getPollStatus(String pollId) {
        Status status = null;
        String statusString = "";
        try {
            DataConn dataConn = new DataConn();
            statusString = dataConn.getPollStatusByID(pollId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        switch (statusString){
            case "CREATED":
                status = Status.created;
                break;
            case "RUNNING":
                status = Status.running;
                break;
            case "RELEASED":
                status = Status.released;
                break;
            case "CLOSED":
                break;
        }
        return status;
    }

    public String generatePIN() {
        SecureRandom random = new SecureRandom();
        int num = random.nextInt(1000000);
        String formatted = String.format("%06d", num);
        return formatted;
    }
}
