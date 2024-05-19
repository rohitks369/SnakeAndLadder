package Driver;

import java.util.*;

import models.Ladder;
import models.Player;
import models.Snake;
import service.SnakeAndLadderService;

public class Driver {

	public static void main(String[] args) {
		System.out.println("WELCOME TO GAME");
		Scanner scanner = new Scanner(System.in);		
		
		//snakes
		int noOfSnakes=scanner.nextInt();
		List<Snake> snakes=new ArrayList<Snake>();
		
		for(int i=0;i<noOfSnakes;i++) {
			snakes.add(new Snake(scanner.nextInt(), scanner.nextInt()));
		}
		
		//Ladder
		int noOfLadder=scanner.nextInt();
		List<Ladder> ladders=new ArrayList<Ladder>();
		
		for(int i=0;i<noOfLadder;i++) {
			ladders.add(new Ladder(scanner.nextInt(), scanner.nextInt()));
		}
		
		//player
		int noOfPlayer=scanner.nextInt();
		List<Player> players=new ArrayList<Player>();
		
		for(int i=0;i<noOfPlayer;i++) {
			players.add(new Player(scanner.next()));
		}
		
		SnakeAndLadderService snakeAndLadderService=new SnakeAndLadderService();
		snakeAndLadderService.setSnakes(snakes);
		snakeAndLadderService.setLadder(ladders);
		snakeAndLadderService.setPlayers(players);
		
		snakeAndLadderService.startGame();
		
	}

}
