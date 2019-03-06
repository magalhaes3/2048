import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

class GUI {

    private Game currentGame;
    private JFrame frame;
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 600;
    private static Map<Integer, Color> colorMap = new HashMap<>();

    GUI(Game game){
        initColorMap();
        this.currentGame = game;
        gui_init();
    }

    private void initColorMap() {
        colorMap.put(2, new Color(255, 178, 73));
        colorMap.put(4, new Color(230, 160, 66));
        colorMap.put(8, new Color(197, 136, 57));
        colorMap.put(16, new Color(247, 255, 71));
        colorMap.put(32, new Color(253, 255, 45));
        colorMap.put(64, new Color(237, 211, 26));
        colorMap.put(128, new Color(59, 218, 197));
        colorMap.put(256, new Color(55, 183, 218));
        colorMap.put(512, new Color(27, 146, 191));
        colorMap.put(1024, new Color(107, 112, 114));
        colorMap.put(2048, new Color(244, 255, 243));
    }


    private class EventHandler implements KeyListener {
        @Override
        public void keyReleased(KeyEvent e) {

        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT) {
                if (currentGame.isRunning())
                    currentGame.moveLeft();
            }

            if (key == KeyEvent.VK_RIGHT) {
                if (currentGame.isRunning())
                    currentGame.moveRight();
            }

            if (key == KeyEvent.VK_UP) {
                if (currentGame.isRunning())
                    currentGame.moveUp();
            }

            if (key == KeyEvent.VK_DOWN) {
                if (currentGame.isRunning())
                    currentGame.moveDown();
            }
            if (key == KeyEvent.VK_SPACE){
                //Function to start game
                if (!currentGame.isRunning())
                    currentGame.startRunning();
            }
            frame.repaint();
        }
    }

    private void gui_init(){
        frame = new JFrame("2048");
        frame.setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(new EventHandler());

        Panel panel = new Panel();
        frame.add(panel);
    }

    private class Panel extends JPanel{

        Panel(){
            super();
            this.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()));
            this.setVisible(true);
            this.setBackground(Color.BLACK);
        }

        public void paintComponent(Graphics gg){
            super.paintComponent(gg);
            Graphics2D g = (Graphics2D) gg;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(new Color(179, 97, 29));
            g.fillRect(12, 12, this.getWidth() - 24, this.getHeight() - 24);

            if (currentGame.isRunning()){
                for (int i = 0; i != currentGame.getGameBoard().length; i++){
                    for (int j = 0; j != currentGame.getGameBoard().length; j++){
                        int value = currentGame.getGameBoard()[i][j].getValue();
                        if(value != 0){
                            g.setColor(getColor(value));
                            int num = currentGame.getGameBoard().length;
                            int x = (j*(this.getWidth()-24)/num)+12;
                            int y = (i*(this.getHeight()-24)/num)+12;
                            int width = (this.getWidth()-24)/num;
                            int height = (this.getHeight()-24)/num;
                            g.fillRoundRect(x+5, y+5, width-10, height-10, 20, 20);

                            String text = "" + value;

                            Font font = new Font("Arial", Font.BOLD, width/2);
                            FontMetrics metrics = g.getFontMetrics(font);
                            Rectangle rect = new Rectangle(x+5, y+5, width-10, height-10);

                            int stringWidth = metrics.stringWidth(text);
                            while (stringWidth >= rect.width){
                                font = new Font("Arial", Font.BOLD, font.getSize()-10);
                                metrics = g.getFontMetrics(font);
                                stringWidth = metrics.stringWidth(text);
                            }

                            // Determine the X coordinate for the text
                            int x1 = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
                            // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
                            int y1 = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
                            // Set the font
                            g.setColor(Color.BLACK);
                            g.setFont(font);
                            // Draw the String
                            g.drawString(text, x1, y1);
                        }
                    }
                }
            } else {
                switch (currentGame.getFlag()){
                    case 0:
                        Font font = new Font("Arial", Font.BOLD, 100);
                        FontMetrics fm = g.getFontMetrics(font);

                        g.setColor(Color.BLACK);
                        g.setFont(font);
                        int titleLen = fm.stringWidth("2048");
                        int titleAlt = fm.getHeight();
                        g.drawString("2048", (this.getWidth()/2)-(titleLen/2), (this.getHeight()/3)-(titleAlt/2)+fm.getAscent());

                        font = new Font("Arial", Font.PLAIN, 30);
                        fm = g.getFontMetrics(font);

                        g.setColor(Color.BLACK);
                        g.setFont(font);

                        String text1 = "Press (space) to start the game";
                        String text2 = "use arrow keys to play";

                        int descLen = fm.stringWidth(text1);
                        int descAlt = fm.getHeight();

                        g.drawString(text1, (this.getWidth()/2)-(descLen/2), (this.getHeight()/2)-(descAlt/2)+fm.getAscent());

                        descLen = fm.stringWidth(text2);

                        g.drawString(text2, (this.getWidth()/2)-(descLen/2), (this.getHeight()/2)-(descAlt/2)+fm.getAscent()+30);
                        break;

                    case 1:
                        font = new Font("Arial", Font.BOLD, 70);
                        fm = g.getFontMetrics(font);

                        g.setColor(Color.BLACK);
                        g.setFont(font);
                        titleLen = fm.stringWidth("GAME OVER");
                        titleAlt = fm.getHeight();
                        g.drawString("GAME OVER", (this.getWidth()/2)-(titleLen/2), (this.getHeight()/2)-(titleAlt/2)+fm.getAscent());
                        break;
                }
            }
        }
    }

    private Color getColor(int value){
        return colorMap.get(value);
    }

    void show_gui(){
        frame.setVisible(true);
    }
}
