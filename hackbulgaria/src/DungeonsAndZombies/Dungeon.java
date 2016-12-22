package DungeonsAndZombies;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Dungeon {
	private class Position {
		int x;
		int y;
		
		public void setX(int x) {
			this.x = x;
		}
		
		public void setY(int y) {
			this.y = y;
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
	}
	
	private static class Treasure {
		static final Weapon[] armory = {new Weapon("Silver lance", 40, 1), new Weapon("Silver bow", 40, 2), new Weapon("Silver sword", 40, 1)};
		static final Spell[] library = {new Spell("Fire", 45, 45, 2), new Spell("Nosferatu", 30, 45, 2), new Spell("Shine", 40, 40, 2)};
		static final Potion[] potions = {new Potion(25), new Potion(50), new Potion(100)};
		
		String contains;
		Treasureable treasure;
		
		public Treasure() {
			Random rand = new Random(); 
			int type = rand.nextInt(3); 
			int index = rand.nextInt(3); 
			switch (type) {
			case 0: 
				treasure = armory[index];
				contains = armory[index].toString();
				break;
			case 1: 
				treasure = library[index];
				contains = library[index].toString();
				break;
			case 2: 
				treasure = potions[index];
				contains = potions[index].toString(); 
				break;
			}
		}
		
		public String toString() {
			return "The treasure contains " + contains;
		}
		
		public Treasureable getTreasure() {
			return treasure;
		}
	}
	
	Position coords = new Position();
	Hero hero;
	AttackTypes currentType;
	boolean mapCleared = false;
	char[][] map = {
			{'S', '.', '#', '#', '.', '.', '.', '.', '.', 'T'},
			{'#', 'T', '#', '#', '.', '.', '#', '#', '#', '.'},
			{'#', '.', '#', '#', '#', 'E', '#', '#', '#', 'E'},
			{'#', '.', 'E', '.', '.', '.', '#', '#', '#', '.'},
			{'#', '#', '#', 'T', '#', '#', '#', '#', '#', 'G'}
	};
	
	
	
	public void printMap() {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				System.out.print(map[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("-------------------------------");
	}
	
	public boolean spawn(Hero hero) {
		this.hero = hero;
		boolean success = false;
		
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if ( map[i][j] == 'S' || map[i][j] == '.' ) {
					map[i][j] = 'H';
					coords.setX(i);
					coords.setY(j);
					success = true;
					break;
				}
				if (success) {
					break;
				}
			}
		}
		
		return success;
	}
	
	public void moveHero(String direction) {
		int heroX = coords.getX();
		int heroY = coords.getY();
		
		boolean validMove = false;
		int[] movement = {0, 0};
		
		switch(direction) {
		case "up":
			if (heroX > 0 && map[heroX - 1][heroY] != '#') {
				movement[0] -= 1; 
				validMove = true;
			}
			break;
		case "down": 
			if (heroX < map.length && map[heroX + 1][heroY] != '#') {
				movement[0] += 1; 
				validMove = true;
			}
			break;
		case "left": 
			if (heroY > 0 && map[heroX][heroY - 1] != '#') {
				movement[1] -= 1; 
				validMove = true;
			}
			break;
		case "right": 
			if (heroY < map[0].length && map[heroX][heroY + 1] != '#') {
				movement[1] += 1; 
				validMove = true;
			}
			break;
		}
		
		if (validMove) {
			map[heroX][heroY] = '.';
			coords.setX(heroX + movement[0]);
			coords.setY(heroY + movement[1]);
			trigger(heroX + movement[0], heroY + movement[1]);			
		} else {
			System.out.println("Invalid move. Turn was wasted.");
		}
	}
	
	private void trigger(int x, int y){
		char tyle = map[x][y];
		map[x][y] = 'H';
		
		switch(tyle){
		case 'E':
			System.out.println("You encountered an enemy!");
			startBattle(new Enemy(100, 100, 1));
			break;
		case 'T':
			System.out.println("You picked up a treasure!");
			startTreasure();
			break;
		case 'G':
			System.out.println("You cleared the map!");
			mapCleared = true;
			break;
		}
	}
	
	private void startBattle(Enemy enemy) {
		String attackOptions = "";
		if (hero.weapon != null) {
			attackOptions = "weapon";
		} 
		if (hero.spell != null) {
			attackOptions = (attackOptions.equals("")) ?  "spell" :"weapon and spell" ;
		}
		System.out.println("You can attack by " + attackOptions + ".");
		System.out.println("What is your choice?");
		List<String> validChoice =  Arrays.asList(attackOptions.split(" and "));
		Scanner s = new Scanner(System.in);
		String choice = s.nextLine();
		while (!validChoice.contains(choice)) {
			System.out.print("Please enter a valid choice: ");
			choice = s.nextLine();
			System.out.println();
		}
		
		
		while(hero.getHealth() > 0 && enemy.getHealth() > 0) {
			System.out.println(hero.knownAs() + " attacked the enemy for " + hero.getDamage(choice) + " damage.");
			enemy.takeDamage(hero.attack(choice));
			
			if(enemy.getHealth() > 0) {
				System.out.println("Enemy was left at " + enemy.getHealth() + "hp.");
				System.out.println("The enemy attacked " + hero.knownAs() + " for " + enemy.attack("weapon") + " damage.");
				hero.takeDamage(enemy.attack("weapon"));
				System.out.println(hero.knownAs() + " was left at " + hero.getHealth() + "hp.");
			}
		}
		
		if(enemy.getHealth() <= 0) {
			System.out.println(hero.knownAs() + " defeated the enemy!" );
		} else {
			System.out.println(hero.knownAs() + " was defeated!" );	
		}
	}
	
	private void startTreasure() {
		Treasure luck = new Treasure();
		System.out.println(luck);
		
		Scanner s = new Scanner(System.in);
		boolean invalidAnswer = true;
		while(invalidAnswer) {
			System.out.println("Would you like to use this item?");
			System.out.print("Your answer (yes/no): ");
			String response = s.nextLine();
			if (response.equals("yes")) {
				luck.getTreasure().activate(hero);
				invalidAnswer = false;
			} else if (response.equals("no")) {
				invalidAnswer = false;
			}
		}
	}
	
	public void startGame() {
		printMap();
		Scanner s = new Scanner(System.in);
		while(hero.getHealth() > 0 && !mapCleared) {
			if (hero.getMana() < 100) {
				hero.takeMana(hero.getManaRegen());
				System.out.print(hero.knownAs() + " restored " + hero.getManaRegen() + " mp. ");
			}
			System.out.println("HP = " + hero.getHealth() + ", MP = " + hero.getMana() + ".");
			System.out.println("Currently equiped:");
			System.out.println("\t" + hero.weapon);
			System.out.println("\t" + hero.spell);
			
			if (visibleEnemyInRange()) {
				//TODO ^^^^^^^^^^^^
			}
			System.out.print("Choose direction: ");
			String direction = s.nextLine();
			moveHero(direction);
			printMap();
		}
		
		if(mapCleared) {
			System.out.println(hero.knownAs() + " cleared the map!" );
		} else {
			hero.death();
		}
		s.close();
	}
	
	private boolean visibleEnemyInRange() {
		int range = hero.getRange();
		//TODO TEST!!!!!!!!!!!!!!
		//TODO finish this method after you make a Map class
		//TODO because it's a pain in the ass to check 2D char array
		
		return true;
	}

	
	public static void main(String[] args) {
		Dungeon level1 = new Dungeon();
		Hero emo = new Hero("Emo", "Hacker", 100, 100, 2);
		
		Weapon sword = new Weapon("Sword of Seals", 50, 1);
		emo.equip(sword);
		Spell dark = new Spell("Luna", 50, 40, 2);
		emo.learn(dark);
		
		level1.spawn(emo);
		level1.startGame();
	}
}