package edu.sdccd.cisc191.template;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class HomeworkTable<Homework> extends TableView {

    //TODO set table style
    public HomeworkTable() {
        this.setStyle("-fx-control-inner-background: #FBF6FC; -fx-border-color: #FDFCE5; -fx-border-width: 5");
    }
}
