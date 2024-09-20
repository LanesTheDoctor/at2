/*
    File name:  GC_ESports_GUI.java
    Purpose:    Runs the GUI JFrame app
    Author:     
    Date:       
    Version:    1.0
    NOTES:      
    
    Required functionalities:
    1.  APPLICATION LAUNCHED
    1.1 Display read-in competition results in JTable
    1.2 Display read-in team names in 2 JComboBoxes
    1.3 Display team info for first team name in Update panel

    2.  ADD NEW COMPETITION RESULT
    2.1 Add a new (validated) competition result
        to ArrayList<Competition> competitions
        and display in JTable

    3.  ADD NEW TEAM
    3.1 Add a new (validated) team                      
        to ArrayList<Team> teams
        and add to the 2 JComboBoxes

    4.  UPDATE AN EXISTING TEAM
    4.1 Update details for a selected (existing) team
        Validate changes to person, phone, email and player names
        and change values in ArrayList<Team> for the specific team

    5.  DISPLAY TOP TEAMS WITH LEADERBOARD
    5.1 Calculate total points for each of the teams
    5.2 List the teams and the total points in total points descending order

    6.  APPLICATION CLOSING AND SAVING OF DATA
    6.1 When application is closed, provide option for user
        to save changes (competition results, teams)
    6.2 Save (write) to 2 external csv files the data from:
        ArrayList<Competition> competitions
        ArrayList<Team> teams

*/
package gc_esports_gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


public class GC_ESports_GUI extends javax.swing.JFrame 
{
    public static void main(String[] args)
        {

        }

    // private data
    // for storing comp results
    private ArrayList<Competition> competitions;
    // for storing team data
    private ArrayList<Team> teams;
    // boolean to track the item selection changes to the JComboBoxes
    boolean comboBoxStatus;
    
    // for customising the JTable (which display comp results)
    private DefaultTableModel compResultsTableModel;

    //*********** CONSTRUCTOR METHOD **********************//
    public GC_ESports_GUI() 
    {
        /*********** 1. INITIALISE PRIVATE DATA FIELDS ***********/
        competitions = new ArrayList<Competition>();
        teams = new ArrayList<Team>();
        comboBoxStatus = false;
        compResultsTableModel = new DefaultTableModel();
        
        /*********** 2. CUSTOMISE TABLE MODEL ***********/
        // customised column names for movie JTable
        String [] columnNames_Results = new String[] {"Date", "Location", "Game", "Team", "Points"};
        // set up customisation
        compResultsTableModel.setColumnIdentifiers(columnNames_Results);
        
        
        /*********** INITIALISE ALL SWING CONTROLS ***********/
        initComponents();
        
        /*********** CUSTOMISE TABLE COLUMNS IN JTABLE ***********/
        resizeTableColumns();
        
        /*********** READ IN EXTERNAL CSV FILES ***********/
        readCompetitionData();
        readTeamData();
        
        /*********** DISPLAY COMPETITION DATE IN JTABLE ***********/
        displayJTable();
        
        /*********** DISPLAY TEAM NAMES IN COMBO-BOXES ************/
        displayTeams();
        
        /*********** DISPLAY TEAM DETAILS IN PANELS WITH TEAM COMBO BOXES **********/
        displayTeamDetails();
        
        
        comboBoxStatus = true;
    }
    
    /*******************************************************************
    Method:     displayJTable()
    Purpose:    display ArrayList competitions in JTable
    Inputs:     void
    Outputs:    void
    *******************************************************************/
    private void displayJTable()
    {
        // populate competition data into JTable
        if (competitions.size() > 0)
        {
            // create Object[] 2D array for JTable
            Object[][] competitions2DArray = new Object[competitions.size()][];
            // populate 2D array from competitions ArrayList
            for (int i = 0; i < competitions.size(); i++)
            {
                // create Object[] for single row of data containing 6 components
                Object[] competition = new Object[5];
                // date
                competition[0] = competitions.get(i).getCompetitionDate();
                // location
                competition[1] = competitions.get(i).getLocation();
                // game
                competition[2] = competitions.get(i).getGame();
                // team
                competition[3] = competitions.get(i).getTeam();
                // points
                competition[4] = competitions.get(i).getPoints();
                // append to 2D array
                competitions2DArray[i] = competition;
            }
            
            // first, remove all existing rows if there are any
            if (compResultsTableModel.getRowCount() > 0)
            {
                for (int i = compResultsTableModel.getRowCount() - 1; i > -1; i--) 
                {
                    compResultsTableModel.removeRow(i);
                }
            }
            
            // next, put new set of row data
            if (competitions2DArray.length > 0)
            {
                // add data to tableModel
                for (int row = 0; row < competitions2DArray.length; row++)
                {
                    compResultsTableModel.addRow(competitions2DArray[row]);
                }
                // update
                compResultsTableModel.fireTableDataChanged();
            }
            
        }
    }
    
