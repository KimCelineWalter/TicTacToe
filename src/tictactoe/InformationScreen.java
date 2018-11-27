/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import mywsdl.TTTWebService;
import mywsdl.TTTWebService_Service;


public class InformationScreen extends JFrame implements ActionListener,TableModelListener{

    //components for WebServer connection
    private TTTWebService_Service link;
    private TTTWebService proxy;
    
    //components for the three Buttons
    private int userID=0;
    private String userName=null;
    private JButton[] button= new JButton[3];
    private JLabel label= new JLabel();
    private JLabel label_infoTable= new JLabel(" ");
    private JLabel label_info= new JLabel(" ");
    
    //components for the JTable
    private JTable table= new JTable();
    private int numOpenGames=0;
    private int[] autokey;
    private String[] player1;
    private String[] player1_all;
    private Object[][] data; 
    private int newGameID=0;
   
    
    public InformationScreen(int uID, String uName){
    
      link= new TTTWebService_Service();
      proxy=link.getTTTWebServicePort();
      
      userID=uID;
      userName=uName;
      setTitle("Information Screen");
      setBounds(50, 100, 700, 800);
      setLayout(new GridLayout(7,1));
      
      //Title of Login Screen
      label= new JLabel("Information Screen");
      label.setVerticalAlignment(JLabel.CENTER);
      label.setHorizontalAlignment(JLabel.CENTER);
      label.setFont(new Font("Serif", Font.BOLD, 30));
      
      button[0]=new JButton("Score Board");
      button[0].addActionListener(this);
      
      button[1]=new JButton("Leader Board");
      button[1].addActionListener(this);  

      button[2]=new JButton("New Game");
      button[2].addActionListener(this); 
      
      add(label);
      add(button[0]);
      add(button[1]);
      add(button[2]);
      
      try{
        String result=proxy.showOpenGames();
   
        switch(result){
            
            case "ERROR-NOGAMES":
                System.out.println("ATTENTION: No open games found.");
                label_infoTable.setText("No open games.");
                label_infoTable.setFont(new Font("Serif", Font.BOLD, 15));
                label_infoTable.setForeground(Color.RED);
                add(label_infoTable);
                break;
           case "ERROR-DB":
                System.out.println("ATTENTION: Cannot access the DBMS.");
                label_infoTable.setText("Cannot access the DBMS.");
                label_infoTable.setFont(new Font("Serif", Font.BOLD, 15));
                label_infoTable.setForeground(Color.RED);
                add(label_infoTable);
                break;
                    
           default:    
                System.out.println("Games found");
                //find the number of wins, losses and drafts of the player
                OpenGamesTable(result);
                break;
        }  
      }catch(Exception exc){
        JOptionPane.showMessageDialog(null, exc.getMessage());
      }
 
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      setVisible(true);
    }

    public void OpenGamesTable(String result){
      Object[] columnNames = {"GameID","Player1","Join the game?"};
      String[] parts = result.split("\n");
      System.out.println(result);
      //number of open games
      numOpenGames=parts.length;
      player1_all=new String[numOpenGames];
      int numValidOpenGames=numOpenGames;
      System.out.println("number of open games: "+numOpenGames);
      
    //only consider the open games where user is not player1
    //calculate the number of valid games for player
      for (int i=0; i<numOpenGames; i++){
             String[] parameters=parts[i].split(",");
             player1_all[i]=parameters[1]; 
             
             if(player1_all[i].equals(userName))
                 numValidOpenGames--;       
      }
       System.out.println("number of valid open games: "+numValidOpenGames);
      
      //if valid open games for user exist
      if(numValidOpenGames>0){
        //initiliase the parameters with the right size  
        autokey=new int[numValidOpenGames];
        player1=new String[numValidOpenGames];
        data = new Object[numValidOpenGames][3]; 
        int indexValid=0;
      
        for(int i=0; i<numOpenGames; i++){
             String[] parameters=parts[i].split(",");
             player1_all[i]=parameters[1];
             if(!(player1_all[i].equals(userName))){      
                autokey[indexValid]=Integer.parseInt(parameters[0]);
                player1[indexValid]=parameters[1]; 
                data[indexValid][0]=autokey[indexValid];
                data[indexValid][1]=player1[indexValid];
                data[indexValid][2]="Type yes to join the game";  
                indexValid++;
             }
        }
        //Set Label above the table
        label_infoTable.setText("Open Games Table: to join a game type 'yes' in the 'Join the game' column");
        label_infoTable.setFont(new Font("Serif", Font.BOLD, 15));
        label_infoTable.setForeground(Color.RED);
        add(label_infoTable);
  
        //Create the table
        table = new JTable(data, columnNames);
        TableModel tabModel = table.getModel();
        tabModel.addTableModelListener(this);
        table.setRowHeight(20);
        add(table.getTableHeader());
        add(table); 
        JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.setPreferredSize(new Dimension(480, 500));
        add(scrollpane);
        
      }else{
        //No valid open games for user
        //Set only the label, no table required?
        label_infoTable.setText("No Open Games to join");
        label_infoTable.setFont(new Font("Serif", Font.BOLD, 15));
        label_infoTable.setForeground(Color.RED);
        add(label_infoTable);
      }
    }
    
