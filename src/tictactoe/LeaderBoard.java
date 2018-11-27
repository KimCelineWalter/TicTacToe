/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import static java.lang.Boolean.FALSE;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import mywsdl.TTTWebService;
import mywsdl.TTTWebService_Service;


public class LeaderBoard extends JFrame {
    
 private TTTWebService_Service link;
 private TTTWebService proxy;
 
 private JTable table= new JTable();
 private Object[][] data; 
 private Object[] columnNames;
    
    public LeaderBoard(){
        
     link= new TTTWebService_Service();
     proxy=link.getTTTWebServicePort();
     
     setTitle("Leader Board");
     setBounds(50, 100, 250, 300);
     setLayout(new GridLayout(2,1));
        
     JLabel title=new JLabel("Leader Board");
     title.setHorizontalAlignment(JLabel.CENTER);
     title.setFont(new Font("Serif", Font.BOLD, 20));
     add(title);
     
     try{
         String result= proxy.leagueTable();

        switch(result){
            
            case "ERROR-NOGAMES":
                System.out.println("No games found.");
                JLabel label_noGames=new JLabel("No games found.");
                add(label_noGames);
                break;
            case "ERROR-DB":
                System.out.println("Cannot access the DBMS.");
                JLabel label_DBMS=new JLabel("No access to DBMS possible.");
                add(label_DBMS);
                break;
                    
            default:    
                System.out.println("Games found");
                //create Leader Board
                FindWinsLossesDraftsAll(result);
                break;
        }
    }catch(Exception exc){
         JOptionPane.showMessageDialog(null, exc.getMessage());
    }
      
     setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
     setVisible(true);
    
    }
    
    public void FindWinsLossesDraftsAll(String result){
        
     String[] parts = result.split("\n");
     //number of games
     int numGames=parts.length;
     System.out.println("number of games: "+numGames);
     
     int[] gameID = new int[numGames];
     String[] player1 = new String[numGames];
     String[] player2 = new String[numGames];
     String[] gstate = new String[numGames];
     String[] gstarted = new String[numGames];

     //Store for all games, player1 player2, gamestate and game started
     for (int i=0; i<numGames; i++){
             String[] parameters=parts[i].split(",");
             //parameter 0 is the autoID
             gameID[i]=Integer.parseInt(parameters[0]);
             //parameter 1 is player 1 (who createtd the game)
             player1[i]=parameters[1];
              //parameter 2 is player 2 
             player2[i]=parameters[2];
             //parameter 3 is game state
             gstate[i]=parameters[3];
             //parameter 4 is game started
             gstarted[i]=parameters[4];
     }

     //find all players
     List<String> userNames = new ArrayList<>();

     int p1_exist=0;
     int p2_exist=0;
     
     for(int i=0; i<numGames; i++){   
        if(userNames.isEmpty()){
            //player1 and player2 are not stored in the array
            p1_exist=0;
            p2_exist=0;
        }else{ //array is not empty --> check if player 1 and 2 are already stored in the array
            for(int j=0; j<userNames.size();j++){ 
              if(userNames.get(j).equals(player1[i])){
                  p1_exist=1;
              }
              if(userNames.get(j).equals(player2[i])){
                  p2_exist=1;
              }
            } 
        }
        
        if(p1_exist==0){
            userNames.add(player1[i]);
        }
        if(p2_exist==0){
            userNames.add(player2[i]);
        }
        p1_exist=0;
        p2_exist=0;
     }
     /*
     for (int i=0; i<userNames.size(); i++){
         System.out.println(userNames.get(i));
     }*/
     int [] wins= new int[userNames.size()];
     int [] losses= new int[userNames.size()];
     int [] draws= new int[userNames.size()];
     
     //now all names of players are stored in userNames
     //find the number of wins, losses, draws for each player
     for (int i=0; i< numGames; i++){
         for(int j=0; j<userNames.size(); j++){
             if(player1[i].equals(userNames.get(j))){
                 switch (gstate[i]){
                    case "1":
                         //Player 1 won --> user won
                         wins[j]++;
                         break;
                    case "2":
                         //Player 2 won --> user lost
                        losses[j]++;
                         break;
                    case "3":
                         //Game is a draw
                        draws[j]++;
                         break;
                    default: // if 0 --> game in progress
                         break;
                 }
             }
             if(player2[i].equals(userNames.get(j))){
                 switch (gstate[i]){
                    case "1":
                         //Player 1 won --> user won
                         losses[j]++;
                         break;
                    case "2":
                         //Player 2 won --> user lost
                        wins[j]++;
                         break;
                    case "3":
                         //Game is a draw
                        draws[j]++;
                         break;
                    default: //if -1 --> waiting for second player; if 0 --> game in progress
                         break;
                 }
             }            
         }
     }
     
     //Create the Leader Board
     Object[] columnNames = {"User","Wins","Losses", "Draws"};
     data = new Object[userNames.size()][4]; 
     for (int i=0; i<userNames.size(); i++){
         data[i][0]=userNames.get(i);
         data[i][1]=wins[i];
         data[i][2]=losses[i];
         data[i][3]=draws[i];
     }
     table = new JTable(data, columnNames);
     TableModel tabModel = table.getModel();
     table.setRowHeight(20);
     add(table.getTableHeader());
     add(table); 
     JScrollPane scrollpane = new JScrollPane(table);
     scrollpane.setPreferredSize(new Dimension(480, 500));
     add(scrollpane);
    }  
}