    /*******************************************************************
    Method:     readCompetitionData()
    Purpose:    read in external CSV file data for competitions ArrayList
    Inputs:     void
    Outputs:    void
    *******************************************************************/
    private void readCompetitionData()
    {
        try
        {
            // 1. Create reader and designate external file to read from
            FileReader reader = new FileReader("competitions.csv");
            // 2. Create bufferedReader which uses the reader object
            BufferedReader bufferedReader = new BufferedReader(reader);
            // 3. Declare line string (used to read and store each 
            //    line read from the external file)
            String line;
            // 4. Loop through each line in the external file 
            //    until the EOF (end of file)
            while ((line = bufferedReader.readLine()) != null)
            {
                //System.out.println(line);
                // Check if line is not empty and contains something
                if (line.length() > 0)
                {
                    // split the line by its delimiter comma 
                    // and set up each value in the lines array
                    // League of Legends,14-Jan-2022,TAFE Coomera,BioHazards,5
                    String [] lineArray = line.split(",");
                    // set up individual variables for each split line component
                    String game = lineArray[0];
                    String location = lineArray[1];
                    String compDate = lineArray[2];
                    String team = lineArray[3];
                    int points = Integer.parseInt(lineArray[4]);
                    // create Competition instance
                    Competition comp = new Competition(game, compDate, location, team, points);
                    // add instance to ArrayList
                    competitions.add(comp);
                }
            }
            
            // 5. Close the reader object
            reader.close();
        }
        catch (FileNotFoundException fnfe)
        {
            // catch any file not found exception
            System.out.println("ERROR: competitions.csv file not found!");
        }
        catch (IOException ioe)
        {
            // catch any File IO-related exception
            System.out.println("ERROR: competitions.csv file found, but there is a read problem!");
        }
        catch (NumberFormatException nfe)
        {
            System.out.println("ERROR: Number format exception - trying to covert a string to an integer!");
        }
    }
    
    /*******************************************************************
    Method:     readTeamData()
    Purpose:    read in external CSV file data for teams teams ArrayList
    Inputs:     void
    Outputs:    void
    *******************************************************************/
    // read in team data from external csv files
    private void readTeamData()
    {
        // read external file of teams.csv
        try
        {
            // 1. Create reader and designate external file to read from
            FileReader reader = new FileReader("teams.csv");
            // 2. Create bufferedReader which uses the reader object
            BufferedReader bufferedReader = new BufferedReader(reader);
            // 3. Declare line string (used to read and store each 
            //    line read from the external file)
            String line;
            // 4. Loop through each line in the external file 
            //    until the EOF (end of file)
            while ((line = bufferedReader.readLine()) != null)
            {
                //System.out.println(line);
                // Check if line is not empty and contains something
                if (line.length() > 0)
                {
                    // split the line by its delimiter comma 
                    // and set up each value in the lines array
                    // Coomera Bombers,James Taylor,0433948765,jamestaylor123@coolmail.com
                    String [] lineArray = line.split(",");
                    // set up individual variables for each split line component
                    String teamName = lineArray[0];
                    String contactName = lineArray[1];
                    String contactPhone = lineArray[2];
                    String contactEmail = lineArray[3];
                    // create Team instance
                    Team team = new Team(teamName, contactName, contactPhone, contactEmail);
                    // add instance to ArrayList<Team> teams
                    teams.add(team);
                }
            }
            
            // 5. Close the reader object
            reader.close();
        }
        catch (FileNotFoundException fnfe)
        {
            // catch any file not found exception
            System.out.println("ERROR: teams.csv file not found!");
        }
        catch (IOException ioe)
        {
            // catch any File IO-related exception
            System.out.println("ERROR: teams.csv file found, but there is a read problem!");
        }

    }
    
    /*******************************************************************
    Method:     saveCompetitionData()
    Purpose:    saves (writes) the ArrayList of competitions 
                to external CSV file (competitions.csv)
    Inputs:     void
    Outputs:    void
    *******************************************************************/
    private void saveCompetitionData()
    {
        // TO DO
        
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

        try 
        {
            fileWriter = new FileWriter("competitions.csv");
            bufferedWriter = new BufferedWriter(fileWriter);

            // Assuming `competitions` is a List of Competition objects
            for (Competition competition : competitions) 
            {
                bufferedWriter.write(competition.toCSVString());
                bufferedWriter.newLine();
            }
        }

        catch (IOException e) 
        {
            JOptionPane.showMessageDialog(null, "Error saving competition data: " + e.getMessage());
        }

        finally 
        {
            try 
            {
                if (bufferedWriter != null) bufferedWriter.close();
                if (fileWriter != null) fileWriter.close();
            }
            catch (IOException ex) 
            {
                JOptionPane.showMessageDialog(null, "Error closing file writer: " + ex.getMessage());
            }
        }

    }
    
