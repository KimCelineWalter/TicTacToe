/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;
import mywsdl.TTTWebService;
import mywsdl.TTTWebService_Service;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TicTacToeGame extends JFrame{
 private JButton [][] buttons= new JButton[3][3];
 private JLabel label_info =new JLabel("PLAY GAME");
 private TicTacToePanel panel;
 private TTTWebService_Service link;
 private TTTWebService proxy;
 private int userID=0;
 private int gameID=0;
 
 public TicTacToeGame(int uID, int gID){
     
     link= new TTTWebService_Service();
     proxy=link.getTTTWebServicePort();
     userID=uID;
     gameID=gID;
     
     setTitle("Tic Tac Toe!");
     setBounds(500,500,400,400);
     setLayout(new BorderLayout());
 
     add(label_info, BorderLayout.NORTH);

     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     setVisible(true);
     PlayGame();
     
 
    
 
 }
 
 public void PlayGame(){
 
     String result=proxy.getGameState(gameID);
     System.out.println(result);
     if(result.equals("-1")){
         label_info.setText("Waiting for second Player");
         //Setting buttons unenable
         /*
         for(int i=0;i<3;i++){
              for(int j=0;j<3;j++) {
                buttons[i][j].setEnabled(false);
              } 
          }*/
         //waiting for second player
         while(result.equals("-1")){
             result=proxy.getGameState(gameID);
         }
     }
     //case 1: client is player 1 and second player joined the game
     //case 2: client is player 2 and joined the created game from player 1
     panel=new TicTacToePanel();
     add(panel,BorderLayout.CENTER);
     

 
 }

    class TicTacToePanel extends JPanel implements ActionListener{
        public TicTacToePanel() {
          setLayout(new GridLayout(3,3));
          //setBounds(300,300,200,200);
          for(int i=0;i<3;i++){
              for(int j=0;j<3;j++) {
               buttons[i][j]=new JButton();
               buttons[i][j].addActionListener(this);
               add(buttons[i][j]);        
              } 
          }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
    
}
