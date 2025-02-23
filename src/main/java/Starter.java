import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jasypt.util.text.BasicTextEncryptor;

public class Starter extends Application {

    public static void main(String[] args) {
        String key = "1234";
        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(key);

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginForm.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
