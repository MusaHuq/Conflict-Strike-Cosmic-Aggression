
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;


public class Game extends Canvas implements Runnable 
{
    private static final long serialVersionUID = 1L;
    private boolean isRunning = false;
    private Thread thread;
    private Handler handler;
    private Camera camera;

    public static int enemyCounter = 0;
    public int ammo = 100;
    public int hp = 100;
    
    private BufferedImage level= null;
    private BufferedImage sprite_sheet = null;
    private SpriteSheet ss;
    private BufferedImage floor = null;
    
    public static enum STATE
    {
        MENU,
        GAME,
        HELP,
        QUIT,
        GAMEOVER,
        WIN;
    }
    
    public static STATE State = STATE.MENU;
    
    public Game()
    {
        Window window = new Window(1000, 563, "Wizard Game", this);
        start();
        

        handler = new Handler();
        camera = new Camera(0, 0);

        this.addKeyListener(new KeyInput(handler));
  
        BufferedImageLoader loader = new BufferedImageLoader();
        level = loader.loadImage("/wizard_level.png");
        sprite_sheet = loader.loadImage("/sprite_sheet.png");
        
        ss = new SpriteSheet(sprite_sheet);
        
        this.addMouseListener(new MouseInput(handler, camera, this, ss));   
        loadLevel(level);
    }

    
    private void start()
    {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }
    
    private void stop()
    {
        isRunning = false;
        try { 
            thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
        }
    }
    
    public void run()
    {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 45.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        
        
        while(isRunning) 
        {
         long now = System.nanoTime();
         delta += (now - lastTime) / ns;
         lastTime = now;
         while(delta >= 1) {
          tick();
          //updates++;
          delta--;
         }
         render();
         frames++;

         if(System.currentTimeMillis() - timer > 1000) 
         {
          timer += 1000;
          frames = 0;
          //updates = 0;
         }
        }
        stop(); 
    }
    
    public void tick()
    {
        if(State == STATE.GAME)
        {
        for(int i = 0; i < handler.object.size(); i++)
        {
            if (handler.object.get(i).getId() == ID.Player)
            {
                camera.tick(handler.object.get(i));
            }
        }
        handler.tick();
        }
    }
    
    public void render()
    {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null)
        {
            this.createBufferStrategy(3);
            return;
        }
        
        Graphics g = bs.getDrawGraphics();
        Graphics2D g2d =(Graphics2D) g;
        /////////////////

        g2d.translate(-camera.getX(), -camera.getY());
        
        for(int xx = 0; xx < 90*72; xx += 32)
        {
            for(int yy = 0;yy < 90*72; yy += 32)
            {
                g.drawImage(floor, xx, yy, null);
            }
        }
        
