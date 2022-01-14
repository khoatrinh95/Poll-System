package business;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Poll {
    private String name;
    private String question;
    private List<Choice> choices;
    private String id;
    private Status status;
    private Ballot ballot = null;
    private LocalDate createTime;
    private LocalDate releasedTime;

    public Poll() {
    }

    /**
     * Default Parameterized Constructor
     */
    public Poll(String name, String question, List<String> choicesList, List<String> descriptionsList, Status status) {
        this.name = name;
        this.question = question;
        this.status = status;
        this.createTime = LocalDate.now();
        choices = new ArrayList<>();
        for (int i =0; i<choicesList.size(); i++){
            choices.add(new Choice(choicesList.get(i), descriptionsList.get(i)));
        }
    }

    public Poll(String pollID, HashMap<String, HashMap<String,String>> pollMap, ArrayList<HashMap<String, String>> choices) {
        System.out.println("Creating a new poll object with poll id: " + pollID);
        id = pollID;
        HashMap<String, String> pollData = pollMap.get(pollID);

        if (pollData!= null){
            name = pollData.get("PollName");
            question = pollData.get("Question");
            switch(pollData.get("PollStatus")){
                case "CREATED":{status = status.created;}break;
                case "RUNNING":{status = status.running;}break;
                case "RELEASED":{status = status.released;}break;
                case "CLOSED":{status = status.closed;}break;
                default:
                    status = null;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            if (pollData.get("ReleaseTime")!= null)
                releasedTime = LocalDate.parse(pollData.get("ReleaseTime"), formatter);
            if (pollData.get("CreateTime")!= null && !pollData.get("CreateTime").equalsIgnoreCase("0000-00-00"))
                createTime = LocalDate.parse(pollData.get("CreateTime"), formatter);
        } else {
            System.err.println("pollData is null. pollID might be null");
        }

        this.choices = new ArrayList<>();
        if (choices != null) {
            for (HashMap<String, String> choice : choices) {
                Choice c = new Choice(choice.get("Option"), choice.get("Description"));
                this.choices.add(c);
            }
        } else {
            System.err.println("list of choices is null. pollID might be null");
        }

    }

    public String getId() {
        return id;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return question
     */
    public String getQuestion() {
        return question;
    }
    /**
     * @param question
     */
    public void setQuestion(String question) {
        this.question = question;
    }
    /**
     * @return choices
     */
    public List<Choice> getChoices() {
        return choices;
    }

    public Status getPollStatus() {
        return status;
    }

    public void setPollStatus(Status pollStatus) {
        this.status = status;
    }

    public Ballot getBallot() {
        return ballot;
    }

    public void setBallot(Ballot ballot) {
        this.ballot = ballot;
    }

    public LocalDate getReleasedTime() {
        return releasedTime;
    }

    public void setReleasedTime(LocalDate releasedTime) {
        this.releasedTime = releasedTime;
    }

    /**
     * replace the current choice list by the provided choice list
     * @param choicesList
     * @param descriptionsList
     */
    public void setChoices(List<String> choicesList, List<String> descriptionsList) {
        choices.clear();
        for (int i =0; i<choicesList.size(); i++){
            choices.add(new Choice(choicesList.get(i), descriptionsList.get(i)));
        }
    }

    /**
     * add the provided choice list to the current choice list
     * @param choicesList
     * @param descriptionsList
     */
    public void addChoices(List<String> choicesList, List<String> descriptionsList) {
        for (int i =0; i<choicesList.size(); i++){
            choices.add(new Choice(choicesList.get(i), descriptionsList.get(i)));
        }
    }
}
