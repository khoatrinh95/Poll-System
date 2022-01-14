package business;

import java.util.ArrayList;
import java.util.HashMap;

public class Ballot {
	private ArrayList<Vote> votes;
    
    public Ballot(){
		this.votes= new ArrayList<>();
    }
    /**
       will add a vote object
    */
    public boolean submit(Vote vote){
	if (!didVote(vote.getParticipant())){
	    votes.add(vote);
	    return true;
	}
	return false;
    }

    /**
        will clear vote list
     */
    public void clearVotes(){
		votes.clear();
    };

    /**
     * check if a participant has already voted
     * @param participant
     * @return true if participant has already voted, false otherwise
     */
    public boolean didVote(String participant){
		for (Vote v : votes) {
			if (v.getParticipant().equalsIgnoreCase(participant))
				return true;
		}
		return false;
    };

    /**
     *
     * @return the results in a hashmap with the choice as key, and vote count as value
     */
    public HashMap<String, Integer> getResults(){
        HashMap<String, Integer> results = new HashMap<>();
        for (Vote vote : votes){
            String choice = vote.getChoice();
            if (!results.containsKey(choice)){
                results.put(choice,1);
            }
            else {
                int currentCount = results.get(choice);
                results.put(choice, ++currentCount);
            }
        }
        return results;
    }
}
