/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import mywsdl.TTTWebService;
import mywsdl.TTTWebService_Service;

/**
 *
 * @author kim25
 */
public class LoginScreen extends JFrame implements ActionListener {
    
    private TTTWebService_Service link;
    private TTTWebService proxy;
    
    private JLabel[] label= new JLabel[4];
    private JButton button= new JButton();
    private JTextField [] textField= new JTextField[2];
    private JLabel label_info=new JLabel(" ");
    
    public LoginScreen(){

     link= new TTTWebService_Service();
     proxy=link.getTTTWebServicePort();
     
      setTitle("Login");
      setBounds(50, 100, 650, 450);
      setLayout(new GridLayout(8,1));
      
      //Title of Login Screen
      label[0]= new JLabel("Login");
      label[0].setVerticalAlignment(JLabel.CENTER);
      label[0].setHorizontalAlignment(JLabel.CENTER);
      label[0].setFont(new Font("Serif", Font.BOLD, 30));
      
      //Label for username and password
      label[1]= new JLabel("Username:");
      label[1].setVerticalAlignment(JLabel.BOTTOM);
      label[2]= new JLabel("Password");
      label[2].setVerticalAlignment(JLabel.BOTTOM);
      label[3]= new JLabel(" ");
      
      //Textfield for username und password
      textField[0]=new JTextField();
      textField[1]=new JTextField();
      
      //button to send userinformations to webservice
      button.setText("Login");
      button.addActionListener(this);
      
      //add components
        add(label[0]);
        add(label[1]);
        add(textField[0]);
        add(label[2]);
        add(textField[1]);
        add(label[3]);
        add(button);
      
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       Object source= e.getSource();
       String username=null;
       String password=null;
       int userID=0;
       
       if(source== button){
           username=textField[0].getText();
           password=textField[1].getText();
           System.out.println(username+" "+ password);
           //check if the user is on the system
          try{ 
            userID=proxy.login(username, password);
           
           switch(userID){
               //login was not successful
               case -1:
                   System.out.print("Login was not successful");
                   //JOptionPane.showMessageDialog(null, "Login was not successful");
                   label_info.setText("ATTENTION: Login was not sucessfull, even the user does not exist or the password is wrong");
                   add(label_info);
                   //open Registartion screen??
                   //RegistrationScreen regScreen= new RegistrationScreen();
                   break;
                   
                   //login was successful
               default:
                   System.out.print("Login was successful, userID :"+ userID);
                   //open Information screen
                   InformationScreen infScreen=new InformationScreen(userID,username);
                   break;
           
           } 
          }catch(Exception exc){
            JOptionPane.showMessageDialog(null, exc.getMessage());
          }

           add(label_info);
           label_info.setFont(new Font("Serif", Font.BOLD, 15));
           label_info.setForeground(Color.RED);
           
           
       }
    }

}
