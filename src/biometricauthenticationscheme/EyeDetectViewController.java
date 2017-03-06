/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biometricauthenticationscheme;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

/**
 * FXML Controller class
 *
 * @author Isura Manchanayake
 */
public class EyeDetectViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    Circle circle;

    @FXML
    private Canvas canvas1;

    @FXML
    private Pane pane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Group root = new Group();

        GraphicsContext gc = canvas1.getGraphicsContext2D();
        circle = new Circle(50);
        circle.relocate(300, 100);

        MouseGestures mg = new MouseGestures();
        
        circle.setStroke(Color.FORESTGREEN);
        circle.setStrokeWidth(10);
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setFill(Color.AZURE);
        Text text = new Text("42");
        text.setBoundsType(TextBoundsType.VISUAL);
        StackPane stack = new StackPane();
        stack.getChildren().addAll(circle, text);
        pane.getChildren().add(stack);

        mg.makeDraggable(stack);
        //root.getChildren().addAll(canvas1, overlay);
        //primaryStage.setScene(new Scene(root, 600, 600));
        //primaryStage.show();   
        if (canvas1 == null) {
            System.out.println("shit");
        } else {
            System.out.println("ok");
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

                if (newTranslateX > 0 && newTranslateX < 500 && newTranslateY > 0 && newTranslateY < 500) {
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
