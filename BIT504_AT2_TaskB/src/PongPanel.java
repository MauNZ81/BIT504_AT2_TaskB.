import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;
// branch

public class PongPanel extends JPanel implements ActionListener, KeyListener{
	private final static Color BACKGROUND_COLOR = Color.BLACK;	
	private final static int TIMER_DELAY = 5;
	private final static int BALL_MOVEMENT_SPEED = 4;
	private final static int POINTS_TO_WIN = 11;
	int player1Score = 0, player2Score = 0;
	Player gamewinner;
	Ball ball;
	Paddle paddle1, paddle2;
	
	GameState gameState = GameState.Initialising;
	
	private void addScore(Player player) {
		if (player == Player.One) {
			player1Score ++;
		}
		else if (player == Player.Two) {
			player2Score ++;
		}
		
	}
	
	private void checkWin() {
		if(player1Score >= POINTS_TO_WIN ) {
			gamewinner = Player.One;
			gameState = GameState.GameOver;
		}
		else if (player2Score >= POINTS_TO_WIN ) {
			gamewinner = Player.Two;
			gameState = GameState.GameOver;
			
		}
		
	}
	
	private void moveObject(Sprite obj) {
		obj.setxPosition(obj.getxPosition()+ obj.getxVelocity(), getWidth());
		obj.setyPosition(obj.getyPosition()+ obj.getyVelocity(), getHeight());
	}
	
	private void resetBall() {
		ball.resetToInitialPosition();
	}
	
	private void checkPaddleBounce() {
		if(ball.getxVelocity() <0 && ball.getRectangle().intersects(paddle1.getRectangle())){
			ball.setxVelocity(BALL_MOVEMENT_SPEED);
		}
		if(ball.getxVelocity()>0 && ball.getRectangle().intersects(paddle2.getRectangle())) {
			ball.setxVelocity(-BALL_MOVEMENT_SPEED);
		}
		
	}
	
	private void checkWallBounce() {
		if(ball.getxPosition() <=0) {
			// ball hitting left side of screen
			ball.setxVelocity(-ball.getxVelocity());
			addScore(Player.One);
			resetBall();
		}
		else if (ball.getxPosition() >= getWidth() - ball.getWidth()) {
			// ball hitting right side of screen
			ball.setxVelocity(-ball.getxVelocity());
			addScore(Player.Two);
			resetBall();
		}
		if(ball.getyPosition() <=0 || ball.getyPosition() >= getHeight() - ball.getHeight()) {
			// hit top or bottom of screen
			ball.setyVelocity(-ball.getyVelocity());
		}
	}
	
	public void createObjects() {
		ball = new Ball (getWidth(), getHeight());
		paddle1 = new Paddle (Player.One, getWidth(), getHeight());
		paddle2 = new Paddle (Player.Two, getWidth(), getHeight());
	}
	
	public PongPanel() {
		setBackground(BACKGROUND_COLOR);
		Timer timer = new Timer(TIMER_DELAY, this);
			timer.start();
			addKeyListener(this);
			setFocusable(true);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		paintDottedLine(g);
		if(gameState !=GameState.Initialising) {
			paintSprite(g,ball);
			paintSprite(g,paddle1);
			paintSprite(g,paddle2);
			paintScores(g);
			paintWin(g);
		}
		
	}
	
	private void paintDottedLine(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		Stroke dashed = new BasicStroke(3,BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {9}, 0);
		g2d.setStroke(dashed);
		g2d.setPaint(Color.WHITE);
		g2d.drawLine(getWidth()/2,0, getWidth()/2, getHeight());
		g2d.dispose();
	}
	
	private void update() {
		
		switch(gameState) {
		case Initialising: {
			createObjects();
			gameState = GameState.Playing;
			ball.setxVelocity(BALL_MOVEMENT_SPEED);
			ball.setyVelocity(BALL_MOVEMENT_SPEED);
			
		}
		case Playing:{
			moveObject(paddle1);
			moveObject(paddle2);
			moveObject(ball);
			checkWallBounce();
			checkPaddleBounce();
			checkWin();
			break;
		}
		case GameOver:{
			break;
		}
		}
		
	}
	
	private void paintSprite(Graphics g, Sprite sprite) {
		g.setColor(sprite.getcolour());
		g.fillRect(sprite.getxPosition(), sprite.getyPosition(),sprite.getWidth(), sprite.getHeight());
	}
	
	@Override
	public void keyTyped(KeyEvent event) {

	
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.VK_UP) {
			paddle2.setyVelocity(-4);
		}
		else if (event.getKeyCode() == KeyEvent.VK_DOWN) {
			paddle2.setyVelocity(4);
		}
		
		if(event.getKeyCode() == KeyEvent.VK_W) {
			paddle1.setyVelocity(-4);
		}
		else if (event.getKeyCode() == KeyEvent.VK_S) {
			paddle1.setyVelocity(4);
		}
		
				
	}

	@Override
	public void keyReleased(KeyEvent event) {
		if(event.getKeyCode()==KeyEvent.VK_UP || event.getKeyCode() == KeyEvent.VK_DOWN) {
			paddle2.setyVelocity(0);
		}
		
		if(event.getKeyCode()==KeyEvent.VK_W || event.getKeyCode() == KeyEvent.VK_S) {
			paddle1.setyVelocity(0);
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		update();
		repaint();
	}

	public void paintScores(Graphics g) {
		int xPadding = 100;
		int yPadding = 100;
		int fontSize = 50;
		Font scoreFont = new Font ("Serif", Font.BOLD, fontSize);
		String leftScore = Integer.toString(player1Score);
		String rightScore = Integer.toString(player2Score);
		g.setFont(scoreFont);
		g.drawString(leftScore, xPadding, yPadding);
		g.drawString(rightScore, getWidth()-xPadding, yPadding);
				
	}

	public void paintWin(Graphics g) {
		int xPadding = 300;
		int yPadding = 200;
		int fontSize = 50;
		Font scoreFont = new Font ("Serif", Font.BOLD, fontSize);
		String leftScore = "WIN";
		String rightScore = "WIN";
		g.setFont(scoreFont);
		if (gamewinner == Player.One) {
			g.drawString(leftScore, xPadding, yPadding);
		}
		else if(gamewinner == Player.Two) {
			g.drawString(rightScore, getWidth()-xPadding, yPadding);
		}
		
	}
}
