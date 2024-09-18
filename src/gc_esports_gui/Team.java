/*
    File name:  Team.java
    Purpose:    Class template for team details (Gold Coast E-Sports app)
    Author:     Hans Telford
    Date:       18-Aug-2022
    Version:    1.0
    NOTES:      Contains a default constructor method (not used in this project)
                Contains an override toString() method (used for testing)
*/
package gc_esports_gui;


public class Team 
{
    // Coomera Bombers,James Taylor,0433948765,jamestaylor123@coolmail.com
    // private data fields
    private String teamName;
    private String contactName;
    private String contactPhone;
    private String contactEmail;
    
    // constructor method (with input parameters)
    public Team (String teamName, String contactName, String contactPhone, String contactEmail)
    {
        this.teamName = teamName;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
    }
    
    // default constructor method (no input parameters)
    public Team()
    {
        this.teamName = "Generic team";
        this.contactName = "Generic contact name";
        this.contactPhone = "55111222";
        this.contactEmail = "generic@geemail.com";
    }
    
    // get methods
    public String getTeamName()
    {
        return teamName;
    }
    
    public String getContactName()
    {
        return contactName;
    }
    
    public String getContactPhone()
    {
        return contactPhone;
    }
    
    public String getContactEmail()
    {
        return contactEmail;
    }
    
    // set method
    public void setTeamName (String teamName)
    {
        this.teamName = teamName;
    }
    
    public void setContactName (String contactName)
    {
        this.contactName = contactName;
    }
    
    public void setContactPhone (String contactPhone)
    {
        this.contactPhone = contactPhone;
    }
    
    public void setContactEmail (String contactEmail)
    {
        this.contactEmail = contactEmail;
    }
    
    // overloaded toString() method (originates from the Object class)
    @Override
    public String toString()
    {
        return teamName + "," + contactName + "," + contactEmail + "," + contactPhone;
    }
    
}