        switch (State)
        {
        case GAME: 
        handler.render(g);
        
        g2d.translate(camera.getX(), camera.getY());
        
        g.setColor(Color.gray);
        g.fillRect(5, 5, 200, 32);
        g.setColor(Color.green);
        g.fillRect(5, 5, hp*2, 32);
        g.setColor(Color.black);
        g.drawRect(5, 5, 200, 32);
        
        g.setColor(Color.white);
        g.drawString("Ammo: "+ ammo, 5, 50);
        
        
        break;
        
        
        case MENU:
            camera.setX(0);
            camera.setY(0);
            Rectangle playButton = new Rectangle(460, 150, 100, 50);
            Rectangle helpButton = new Rectangle(460, 250, 100, 50);
            Rectangle quitButton = new Rectangle(460, 350, 100, 50);
            
            Font fnt0 = new Font("arial", Font.BOLD, 50);
            g.setFont(fnt0);
            g.setColor(Color.white);
            g.drawString("CONFLICT STRIKE", 280, 50);
            g.drawString("COSMIC AGGRESSION", 230, 100);
            
            Font fnt1 = new Font("arial", Font.BOLD, 30);
            g.setFont(fnt1);
            g.drawString("Play", playButton.x + 20, playButton.y + 35);
            g.drawString("Help", helpButton.x + 20, helpButton.y + 35);
            g.drawString("Quit", quitButton.x + 20, quitButton.y + 35);
            g2d.draw(playButton);
            g2d.draw(helpButton);
            g2d.draw(quitButton);
        break;

        case HELP:
            Font fnt2 = new Font("arial", Font.BOLD, 50);
            g.setFont(fnt2);
            g.setColor(Color.white);
            g.drawString("CONTROLS", 350, 50);
            
            Font fnt3 = new Font("arial", Font.PLAIN, 30);
            g.setFont(fnt3);
            g.setColor(Color.red);
            g.drawString("W Key", 115, 100);
            g.drawString("A Key", 115, 200);
            g.drawString("S Key", 115, 300);
            g.drawString("D Key", 115, 400);
            g.drawString("Left Mouse Button", 115, 500);
            
            Font fnt4 = new Font("arial", Font.PLAIN, 20);
            g.setFont(fnt4);
            g.setColor(Color.white);
            g.drawString("- Use the W key to move forward.", 210, 100);
            g.drawString("- Use the A key to move to the left.", 210, 200);
            g.drawString("- Use the S key to move down.", 210, 300);
            g.drawString("- Use the D key to move to the right.", 210, 400);
            g.drawString("- Clicking the Left Mouse button to shoot enemies.", 365, 500);
            Rectangle backButton = new Rectangle(800, 200, 100, 50);
            g.drawString("BACK", backButton.x + 25, backButton.y + 35);
            g2d.draw(backButton);
            
        break;
        
        case QUIT:
            System.exit(0);
        break;
        
        case WIN:
            camera.setX(1500);
            camera.setY(1500);
            Font fnt7 = new Font("arial", Font.BOLD, 100);
            g.setFont(fnt7);
            g.setColor(Color.red);
            g.drawString("YOU WIN!!", 1750, 1800);
            
            Font fnt8 = new Font("arial", Font.PLAIN, 30);
            g.setFont(fnt8);
            g.setColor(Color.white);
            quitButton = new Rectangle(1950, 1850, 100, 50);
            g.drawString("Quit", quitButton.x + 23, quitButton.y + 35);
            g2d.draw(quitButton);
        break;
        
        case GAMEOVER:
            camera.setX(900);
            camera.setY(900);
            Font fnt5 = new Font("arial", Font.BOLD, 100);
            g.setFont(fnt5);
            g.setColor(Color.red);
            g.drawString("GAME OVER", 1100, 1200);
            
            Font fnt6 = new Font("arial", Font.PLAIN, 30);
            g.setFont(fnt6);
            g.setColor(Color.white);
            quitButton = new Rectangle(1350, 1300, 100, 50);
            g.drawString("Quit", quitButton.x + 23, quitButton.y + 35);
            g2d.draw(quitButton);
        break;
        ////////////////
        }
        g.dispose();
        bs.show();
    }
    //loading the level
    private void loadLevel(BufferedImage image)
    {
        int w = image.getWidth();
        int h = image.getHeight();
        
        for(int xx = 0; xx < w; xx++)
        {
            for(int yy = 0; yy < h; yy++)
            {
                int pixel = image.getRGB(xx, yy);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;
                
                if(green == 0 && blue == 0 && red == 0)
                {
                    floor = ss.grabImage(4, 2, 32, 32);
                }
                if(red == 255)
                {
                    handler.addObject(new Block(xx*32, yy*32, ID.Block, ss));
                }      
                if(red == 230 && green == 100 && blue == 255)
                {
                    handler.addObject(new Block6(xx*32, yy*32, ID.Block3, ss));
                }     
                if(red == 255 && green == 240)
                {
                    handler.addObject(new Block5(xx*32, yy*32, ID.Block2, ss));
                }                   
                if(blue == 255 && green == 0)
                {
                    handler.addObject(new Wizard(xx*32, yy*32, ID.Player, handler, this, ss));    
                }
                if(green == 255 && blue == 0)
                {
                    handler.addObject(new Enemy(xx*32, yy*32, ID.Enemy, handler, ss));   
                }
                if(green == 255 && blue == 255)
                {
                    handler.addObject(new Crate(xx*32, yy*32, ID.Crate, ss));
                }
                if(green == 100 && blue == 100)
                {
                    handler.addObject(new Block2(xx*32, yy*32, ID.Wall, ss));
                }
                if(green == 150 && blue == 150)
                {
                    handler.addObject(new Block3(xx*32, yy*32, ID.Wall2, ss));
                }
                if(green == 55 && blue == 55)
                {
                    handler.addObject(new Block4(xx*32, yy*32, ID.EndWall, ss));
                }
                if(red == 200 && green == 200)
                {
                    handler.addObject(new Enemy2(xx*32, yy*32, ID.Enemy2,handler, ss));
                }
                if(red == 230 && green == 230)
                {
                    handler.addObject(new Enemy3(xx*32, yy*32, ID.Enemy3,handler, ss));
                }
            }
        }
    }    
    
    public static void main(String args[])
    {
      new Game();
    }  
}
