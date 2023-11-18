import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random; 

public class GamePanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L; 

	// set the size of the window
	static final int SCREEN_WIDTH = 600; 
	static final int SCREEN_HEIGHT = 600; 
	
	// how many blocks it will be across 
	private static int unitSize = 1; // Default UNIT_SIZE
	static final int GAME_UNITS = ((SCREEN_WIDTH- 100) * (SCREEN_HEIGHT - 100)) / unitSize; 
	
	// set the delay
	static final int DELAY = 75; 
	
	// set the array holding snake coordinates
	final int x[] = new int [GAME_UNITS]; 
	final int y[] = new int [GAME_UNITS]; 
	
	// body length starts as 5 units
	int bodyLength = 5; 
	int applesEaten; 
	
	// apple coordinates
	int appleX; 
	int appleY; 
	
	// initial direction starts as down 
	char direction = 'D'; 
	
	// game isn't running until difficulty chosen 
	boolean running = false; 
	
	Timer timer; 
	Random random; 
	
	// game doesn't run until difficulty is selected
	boolean difficultySelected = false; 
	
	// create difficulty and start over buttons 
	JButton easyButton = new JButton("Easy"); 
	JButton mediumButton = new JButton("Medium"); 
	JButton hardButton = new JButton("Hard"); 
	JButton confirmButton = new JButton("Confirm"); 
	JButton startOverButton = new JButton("Start Over?"); 
	
	
	// constructor 
	GamePanel () {
		random = new Random(); 
		
		// set the basic screen stuff 
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)); 
		this.setBackground(Color.black); 
		this.setFocusable(true);
		
		
		this.addKeyListener(new MyKeyAdapter()); 

		// add action listeners to the e/m/h buttons  
		easyButton.addActionListener(e -> setDifficulty(20));
		mediumButton.addActionListener(e -> setDifficulty(16));
		hardButton.addActionListener(e -> setDifficulty(10));
		
		// add action listener to the confirm button 
		confirmButton.addActionListener(e -> { 
			difficultySelected = true; 
			this.remove(easyButton); 
			this.remove(mediumButton); 
			this.remove(hardButton); 
			this.remove(confirmButton); 
			timer = new Timer(DELAY, this); 
			timer.start(); 
			running = true; 
		});
		
		// add action listener to the start over button 
		startOverButton.addActionListener(e -> { 
			this.removeKeyListener(this.getKeyListeners()[0]); 
			this.remove(startOverButton); 
			running = true; 
			difficultySelected = false; 
			applesEaten = 0; 
			bodyLength = 5; 
			direction = 'D'; 
			repaint();
			timer.start();  
			startGame(); 
			
		}); 
			
		// start the game 
		startGame(); 
	}
	
	// sets the difficulty
	public void setDifficulty(int selectedDifficulty) {
		unitSize = selectedDifficulty; 
		newApple();   
	}
	
	// starts the game off by adding the difficulty buttons 
	public void startGame () {
		
		// doesn't run if the user confirms difficulty 
		if (!difficultySelected) { 
			// add all the buttons 
			this.add(confirmButton); 
			this.add(easyButton); 
			this.add(mediumButton); 
			this.add(hardButton);
		}
		snakePos(); 
	

	}
	

	
	// set the position of the snake 
	public void snakePos() { 
		// set the inital position 
		// DOESN'T WORK IN MEDIUM 
		int initialX = (int)((SCREEN_WIDTH - 100) / 2); 
		int initialY = (int)((SCREEN_HEIGHT - 100) /2); 
		
		for (int i = 0; i < bodyLength; i++) { 
			x[i] = initialX * unitSize; 
			y[i] = initialY * unitSize; 
		}
	}
	
	// paint or draw the snake and apple 
	public void paintComponent (Graphics g) { 
		super.paintComponent(g); 
		draw(g); 
	}

	// draw all of the graphics 
	public void draw (Graphics g) { 
		// if the difficulty has been chosen 
		if (running) { 
		
			// drawing grid on the screen 
			for (int i = 0; i <= (SCREEN_HEIGHT - 100) / unitSize; i++) { 
				// vertical lines 
				g.drawLine(50 + i * unitSize, 50, 50 + i * unitSize, (SCREEN_HEIGHT - 50)); 
				// horizontal lines
				g.drawLine(50, 50 + i * unitSize, (SCREEN_WIDTH - 50), 50 + i * unitSize); 
			}
			
		
			// draw the apple on the screen
			g.setColor(Color.red); 
			g.fillOval(appleX, appleY, unitSize, unitSize); 
		
			// draw the snake on the screen 
			for (int i = 0; i < bodyLength; i ++) { 
				// paint the head green 
				if (i == 0) { 
					g.setColor(Color.green); 
					g.fillRect(x[i], y[i], unitSize, unitSize); 
				}
				// paint the rest of the body some shade of blue 
				else { 
					Color value = new Color(random.nextInt(255)); 
					g.setColor(new Color(45,180,0)); 
					g.setColor(value); 
					g.fillRect(x[i], y[i], unitSize, unitSize); 
				}
			}
			
			// score text 
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont()); 
			
			// ensures the text appears on the middle of the screen 
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
		} 
		else { 
			// don't execute if the game hasn't started yet 
			if (difficultySelected) {
				gameOver(g); 
			} 
		}
	}
	
	// generate coordinates of a new apple 
	public void newApple () { 
		// identify the random x coordinate of the apple 
		appleX = (random.nextInt((int)((SCREEN_WIDTH - 100)/ unitSize)) * unitSize) + 50;
		
		// identify the random y coordinate of the apple 
		appleY = (random.nextInt((int)((SCREEN_HEIGHT - 100) / unitSize)) * unitSize) + 50; 
	}
	
	// allow the snake to move 
	public void move () { 
		for (int i = bodyLength; i > 0; i--) { 
			x[i] = x[i - 1]; 
			y[i] = y[i - 1]; 
		}
		
		// switch cases for the direction depending on what is chosen 
		switch(direction) { 
		// UP 
		case 'U' : 
			y[0] = y[0] - unitSize; 
			break; 
		// DOWN 
		case 'D' : 
			y[0] = y[0] + unitSize; 
			break; 
		//LEFT
		case 'L' : 
			x[0] = x[0] - unitSize; 
			break; 
		//RIGHT 
		case 'R' : 
			x[0] = x[0] + unitSize; 
			break; 
		} 
	}
	
	// check if the snake collides with the apple 
	public void checkApple () { 
		// if the snake's head collides with the apple, add one to length and create new apple 
		if ((x[0] == appleX) && (y[0] == appleY)) { 
			bodyLength++; 
			applesEaten++; 
			newApple(); 
		}
	}
	
	public void checkCollisions () { 
		// checks if head collides with body 
		for (int i = bodyLength; i > 0; i--) { 
			if ((x[0] == x[i]) && (y[0] == y[i])) { 
				running = false; 
			}
		}
		
		// checks if head collides with left border 
		if (x[0] < 50) { 
			running = false; 
		}
		
		// checks if head collides with right border 
		if (x[0] > SCREEN_WIDTH - 51) { 
			running = false; 
		}
		
		// checks if head collides with top border 
		if (y[0] < 50) { 
			running = false; 
		}
		
		// checks if head collides with bottom border 
		if (y[0] > SCREEN_HEIGHT - 51) { 
			running = false; 
		}
		
		if (!running) { 
			timer.stop(); 
		}
		
	}
	
	// create the game over sceen 
	public void gameOver (Graphics g) { 
		
		// game is no longer running 
		running = false; 
		
		// score display 
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont()); 
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
		
		// Game over text 
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont()); 
		g.drawString("GAME OVER", (SCREEN_WIDTH - metrics2.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT /2);
		
		// create a start over button 
		startOverButton.setBounds(SCREEN_WIDTH / 2 - 100, SCREEN_HEIGHT / 2 + 50, 200, 50);
		this.add(startOverButton); 
		
	}
	
	// identify if an action is performed 
	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) { 
			move(); 
			checkApple(); 
			checkCollisions(); 
			repaint();
		} 
		
	}
	
	public class MyKeyAdapter extends KeyAdapter { 
		@Override 
		public void keyPressed(KeyEvent e) { 
			switch(e.getKeyCode()) { 
			case KeyEvent.VK_LEFT: 
				if (direction != 'R') { 
					direction = 'L'; 
				}
				break; 
			
			case KeyEvent.VK_RIGHT: 
				if (direction != 'L') { 
					direction = 'R'; 
				}
				break; 
			
			case KeyEvent.VK_UP: 
				if (direction != 'D') { 
					direction = 'U'; 
				}
				break; 
				
			case KeyEvent.VK_DOWN: 
				if (direction != 'U') { 
					direction = 'D'; 
				}
				break; 
				
			}
		}
	} 
	
	

}