    /*******************************************************************
    Method:     saveTeamData()
    Purpose:    saves (writes) the ArrayList of teams 
                to external CSV file (competitions.csv)
    Inputs:     void
    Outputs:    void
    *******************************************************************/
    private void saveTeamData()
    {
        // TO DO

        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

        try 
        {
            fileWriter = new FileWriter("teams.csv");
            bufferedWriter = new BufferedWriter(fileWriter);

            // Assuming `teams` is a List of Team objects
            for (Team team : teams) 
            {
                bufferedWriter.write(team.toCSVString());
                bufferedWriter.newLine();
            }
        }

        catch (IOException e) 
        {
            JOptionPane.showMessageDialog(null, "Error saving team data: " + e.getMessage());
        } 

        finally 
        {
            try 
            {
                if (bufferedWriter != null) bufferedWriter.close();
                if (fileWriter != null) fileWriter.close();
            } catch (IOException ex) 

            {
                JOptionPane.showMessageDialog(null, "Error closing file writer: " + ex.getMessage());
            }
        }

    }
    
    /*******************************************************************
    Method:     displayTeams()
    Purpose:    displays the teams ArrayList of team data
                (team name only) to 2 JComboBoxes
    Inputs:     void
    Outputs:    void
    *******************************************************************/
    private void displayTeams()
    {
        // populate the team names from teams ArrayList<Team>
        // into the 2 JComboBoxes
        // newCompResult_JComboBox
        // updateTeam_JComboBox
        // checks if there are items in the combo boxes
        // if any, remove them
        if (newCompResult_JComboBox.getItemCount() > 0)
        {
            newCompResult_JComboBox.removeAllItems();
        }
        
        if (updateTeam_JComboBox.getItemCount() > 0)
        {
            updateTeam_JComboBox.removeAllItems();
        }
        
        if (teams.size() > 0)
        {
            for (int i = 0; i < teams.size(); i++)
            {
                newCompResult_JComboBox.addItem(teams.get(i).getTeamName());
                updateTeam_JComboBox.addItem(teams.get(i).getTeamName());
            }
        }
    }
    
    /*******************************************************************
    Method:     displayTeamDetails()
    Purpose:    displays the team details (contact name, phone, email)
                from ArrayList of team data to the Update Team text fields
    Inputs:     void
    Outputs:    void
    *******************************************************************/
    private void displayTeamDetails()
    {
        int itemIndexSelected = 0;
        if (comboBoxStatus == true)
        {
            itemIndexSelected = updateTeam_JComboBox.getSelectedIndex();
            if (itemIndexSelected < 0)
            {
                itemIndexSelected = 0;
            }
        }
         
        updateContactPerson_JTextField.setText(teams.get(itemIndexSelected).getContactName());
        updateContactPhone_JTextField.setText(teams.get(itemIndexSelected).getContactPhone());
        updateContactEmail_JTextField.setText(teams.get(itemIndexSelected).getContactEmail());
        
    }
    
    /*******************************************************************
    Method:     resizeTableColumns()
    Purpose:    sets up customised widths for a JTable
    Inputs:     void
    Outputs:    void
    *******************************************************************/
    private void resizeTableColumns()
    {
        // Columns: Date, Location, Competition, Platform, Team, Points
        // (total numeric values must = 1)
        double[] columnWidthPercentage = {0.2f, 0.2f, 0.3f, 0.2f, 0.1f};
        int tableWidth = compResults_jTable.getWidth();
        TableColumn column;
        TableColumnModel tableColumnModel = compResults_jTable.getColumnModel();
        int cantCols = tableColumnModel.getColumnCount();
        for (int i = 0; i < cantCols; i++) 
        {
            column = tableColumnModel.getColumn(i);
            float pWidth = Math.round(columnWidthPercentage[i] * tableWidth);
            column.setPreferredWidth((int)pWidth);
        }
    }
	

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        header_jPanel = new javax.swing.JPanel();
        image_jLabel = new javax.swing.JLabel();
        body_jPanel = new javax.swing.JPanel();
        bodyjTabbedPane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        compResults_jTable = new javax.swing.JTable();
        compResults_jLabel = new javax.swing.JLabel();
        displayTopTeams_jButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        titleCompetitions_JLabel = new javax.swing.JLabel();
        addDate_JLabel = new javax.swing.JLabel();
        newCompDate_JTextField = new javax.swing.JTextField();
        addLocation_JLabel = new javax.swing.JLabel();
        newCompLocation_JTextField = new javax.swing.JTextField();
        addGame_JLabel = new javax.swing.JLabel();
        newCompGame_JTextField = new javax.swing.JTextField();
        addNewCompTeam_JLabel = new javax.swing.JLabel();
        newCompResult_JComboBox = new javax.swing.JComboBox<>();
        addNewCompPoints_JLabel = new javax.swing.JLabel();
        newCompPoints_JTextField = new javax.swing.JTextField();
        addNewCompResult_jButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        titleAddNewTeam_JLabel1 = new javax.swing.JLabel();
        newTeamName_JLabel1 = new javax.swing.JLabel();
        newTeamName_JTextField = new javax.swing.JTextField();
        newContactPerson_JLabel1 = new javax.swing.JLabel();
        newContactPerson_JTextField = new javax.swing.JTextField();
        newContactPhone_JLabel1 = new javax.swing.JLabel();
        newContactPhone_JTextField = new javax.swing.JTextField();
        newContactEmail_JLabel1 = new javax.swing.JLabel();
        newContactEmail_JTextField = new javax.swing.JTextField();
        addNewTeam_jButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        addResultTitle_JLabel = new javax.swing.JLabel();
        AddResultTeam_JLabel = new javax.swing.JLabel();
        updateTeam_JComboBox = new javax.swing.JComboBox<>();
        updateContactPerson_JLabel = new javax.swing.JLabel();
        updateContactPerson_JTextField = new javax.swing.JTextField();
        updateContactPhone_JLabel = new javax.swing.JLabel();
        updateContactPhone_JTextField = new javax.swing.JTextField();
        updateContactEmail_JLabel = new javax.swing.JLabel();
        updateContactEmail_JTextField = new javax.swing.JTextField();
        updateExistingTeam_jButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        header_jPanel.setBackground(new java.awt.Color(255, 255, 255));

