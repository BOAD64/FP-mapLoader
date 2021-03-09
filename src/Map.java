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

    private int layersSize;


    private ArrayList<BufferedImage> tiles;

    private int[][] map;

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

        layersSize = root.getJsonArray("layers").size();

        this.map = new int[this.width * this.height][layersSize];
        for (int layer = 0; layer < layersSize; layer++) {
            if(root.getJsonArray("layers").getJsonObject(layer).getJsonArray("data") != null) {
                int dataSize = root.getJsonArray("layers").getJsonObject(layer).getJsonArray("data").size();
                for (int i = 0; i < dataSize; i++) {
                    int tileIndex = root.getJsonArray("layers").getJsonObject(layer).getJsonArray("data").getInt(i) - 1;
                    if (tileIndex >= 0) {
                        this.map[i][layer] = tileIndex;
                    }
                }
            }
        }
    }

    public void draw(Graphics2D graphics){
        for (int layer = 0; layer < this.layersSize; layer++) {
            int i = 0;

            for (int y = 0; y < this.height; y++) {
                for (int x = 0; x < this.width; x++) {

                    if (this.map[i][layer] == 0){
                        i++;
                        continue;
                    }
                    graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                    graphics.drawImage(
                            this.tiles.get(this.map[i][layer]),
                            AffineTransform.getTranslateInstance(x * this.tileWidth, y * this.tileHeight),
                            null);
                    i++;

                }
            }
        }
    }
}
