package ScrabTacToe;

import java.lang.Character;

// describes a piece 
public class Piece {
	public char letter;
	public int row, col;
	
	public Piece (char l, int r, int c) {
		letter = Character.toLowerCase(l);
		row = r;
		col = c;
	}
	
	public Piece (Piece p) {
		letter = p.letter;
		row = p.row;
		col = p.col;
	}
	
	@Override
	public String toString() {
		return letter + "";
	}
	
	@Override
	public boolean equals (Object other) {
		if (other == null || getClass() != other.getClass())
    		return false;
    	Piece p = (Piece) other;
    	return p.row == row && p.col == col && p.letter == letter;
	}
	
	@Override
	public int hashCode() {
		int val = 1;
		for (int i = 0; i < 15 * row + col; i++)
			val *= 31;
		return letter * val;
	}
	
	public static void main (String[] args) {
		Piece p1 = new Piece('A', 0, 0);
		System.out.println(p1.hashCode());
		Piece p2 = new Piece('b', 0, 1);
		System.out.println(p2.hashCode());
		
	}
	

}
