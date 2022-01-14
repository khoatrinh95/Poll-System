package business;

public class Manager {
    String name;
    String password;
    boolean authorized;

    public Manager() {
    }

    public Manager(String password, boolean authorized) {
        this.password = password;
        this.authorized = authorized;
    }
    /**
     * @param description
     */
    public Manager(String name, String password, boolean authorized) {
        this.name = name;
        this.password = password;
        this.authorized = authorized;
    }
    /**
     * @return name
     */
    public String getName() {
        return name;
    }
    /**
     * @param description
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return password
     */ 
    public String getPassword() {
        return password;
    }
    /**
     * @param description
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * @return authorized
     */ 
    public boolean isAuthorized() {
        return authorized;
    }
    /**
     * @param description
     */
    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }
}
