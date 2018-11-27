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

public class RegistrationScreen extends JFrame implements ActionListener {
    
    private TTTWebService_Service link;
    private TTTWebService proxy;
    
    private JLabel[] label= new JLabel[6];
    private JButton button= new JButton();
    private JTextField [] textField= new JTextField[4];
    private JLabel label_info=new JLabel(" ");
    
    public RegistrationScreen(){
    
     link= new TTTWebService_Service();
     proxy=link.getTTTWebServicePort();
     
     setTitle("Registration");
     setBounds(50, 100, 650, 450);
     setLayout(new GridLayout(12,1));
      
      //Title of Login Screen
     label[0]= new JLabel("Registration");
     label[0].setVerticalAlignment(JLabel.CENTER);
     label[0].setHorizontalAlignment(JLabel.CENTER);
     label[0].setFont(new Font("Serif", Font.BOLD, 30));
     
     //Labels for user details
      label[1]= new JLabel("Username:");
      label[1].setVerticalAlignment(JLabel.BOTTOM);
      label[2]= new JLabel("Password");
      label[2].setVerticalAlignment(JLabel.BOTTOM);
      label[3]= new JLabel("Name");
      label[3].setVerticalAlignment(JLabel.BOTTOM);
      label[4]= new JLabel("Surname");
      label[4].setVerticalAlignment(JLabel.BOTTOM);
      label[5]= new JLabel(" ");
     
      //Textfield for username und password
      textField[0]=new JTextField();
      textField[1]=new JTextField();
      textField[2]=new JTextField();
      textField[3]=new JTextField();
      
      //button to send userinformations to webservice
      button.setText("Register");
      button.addActionListener(this);
      
       //add components
        add(label[0]);
        add(label[1]);
        add(textField[0]);
        add(label[2]);
        add(textField[1]);
        add(label[3]);
        add(textField[2]);
        add(label[4]);
        add(textField[3]);
        add(label[5]);
        add(button);
        
              
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
       Object source= e.getSource();
       String username=null;
       String password=null;
       String name=null;
       String surname=null;
       String userID=null;

       
       if(source== button){
           
           username=textField[0].getText();
           password=textField[1].getText();
           name=textField[2].getText();
           surname=textField[3].getText();
           System.out.println(name.isEmpty());
           System.out.println(username+" "+ password+ " "+ name+ " "+surname);
           
           if(!(username.isEmpty())&&!(password.isEmpty())&&!(name.isEmpty())&&!(surname.isEmpty())){
            //registration
              try{
                userID=proxy.register(username, password, name, surname); 
            
                switch(userID){
                //login was not successful
                 case "ERROR-REPEAT":
                   System.out.print("user with the username already exists");
                   label_info.setText("ATTENTION: user with the username already exists");
                    break;
                 case "ERROR-INSERT":
                   System.out.print("couldn’t add the data to the users table");
                   label_info.setText("ATTENTION: couldn’t add the data to the users table");
                    break;
                 case "ERROR-RETRIEVE":
                   System.out.print("cannot retrieve the newly inserted data from the users table");
                   label_info.setText("ATTENTION: cannot retrieve the newly inserted data from the users table");
                    break;
                 case "ERROR-DB":
                   System.out.print("cannot find the DB");
                   label_info.setText("ATTENTION: cannot find the DB");
                    break;
                 default:
                   System.out.print("Registration was successful");
                   //open Login Screen
                   LoginScreen loginscreen= new LoginScreen();    
                }
              }catch(Exception exc){
                   JOptionPane.showMessageDialog(null, exc.getMessage());
              }
           }else{
              label_info.setText("Please fill out all information!");
           }

            add(label_info);
            label_info.setFont(new Font("Serif", Font.BOLD, 15));
            label_info.setForeground(Color.RED);
       }
    }
    
}
