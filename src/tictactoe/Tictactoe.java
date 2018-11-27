/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import mywsdl.TTTWebService;
import mywsdl.TTTWebService_Service;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;


/**
 *
 * @author kim25
 */
public class Tictactoe extends JFrame implements ActionListener {

    //components
    private JLabel title= new JLabel();
    private JButton[] button= new JButton[2];
    //private TTTWebService_Service link;
    //private TTTWebService proxy;
    
    public Tictactoe() {
        
        //link= new TTTWebService_Service();
        //proxy=link.getTTTWebServicePort();
        
        setTitle("Tic Tac Toe");
        setBounds(50, 100, 650, 450);
        setLayout(new GridLayout(3,1,50,50));
        
        //Title of Main Scree
        title.setText("Tic Tac Toe");
        title.setVerticalAlignment(JLabel.CENTER);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 50));
        title.setForeground(Color.RED);
        
        //Login and Registration Button of Main Screen
        button[0]= new JButton("Login");
        button[0].addActionListener(this);
        button[1]= new JButton("Create new account");
        button[1].addActionListener(this);
        
        //add components
        add(title);
        add(button[0]);
        add(button[1]);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        Tictactoe mainscreen= new Tictactoe();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source=e.getSource();
        if(source==button[0]){
           //open Login page 
          System.out.println("Login");
          this.dispose();
          LoginScreen loginscreen= new LoginScreen();
         
        }
        if(source==button[1]){
          //open Registration page
          System.out.println("Registration");
          this.dispose();
          RegistrationScreen regScreen= new RegistrationScreen();
          
        }
    }
    
}
