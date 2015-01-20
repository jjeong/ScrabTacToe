package ScrabTacToe;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Color;


public class STTgui extends JPanel{

	private static final long serialVersionUID = 324231L;
	static public int SIZE = STT.SIZE;
    static private STT game = new STT();
 
    public STTgui() {
    	
        super(new GridLayout(1,0));
 
        STTboard b = new STTboard();
    	b.printInstructions();
    	
        JTable table = new JTable(b);
        table.setRowHeight(500 / SIZE);
        table.setGridColor(Color.DARK_GRAY);
 
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 500));
        
        add(scrollPane);
    }
 
    class STTboard extends AbstractTableModel {
    	
		private static final long serialVersionUID = 876781L;
		private int[] columnLabels = new int[SIZE];
        private Object[][] data = game.layout;
 
        public int getColumnCount() {
            return columnLabels.length;
        }
 
        public int getRowCount() {
            return data.length;
        }
 
 
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

 
        public boolean isCellEditable(int row, int col) {
            return (game.userTurn && data[row][col] == null);
        }
 

        public void setValueAt(Object value, int row, int col) {
        	char ch = ((String) value).charAt(0);
        	if (game.userPlay(ch, row, col) && ! game.done) {

	            fireTableCellUpdated(row, col);
	            game.AIplay();
	            
	            if (! game.done)
	            	printInstructions();
	            else
	            	game.conclude();
        	}
        }
        
        public void printInstructions (){
        	if (game.vowelTurn)
    			System.out.println("Your turn: click on a cell and enter a vowel.");
    		else
    			System.out.println("Your turn: click on a cell and enter a consonant.");
        }
    } // end of class STTboard
 

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Scrab-Tac-Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Create and set up the content pane.
        STTgui newContentPane = new STTgui();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

 
    public static void main(String[] args) {
    	
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
