
import javax.swing.*;

public class GameFrame extends JFrame {
	private static final long serialVersionUID = 1L; 

	GameFrame() { 
		this.add(new GamePanel()); 
		this.setTitle("Snake Game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false); 
		
		
		// fits frame to componenets 
		this.pack(); 
		this.setVisible(true);
		
		// sets window to the middle of computer 
		this.setLocationRelativeTo(null);
	}
	
	
}
