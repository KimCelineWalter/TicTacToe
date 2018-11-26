/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import mywsdl.TTTWebService;

/*
 * @author Patryk
 */
public class NewGameThread extends Thread {
//public class ThreadPanel implements Runnable {    

    private TTTWebService proxy;
    private int gameID;
    
    public NewGameThread(int gameID, TTTWebService proxy) {
       this.proxy = proxy;
       this.gameID = gameID;
    }
      
    @Override
    public void run() {
        int i = -1; 
        int interval = 1000;
        while(i != 0) {
            try {
                sleep(interval);
            } catch(Exception e ) {
                
            }
            i = Integer.parseInt(proxy.getGameState(gameID)); 
            
        }
        System.out.println("-------------" + i);
    }
    
}
 
