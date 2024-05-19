package service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.management.Query;

import models.Ladder;
import models.Player;
import models.Snake;
import models.SnakeAndLadderBoard;

public class SnakeAndLadderService {
	private SnakeAndLadderBoard snakeAndLadderBoard;
	private int initialNumberOfPlayers;
	private Queue<Player> players;
	private boolean isGamecompleted;

	private int noOfDices; // Optional rule 1
	private boolean shouldGameContinueTillLastPlayer; // Optional rule 3
	private boolean shouldAllowedMultipleDiceRollOnSix; // Optional rule 4

	private static final int DEFAULT_BOARD_SIZE = 100;
	private static final int NO_OF_DICES = 1;

	public SnakeAndLadderService(int boardSize) {
		this.snakeAndLadderBoard = new SnakeAndLadderBoard(boardSize);
		this.noOfDices = SnakeAndLadderService.NO_OF_DICES;
	}

	public SnakeAndLadderService() {
		this(SnakeAndLadderService.DEFAULT_BOARD_SIZE);
	}

	// setter for making program for extensible
	public void SetNoOfDices(int noOfDices) {
		this.noOfDices = noOfDices;
	}

	public void setShouldGameContinueTillLastPlayer(boolean shouldGameContinueTillLastPlayer) {
		this.shouldGameContinueTillLastPlayer = shouldGameContinueTillLastPlayer;
	}

	public void setShouldAllowedMultipleDiceRollOnSix(boolean shouldAllowedMultipleDiceRollOnSix) {
		this.shouldAllowedMultipleDiceRollOnSix = shouldAllowedMultipleDiceRollOnSix;
	}

	// Initial the player
	public void setPlayers(List<Player> players) {
		this.players = new LinkedList<Player>();
		this.initialNumberOfPlayers = players.size();

		Map<String, Integer> playersPieces = new HashMap();
		for (Player player : players) {
			this.players.add(player);
			playersPieces.put(player.getId(), 0);
		}

		snakeAndLadderBoard.setPlayerPieces(playersPieces);
	}

	public void setSnakes(List<Snake> snakes) {
		snakeAndLadderBoard.setSnakes(snakes);
	}

	public void setLadder(List<Ladder> ladders) {
		snakeAndLadderBoard.setLadders(ladders);
	}

	// business logic

	// start the game
	public void startGame() {
		while (!isGamecompleted) {
			int totalDiceValue = getTotalValueAfterDiceRoll();

			Player currentPlayer = players.poll();

			movePlayer(currentPlayer, totalDiceValue);

			if (hasPlayerWon(currentPlayer)) {
				System.out.println(currentPlayer.getName() + " win the game");
				snakeAndLadderBoard.getPlayerPieces().remove(currentPlayer.getId());
				isGamecompleted=true;
			} else {
				players.add(currentPlayer);
			}

		}
	}

	private boolean hasPlayerWon(Player currentPlayer) {
		int playerPosition=snakeAndLadderBoard.getPlayerPieces().get(currentPlayer.getId());
		int winningPosition=snakeAndLadderBoard.getSize();
		
		return playerPosition==winningPosition;
	}

	private int getTotalValueAfterDiceRoll() {
		return DiceService.roll();
	}
	
	private void movePlayer(Player player, int position) {
		int oldPosition = snakeAndLadderBoard.getPlayerPieces().get(player.getId());
		int newPosition = oldPosition + position;

		int boardSize = snakeAndLadderBoard.getSize();

		if (newPosition > boardSize) {
			newPosition = oldPosition;
		} else {
			newPosition = getNewPositionAfterGoingThroughSnakesAndLadders(newPosition);
		}

		snakeAndLadderBoard.getPlayerPieces().put(player.getId(), newPosition);

		System.out.println(
				player.getName() + " rolled a " + position + " and moved from " + oldPosition + " to " + newPosition);

	}
	
	private int getNewPositionAfterGoingThroughSnakesAndLadders(int newPosition) {
		int previousPosition;

		do {
			previousPosition = newPosition;

			// snake
			for (Snake snake : snakeAndLadderBoard.getSnakes()) {
				if (snake.getStart() == newPosition) {
					newPosition = snake.getEnd();
				}
			}

			// ladder
			for (Ladder ladder : snakeAndLadderBoard.getLadders()) {
				if (ladder.getStart() == newPosition) {
					newPosition = ladder.getEnd();
				}
			}

		} while (newPosition != previousPosition);
		return newPosition;
	}


}
