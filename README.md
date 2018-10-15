# Distributed Tic Tac Toe
- The aim of the project is to develop a distributed Tic Tac Toe, in a client-server setup using Java RMI protocol.
- You can think of the server as moderator conducting the game. The server will be maintaining the state of the game and also the server will be one providing APIs to inquire or change the state of board.
- Clients will be the player, which will inquire or play their chance by remotely calling the server APIs.

### Conduct
#### Server-Client Relationship
- Server will be up always.
- User will deal only with client. User should be prompted to start the game. As soon as she wants to play, client should connect to server prompting to join the game.
- At a time, only two clients can be connected to server. The server should send a Busy/Wait message in case third client wants to connect. 

#### Game Play
- When first client connects, the server should wait for the second client to connect and send a corresponding message to the first client.
- As soon as second client connects, it assigns player1/player2 randomly to the two clients and should start the game. Player 1 has ‘x’ marker and Player 2 has ‘o’ marker.
- Whichever player has to make a move will receive the current state of the board from the server and other player will wait for the other player to make a move. Respective messages should be displayed for both.
- Player 1 is requested to make a move first.
- As soon as Player 1 makes the move, updates in the board state will be made and Player 2 will receive the updated board state.
- When the game is over:
    - Player who won should get the **Win** message
    - Player who lost should get the **Lose** message
    - In case of draw, both should get **Draw** message.
    - Both should be prompted with **Play another game?** message. If both agrees, fresh game should start else both the clients disconnect and close.
* **Game Parallelization**
    * The server should be able to run multiple games in parallel.
    * If X clients connect to server, where X is even, X/2 games should run in parallel.
    * The players will play in the order they connect. If P1 and P2 connect, they play a game together. While the game for P1 and P2 is going on, if P3 and P4 connects, their game starts fresh irrespective of the first game.
- **Timer**
    * For each move, a 10 seconds timer should be started. If the player is not able to make a move in 10 seconds, she will lose the game and prompt for another game should be displayed.

### Open one terminal. Run :
    rmic TicTacToeImpl
    rmiregistry 5000

### Open another Terminal. Run :
    javac MyServer.java
    java MyServer

### Open client terminals. Run :
    javac MyClient.java
    java MyClient 