import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;
import static java.lang.Math.*;

class Tag {
    static int width = 30;
    static int height = 19;
    static int tileSize = 50;
    static void main(String[] args) {

        JFrame frame = new JFrame("Tag");
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.out.println((int) -1.7);

        Game game = new Game();
        frame.add(game);
        frame.pack();
        game.requestFocus();
        frame.setVisible(true);
    }
}

class Game extends JPanel implements ActionListener, KeyListener {
    String[][] maps = {{
            "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
            "x           gxxxx            x",
            "x                            x",
            "x x                        x x",
            "x                            x",
            "x                            x",
            "        xxx   xx  x  x        ",
            "                              ",
            "x                            x",
            "o     xx    x         xx     o",
            "x           x                x",
            "o           x                o",
            "x          xox  xxx          x",
            "                              ",
            "       b               r      ",
            "       xxxxx      x    xxx  x ",
            "x                 x          x",
            "x              x  x          x",
            "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
    }, {
            "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
            "x            xxxx            x",
            "x                            x",
            "x                            x",
            "x                            x",
            "x             g              x",
            "x       xxx   xx   xxx       x",
            "x                            x",
            "x                            x",
            "      xx              xx      ",
            "                              ",
            "                              ",
            "x    b     xxxxxxxx     r    x",
            "x                            x",
            "x                            x",
            "xxxxxxxx              xxxxxxxx",
            "x                            x",
            "x                            x",
            "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
    }, {

    }
    };
    String[] map;
    int width = Tag.width;
    int height = Tag.height;
    static int tileSize = Tag.tileSize;
    static HashSet<Block> blocks = new HashSet<>();
    Random random = new Random();
    boolean[] keys = new boolean[12];
    Timer loop;
    Player blue;
    Player red;
    Player green;
    double playerSpeed = 1.2;
    double gravity = -0.6;
    double jumpPower = 15;
    int cooldown = 0;
    int[] originalTimer = {0, 0, 3, 0};
    int[] digits = originalTimer.clone();
    char tagger;
    int fps = 100;
    int lag = 25;
    int tick = 0;
    boolean resets = true;
    boolean pause = true;
    boolean gameOver = false;
    Character cheats = null;
    boolean effects = true;