        image_jLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/GoldCoastESports_Header.jpg"))); // NOI18N

        javax.swing.GroupLayout header_jPanelLayout = new javax.swing.GroupLayout(header_jPanel);
        header_jPanel.setLayout(header_jPanelLayout);
        header_jPanelLayout.setHorizontalGroup(
            header_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(image_jLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        header_jPanelLayout.setVerticalGroup(
            header_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(image_jLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        body_jPanel.setBackground(new java.awt.Color(255, 255, 255));

        bodyjTabbedPane.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        compResults_jTable.setModel(compResultsTableModel);
        jScrollPane1.setViewportView(compResults_jTable);

        compResults_jLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        compResults_jLabel.setText("Team Competition Results");

        displayTopTeams_jButton.setText("Display top teams");
        displayTopTeams_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                displayTopTeams_jButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(displayTopTeams_jButton)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(compResults_jLabel)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 745, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(compResults_jLabel)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(displayTopTeams_jButton)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        bodyjTabbedPane.addTab("Team competition result", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        titleCompetitions_JLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        titleCompetitions_JLabel.setText("Add new competition result");

        addDate_JLabel.setText("Date:");

        addLocation_JLabel.setText("Location:");

        addGame_JLabel.setText("Game:");

        addNewCompTeam_JLabel.setText("Team:");

        newCompResult_JComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        addNewCompPoints_JLabel.setText("Points");

        newCompPoints_JTextField.setText("0");

        addNewCompResult_jButton.setText("ADD NEW COMPETITION RESULT");
        addNewCompResult_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewCompResult_jButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleCompetitions_JLabel)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(addNewCompPoints_JLabel)
                                .addComponent(addNewCompTeam_JLabel))
                            .addGap(53, 53, 53)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(newCompResult_JComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(newCompPoints_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(addLocation_JLabel)
                                .addComponent(addDate_JLabel)
                                .addComponent(addGame_JLabel))
                            .addGap(53, 53, 53)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(newCompDate_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(newCompGame_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(newCompLocation_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(addNewCompResult_jButton)))
                .addContainerGap(332, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(titleCompetitions_JLabel)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addDate_JLabel)
                    .addComponent(newCompDate_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addLocation_JLabel)
                    .addComponent(newCompLocation_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addGame_JLabel)
                    .addComponent(newCompGame_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newCompResult_JComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addNewCompTeam_JLabel))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newCompPoints_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addNewCompPoints_JLabel))
                .addGap(34, 34, 34)
                .addComponent(addNewCompResult_jButton)
                .addContainerGap(128, Short.MAX_VALUE))
        );

        bodyjTabbedPane.addTab("Add new competition result", jPanel2);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        titleAddNewTeam_JLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        titleAddNewTeam_JLabel1.setText("Add new team");

        newTeamName_JLabel1.setText("Team Name:");

        newContactPerson_JLabel1.setText("Contact Person:");

        newContactPhone_JLabel1.setText("Contact Phone:");

        newContactEmail_JLabel1.setText("Contact Email:");

