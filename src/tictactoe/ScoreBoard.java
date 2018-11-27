/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import mywsdl.TTTWebService;
import mywsdl.TTTWebService_Service;

public class ScoreBoard extends JFrame {

 private TTTWebService_Service link;
 private TTTWebService proxy;
 private int wins=0;
 private int losses=0;
 private int drafts=0;
 private int userID=0;
 private String userName=null;
 private JLabel[] label=new JLabel[5];

 
 @SuppressWarnings("empty-statement")
    public ScoreBoard(int uID,String uName){
        
        link= new TTTWebService_Service();
        proxy=link.getTTTWebServicePort();
        userID=uID;
        userName=uName;
        
        setTitle("Score Board");
        setBounds(50, 100, 300, 200);
        setLayout(new GridLayout(5,1));
        
        label[0]=new JLabel("Score Board of userID: "+userID);
        label[0].setHorizontalAlignment(JLabel.CENTER);
        label[0].setFont(new Font("Serif", Font.BOLD, 20));
        
        //show score system that will keep track of the number of wins, losses and draws the player has.
        String result=null;
        try{
            result=proxy.showAllMyGames(userID);

            switch(result){
            
                case "ERROR-NOGAMES":
                    System.out.println("No games found.");
                    label[1]=new JLabel("Wins: 0 ");
                    label[2]=new JLabel("Losses: 0 ");
                    label[3]=new JLabel("Drafts: 0 ");
                    label[4]=new JLabel("No games found");
                    break;
                case "ERROR-DB":
                    label[1]=new JLabel("Wins: - ");
                    label[2]=new JLabel("Losses: - ");
                    label[3]=new JLabel("Drafts: - ");
                    label[4]=new JLabel("No access to DBMS possible");
                    System.out.println("Cannot access the DBMS.");
                    break;
                    
                default:    
                    System.out.println("Games found");
                    //find the number of wins, losses and drafts of the player
                    FindWinsLossesDrafts(result);
                    label[1]=new JLabel("Wins: "+ wins);
                    label[2]=new JLabel("Losses: "+ losses);
                    label[3]=new JLabel("Drafts: "+ drafts);
                    label[4]=new JLabel(" ");
                    break;
            }
        }catch(Exception exc) {
            JOptionPane.showMessageDialog(null, exc.getMessage());
        }

        add(label[0]);
        add(label[1]);
        add(label[2]);
        add(label[3]);
        add(label[4]);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

    }
    
    public void FindWinsLossesDrafts(String result){
         String[] parts = result.split("\n");
         //number of games
         int numGames=parts.length;
         System.out.println("number of games: "+numGames);
         
         int gameID=0;
         String user1=null;
         String user2=null;
         String started=null;
         String win=null;
         
         for (int i=0; i<numGames; i++){
             String[] parameters=parts[i].split(",");
             //parameter 0 is the autoID
             gameID=Integer.parseInt(parameters[0]);
             //parameter 1 is player 1 (who createtd the game)
             user1=parameters[1];
              //parameter 2 is player 2 
             user2=parameters[2];
             //System.out.println(gameID+" "+user1+" "+user2+" "+userName);
             started=parameters[3];

             try{
                win=proxy.getGameState(gameID);

                if(user1.equals(userName)){
                    switch(win){
                        case "1":
                            //Player 1 won --> user won
                            wins++;
                            break;
                        case "2":
                            //Player 2 won --> user lost
                            losses++;
                            break;
                        case "3":
                            //Game is a draw
                            drafts++;
                            break;
                        default: //case -1 and 0 waiting for second player or game is in progress
                            break;

                   }     
                }else { //user == player2
                    switch(win){
                        case "1":
                            //Player 1 won --> user lost
                            losses++;
                            break;
                        case "2":
                            //Player 2 won --> user won
                            wins++;
                            break;
                        case "3":
                            //Game is a draw
                            drafts++;
                            break;
                        default://case -1 and 0 waiting for second player or game is in progress
                            break;

                    }     
                }
               }catch(Exception e){
                  System.out.println(e.getMessage());
                  //JOptionPane.showMessageDialog(null, e.getMessage());
               }
            }
         
         System.out.println("wins: "+wins+" losses: "+losses+" draft: "+drafts);
    }
         //per game 4 parameters
}


