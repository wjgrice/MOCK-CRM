package grice.wguc482pa;

/**
 *
 * @author William Grice
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This application models an inventory management program which represents Parts and part assemblies
 * identified as Products along with thier associated values and inventory levels.
 *
 *  FUTURE ENHANCEMENT - A potential new feature for consideration would be the inclusion of a reporting tool to create manual and automated
 *  reports for key metrics such as: inventory levels, value of inventory on hand, automated notification of low
 *  stock levels
 *
 * @author William Grice
 */
public class main extends Application{

    /**
     * The start method uses FXML to create the initial/main stage of the application.
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("mainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main method is the kickoff point of the application.  It populates test data starts the program.
     *
     * RUNTIME ERROR - During development I reached a point where my main screen would load and test data instances
     * would instantiate without error or failure, but the tableview element of the main screen would not display test
     * Parts items while the products pane would. After considerable time reviewing the code I discovered I forgot the
     * essential and easy step of actuall adding the newly created parts to the inventory data model. The silent
     * failure was corrected by adding Inventory.addPart(XXXX) after the creation of each new test item.
     *
     * @param args
     */
    public static void main(String[] args) {
        // Pre-populate test Part data.
        InHouse nut = new InHouse(Inventory.getCounter(),"Regular Nut",1.95, 69, 50, 5000, 47 );
        Inventory.addPart(nut);
        Outsourced locknut = new Outsourced(Inventory.getCounter(),"Lock Nut",2.95, 225, 130, 6000, "Nuts, LLC" );
        Inventory.addPart(locknut);
        Outsourced bolt = new Outsourced(Inventory.getCounter(),"Bolt",13.28, 96, 25, 10000, "Bolts, Inc." );
        Inventory.addPart(bolt);
        InHouse washer = new InHouse(Inventory.getCounter(),"Washer",3.36, 17 , 5, 15000,  83);
        Inventory.addPart(washer);

        // Pre-populate test Product data.
        Product fastener = new Product(Inventory.getCounter(), "Fastener", 15.98, 15, 5, 300);
        fastener.addAssociatedPart(nut);
        fastener.addAssociatedPart(bolt);
        fastener.addAssociatedPart(washer);
        Inventory.addProduct(fastener);
        Product secureFastener = new Product(Inventory.getCounter(), "Secure Fastener", 19.99, 33, 10, 250);
        secureFastener.addAssociatedPart(nut);
        secureFastener.addAssociatedPart(locknut);
        secureFastener.addAssociatedPart(washer);
        Inventory.addProduct(secureFastener);

        //Application start
        launch();
    }

}