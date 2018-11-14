/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import java.awt.Font;
import java.awt.GridLayout;
import static java.lang.Boolean.FALSE;
import javax.swing.JFrame;
import javax.swing.JLabel;
import mywsdl.TTTWebService;
import mywsdl.TTTWebService_Service;


public class LeaderBoard extends JFrame {
    
 private TTTWebService_Service link;
 private TTTWebService proxy;
    
    public LeaderBoard(){
        
     link= new TTTWebService_Service();
     proxy=link.getTTTWebServicePort();
     
     setTitle("Score Board");
     setBounds(50, 100, 300, 200);
     setLayout(new GridLayout(50,1));
        
     JLabel title=new JLabel("Leader Boar");
     title.setHorizontalAlignment(JLabel.CENTER);
     title.setFont(new Font("Serif", Font.BOLD, 20));
     add(title);
     
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
     String[] checkWin=new String[numGames];
     int [] p1_wins= new int[numGames];
     int [] p1_losses=new int[numGames];
     int [] p1_draft=new int[numGames];
     int [] p2_wins= new int[numGames];
     int [] p2_losses=new int[numGames];
     int [] p2_draft=new int[numGames];
     
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
             
//             checkWin[i]=proxy.checkWin(gameID[i]);    
               checkWin[i]="1";
     }

     for (int i=0; i<numGames; i++){       
         switch(checkWin[i]){
                     case "0":
                         //game hasn’t been won but can continue to be played
                         break;
                     case "1":
                         //Player 1 won --> user won
                         p1_wins[i]=1;
                         p2_wins[i]=0;                         
                         break;
                     case "2":
                         //Player 2 won --> user lost
                         p1_losses[i]=0;
                         p2_losses[i]=1;
                         break;
                     case "3":
                         //Game is a draw
                         p1_draft[i]=1;
                         p2_draft[i]=1;
                         break;
                     default:
                         break;
                } 
                System.out.println(player1[i]);
                 System.out.println(player2[i]);
     }
     String user=null;
     for(int i=0; i<numGames; i++){
         user=player1[i];
         int wins=0;
         int losses=0;
         int drafts=0;
         System.out.println(player1[i]);
         for(int j=0; j<numGames; j++){
             if(player1[i].equals(player1[j])){
                 player1[j]="0";
                 wins=+p1_wins[j];
                 losses=+p1_losses[j];
                 drafts=+p1_draft[j];
             }
             if(player1[i].equals(player2[j])){
                player2[j]="0";
                wins=+p2_wins[j];
                losses=+p2_losses[j];
                drafts=+p2_draft[j];
             }
         
         }
         JLabel username=new JLabel("Player: "+user);
         JLabel win= new JLabel("Wins: "+ wins);
         JLabel los=new JLabel("Losses: "+ losses);
         JLabel draft=new JLabel("Drafts: "+ losses);
         add(username);
         add(win);
         add(los);
         add(draft);
     }
  
    
    }
    
    
}
