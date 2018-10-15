import java.rmi.*;
import java.util.*;
public interface TicTacToeContract extends Remote{

    public List<Integer> registerPlayer() throws RemoteException;
    public Integer assignGame(Integer playerID,Integer gameID) throws RemoteException;
    public Integer isItMyTurn(Integer gameID,Integer playerID,Integer opponentID) throws RemoteException;
    public void toggleTurn(Integer gameID,Integer playerID,Integer opponentID) throws RemoteException;
    public String registerMove(Integer gameID,Integer cell_number,Integer playerID) throws RemoteException;
    public String validateBoard(Integer gameID,Integer cell_number,Integer playerID) throws RemoteException;
    public void playersResponse(Integer gameID,Integer ans) throws RemoteException;
    public Integer continueGame(Integer gameID,Integer playerID) throws RemoteException;
    public boolean madeMove(Integer gameID,Integer playerID) throws RemoteException;
    public String retrieveBoard(Integer gameID) throws RemoteException;
}