    Game() {
        setPreferredSize(new Dimension(width * tileSize, height * tileSize));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);
        loop = new Timer(1000 / fps, this);
        loop.start();

//        map = maps[random.nextInt(maps.length)];
        map = maps[0];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (map[i].charAt(j) == 'b') {
                    blue = new Player(j * tileSize, i * tileSize);
                }
                if (map[i].charAt(j) == 'r') {
                    red = new Player(j * tileSize, i * tileSize);
                }
                if (map[i].charAt(j) == 'g') {
                    green = new Player(j * tileSize, i * tileSize);
                }
                if (map[i].charAt(j) == 'x') {
                    blocks.add(new Block(j * tileSize, i * tileSize));
                }
            }
        }

        if (random.nextInt(3) == 0) {
            tagger = 'b';
        } else if (random.nextInt(2) == 0) {
            tagger = 'g';
        } else {
            tagger = 'r';
        }
    }

    static class Block {
        double x;
        double y;
        boolean bouncy = false;

        Block(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Block(int x, int y, boolean bouncy) {
            this.x = x;
            this.y = y;
            this.bouncy = bouncy;
        }
    }

    static class Player {
        double x;
        double y;
        double xVel = 0;
        double yVel = 0;

        Player(int x, int y) {
            this.x = x;
            this.y = y;
        }

        boolean isTouching(Block block) {
            return this.x < block.x + tileSize &&
                    this.x + tileSize > block.x &&
                    this.y < block.y + tileSize &&
                    this.y + tileSize > block.y;
        }

        boolean isOnGround() {
            for (Block block : blocks) {
                if (block.y - this.y == tileSize && abs(block.x - this.x) < tileSize) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'p' || e.getKeyChar() == 'P') {
            pause = !pause;
            System.out.println(pause);
        }
        if (e.getKeyChar() == '/' && cheats == 'r') {
            Player temp = red;
            red = blue;
            blue = temp;
        }
        if ((e.getKeyChar() == 'q' || e.getKeyChar() == 'Q') && cheats == 'b') {
            Player temp = red;
            red = blue;
            blue = temp;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            keys[0] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            keys[1] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            keys[2] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            keys[3] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            keys[4] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            keys[5] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            keys[6] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            keys[7] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_I) {
            keys[8] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_J) {
            keys[9] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_K) {
            keys[10] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_L) {
            keys[11] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            keys[0] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            keys[1] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            keys[2] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            keys[3] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            keys[4] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            keys[5] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            keys[6] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            keys[7] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_I) {
            keys[8] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_J) {
            keys[9] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_K) {
            keys[10] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_L) {
            keys[11] = false;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g.setColor(Color.cyan);
        g.fillRect((int) blue.x, (int) blue.y, tileSize, tileSize);
        g.setColor(Color.red);
        g.fillRect((int) red.x, (int) red.y, tileSize, tileSize);
        g.setColor(Color.green);
        g.fillRect((int) green.x, (int) green.y, tileSize, tileSize);

        g2d.setColor(Color.blue.darker());
        g2d.setStroke(new BasicStroke(6));
        for (int i = 1; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (map[i].charAt(j) == 'x' && map[i - 1].charAt(j) != 'x') {
                    g2d.drawLine(j * tileSize, i * tileSize, (j + 1) * tileSize, i * tileSize);
                }
            }
        }
        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width; j++) {
                if (map[i].charAt(j) == 'x' && map[i + 1].charAt(j) != 'x') {
                    g2d.drawLine(j * tileSize, (i + 1) * tileSize, (j + 1) * tileSize, (i + 1) * tileSize);
                }
            }
        }
        for (int i = 1; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (map[j].charAt(i) == 'x' && map[j].charAt(i - 1) != 'x') {
                    g2d.drawLine(i * tileSize, j * tileSize, i * tileSize, (j + 1) * tileSize);
                }
            }
        }
        for (int i = 0; i < width - 1; i++) {
            for (int j = 0; j < height; j++) {
                if (map[j].charAt(i) == 'x' && map[j].charAt(i + 1) != 'x') {
                    g2d.drawLine((i + 1) * tileSize, j * tileSize, (i + 1) * tileSize, (j + 1) * tileSize);
                }
            }
        }
        g2d.setColor(Color.yellow);
        g2d.setFont(new Font("Arial", Font.PLAIN, 70));
        g2d.drawString(digits[0] + "" + digits[1] + ":" + digits[2] + "" + digits[3], width * tileSize / 2 - tileSize * 7 / 4, tileSize * 3 / 2);
        g.setColor(Color.white);
        if (cooldown > 0) g.setColor(new Color(255,255,255, 127));
        if (tagger == 'b') {
            g.fillOval((int) blue.x + tileSize / 6, (int) blue.y + tileSize / 6, tileSize * 2 / 3, tileSize * 2 / 3);
            g.setColor(Color.black);
            g.fillOval((int) blue.x + tileSize / 3, (int) blue.y + tileSize / 3, tileSize / 3, tileSize / 3);
        } else if (tagger == 'r'){
            g.fillOval((int) red.x + tileSize / 6, (int) red.y + tileSize / 6, tileSize * 2 / 3, tileSize * 2 / 3);
            g.setColor(Color.black);
            g.fillOval((int) red.x + tileSize / 3, (int) red.y + tileSize / 3, tileSize / 3, tileSize / 3);
        } else {
            g.fillOval((int) green.x + tileSize / 6, (int) green.y + tileSize / 6, tileSize * 2 / 3, tileSize * 2 / 3);
            g.setColor(Color.black);
            g.fillOval((int) green.x + tileSize / 3, (int) green.y + tileSize / 3, tileSize / 3, tileSize / 3);
        }
        if (pause) {
            return;
        }

        if (keys[0] && blue.isOnGround()) {
            blue.yVel -= jumpPower;
        }
        blue.yVel -= gravity;
        blue.y += blue.yVel;
        for (Block block : blocks) {
            if (blue.isTouching(block)) {
                if (blue.yVel > 0) {
                    blue.y = block.y - tileSize;
                    if (block.bouncy) {
                        blue.yVel *= -0.5;
                    } else {
                        blue.yVel = 0;
                    }
                } else if (blue.yVel < 0) {
                    blue.y = block.y + tileSize;
                    blue.yVel = 0;
                }
            }
        }
        if (keys[1]) {
            blue.xVel -= playerSpeed;
        }
        if (keys[3]) {
            blue.xVel += playerSpeed;
        }
        blue.x += blue.xVel;
        blue.xVel *= 0.82;
        for (Block block : blocks) {
            if (blue.isTouching(block)) {
                if (blue.xVel < 0) {
                    blue.x = block.x + tileSize;
                    blue.xVel = 0;
                } else if (blue.xVel > 0) {
                    blue.x = block.x - tileSize;
                    blue.xVel = 0;
                }
            }
        }
        if (blue.x < -tileSize / 2.0) {
            blue.x = width * tileSize - tileSize / 2.0;
        }
        if (blue.x > tileSize * width - tileSize / 2.0) {
            blue.x = 0;
        }

        if (keys[4] && red.isOnGround()) {
            red.yVel -= jumpPower;
        }
        red.yVel -= gravity;
        red.y += red.yVel;
        for (Block block : blocks) {
            if (red.isTouching(block)) {
                if (red.yVel > 0) {
                    red.y = block.y - tileSize;
                    if (block.bouncy) {
                        red.yVel *= -0.5;
                    } else {
                        red.yVel = 0;
                    }
                } else if (red.yVel < 0) {
                    red.y = block.y + tileSize;
                    red.yVel = 0;
                }
            }
        }
        if (keys[5]) {
            red.xVel -= playerSpeed;
        }
        if (keys[7]) {
            red.xVel += playerSpeed;
        }
        red.x += red.xVel;
        red.xVel *= 0.82;
        for (Block block : blocks) {
            if (red.isTouching(block)) {
                if (red.xVel < 0) {
                    red.x = block.x + tileSize;
                    red.xVel = 0;
                } else if (red.xVel > 0) {
                    red.x = block.x - tileSize;
                    red.xVel = 0;
                }
            }
        }
        if (red.x < -tileSize / 2.0) {
            red.x = width * tileSize - tileSize / 2.0;
        }
        if (red.x > tileSize * width - tileSize / 2.0) {
            red.x = 0;
        }

        if (keys[8] && green.isOnGround()) {
            green.yVel -= jumpPower;
        }
        green.yVel -= gravity;
        green.y += green.yVel;
        for (Block block : blocks) {
            if (green.isTouching(block)) {
                if (green.yVel > 0) {
                    green.y = block.y - tileSize;
                    if (block.bouncy) {
                        green.yVel *= -0.5;
                    } else {
                        green.yVel = 0;
                    }
                } else if (green.yVel < 0) {
                    green.y = block.y + tileSize;
                    green.yVel = 0;
                }
            }
        }
        if (keys[9]) {
            green.xVel -= playerSpeed;
        }
        if (keys[11]) {
            green.xVel += playerSpeed;
        }
        green.x += green.xVel;
        green.xVel *= 0.82;
        for (Block block : blocks) {
            if (green.isTouching(block)) {
                if (green.xVel < 0) {
                    green.x = block.x + tileSize;
                    green.xVel = 0;
                } else if (green.xVel > 0) {
                    green.x = block.x - tileSize;
                    green.xVel = 0;
                }
            }
        }
        if (green.x < -tileSize / 2.0) {
            green.x = width * tileSize - tileSize / 2.0;
        }
        if (green.x > tileSize * width - tileSize / 2.0) {
            green.x = 0;
        }

        if (tagger == 'b') {
            if (blue.x < red.x + tileSize &&
                    blue.x + tileSize > red.x &&
                    blue.y < red.y + tileSize &&
                    blue.y + tileSize > red.y &&
                    cooldown < 0) {
                cooldown = 50;
                tagger = 'r';
                if (resets) {
                    digits = originalTimer.clone();
                }
            }
            if (blue.x < green.x + tileSize &&
                    blue.x + tileSize > green.x &&
                    blue.y < green.y + tileSize &&
                    blue.y + tileSize > green.y &&
                    cooldown < 0) {
                cooldown = 50;
                tagger = 'g';
                if (resets) {
                    digits = originalTimer.clone();
                }
            }
            
        } else if (tagger == 'r') {
            if (green.x < red.x + tileSize &&
                    green.x + tileSize > red.x &&
                    green.y < red.y + tileSize &&
                    green.y + tileSize > red.y &&
                    cooldown < 0) {
                cooldown = 50;
                tagger = 'g';
                if (resets) {
                    digits = originalTimer.clone();
                }
            }
            if (blue.x < red.x + tileSize &&
                    blue.x + tileSize > red.x &&
                    blue.y < red.y + tileSize &&
                    blue.y + tileSize > red.y &&
                    cooldown < 0) {
                cooldown = 50;
                tagger = 'b';
                if (resets) {
                    digits = originalTimer.clone();
                }
            }
        }
        else {
            if (blue.x < green.x + tileSize &&
                    blue.x + tileSize > green.x &&
                    blue.y < green.y + tileSize &&
                    blue.y + tileSize > green.y &&
                    cooldown < 0) {
                cooldown = 50;
                tagger = 'b';
                if (resets) {
                    digits = originalTimer.clone();
                }
            }
            if (green.x < red.x + tileSize &&
                    green.x + tileSize > red.x &&
                    green.y < red.y + tileSize &&
                    green.y + tileSize > red.y &&
                    cooldown < 0) {
                cooldown = 50;
                tagger = 'r';
                if (resets) {
                    digits = originalTimer.clone();
                }
            }
        }
        cooldown--;

        tick++;
        tick %= fps - lag;
        if (tick == 0) {
            digits[3]--;
            if (digits[3] == -1) {
                digits[3] = 9;
                digits[2]--;
                if (digits[2] == -1) {
                    digits[2] = 5;
                    digits[1]--;
                    if (digits[1] == -1) {
                        digits[0]--;
                        if (digits[0] == -1) {
                            digits = new int[]{0, 0, 0, 0};
                            g.setColor(Color.black);
                            g.fillRect(0, 0, this.getWidth(), this.getHeight());
                            gameOver = true;
                            if (tagger == 'b') {
                                g2d.setColor(Color.red);
                                g2d.setFont(new Font("Arial", Font.PLAIN, 200));
                                g2d.drawString("RED WINS",
                                        this.getWidth() / 2 - g2d.getFontMetrics(g2d.getFont()).stringWidth("RED WINS") / 2, this.getHeight() / 2);
                            } else if (tagger == 'r') {
                                g2d.setColor(Color.blue);
                                g2d.setFont(new Font("Arial", Font.PLAIN, 200));
                                g2d.drawString("BLUE WINS",
                                        this.getWidth() / 2 - g2d.getFontMetrics(g2d.getFont()).stringWidth("BLUE WINS") / 2, this.getHeight() / 2);
                            } else {
                                g2d.setColor(Color.green);
                                g2d.setFont(new Font("Arial", Font.PLAIN, 200));
                                g2d.drawString("GREEN WINS",
                                        this.getWidth() / 2 - g2d.getFontMetrics(g2d.getFont()).stringWidth("BLUE WINS") / 2, this.getHeight() / 2);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            repaint();
        }
    }
}