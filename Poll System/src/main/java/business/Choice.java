package business;

public class Choice {
    private String text;
    private String description;
    /**
     * Default Constructor
     */
    public Choice(String text, String description) {
        this.text = text;
        this.description = description;
    }
    /**
     * @return text
     */
    public String getText() {
        return text;
    }
    /**
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }
    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
