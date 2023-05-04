import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class carRace extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    double finalVelocity;
    double finalDistance;
    double coeff;
    double force1;
    double force2;
    double mass1;
    double mass2;
    double acceleration1;
    double acceleration2;
    double finalAcceleration;
    int time;
    TextField friction;

    @Override
    public void start(Stage primaryStage) {

        // картинка левой машины(car1)
        Image image = new Image("leftsidecar.png");
        ImageView img = new ImageView(image);
        img.setFitWidth(200);
        img.setFitHeight(200);
        img.setX(200);
        img.setY(650);

        //картинка правой машины(car2)
        Image image2 = new Image("rightsidecar.png");
        ImageView img2 = new ImageView(image2);
        img2.setFitWidth(200);
        img2.setFitHeight(200);
        img2.setX(600);
        img2.setY(650);

        //создание и размещение прямоугольника первой машины
        Rectangle car1 = new Rectangle(200,100,Color.ORANGE);
        car1.setX(200);
        car1.setY(700);
        //создание и размещение прямоугольника второй машины
        Rectangle car2 = new Rectangle(200,100, Color.BLUE);
        car2.setX(600);
        car2.setY(700);
        //создание и размещение каната между машинами
        Rectangle rope = new Rectangle(200,10, Color.BROWN);
        rope.setX(400);
        rope.setY(760);
        //группировка всех объектов в каргруп
        Group carGroup = new Group();
        carGroup.getChildren().addAll(car1,car2,rope,img,img2);

        //инпут трения
        friction = new TextField("Input friction coefficient");
        friction.setLayoutX(410);
        friction.setLayoutY(550);

        //инпут силы и массы первой машины
        Group TextAreaCar1 =new Group();
        TextField car1F = new TextField("Input car 1 Force in N");
        car1F.setLayoutX(200);
        car1F.setLayoutY(0);
        TextField car1m = new TextField("Input car 1 mass in kg");
        car1m.setLayoutX(200);
        car1m.setLayoutY(50);
        //группируем и двигаем
        TextAreaCar1.getChildren().addAll(car1F,car1m);
        TextAreaCar1.setLayoutY(500);

        //инпут силы и массы второй машины
        Group TextAreaCar2 =new Group();
        TextField car2F = new TextField("Input car 2 Force in N");
        car2F.setLayoutX(600);
        car2F.setLayoutY(0);
        TextField car2m = new TextField("Input car 2 mass in kg");
        car2m.setLayoutX(600);
        car2m.setLayoutY(50);
        //группируем и двигаем
        TextAreaCar2.getChildren().addAll(car2F,car2m);
        TextAreaCar2.setLayoutY(500);

        //кнопка старта машин
        Button button = new Button("GO");
        //скрытие кнопки старта до того как пользователь не подтвердил значение силы и массы
        button.setLayoutX(10000);
        //логика кнопки
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                 time = 10;

                // F = m * a => a = F/m
                acceleration1 = (force1-frictionForce(mass1)) / mass1;
                acceleration2 = (force2-frictionForce(mass2)) / mass2;
                System.out.println("First car acceleration: " + acceleration1 + "  m/s2");
                System.out.println("Second car acceleration: " + acceleration2 + "  m/s2");

                //выбор какая машина поедет: едет та у которой ускорение больше
                if(acceleration1 > acceleration2){
                    finalAcceleration = acceleration1 - acceleration2;
                }
                else if (acceleration2 > acceleration1){
                    finalAcceleration = acceleration2 - acceleration1;
                }else {
                    finalAcceleration = 0;
                }

                // v = a * t
                double velocity1 = acceleration1 * time;
                double velocity2 = acceleration2 * time;
                finalVelocity = finalAcceleration * time;

                // s = v * t
                double distance1 = velocity1 * time;
                double distance2 = velocity2 * time;
                finalDistance = finalVelocity * time;
                System.out.println("Total distance: " + finalDistance);


                //я хз как работает таймлайн я его взял из прошлого проекта но он делает прикольный эффект дергания машин
                Timeline line = new Timeline(new KeyFrame(Duration.millis(16),event1 ->{

                    //непосредственно движение авто
                    TranslateTransition tt = new TranslateTransition(Duration.millis(time* 100) , carGroup);
                    //если ускорение 1 машины больше то едет 1 машина и если ускорение больше или равно 1 т.е не замедляется
                    if(acceleration1 > acceleration2 && finalAcceleration >= 1){
                        tt.setToX(carGroup.getLayoutX() - finalDistance);
                    }
                    //если ускорение 2 машины больше то едет 2 машина и если ускорение больше или равно 1 т.е не замедляется
                    else if(acceleration2 > acceleration1 && finalAcceleration >= 1){
                        tt.setToX(carGroup.getLayoutX() + finalDistance);
                    }
                    //во всех остальных случаях не двигать
                    else {
                        tt.setToX(carGroup.getLayoutX());
                    }
                    tt.play();

                }));

                line.setCycleCount(Animation.INDEFINITE);
                line.play();
                //исчезновение кнопки после запуска
                button.setLayoutX(10000);
            }
        });

        //кнопка подтверждающая введенные величины
        Button confrim = new Button("Confrim");
        confrim.setLayoutX(450);
        confrim.setLayoutY(500);
        confrim.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //получение значений первой машины
                force1 = Double.parseDouble(car1F.getText());
                System.out.println("Force first car: " + force1 + " N");
                mass1 = Double.parseDouble(car1m.getText());
                System.out.println("Mass first car: " + mass1 + " m");

                //получение значений второй машины
                force2 = Double.parseDouble(car2F.getText());
                System.out.println("Force second car: " + force2 + " N");
                mass2 = Double.parseDouble(car2m.getText());
                System.out.println("Mass second car: " + mass2 + " m");

                //получение значений коэфицента трения
                coeff = Double.parseDouble(friction.getText());
                System.out.println("Friction coefficient: " + coeff);
                System.out.println("");

                //релокейт ненужных значений
                TextAreaCar1.relocate(10000,10000);
                TextAreaCar2.relocate(10000,10000);
                confrim.relocate(10000,10000);
                button.setLayoutX(475);
                button.setLayoutY(550);
                friction.setLayoutX(10000);
            }
        });

        //создание земли под машинами
        Rectangle terra = new Rectangle(10000,500, Color.GREENYELLOW);
        terra.setY(800);
        Rectangle line = new Rectangle(10000, 3, Color.BLACK);
        line.setY(797);

        //группа
        Group root = new Group();
        root.getChildren().addAll(carGroup, button,TextAreaCar1,TextAreaCar2,confrim, friction,terra,line);

        // сцена
        Scene scene = new Scene(root, 1000, 1000);
        scene.setFill(Color.GAINSBORO);

        primaryStage.setTitle("Перетягивание каната");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //подсчет силы трения
    private double frictionForce(double mass) {
        coeff = Double.parseDouble(friction.getText());
        double mu = coeff; // friction coefficient
        double g = 9.81; // gravitational acceleration
        return mu * mass * g;
    }
}