    public void tableChanged(TableModelEvent e) {
        
        int row = e.getFirstRow();
        int column = e.getColumn();
        TableModel model = (TableModel)e.getSource();
        String columnName = model.getColumnName(column);
        Object data = model.getValueAt(row, column);
        System.out.println(data);
        System.out.println(columnName);
        System.out.println(row+" "+ column);
        
        //label_info=new JLabel();
        // if user types "yes" and the first player is not the user 
        if(data.equals("yes")){
            //Join the game
            try{
                String result=proxy.joinGame(userID,autokey[row]);
                switch(result){
                    case "1":
                        System.out.println("successful");
                        label_info.setText("ATTENTION: Successfully joined the game");
                        TicTacToeGame game= new TicTacToeGame(userID,autokey[row]);
                        break;
                    case "0":
                        System.out.println("Unable to join the game.");
                        label_info.setText("ATTENTION: Unable to join the game");
                        break;
                    case "ERROR-DB":
                        System.out.println("cannot access the DBMS.");
                        label_info.setText("ATTENTION: Cannot access the DBMS");
                        break;
                }  
            }catch(Exception exc){
                JOptionPane.showMessageDialog(null, exc.getMessage());
            }
        }else{
            System.out.println("Incorrect typing");
            label_info.setText("ATTENTION: Incorrect typing");
        }
 
        add(label_info);
        label_info.setFont(new Font("Serif", Font.BOLD, 15));
        label_info.setForeground(Color.RED);
    
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source= e.getSource();
        
        //Create Score Board
        if(source== button[0]){
            
            ScoreBoard scoreBorad= new ScoreBoard(userID,userName);
        }
        
        //Create Leader Board
        if(source== button[1]){
            
            LeaderBoard scoreBoard= new LeaderBoard(); 
            
        }
        //Create a new Game
        if(source== button[2]){
            try{
                String result=proxy.newGame(userID);
            
                switch (result){
                    case "ERROR-NOTFOUND":
                        System.out.print("cannot find the id of the game");
                        label_info.setText("ATTENTION: Cannot find the id of the game");
                        break;
                    case "ERROR-RETRIEVE":
                        System.out.print("cannot access the games table");
                        label_info.setText("ATTENTION: Cannot access the games table");
                        break;
                    case "ERROR-INSERT":
                        System.out.print("cannot add a new game to the system");
                        label_info.setText("ATTENTION: Cannot add a new game to the system");
                        break;
                    case "ERROR-DB":
                        System.out.print("cannot access the DBMS");
                        label_info.setText("ATTENTION: Cannot access the DBMS");
                        break;
                    default:
                        System.out.print("New Game added");
                        newGameID=Integer.valueOf(result);
                        label_info.setText("ATTENTION: Added new game with gameID: "+ newGameID);
                        TicTacToeGame game= new TicTacToeGame(userID,newGameID);
                        break;
                }   
            }catch(Exception exc){
                 JOptionPane.showMessageDialog(null, exc.getMessage());
            }
           
           remove(label_info);
           add(label_info);
           label_info.setFont(new Font("Serif", Font.BOLD, 15));
           label_info.setForeground(Color.RED);
        }
        
        
    }
    
}
