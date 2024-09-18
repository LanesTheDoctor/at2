/*
    File name:  Competition.java
    Purpose:    Class template for competition data (Gold Coast E-Sports app)
    Author:     
    Date:       
    Version:    1.0
    NOTES: 
*/
package gc_esports_gui;


public class Competition 
{
    // private data
    // League of Legends,14-Jan-2023,TAFE Coomera,BioHazards,2
    private String game;
    private String competitionDate;
    private String location;
    private String team;
    private int points;
    
    // constructor method
    public Competition (String game, String competitionDate, String location, String team, int points)
    {
        this.game = game;
        this.competitionDate = competitionDate;
        this.location = location;
        this.team = team;
        this.points = points;
    }
    //  get methods for each private data field
    public String getGame()
    {
        return game;
    }
    public String getCompetitionDate()
    {
        return competitionDate;
    }
    public String getLocation()
    {
        return location;
    }
    public String getTeam()
    {
        return team;
    }
    public int getPoints()
    {
        return points;
    }
    // set methods for each private data field
    public void setGame(String game)
    {
        this.game = game;
    }
    public void setCompetitionDate(String competitionDate)
    {
        this.competitionDate = competitionDate;
    }
    public void setLocation(String location)
    {
        this.location = location;
    }
    public void setTeam(String team)
    {
        this.team = team;
    }
    public void setPoints(int points)
    {
        this.points = points;
    }
    
    @Override
    public String toString()
    {
        // League of Legends,14-Jan-2023,TAFE Coomera,BioHazards,2
        return game + "," + competitionDate + "," + location + "," + team + "," + points;
    }
    
}
