/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biometricauthenticationscheme;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
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
public class EyeDetectSignUpController implements Initializable {

    /**
     * Initializes the controller class.
     */
    //private Circle circle;
    private final HashMap<PointName, Marker> markers = new HashMap<>();

    @FXML
    private Canvas canvas1;
    @FXML
    private Pane pane;
    @FXML
    private ImageView userImageView;
    @FXML
    private ImageView guideImageView;
    @FXML
    private TextField txtName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        Group root = new Group();

        GraphicsContext gc = canvas1.getGraphicsContext2D();
//        userImageView.fitWidthProperty().bind(pane.prefWidthProperty());
//        pane.setPrefWidth(1100.0);

        guideImageView.setImage(new Image("/img/GUIDE_PT_PUPIL.png"));

//        setImage("/img/3.jpeg");

        for (int i = 0; i < PointName.values().length; i++) {
            Marker st = createMarker(10.0 + i * 60, 600.0, "" + i, PointName.values()[i]);
            markers.put(PointName.values()[i], st);
            pane.getChildren().add(st);
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

    private Marker createMarker(double x, double y, String text, PointName pointName) {
        Circle circle = new Circle(9);
        circle.setStroke(Color.FORESTGREEN);
        circle.setStrokeWidth(2);
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setFill(Color.rgb(240, 255, 255, 0.2));

        Text t = new Text(text);
        t.setBoundsType(TextBoundsType.VISUAL);
        t.setStyle("-fx-font: 8 consolas; -fx-font-weight: bolder");

        Marker marker = new Marker();
        marker.getChildren().addAll(circle, t);
        marker.setTranslateX(x);
        marker.setTranslateY(y);
        marker.pointName = pointName;
        circle.styleProperty().bind(
                Bindings
                .when(marker.hoverProperty())
                .then(
                        new SimpleStringProperty("-fx-fill: rgb(240, 255, 255, 0.5)")
                )
                .otherwise(
                        new SimpleStringProperty("-fx-fill: rgb(240, 255, 255, 0.2)")
                )
        );
        MouseGestures mg = new MouseGestures();
        mg.makeDraggable(marker);

        return marker;
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

    @FXML
    void signUpBtnClicked() {
        String name = txtName.getText();
        System.out.println("dsf" + name + "sdf");
        if (name.trim().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Sign Up failed");
            alert.setHeaderText("");
            alert.setContentText("Fill the name field");
            alert.show();
            return;
        }

        FaceBiometric faceBiometric = new FaceBiometric();
        for (PointName nameKey : PointName.values()) {
            Marker marker = markers.get(nameKey);
            Bounds b = marker.localToScene(marker.getBoundsInLocal());
            double cx = (b.getMinX() + b.getMaxX()) / 2;
            double cy = (b.getMinY() + b.getMaxY()) / 2;
            faceBiometric.getMarkedPoints().put(nameKey, new PointXY(cx, cy));
        }

        PointXY ptOrigin = new PointXY(faceBiometric.getMarkedPoints().get(PointName.PT_PUPIL));
        for (PointName nameKey : PointName.values()) {
            faceBiometric.getMarkedPoints().get(nameKey).setRelativeXY(ptOrigin);
        }

        try (Writer writer = new BufferedWriter(new FileWriter(new File("data").getAbsoluteFile(), true))) {
            writer.write(txtName.getText() + "\n");
            for (PointName nameKey : PointName.values()) {
                writer.write(faceBiometric.getMarkedPoints().get(nameKey) + "\n");
            }

        } catch (Exception ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Sign Up failed");
            alert.setHeaderText("");
            alert.setContentText("File IO Error");
            alert.show();
            return;
        }
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Sign Up Successfull");
        alert.setHeaderText("");
        alert.setContentText(name + ", You are successfully signed up");
        alert.show();
    }

    @FXML
    void homeBtnClicked() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EyeDetectView.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Home");
            stage.setWidth(1280);
            stage.setHeight(720);
            stage.setResizable(false);
            stage.setScene(new Scene(root1));
            stage.show();
            ((Stage) pane.getScene().getWindow()).close();
        } catch (IOException ex) {
            Logger.getLogger(EyeDetectViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public class MouseGestures {

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
                } else if (t.getSource() instanceof Marker) {
                    Marker p = (Marker) (t.getSource());
                    orgTranslateX = p.getTranslateX();
                    orgTranslateY = p.getTranslateY();

                    Image img = new Image("/img/GUIDE_" + p.pointName.name() + ".png");
                    guideImageView.setImage(img);

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
                    } else if (t.getSource() instanceof Marker) {
                        Marker p = (Marker) (t.getSource());
                        p.setTranslateX(newTranslateX);
                        p.setTranslateY(newTranslateY);

                        Image img = new Image("/img/GUIDE_" + p.pointName.name() + ".png");
                        guideImageView.setImage(img);

                    } else {
                        Node p = ((Node) (t.getSource()));
                        p.setTranslateX(newTranslateX);
                        p.setTranslateY(newTranslateY);
                    }
                }
            }
        };
    }

    private class Marker extends StackPane {

        PointName pointName;
    }

}
