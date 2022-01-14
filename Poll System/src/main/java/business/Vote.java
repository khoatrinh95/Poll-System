package business;

public class Vote{
    private String participant;
    private String choice;
    /**
     * 
     * @param participant, choice
     */
    public Vote(String participant, String choice){
	this.participant = participant;
	this.choice = choice;
    }
    /**
     * @return participant
     */
    public String getParticipant(){
	return this.participant;
    }
    /**
     * @return choice
     */
    public String getChoice(){
	return this.choice;
    }
    /** 
     * @param participant
     */
    public void setParticipant(String participant){
	this.participant = participant;
    }
    /**
     * @param choice
     */
    public void setChoice(String choice){
	this.choice = choice;
    }

}
