package Game;

import gamesframework.AFrameOnImage;
import gamesframework.Animation;
import gamesframework.GameScreen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FlappyBirds extends GameScreen {
    private BufferedImage birds;
    private Animation bird_anim;
    public static float g = 0.15f;
    private Bird bird;
    private Ground ground;
    private Chimneygroup chimneygroup;
    private int Point = 0;
    private int BEGIN_SCREEN = 0;
    private int GAMEPLAY_SCREEN = 1;
    private int GAMEOVER_SCREEN = 2;
    private int CurrentScreen = BEGIN_SCREEN;

    public FlappyBirds() {
        super(800,600);
        try{
            birds = ImageIO.read(new File("src/Assets/bird_sprite.png"));

        } catch (IOException ex) {}
        bird_anim = new Animation(70);

        AFrameOnImage f;

        f = new AFrameOnImage(0,0,60,60);
        bird_anim.AddFrame(f);
        f = new AFrameOnImage(60,0,60,60);
        bird_anim.AddFrame(f);
        f = new AFrameOnImage(120,0,60,60);
        bird_anim.AddFrame(f);

        bird = new Bird(350,350,50, 50);
        ground = new Ground();

        chimneygroup = new Chimneygroup();

        BeginGame();
    }


    public static void main(String[] args) {

        new FlappyBirds();
    }
    private void resetGame(){
        bird.setPos(350,250);
        bird.setVt(0);
        bird.setLive(true);
        Point = 0;
        chimneygroup.resetChimneys();
    }
    @Override
    public void GAME_UPDATE(long deltaTime) {
        if (CurrentScreen == BEGIN_SCREEN) {
            resetGame();
        }
        else if (CurrentScreen == GAMEPLAY_SCREEN){
            if(bird.getLive()){
                bird_anim.Update_Me(deltaTime);
            }
            bird.update(deltaTime);
            ground.Update();
            chimneygroup.update();

            for(int i=0; i<Chimneygroup.SIZE; i++){
                if(bird.getRect().intersects(chimneygroup.getChimney(i).getRect())){
                    if(bird.getLive()) bird.bupSound.play();
                    bird.setLive(false);
                    System.out.println("Set live = false");
                }
            }

            for(int i=0; i<Chimneygroup.SIZE; i++){
                if(bird.getPosX() > chimneygroup.getChimney(i).getPosX() && !chimneygroup.getChimney(i).getBehindBird() && i%2==0){
                    Point++;
                    bird.getMoneySound.play();
                    chimneygroup.getChimney(i).setIsBehindBird(true);
                }
            }

         if (bird.getPosY() + bird.getH() > ground.getYGround()){
             CurrentScreen = GAMEOVER_SCREEN;
         }
        }
        else{}


    }

    @Override
    public void GAME_PAINT(Graphics2D g2) {
        g2.setColor(Color.decode("#b8daef"));
        g2.fillRect(0,0, MASTER_WIDTH, MASTER_HEIGHT);

        chimneygroup.paint(g2);
        ground.Paint(g2);

        if (bird.getIsFlying())
            bird_anim.PaintAnims( (int) bird.getPosX(), (int) bird.getPosY(), birds, g2,0,-1);
        else
            bird_anim.PaintAnims( (int) bird.getPosX(), (int) bird.getPosY(), birds, g2,0,0);
        if (CurrentScreen == BEGIN_SCREEN){
            g2.setColor(Color.red);
            g2.drawString("Press space to play game", 200,300);
        }
        if (CurrentScreen == GAMEOVER_SCREEN){
            g2.setColor(Color.red);
            g2.drawString("Press space turn back begin screen",200,300);
        }

        g2.setColor(Color.red);
        g2.drawString("Point: "+Point, 20, 50 );
    }

    @Override
    public void KEY_ACTION(KeyEvent e, int Event) {
        if (CurrentScreen == BEGIN_SCREEN){
            CurrentScreen = GAMEPLAY_SCREEN;
        }
        else if (CurrentScreen == GAMEPLAY_SCREEN){
            if(bird.getLive()){
            bird.fly();
            }
        }
        else if (CurrentScreen == GAMEOVER_SCREEN){
            CurrentScreen = BEGIN_SCREEN;
        }
    }
}
