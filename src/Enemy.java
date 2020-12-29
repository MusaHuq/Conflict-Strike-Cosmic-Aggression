
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Enemy extends GameObject
{
    private Handler handler;
    Random r = new Random();
    int choose = 0;
    int hp = 100;
    
    private BufferedImage[] enemy_image = new BufferedImage[3];
    
    Animation anim;
    
    public Enemy(int x, int y, ID id, Handler handler, SpriteSheet ss)
    {
        super(x, y, id, ss);
        this.handler = handler;

        enemy_image[0] = ss.grabImage(4, 1, 32, 32);
        enemy_image[1] = ss.grabImage(5, 1, 32, 32);
        enemy_image[2] = ss.grabImage(6, 1, 32, 32);
        
        anim = new Animation(3, enemy_image[0], enemy_image[1], enemy_image[2]);

    }
    
    public void tick() 
    {
        x += velX;
        y += velY;
        int maxSpeed = 5;
        choose = r.nextInt(10);
        
                for(int i = 0; i < handler.object.size(); i++)
                {
                    GameObject tempObject = handler.object.get(i);
                    
                    if(tempObject.getId() == ID.Block || tempObject.getId() ==  ID.Wall || tempObject.getId() ==  ID.Wall2 || tempObject.getId() ==  ID.EndWall|| tempObject.getId() ==  ID.Block2 || tempObject.getId() ==  ID.Block3)
                    {
                        if(getBoundsBig().intersects(tempObject.getBounds()))
                        {
                            x += (velX *5) * -1;
                            y += (velY *5) * -1;
                            velX *= -1;
                            velY *= -1;
                        }
                        else if(choose == 0)
                        {
                            velX = (r.nextInt(2*maxSpeed+1) - maxSpeed);
                            velY = (r.nextInt(2*maxSpeed+1) - maxSpeed);
                        }
                    }
                    
                    if(tempObject.getId() == ID.Bullet)
                    {
                        if(getBounds().intersects(tempObject.getBounds()))
                        {
                            hp -= 50;
                            handler.removeObject(tempObject);                            
                        }
                    }
                    
                }
         anim.runAnimation();
         if(hp <= 0) 
         {
             handler.removeObject(this);
             Game.enemyCounter++;
             System.out.println(Game.enemyCounter);
         }
         
    }
    
    public void render(Graphics g) 
    {
        anim.drawAnimation(g, x, y, 0);
    }

    public Rectangle getBounds() 
    {
        return new Rectangle(x, y, 32, 32);
    }
    
    public Rectangle getBoundsBig() 
    {
        return new Rectangle(x-16, y-16, 64, 64);
    }
    
}