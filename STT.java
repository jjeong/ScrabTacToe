package ScrabTacToe;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Random;


public class STT {
	static public int SIZE = 5;
	static public int WORD_LEN = 3;
	public boolean done, userTurn, vowelTurn;
	public Piece [][] layout = new Piece [SIZE][SIZE];
	static public HashSet<String> dictionary = new HashSet<String>();
	public String created;
	
	public STT() {
		done = false;
		userTurn = true;
		vowelTurn = false;
		loadDictionary();
		printRules();
	}
	
	
	public STT (STT g) {
		done = g.done;
		userTurn = g.userTurn;
		vowelTurn = g.vowelTurn;
		layout = new Piece [SIZE][SIZE];
		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++)
				if (g.layout[i][j] != null)
					layout[i][j] = new Piece(g.layout[i][j]);
	}
	
	
	private void loadDictionary() {
		FileReader file;
		BufferedReader fileIn;
		String entry;
		try {
			file = new FileReader("words.txt");
			fileIn = new BufferedReader(file);
			while ((entry = fileIn.readLine()) != null) {
				if (entry.length() >= WORD_LEN)
					dictionary.add(entry);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	
	private void printRules() {
		System.out.println("=========================   RULES =========================");
    	System.out.println("1. The goal is to create a word consisting of at least 3 letters.");
    	System.out.println("2. The word must be a proper noun and contain only alphabets.");
    	System.out.println("3. The player must alternate between consonants and vowels.");
    	System.out.println("4. Everything else remains the same as Tic Tac Toe.");
    	System.out.println("===========================================================");
	}
	
	
    public void printInstructions() {
		if (vowelTurn)
			System.out.println("Your turn: enter a vowel, row, and col "
					+ "separated by space:");
		else
			System.out.println("Your turn: enter a consonant, row and col "
					+ "separated by space:");
    }
	
    
	public boolean addPiece (Piece p) {
		if (layout[p.row][p.col] != null)
			return false;
		layout[p.row][p.col] = p;
		
//		if (vowelTurn)
//			vowelTurn = false;
//		else
//			vowelTurn = true;
		
		return true;
	}
	
	
	private boolean removePiece (Piece p) {
		if (p == null)
			return false;
		layout[p.row][p.col] = null;
		return true;
	}
	
	
	private boolean wordCreated (Piece p) {
		int start, end;
		
		//check horizontal letters
		start = p.col;
		end = p.col;
		while (start - 1 >= 0 && layout[p.row][start - 1] != null)
			start--;
		while (end + 1 < SIZE && layout[p.row][end + 1] != null)
			end++;
		if (end - start > WORD_LEN - 2) {
			String word = "";
			for (int i = start; i <= end; i++)
				word += layout[p.row][i].letter;
			for (String entry : dictionary) {
				if (word.contains(entry)) {
					created = entry; // word created
					return true; // the game is done
				}
			}
		}
		
		// check vertical letters
		start = p.row;
		end = p.row;
		while (start - 1 >= 0 && layout[start - 1][p.col] != null)
			start--;
		while (end + 1 < SIZE && layout[end + 1][p.col] != null)
			end++;
		
		if (end - start > WORD_LEN - 2) {
			String word = "";
			for (int i = start; i <= end; i++)
				word += layout[i][p.col].letter;
			for (String entry : dictionary) {
				if (word.contains(entry)) {
					created = entry; // word created
					return true; // game is done
				}
			}
		}
		return false; // game continues
	}
	
	
	public boolean userPlay(char ch, int r, int c) {
		if (layout[r][c] != null) {
			System.out.println("Space already taken. Please try again.");
			return false;
		}
		
		if (! "abcdefghijklmnopqrstuvwxyz".contains((ch + "").toLowerCase())) {
			System.out.println("Must be an alphabet");
			return false;
		}
		
		if (isVowel(ch)) {
			if (! vowelTurn) {
				System.out.println("Must be a consonant");
				return false;
			}
			vowelTurn = false; // constant next
		}
		else {
			if (vowelTurn) {
				System.out.println("Must be a vowel");
				return false;
			}
			vowelTurn = true; // vowel next
		}
		
		Piece p = new Piece(ch, r, c);
		addPiece(p);
		done = wordCreated(p);
		if (done)
			conclude();
		userTurn = false;
		return true;
	}
	
	
	private boolean userPlay() {
		
		@SuppressWarnings("resource")
		Scanner userInput = new Scanner(System.in);
		printInstructions();
		try {
			while (true) {
				int[] inputs = {userInput.next().charAt(0), userInput.nextInt(),
						userInput.nextInt()};
			
				// check space
				if (layout[inputs[1]][inputs[2]] != null) {
					System.out.println("Space already taken. Please try again.");
					continue;
				}
				
				// check vowels/consonants
				if (isVowel((char) inputs[0])) {
					if (! vowelTurn) {
						System.out.println("Must be a consonants");
						continue;
					}
					vowelTurn = false; // constant next
				}
				else {
					if (vowelTurn) {
						System.out.println("Must be a vowel");
						continue;
					}
					vowelTurn = true; // vowel next
				}
				
				
				Piece p = new Piece((char) inputs[0], inputs[1], inputs[2]);
				addPiece(p);
				wordCreated(p);
				break;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		
		userTurn = false;
		return true;
	}
	
	
	private boolean isVowel(char c) {
		return "aeiou".contains(c + "");
	}

	
	public void play() {
		
		loadDictionary();
		
		while (! done) {
			if (userTurn)
				userPlay();
			else
				AIplay();
			
			System.out.println(this);
			System.out.println("=========================");
		}
		
		conclude();
	}
	
	public void conclude() {
		System.out.println("word created: " + created);
		if (userTurn)
			System.out.println("you win!");
		else
			System.out.println("you lost. better luck next time!");
	}
	
	
	@Override
	public String toString() {
		String result = "";
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (layout[i][j] == null)
					result += ' ';
				else
					result += layout[i][j];
			}
			result += "\n";
		}
		return result;
	}
	
	
	
	// ============= AI functions =============
	
	private boolean tryToWin(){
		int i, j;
		for (i = 0; i < SIZE; i++)
			for (j = 0; j < SIZE; j++) {
				if (layout[i][j] != null) {
					
					//get list of empty neighbors
					if (i - 1 >= 0 && layout[i - 1][j] == null)
						if (test(i - 1, j))
							return true;
					if (i + 1 < SIZE && layout[i + 1][j] == null)
						if (test(i + 1, j))
							return true;
					if (j - 1 >= 0 && layout[i][j - 1] == null)
						if (test(i, j - 1))
							return true;
					if (j + 1 < SIZE && layout[i][j + 1] == null)
						if (test(i, j + 1))
							return true;
				}
			}
		return false; // no winning move available
	}
	
	
	// helper function for tryToWin()
	private boolean test (int r, int c) {
		STT temp = new STT(this);
		for (char ch = 'a'; ch <= 'z'; ch++) {
			Piece p = new Piece(ch, r, c);
			if ((vowelTurn && isVowel(ch)) || (! vowelTurn && ! isVowel(ch))) {
				temp.addPiece(p);
				if (temp.wordCreated(p)) {
					addPiece(p);
					created = temp.created;
					return true;
				}
				else
					temp.removePiece(p);
			}
		}
		return false; // no winning move available
	}
	
	
	private boolean tryMoving (STT temp, Piece p) {
		
		if (temp.tryToWin())
			temp.removePiece(p);
		else {
			addPiece(p);
			created = temp.created;
			userTurn = true;
			return true;
		}
		// next move will cause AI to lose
		return false;
	}
	
	public boolean AIplay() {
		
		System.out.println("Computer's turn");
		
		if (tryToWin()) {
			done = true;
			return true;
		}
		
		/// prevent human from winning
		// if AI vowelTrun is different from human vowelTurn
			// for each space
				// if human turn is vowel
					// try adding each vowel
						// if it makes a word
							// add a constant
				// else
					// try adding consonants
						// if it makes a word
							// add a vowel

		
		// place an arbitrary letter next to existing letters
		STT temp = new STT (this);
		int i, j;
		for (i = 0; i < SIZE; i++)
			for (j = 0; j < SIZE; j++)
				if (layout[i][j] != null) {
					Piece p;
					if (i - 1 >= 0 && layout[i - 1][j] == null) {
						temp.addPiece( p = new Piece(pickRandomLetter(), i - 1, j) );
						if (tryMoving(temp, p))
							return true;
					}
					if (i + 1 < SIZE && layout[i + 1][j] == null) {
						temp.addPiece( p = new Piece(pickRandomLetter(), i + 1, j) );
						if (tryMoving(temp, p))
							return true;
					}
					if (j - 1 >= 0 && layout[i][j - 1] == null) {
						temp.addPiece( p = new Piece(pickRandomLetter(), i, j - 1) );
						if (tryMoving(temp, p))
							return true;
					}
					if (j + 1 < SIZE && layout[i][j + 1] == null) {
						temp.addPiece( p = new Piece(pickRandomLetter(), i, j + 1) );
						if (tryMoving(temp, p))
							return true;
					}
				}
		
		//// there are no neighboring pieces
		for (i = 0; i < SIZE; i++)
			for (j = 0; j < SIZE; j++)
				if (layout[i][j] == null) {
					// place a random letter
					addPiece( new Piece(pickRandomLetter(), i, j) );
					userTurn = true;
					return true;
				}
		// no move possible
		return false;
	}
	
	private char pickRandomLetter() {
		Random rand = new Random();
		String vowels = "aeiou";
		if (vowelTurn)
			return vowels.charAt(rand.nextInt(vowels.length()));
		String consonants = "bcdfghjklmnpqrstvwxyz";
		return consonants.charAt(rand.nextInt(consonants.length()));
	}
	
	// ======== end of AI functions ===========
	
	
	public static void main (String [] args) {

		STT b = new STT();
		b.play();
	}

}