        addNewTeam_jButton.setText("ADD NEW TEAM");
        addNewTeam_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewTeam_jButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addNewTeam_jButton)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(titleAddNewTeam_JLabel1)
                            .addComponent(newContactEmail_JLabel1)
                            .addComponent(newContactPhone_JLabel1)
                            .addComponent(newContactPerson_JLabel1)
                            .addComponent(newTeamName_JLabel1))
                        .addGap(33, 33, 33)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(newTeamName_JTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                            .addComponent(newContactPhone_JTextField)
                            .addComponent(newContactPerson_JTextField)
                            .addComponent(newContactEmail_JTextField))))
                .addContainerGap(285, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(titleAddNewTeam_JLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newTeamName_JLabel1)
                    .addComponent(newTeamName_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newContactPerson_JLabel1)
                    .addComponent(newContactPerson_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newContactPhone_JLabel1)
                    .addComponent(newContactPhone_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newContactEmail_JLabel1)
                    .addComponent(newContactEmail_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addComponent(addNewTeam_jButton)
                .addContainerGap(162, Short.MAX_VALUE))
        );

        bodyjTabbedPane.addTab("Add new team", jPanel3);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        addResultTitle_JLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        addResultTitle_JLabel.setText("Update an existing team");

        AddResultTeam_JLabel.setText("Team:");

        updateTeam_JComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        updateTeam_JComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                updateTeam_JComboBoxItemStateChanged(evt);
            }
        });

        updateContactPerson_JLabel.setText("Contact Person:");

        updateContactPhone_JLabel.setText("Contact Phone:");

        updateContactEmail_JLabel.setText("Contact Email:");

        updateExistingTeam_jButton.setText("UPDATE EXISTING TEAM");
        updateExistingTeam_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateExistingTeam_jButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                            .addGap(52, 52, 52)
                            .addComponent(AddResultTeam_JLabel)
                            .addGap(33, 33, 33)
                            .addComponent(updateTeam_JComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(updateContactEmail_JLabel)
                                    .addComponent(updateContactPhone_JLabel)
                                    .addComponent(updateContactPerson_JLabel))
                                .addGap(33, 33, 33)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(updateContactPhone_JTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                    .addComponent(updateContactEmail_JTextField)))
                            .addComponent(updateContactPerson_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(updateExistingTeam_jButton)))
                    .addComponent(addResultTitle_JLabel))
                .addContainerGap(308, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(addResultTitle_JLabel)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AddResultTeam_JLabel)
                    .addComponent(updateTeam_JComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateContactPerson_JLabel)
                    .addComponent(updateContactPerson_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateContactPhone_JLabel)
                    .addComponent(updateContactPhone_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateContactEmail_JLabel)
                    .addComponent(updateContactEmail_JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(53, 53, 53)
                .addComponent(updateExistingTeam_jButton)
                .addContainerGap(149, Short.MAX_VALUE))
        );

        bodyjTabbedPane.addTab("Update an existing team", jPanel4);

        javax.swing.GroupLayout body_jPanelLayout = new javax.swing.GroupLayout(body_jPanel);
        body_jPanel.setLayout(body_jPanelLayout);
        body_jPanelLayout.setHorizontalGroup(
            body_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bodyjTabbedPane)
        );
        body_jPanelLayout.setVerticalGroup(
            body_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bodyjTabbedPane)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(header_jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(body_jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(header_jPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(body_jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /*******************************************************************
    Method:     updateTeam_JComboBoxItemStateChanged()
    Purpose:    Event handler method for update team JComboBox option change
                Displays team data for a chosen team name via displayTeamDetails() method
    Inputs:     ItemEvent evt (the event object passed into the ItemStateChanged() method)
                Although not used in this example, the evt object can provide data about
                the JComboBox
    Outputs:    void
    *******************************************************************/
    private void updateTeam_JComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_updateTeam_JComboBoxItemStateChanged
        // TODO add your handling code here:
        if (comboBoxStatus == true)
        {
            displayTeamDetails();
        }
    }//GEN-LAST:event_updateTeam_JComboBoxItemStateChanged

    /*******************************************************************
    Method:     addNewCompResult_JButtonActionPerformed()
    Purpose:    Event handler method for button click when adding a new competition result
                Validates the inputs first using ValidateNewCompResult() method
                If validated, then presents a confirmation dialog to the user
                If agreed (yes button clicked), then the new competition object is created
                and added to the competitions ArrayList
                Update the JTable containing competition data
    Inputs:     ActionEvent evt (the event object passed into the ActionPerformed() method)
                Although not used in this example, the evt object can provide data about
                the JButton that was clicked
    Outputs:    void
    *******************************************************************/
    private void addNewCompResult_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewCompResult_jButtonActionPerformed
        // TODO add your handling code here:
        // newCompDate_JTextField
        // newCompLocation_JTextField
        // newCompGame_JTextField
        // newCompPoints_JTextField
        if (validateNewCompResult() == true)
        {
        }  

private void addNewCompResult_jButtonActionPerformed(java.awt.event.ActionEvent evt) {  
    // Call the validation method
    if (validateNewCompResult()) /* TO DO
    {
        // Retrieve the validated input data
        String date = newCompDate_JTextField.getText();
        String location = newCompLocation_JTextField.getText();
        String game = newCompGame_JTextField.getText();
        String pointsStr = newCompPoints_JTextField.getText();
        int points = Integer.parseInt(pointsStr);

        // Define the path to the Excel file
        String filePath = "path/to/your/excel-file.xlsx";

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {
            
            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

            // Find the next empty row
            int lastRowNum = sheet.getLastRowNum();
            Row newRow = sheet.createRow(lastRowNum + 1);

            // Create cells and set their values
            newRow.createCell(0).setCellValue(date);
            newRow.createCell(1).setCellValue(location);
            newRow.createCell(2).setCellValue(game);
            newRow.createCell(3).setCellValue(points);

            // Write the changes back to the file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }

            JOptionPane.showMessageDialog(null, "Data added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to update Excel file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }*/
    }

            
        }
        
        
    }//GEN-LAST:event_addNewCompResult_jButtonActionPerformed

    /*******************************************************************
    Method:     formWindowClosing()
    Purpose:    Event handler method called whenever the app is closed
                (from clicking the "X" button at top right)
    Inputs:     WindowEvent evt (the event object passed into the formWindowClosing() method)
                Although not used in this example, the evt object can provide data about
                the WindowEvent object
    Outputs:    void
    *******************************************************************/
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // yesOrNo variable for user's selection
        int yesOrNo = JOptionPane.showConfirmDialog(null,
    "Do you wish to save changes before closing?", "SAVE CHANGES?", JOptionPane.YES_NO_OPTION);
        if (yesOrNo == JOptionPane.YES_OPTION)
        {
            // exit --- save changes
            // save competition data
            saveCompetitionData();
            // save team data
            saveTeamData();
            
        }
        else
        {
            // exit --- don't save changes
        }
    }//GEN-LAST:event_formWindowClosing

    /*******************************************************************
    Method:     addNewTeam_JButtonActionPerformed()
    Purpose:    Event handler method for button click when adding a new team
                Validates the inputs first using ValidateNewTeam() method
                If validated, then presents a confirmation dialog to the user
                If agreed (yes button clicked), then the new team object is created
                and added to the teams ArrayList
                Update the 2 JComboBoxes containing new teams with the new team
    Inputs:     ActionEvent evt (the event object passed into the ActionPerformed() method)
                Although not used in this example, the evt object can provide data about
                the JButton that was clicked
    Outputs:    void
    *******************************************************************/
    
    private void addNewTeam_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewTeam_jButtonActionPerformed
        // if new team data is validated
        if (validateNewTeam())
        {
            // get new team data string values
            String newTeamName = newTeamName_JTextField.getText();
            String newContactPerson = newContactPerson_JTextField.getText();
            String newContactPhone = newContactPhone_JTextField.getText();
            String newContactEmail = newContactEmail_JTextField.getText();
            
            int yesOrNo = JOptionPane.showConfirmDialog
        (null, "You are about to add a new team for " + newTeamName + "\nDo you wish to proceed? Yes or No?", "ADD NEW TEAM", JOptionPane.YES_NO_OPTION);
            
            if (yesOrNo == JOptionPane.YES_OPTION)
            {
                // add the new team to the teams ArrayList<Team>
                teams.add(new Team(newTeamName, newContactPerson, newContactPhone, newContactEmail));

                // update the new team name in the
                displayTeams();
            }
            else
            {
                // add new team cancelled
            }
    
        }
        
        
    }//GEN-LAST:event_addNewTeam_jButtonActionPerformed

    /*******************************************************************
    Method:     displayTopTeams_JButtonActionPerformed()
    Purpose:    Event handler method for button click when displaying a leader board
    *           containing each team with their accumulated scores
    Inputs:     ActionEvent evt (the event object passed into the ActionPerformed() method)
                Although not used in this example, the evt object can provide data about
                the JButton that was clicked
    Outputs:    void
    *******************************************************************/
    private void displayTopTeams_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_displayTopTeams_jButtonActionPerformed
        // TODO add your handling code here:
        displayLeaderBoard();
        
        
    }//GEN-LAST:event_displayTopTeams_jButtonActionPerformed

    /*******************************************************************
    Method:     updateExistingTeam_JButtonActionPerformed()
    Purpose:    Event handler method for button click when updating an existing team (in the teams ArrayList)
                Validates the inputs first using ValidateExistingTeam() method
                If validated, then presents a confirmation dialog to the user
                If agreed (yes button clicked), then the existing team object - contact name, phone & email
                is changed
                NOTE: The code assumes that each name in the ArrayList is unique (no duplicates)
    Inputs:     ActionEvent evt (the event object passed into the ActionPerformed() method)
                Although not used in this example, the evt object can provide data about
                the JButton that was clicked
    Outputs:    void
    *******************************************************************/
    private void updateExistingTeam_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateExistingTeam_jButtonActionPerformed
        // if new team data is validated
        if (validateExistingTeam())
        {
            // get new team data string values
            String updateTeamName = updateTeam_JComboBox.getItemAt(updateTeam_JComboBox.getSelectedIndex());
            String updateContactPerson = updateContactPerson_JTextField.getText();
            String updateContactPhone = updateContactPhone_JTextField.getText();
            String updateContactEmail = updateContactEmail_JTextField.getText();
            
            int yesOrNo = JOptionPane.showConfirmDialog(null, "You are about to update team: " + updateTeamName + "\nDo you wish to proceed? Yes or No?", "UPDATE EXISTING TEAM", JOptionPane.YES_NO_OPTION);
            
            if (yesOrNo == JOptionPane.YES_OPTION)
            {
                // get the team name from the list (matches with chosen team name in JComboBox)
                // then set contact name, phone and email data - calling the Team set() methods
                for (int i = 0; i < teams.size(); i++)
                {
                    if (updateTeamName.equals(teams.get(i).getTeamName()))
                    {
                        teams.get(i).setContactName(updateContactPerson);
                        teams.get(i).setContactPhone(updateContactPhone);
                        teams.get(i).setContactEmail(updateContactEmail);
                        break;
                    }
                }
                
                // optional
                displayTeams();
            }
            else
            {
                // add new team cancelled
            }
    
        }
    }//GEN-LAST:event_updateExistingTeam_jButtonActionPerformed

    /*******************************************************************
    Method:     validateNewTeam()
    Purpose:    Basic validation of user inputs when creating a new team
                - Rejects empty text fields for team name, contact person, phone, email
                - Uses boolean validation to track the status of the validation
                - Uses JOptionPane to create a pop-up window if validation is false
                  to advise user of errors
    Inputs:     void
    Outputs:    returns boolean validation (true if all fields contain strings, false if any are empty)
    *******************************************************************/
    private boolean validateNewTeam()
    {
        boolean validation = true;
        
        String errorMsg = "Error(s) encountered!\n";
        
        if (newTeamName_JTextField.getText().isEmpty())
        {
            errorMsg += "New team name required\n";
            validation = false;
        }
        
        if (newContactPerson_JTextField.getText().isEmpty())
        {
            errorMsg += "Contact person required\n";
            validation = false;
        }
        
        if (newContactPhone_JTextField.getText().isEmpty())
        {
            errorMsg += "Contact phone number required\n";
            validation = false;
        }
        
        if (newContactEmail_JTextField.getText().isEmpty())
        {
            errorMsg += "Contact email address required\n";
            validation = false;
        }
        
        if (validation == false)
        {
            JOptionPane.showMessageDialog(null, errorMsg, "ERROR(s)!", JOptionPane.ERROR_MESSAGE);
        }
            
        return validation;
    }
    
    /*******************************************************************
    Method:     validateExistingTeam()
    Purpose:    Basic validation of user inputs when changing details of an existing team
                - Rejects empty text fields for contact person, phone, email
                - Uses boolean validation to track the status of the validation
                - Uses JOptionPane to create a pop-up window if validation is false
                  to advise user of errors
    Inputs:     void
    Outputs:    returns boolean validation (true if all fields contain strings, false if any are empty)
    *******************************************************************/
    private boolean validateExistingTeam()
    {
        boolean validation = true;
        
        String errorMsg = "Error(s) encountered!\n";
        
        // check if updateContactPerson_JTextField is empty
        if (updateContactPerson_JTextField.getText().isEmpty())
        {
            errorMsg += "Contact person's name required\n";
            validation = false;
        }
        
        // check if updateContactPhone_JTextField is empty
        if (updateContactPhone_JTextField.getText().isEmpty())
        {
            errorMsg += "Contact person's phone number required\n";
            validation = false;
        }
        
        // check if updateContactEmail_JTextField is empty
        if (updateContactEmail_JTextField.getText().isEmpty())
        {
            errorMsg += "Contact person's email address required\n";
            validation = false;
        }
        
        // if validation boolean is false, then display error message via JOptionPane to user
        if (! validation)
        {
            JOptionPane.showMessageDialog(null, errorMsg, "ERROR(s)!", JOptionPane.ERROR_MESSAGE);
        }
        
        // return validation boolean
        return validation;
    }
    
    
    /*******************************************************************
    Method:     validateNewCompResult()
    Purpose:    Basic validation of user inputs when adding a new competition result
                - Rejects empty text fields for competition date, location, game and points
                - Rejects value for points if NOT 0, 1 or 2
                - Uses boolean validation to track the status of the validation
                - Uses JOptionPane to create a pop-up window if validation is false
                  to advise user of errors
    Inputs:     void
    Outputs:    returns boolean validation (true if all fields contain strings, false if any are empty)
    *******************************************************************/
    private boolean validateNewCompResult()
    {
        boolean validation = true;
        String errorMsg = "ERROR(s) detected:\n";
        
        // check newCompDate_JTextField if empty
        if (newCompDate_JTextField.getText().length() == 0)
        {
            validation = false;
            errorMsg += "Competition date is required!\n";
        }
        // check newCompLocation_JTextField if empty
        if (newCompLocation_JTextField.getText().length() == 0)
        {
            validation = false;
            errorMsg += "Competition location is required!\n";
        }
        // check newCompGame_JTextField if empty
        if (newCompGame_JTextField.getText().length() == 0)
        {
            validation = false;
            errorMsg += "Game is required!\n";
        }
        // check newCompPoints_JTextField if empty
        if (newCompPoints_JTextField.getText().length() == 0)
        {
            validation = false;
            errorMsg += "Points are required!\n";
        }
        else
            // points text field is not empty at this stage
        {
            try
            {
                // convert numeric string to an integer
                int points = Integer.parseInt(newCompPoints_JTextField.getText());
                // check if points is NOT less than zero --- OR --- NOT greater than 2
                if (points < 0 || points > 2)
                {
                    validation = false;
                    errorMsg += "Points must be either 0, 1 or 2!\n";
                }
            }
            // this catches any exceptions when using parseInt() method
            catch (Exception e)
            {
                validation = false;
                errorMsg += "Points must be numeric - 0, 1 or 2!\n";
            }
        }
        
        // if validation is false, then display error message to user (with JOptionPane)
        if (validation == false)
        {
            JOptionPane.showMessageDialog(null, errorMsg, "ERRORS Detected!", JOptionPane.ERROR_MESSAGE);
        }
        
        // return validation boolean
        return validation;
    }
    
    /*******************************************************************
    Method:     displayLeaderBoard()
    Purpose:    Displays all teams with their accumulated points
                in descending order of total points (highest to lowest)
                Uses 2 ArrayLists that act as parallel arrays
                The points_ArrayList is ordered in descending order using a Selection sort algorithm
                As the numbers are swapped and arranged, so too are the team names in the teams_ArrayList
    Inputs:     void
    Outputs:    void
    *******************************************************************/
    private void displayLeaderBoard()
    {
        String leaderBoardDisplayStr = "TEAM\t\tTotal Points";
        
        JOptionPane.showMessageDialog(null, leaderBoardDisplayStr, "TEAMS LEADER BOARD", JOptionPane.INFORMATION_MESSAGE);
    }
    /*******************************************************************
    Method:     main()
    Purpose:    Entry point to program - using thread-safe run() method
                as provided in NetBeans GUI Builder
    Inputs:     String [] args --- array of String arguments (only used for command-line input)
    Outputs:    void
    *******************************************************************/
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GC_ESports_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GC_ESports_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GC_ESports_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GC_ESports_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GC_ESports_GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AddResultTeam_JLabel;
    private javax.swing.JLabel addDate_JLabel;
    private javax.swing.JLabel addGame_JLabel;
    private javax.swing.JLabel addLocation_JLabel;
    private javax.swing.JLabel addNewCompPoints_JLabel;
    private javax.swing.JButton addNewCompResult_jButton;
    private javax.swing.JLabel addNewCompTeam_JLabel;
    private javax.swing.JButton addNewTeam_jButton;
    private javax.swing.JLabel addResultTitle_JLabel;
    private javax.swing.JPanel body_jPanel;
    private javax.swing.JTabbedPane bodyjTabbedPane;
    private javax.swing.JLabel compResults_jLabel;
    private javax.swing.JTable compResults_jTable;
    private javax.swing.JButton displayTopTeams_jButton;
    private javax.swing.JPanel header_jPanel;
    private javax.swing.JLabel image_jLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField newCompDate_JTextField;
    private javax.swing.JTextField newCompGame_JTextField;
    private javax.swing.JTextField newCompLocation_JTextField;
    private javax.swing.JTextField newCompPoints_JTextField;
    private javax.swing.JComboBox<String> newCompResult_JComboBox;
    private javax.swing.JLabel newContactEmail_JLabel1;
    private javax.swing.JTextField newContactEmail_JTextField;
    private javax.swing.JLabel newContactPerson_JLabel1;
    private javax.swing.JTextField newContactPerson_JTextField;
    private javax.swing.JLabel newContactPhone_JLabel1;
    private javax.swing.JTextField newContactPhone_JTextField;
    private javax.swing.JLabel newTeamName_JLabel1;
    private javax.swing.JTextField newTeamName_JTextField;
    private javax.swing.JLabel titleAddNewTeam_JLabel1;
    private javax.swing.JLabel titleCompetitions_JLabel;
    private javax.swing.JLabel updateContactEmail_JLabel;
    private javax.swing.JTextField updateContactEmail_JTextField;
    private javax.swing.JLabel updateContactPerson_JLabel;
    private javax.swing.JTextField updateContactPerson_JTextField;
    private javax.swing.JLabel updateContactPhone_JLabel;
    private javax.swing.JTextField updateContactPhone_JTextField;
    private javax.swing.JButton updateExistingTeam_jButton;
    private javax.swing.JComboBox<String> updateTeam_JComboBox;
    // End of variables declaration//GEN-END:variables
}
