/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biometricauthenticationscheme;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Isura Manchanayake
 */
public class EyeDetectViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    //private Circle circle;
    private final HashMap<PointName, StackPane> circles = new HashMap<>();

    @FXML
    private Canvas canvas1;
    @FXML
    public Pane pane;
    @FXML
    private ImageView userImageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        Group root = new Group();

        GraphicsContext gc = canvas1.getGraphicsContext2D();
//        userImageView.fitWidthProperty().bind(pane.prefWidthProperty());
//        pane.setPrefWidth(1100.0);

        setImage("/img/3.jpeg");
//        Image img = new Image("/img/leo.jpg");
//        userImageView.setImage(img);
//        userImageView.setFitWidth(960);
//        userImageView.setLayoutX(100);
//        userImageView.setLayoutY(100);
//        userImageView.setFitHeight(960.0 * img.getHeight() / img.getWidth());

//        StackPane st = createMarker(100, 100, "1");
//        circles.put(PointName.PT_PUPIL, st);
//        pane.getChildren().add(st);
        for (int i = 0; i < PointName.values().length; i++) {
            StackPane st = createMarker(10.0 + i * 60, 600.0, "" + i);
            circles.put(PointName.values()[i], st);
            pane.getChildren().add(st);
        }

//        for (PointName value : PointName.values()) {
//            System.out.println(circles.get(value).getTranslateX() + ", " + circles.get(value).getTranslateY());
//        }
        if (userImageView == null) {
            System.out.println("shit");
        }

    }

    public void setImage(String imgPath) {
        Image img = new Image(imgPath);
        userImageView.setImage(img);
        double pw = 962.0;
        double ph = 690.0;
        double ratio = img.getHeight() / img.getWidth();
        double layoutx, layouty, fitwidth, fitheight;
        if (ratio > ph / pw) {
            fitheight = ph;
            fitwidth = ph / ratio;
            layoutx = pw / 2 - fitwidth / 2;
            layouty = 0.0;
        } else {
            fitheight = pw * ratio;
            fitwidth = pw;
            layoutx = 0.0;
            layouty = ph / 2 - fitheight / 2;
        }
        userImageView.setLayoutX(2 + layoutx);
        userImageView.setLayoutY(layouty);
        userImageView.setFitWidth(fitwidth);
        userImageView.setFitHeight(fitheight);
    }

    private StackPane createMarker(double x, double y, String text) {
        Circle circle = new Circle(12);
        circle.setStroke(Color.FORESTGREEN);
        circle.setStrokeWidth(2);
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setFill(Color.rgb(240, 255, 255, 0.2));

        Text t = new Text(text);
        t.setBoundsType(TextBoundsType.VISUAL);
        t.setStyle("-fx-font: 10 consolas; -fx-font-weight: bolder");

        StackPane stack = new StackPane();
        stack.getChildren().addAll(circle, t);
        stack.setTranslateX(x);
        stack.setTranslateY(y);

        MouseGestures mg = new MouseGestures();
        mg.makeDraggable(stack);

        return stack;
    }

    @FXML
    public void browseBtnClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All images", "*.jpg", "*.png", "*.jpeg", "*.gif")
        );
        File file = fileChooser.showOpenDialog((Stage) pane.getScene().getWindow());
        if (file != null) {
            setImage("file:" + file.getPath());
        }
    }

    public static class MouseGestures {

        double orgSceneX, orgSceneY;
        double orgTranslateX, orgTranslateY;

        public void makeDraggable(Node node) {
            node.setOnMousePressed(circleOnMousePressedEventHandler);
            node.setOnMouseDragged(circleOnMouseDraggedEventHandler);
        }

        EventHandler<MouseEvent> circleOnMousePressedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                orgSceneX = t.getSceneX();
                orgSceneY = t.getSceneY();
                if (t.getSource() instanceof Circle) {
                    Circle p = ((Circle) (t.getSource()));
                    orgTranslateX = p.getCenterX();
                    orgTranslateY = p.getCenterY();
                } else {
                    Node p = ((Node) (t.getSource()));
                    orgTranslateX = p.getTranslateX();
                    orgTranslateY = p.getTranslateY();
                }
            }
        };

        EventHandler<MouseEvent> circleOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {

                double offsetX = t.getSceneX() - orgSceneX;
                double offsetY = t.getSceneY() - orgSceneY;

                double newTranslateX = orgTranslateX + offsetX;
                double newTranslateY = orgTranslateY + offsetY;
                if (true) {
//                if (newTranslateX > 0 && newTranslateX < 500 && newTranslateY > 0 && newTranslateY < 500) {
                    if (t.getSource() instanceof Circle) {
                        Circle p = ((Circle) (t.getSource()));
                        p.setCenterX(newTranslateX);
                        p.setCenterY(newTranslateY);
                        Bounds b = p.localToScene(p.getBoundsInLocal());
//                    System.out.println(b.getMinX() + " " + b.getMinY());
                    } else {
                        Node p = ((Node) (t.getSource()));
                        p.setTranslateX(newTranslateX);
                        p.setTranslateY(newTranslateY);
                    }
                }
            }
        };
    }

}
