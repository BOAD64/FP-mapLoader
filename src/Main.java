import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;

public class Main extends Application {

    private Map map;
    private ResizableCanvas canvas;

    @Override
    public void start(Stage stage) throws Exception {
        this.map = new Map("howardsMap.json");
        BorderPane pane = new BorderPane();
        this.canvas = new ResizableCanvas(g -> draw(g), pane);
        pane.setCenter(this.canvas);
        FXGraphics2D graphics = new FXGraphics2D(this.canvas.getGraphicsContext2D());
        new AnimationTimer() {
            long last = -1;
            @Override
            public void handle(long now) {
                if(last == -1)
                    last = now;
                update((now - last) / 1000000000.0);
                last = now;
                draw(graphics);
            }
        }.start();

        stage.setScene(new Scene(pane));
        stage.setTitle("Test");
        stage.show();
        draw(graphics);
    }

    public void draw(Graphics2D graphics) {
        graphics.setBackground(Color.black);
        graphics.clearRect(0,0,(int)this.canvas.getWidth(), (int)this.canvas.getHeight());
        this.map.draw(graphics);
    }

    public void update(double deltaTime) {
    }

    public static void main(String[] args) {
        launch(Main.class);
    }
}
