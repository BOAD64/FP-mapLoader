import javax.imageio.ImageIO;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Map {

    private int width;
    private int height;

    private int tileWidth;
    private int tileHeight;

    private ArrayList<BufferedImage> tiles;

    private int[] map;

    public Map(String filename) {
        this.tiles = new ArrayList<>();
        JsonReader reader = Json.createReader(getClass().getResourceAsStream(filename));
        JsonObject root = reader.readObject();

        this.width = root.getInt("width");
        this.height = root.getInt("height");

        this.tileWidth = root.getInt("tilewidth");
        this.tileHeight = root.getInt("tileheight");

        try{
            BufferedImage tileSet = ImageIO.read(getClass().getResource("city_inside.png"));

            for (int y = 0; y < tileSet.getHeight(); y += this.tileHeight) {
                for (int x = 0; x < tileSet.getWidth(); x += this.tileWidth) {
                    this.tiles.add(tileSet.getSubimage(x, y, this.tileWidth, this.tileHeight));
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        int layersSize = root.getJsonArray("layers").size();

        this.map = new int[this.width * this.height * 3];
        for (int i = 0; i < layersSize; i++) {
            if(root.getJsonArray("layers").getJsonObject(i).getJsonArray("data") != null) {
                int dataSize = root.getJsonArray("layers").getJsonObject(i).getJsonArray("data").size();
                for (int j = 0; j < dataSize; j++) {
                    int index = root.getJsonArray("layers").getJsonObject(i).getJsonArray("data").getInt(j) - 1;
                    if (index >= 0) {
                        this.map[j] = index;
                    }
                }
            }
        }
    }

    public void draw(Graphics2D graphics){
        int i = 0;
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {


                if(this.map[i] == 0){
                    continue;
                }

                graphics.drawImage(
                        this.tiles.get(this.map[i]),
                        AffineTransform.getTranslateInstance(x * this.tileWidth, y * this.tileHeight),
                        null);
                i++;
            }
        }
    }
}
