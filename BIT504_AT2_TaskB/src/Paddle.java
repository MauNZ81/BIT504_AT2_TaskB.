import java.awt.Color;

public class Paddle extends Sprite {
	private static final int paddle_Width = 10;
	private static final int paddle_Height = 100;
	private static final Color paddle_Color = Color.WHITE;
	private static final int distance_From_Edge = 40;
	
	public Paddle (Player player, int panelWidth, int panelHeight) {
		setWidth(paddle_Width);
		setHeight(paddle_Height);
		setColour(paddle_Color);
		int xPos;
		if(player == Player.One) {
			xPos = distance_From_Edge;
		}
		else {
			xPos = panelWidth - distance_From_Edge - getWidth();
		}
		setInitialPosition(xPos, panelHeight / 2 - (getHeight()/2));
		resetToInitialPosition();
	}
	
}
