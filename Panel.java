
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;

public class Panel extends JPanel implements Runnable {

    final int ORIGINAL_TILE_SIZE = 16;
    final int scale = 3;

    final int FINAL_TILE_SIZE = scale * ORIGINAL_TILE_SIZE;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = FINAL_TILE_SIZE * maxScreenCol;
    final int screenHeight = FINAL_TILE_SIZE * maxScreenRow;

    Thread gameThread;

    public Panel() {
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
    }

    public void startGameThread() {
        
        gameThread = new Thread(this);
        gameThread.start();

    }

    @Override
    public void run() {

    }
    
}
