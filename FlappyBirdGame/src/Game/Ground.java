package Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Ground {
    private BufferedImage groundImage;
    private int x1 = 0; int y1 = 500; int x2 = x1 + 830; int y2 = 500;

    public void Update() {
        x1-=2;
        x2-=2;

        if(x2<0) x1 = x2 + 830;
        if(x1<0) x2 = x1 + 830;
    }

    public Ground(){
        try {
            groundImage = ImageIO.read(new File("src/Assets/ground.png"));
        } catch (IOException ex){}
    }
    public void Paint(Graphics2D g2){
        g2.drawImage(groundImage, x1, y1, null);
        g2.drawImage(groundImage, x2, y2, null);
    }
    public int getYGround(){
        return y1;
    }
}
