import java.rmi.*;
import java.util.*;
import java.io.*;

public class MyClient{
    //print board in a grid form
    public static void printBoard(String board){
        System.out.println(board.charAt(0) + " | " + board.charAt(1) + " | " + board.charAt(2));
        System.out.println("___________");
        System.out.println(board.charAt(3) + " | " + board.charAt(4) + " | " + board.charAt(5));
        System.out.println("___________");
        System.out.println(board.charAt(6) + " | " + board.charAt(7) + " | " + board.charAt(8) + "\n");
    }
    
    public static void main(String args[]){
        try{
            TicTacToeContract stub = (TicTacToeContract)Naming.lookup("rmi://localhost:5000/deadpool");
            Scanner in = new Scanner(System.in);
            
            System.out.println("Do you want to start the game ? (y/n) ");
            String reply = in.nextLine();
            if(!reply.equals("y"))
                return;
            
            //Register new player and assign board
            List<Integer> playerInfo = stub.registerPlayer();
            Integer playerID = playerInfo.get(0);
            Integer gameID = playerInfo.get(1);
            Integer opponentID = -1;

            System.out.println("Player"+Integer.toString(playerID)+"\nWait...");
            //keep checking for pairing a player
            while(true){
                Integer res = stub.assignGame(playerID,gameID);
                if(!res.equals(-1)){
                    opponentID = res;
                    break;
                }
            }
            System.out.println("Pairing successful...\nOpponent is Player"+Integer.toString(opponentID)+"...");

            while(true) {
                Integer turn_playerID = stub.isItMyTurn(gameID,playerID,opponentID);
                if(turn_playerID.equals(playerID)) {
                    String board = stub.retrieveBoard(gameID);
                    Integer cell_number = -1;
                    printBoard(board);
                    System.out.println("Enter cell number: ");
                    
                    //check for user imput before 10 seconds
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    long startTime = System.currentTimeMillis();
                    while ((System.currentTimeMillis() - startTime) < 10000  && !br.ready()){
                    }
                    if (br.ready()) {
                        cell_number = Integer.parseInt(br.readLine());
                        
                        String res = "";
                        board = stub.registerMove(gameID,cell_number,playerID);
                        printBoard(board);
                        res = stub.validateBoard(gameID,cell_number,playerID);  
                        stub.toggleTurn(gameID,playerID,opponentID);
                        //if the player's move results in a win
                        if(res.equals("win")){
                            System.out.println("WIN\nPlay another game ?");
                            //YES: 1    NO: 0
                            Integer ans = in.nextInt();
                            stub.playersResponse(gameID,ans);
                            while(true){
                                Integer t = stub.continueGame(gameID,playerID); 
                                if(t.equals(1)) {
                                    System.out.println("New game...");
                                    break;
                                }
                                else if(t.equals(2))
                                    System.exit(0);
                            }
                        }
                        //if the player's move results in a draw
                        else if(res.equals("draw")) {
                            System.out.println("DRAW\nPlay another game ?");
                            //YES: 1    NO: 0
                            Integer ans = in.nextInt();
                            stub.playersResponse(gameID,ans);
                            while(true){
                                Integer t = stub.continueGame(gameID,playerID); 
                                if(t.equals(1)) {
                                    System.out.println("New game...");
                                    break;
                                }
                                else if(t.equals(2))
                                    System.exit(0);
                            }
                        }
                    }
                    //Time out: user loses and prompts for new game
                    else {
                        System.out.println("TimeOut");
                        stub.validateBoard(gameID,-1,playerID);
                        stub.toggleTurn(gameID,playerID,opponentID);
                        System.out.println("LOSE\nPlay another game ?");
                        //YES: 1    NO: 0
                        Integer ans = in.nextInt();
                        stub.playersResponse(gameID,ans);
                        while(true){
                            Integer t = stub.continueGame(gameID,playerID); 
                            if(t.equals(1)) {
                                System.out.println("New game...");
                                break;
                            }
                            else if(t.equals(2))
                                System.exit(0);
                        }       
                        break;
                    }
                }
                else {
                    System.out.println("Wait for your turn...");
                    //while waiting check if oppoent's move brings any conclusive result
                    while(true) {
                        if(stub.madeMove(gameID,playerID)){
                            String res = stub.retrieveBoard(gameID);
                            if(res.equals("win")) {
                                System.out.println("LOSE\nPlay another game ?");
                                //YES: 1    NO: 0
                                Integer ans = in.nextInt();
                                stub.playersResponse(gameID,ans);
                                while(true){
                                    Integer t = stub.continueGame(gameID,playerID); 
                                    if(t.equals(1)) {
                                        System.out.println("New game...");
                                        break;
                                    }
                                    else if(t.equals(2))
                                        System.exit(0);
                                }                               
                            }
                            else if(res.equals("draw")){
                                System.out.println("DRAW\nPlay another game ?");
                                //YES: 1    NO: 0
                                Integer ans = in.nextInt();
                                stub.playersResponse(gameID,ans);
                                while(true){
                                    Integer t = stub.continueGame(gameID,playerID);
                                    if(t.equals(1)) {
                                        System.out.println("New game...");
                                        break;
                                    }
                                    else if(t.equals(2))
                                        System.exit(0);
                                }   
                            }
                            //Time out case: opponent loses and prompt for new game
                            else if(res.equals("lose")) {
                                System.out.println("WIN\nPlay another game ?");
                                //YES: 1    NO: 0
                                Integer ans = in.nextInt();
                                stub.playersResponse(gameID,ans);
                                while(true){
                                    Integer t = stub.continueGame(gameID,playerID);
                                    if(t.equals(1)) {
                                        System.out.println("New game...");
                                        break;
                                    }
                                    else if(t.equals(2))
                                        System.exit(0);
                                }   
                            }
                            break;
                        }
                    }
                }   
            }
        }catch(Exception e){System.out.println(e);}
    }
}