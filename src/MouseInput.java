import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter
{
    private Handler handler;
    private Camera camera; 
    private Game game;
    private SpriteSheet ss;

    
    public MouseInput(Handler handler, Camera camera, Game game, SpriteSheet ss)
    {
        this.handler = handler;
        this.camera = camera;
        this.game = game;
        this.ss = ss;
    }
    
    public void mousePressed(MouseEvent e)
    {
        int mx = (int) (e.getX() + camera.getX());
        int my = (int) (e.getY() + camera.getY());
        
        
        
        
        switch(Game.State)
        {
        
        case MENU:
        if(mx  >= 460 && mx <= 560)
        {
            if(my >= 150 && my <= 200)
            {
                Game.State = Game.STATE.GAME;
            }
        }
        
        if(mx >= 460 && mx <= 560)
        {
            if(my >= 250 && my <= 300)
            {
                Game.State = Game.STATE.HELP;
            }
        }
        if(mx >= 460 && mx <= 560)
        {
            if(my >= 350 && my <= 400)
            {
                Game.State = Game.STATE.QUIT;
            }
        }
        
        break;
        
        case HELP:
        if(mx >= 800 && mx <= 900)
        {
            if(my >= 200 && my <= 250)
            {
                Game.State = Game.STATE.MENU;
            }
        }
        break;
        
        case GAMEOVER:
            if(mx >= 1350 && mx <= 1450)
            {
                if(my >= 1300 && my <= 1350)
                {
                    Game.State = Game.STATE.QUIT;
                }
            }
        break;
        case WIN:
            if(mx >= 1950 && mx <= 2050)
            {
                if(my >= 1850 && my <= 1900)
                {
                    Game.State = Game.STATE.QUIT;
                }
            }
        break;
        case GAME:
        
            for(int i = 0; i < handler.object.size(); i++)
            {
                GameObject tempObject = handler.object.get(i);

                if(tempObject.getId() == ID.Player && game.ammo >= 1)
                {
                    handler.addObject(new Bullet(tempObject.getX()+16, tempObject.getY()+24, ID.Bullet, handler, mx, my, ss));
                    game.ammo --;
                }
            }
        break;
        }
    }
}