package edu.sdccd.cisc191.template;

import edu.sdccd.cisc191.template.models.*;
import edu.sdccd.cisc191.template.views.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Client extends Application {
    private final AppLabel quoteLabel = new AppLabel();
    private final AppLabel homeworkLabel = new AppLabel();
    private final AppLabel notesLabel = new AppLabel();
    private final TextArea notesArea = new TextArea();
    private final AppButton homeworkButton = new AppButton();
    private final AppButton subjectButton = new AppButton();
    private final AppLabel newHomework = new AppLabel();
    private final TextField inputHomeworkName = new TextField();
    private final CheckBox urgentCheck = new CheckBox();
    private final AppLabel completedLabel = new AppLabel();
    private final CheckBox completedCheck = new CheckBox();
    private final DatePicker inputDueDate = new DatePicker();
    private final ComboBox<String> subjectBox = new ComboBox<>();
    private final Subject userSubject = new Subject();
    private final TextField inputSubjectName = new TextField();
    private final TextField inputSubjectNumber = new TextField();
    private final AppButton timerButton = new AppButton();
    private final AppLabel timerLabel = new AppLabel();
    private final Timeline timeline = new Timeline();
    private int minutes = 0;
    private int seconds = 0;
    private int secondsTotal;
    private final TextField inputTimerMinutes = new TextField();
    private final AppLabel studyMessage = new AppLabel();
    private final AppButton snackButton = new AppButton();
    private final AppButton sandwichButton = new AppButton();
    private final AppButton dessertButton = new AppButton();
    private final AppLabel snackLabel = new AppLabel();
    private final AppLabel drinkLabel = new AppLabel();
    private final Sandwich sandwich = new Sandwich();
    private final Dessert dessert = new Dessert();
    private final HomeworkTable<Homework> homeworkTable = new HomeworkTable<>();
    private static final AppButton subjectSubmit = new AppButton();
    private static final AppButton homeworkSubmit = new AppButton();
    private static final AppButton timerSubmit = new AppButton();
    private final String[] drinkArray = {"Green Tea", "Black Tea", "Herbal Tea"};
    private final DrinkTree drinkTree = new DrinkTree();
    private final AppButton saveNotes = new AppButton();
    private String alertText = "";
    private final AppLabel alertLabel = new AppLabel();

    public Client() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        // opens connection to server to receive a quote to display
        Socket clientSocket = new Socket("localhost", 8000);
        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
        String quote = (String) in.readObject();
        clientSocket.close();

        //set label text, prompt text and button text
        homeworkLabel.setText("Homework");
        notesLabel.setText("Notes");
        newHomework.setText("New Homework");
        timerLabel.setText("00:00");
        subjectBox.setPromptText("Select One");
        notesArea.setPromptText("Type Notes Here");
        studyMessage.setText("Hello!");
        completedLabel.setText("Mark as Complete: ");
        snackLabel.setText("Choose Snack Type");
        drinkLabel.setText("");
        quoteLabel.setText(quote);
        timerButton.setText("Timer");
        subjectButton.setText("New Subject");
        snackButton.setText("Snack Generator");
        sandwichButton.setText("Savory");
        dessertButton.setText("Sweet");
        homeworkButton.setText("New Homework");
        saveNotes.setText("Save Notes");

        //set presets for visibility
        snackButton.setVisible(false);
        drinkLabel.setVisible(false);

        //set table columns
        homeworkTable.setEditable(true);
        TableColumn<Homework, String> subjectColumn = new TableColumn<>("Subject");
        subjectColumn.setMinWidth(120);
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        TableColumn<Homework, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(180);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Homework, String> dateColumn = new TableColumn<>("Due Date");
        dateColumn.setMinWidth(120);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        TableColumn<Homework, String> urgentColumn = new TableColumn<>("Urgent");
        urgentColumn.setMinWidth(50);
        urgentColumn.setCellValueFactory(new PropertyValueFactory<>("urgent"));
        homeworkTable.getColumns().addAll(subjectColumn, nameColumn, dateColumn, urgentColumn);

        //set BorderPane and Boxes for layout
        BorderPane root = new BorderPane();
        HBox timerRow = new HBox(10, timerButton, timerLabel, studyMessage, snackButton, saveNotes);
        HBox homeworkRow = new HBox(10,subjectButton, homeworkButton);
        HBox buttonBar = new HBox(10, homeworkRow, timerRow);
        HBox homeworkBar = new HBox(15, homeworkLabel, completedLabel, completedCheck);
        VBox homeworkBox = new VBox(5, homeworkBar, homeworkTable);
        VBox notesBox = new VBox(9, notesLabel, notesArea);
        HBox centerConsole = new HBox(5,homeworkBox, notesBox);
        HBox topLabels = new HBox(10, quoteLabel, alertLabel);

        //set preferred size for table and notes area
        notesArea.setPrefSize(400,400);
        homeworkTable.setPrefSize(500,400);

        //organize components in the Border Pane
        root.setTop(topLabels);
        root.setCenter(centerConsole);
        root.setBottom(buttonBar);

        //add padding around the components
        BorderPane.setMargin(topLabels, new Insets(10, 10, 5, 10));
        BorderPane.setMargin(centerConsole, new Insets(5, 10, 5, 10));
        BorderPane.setMargin(buttonBar, new Insets(5, 10, 10, 10));

        //TODO move this to a css file
        root.setStyle("-fx-background-color: #F2E3F5");
        notesArea.setStyle("-fx-border-color: #E6F5E3; -fx-border-width: 5; -fx-background-color: #E6F5E3");

        //load notes from text file to app
        File notesFile = new File("notes.txt");
        Scanner notesReader = new Scanner(notesFile);
        String input = "";
        String noteLine;
        while (notesReader.hasNextLine()) {
            noteLine = notesReader.nextLine();
            if (noteLine.isEmpty()) {
                break;
            }
            input += noteLine + "\n";
        }
        notesArea.setText(input);
        notesReader.close();

        //saves notes to text file
        saveNotes.setOnAction(event -> {
            try {
                FileWriter notesWriter = new FileWriter("notes.txt", false);
                notesWriter.write(notesArea.getText());
                notesWriter.close();
            }
            catch (IOException e2) {
                throw new RuntimeException(e2);
            }
        });

        //adds previously created homework to the app
        BufferedReader homeworkReader = new BufferedReader(new FileReader("homework.txt"));
        try {
            String line;
            //search for dashes separating types of homework information to create substrings later
            int[] indexesOfDash = new int[4];
            while ((line = homeworkReader.readLine()) != null) {
                indexesOfDash[0] = line.indexOf("--");
                for (int i = 1; i < 4; i++) {
                    indexesOfDash[i] = line.indexOf("--", indexesOfDash[i - 1] + 1);
                }
                //from line, returns a string of information
                String name = line.substring(0, indexesOfDash[0]);
                String subject = line.substring(indexesOfDash[0]+2, indexesOfDash[1]);
                boolean urgent = Boolean.parseBoolean(line.substring(indexesOfDash[1]+2, indexesOfDash[2]));
                String date = line.substring(indexesOfDash[2]+2);
                Homework newHwEntry = new Homework(name, subject, urgent, date);
                //add old homework to table
                homeworkTable.getItems().add(newHwEntry);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        homeworkReader.close();

        //Homework popup
        HomeworkPopup homeworkPopup = new HomeworkPopup();
        VBox homeworkAdding = homeworkPopup.getHomeworkAdding(inputHomeworkName, urgentCheck, inputDueDate, subjectBox, homeworkSubmit);
        homeworkPopup.getContent().addAll(homeworkAdding);

        //remove selected homework
        completedCheck.setOnAction(e -> {
            int index = homeworkTable.getSelectionModel().getSelectedIndex();
            if (index != -1 && completedCheck.isSelected()) {
                homeworkTable.getItems().remove(index);
                try {BufferedReader deleteReader = new BufferedReader(new FileReader("homework.txt"));
                    Stream<String> hwDataStream = deleteReader.lines();
                    String hwData = hwDataStream.collect(Collectors.joining("\n"));
                    String[] lines = hwData.split("\n", -1);
                    FileWriter deleteWriter = new FileWriter("homework.txt", false);
                    for (int i = 0; i < lines.length; i++) {
                        if (i != index) {
                            deleteWriter.write(lines[i] + '\n');
                        }
                    }

                    deleteReader.close();
                    deleteWriter.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                completedCheck.setSelected(false);
            }
        });

        //creates a new homework entry
        homeworkButton.setOnAction(e -> {
            if (!homeworkPopup.isShowing()) {
                homeworkPopup.show(stage);
                homeworkButton.setText("Cancel");
                homeworkSubmit.setText("Create");
                homeworkSubmit.setOnAction(e2 -> {
                    homeworkPopup.hide();
                    homeworkButton.setText("New Homework");
                    //get the homework entry information
                    String name = homeworkPopup.getName(inputHomeworkName);
                    String subject = homeworkPopup.getSubject(subjectBox);
                    boolean urgent = homeworkPopup.getUrgent(urgentCheck);
                    String date = homeworkPopup.getDate(inputDueDate);
                    //create a new homework entry with data inputted/selected by user
                    Homework newHwEntry = new Homework(name, subject, urgent, date);
                    //adds new homework entry to homeworkTable
                    homeworkTable.getItems().add(newHwEntry);
                    ///add homework to text file, separating pieces of data by using dashes
                    try {
                        FileWriter writer = new FileWriter("homework.txt",true);
                        writer.write(name + "--" + subject + "--" + urgent + "--" + date + '\n');
                        writer.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    //reset the values
                    homeworkPopup.reset(inputHomeworkName, urgentCheck, inputDueDate, subjectBox);
                });
            }
            else {
                homeworkPopup.hide();
                homeworkButton.setText("New Homework");
                //reset the values
                homeworkPopup.reset(inputHomeworkName, urgentCheck, inputDueDate, subjectBox);
            }
        });


        //Subject popup
        SubjectPopup subjectPopup = new SubjectPopup();
        VBox subjectAdding = subjectPopup.getSubjectAdding(inputSubjectName, inputSubjectNumber, subjectSubmit);
        subjectPopup.getContent().addAll(subjectAdding);

        //adds previously created subjects to the subject combobox
        BufferedReader subjectReader = new BufferedReader(new FileReader("subject.txt"));
        try {
            String line;
            while ((line = subjectReader.readLine()) != null) {
                userSubject.setSubjectName(line.substring(0, line.indexOf("--")));
                userSubject.setSubjectNumber(line.substring(line.indexOf("--") + 2));
                subjectBox.getItems().add(userSubject.getSubject());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        subjectReader.close();

        //creates a new subject and saves it to the ComboBox
        subjectButton.setOnAction(e -> {
            if (!subjectPopup.isShowing()) {
                subjectPopup.show(stage);
                subjectButton.setText("Cancel");
                subjectSubmit.setText("Create");
                //handles actions when Submit button is pressed
                subjectSubmit.setOnAction(e2 -> {
                    userSubject.setSubjectName(inputSubjectName.getText());
                    userSubject.setSubjectNumber(inputSubjectNumber.getText());
                    subjectBox.getItems().add(userSubject.getSubject());
                    subjectButton.setText("New Subject");
                    subjectPopup.hide();
                    //write new subject to text file to be used later
                    try {
                        FileWriter writer = new FileWriter("subject.txt", true);
                        writer.write(inputSubjectName.getText() + "--" + inputSubjectNumber.getText() + '\n');
                        writer.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    subjectPopup.reset(subjectButton, inputSubjectName, inputSubjectNumber);
                });
            }
            else {
                subjectPopup.hide();
                subjectPopup.reset(subjectButton, inputSubjectName, inputSubjectNumber);
            }
        });

        //timer popup
        TimerPopup timerPopup = new TimerPopup();
        VBox timerAdding = timerPopup.getTimerAdding(inputTimerMinutes, timerSubmit);
        timerPopup.getContent().add(timerAdding);

        //sets timer in minutes and timer label in mm:ss
        timerButton.setOnAction(e -> {
            if (!timerPopup.isShowing()) {
                timerPopup.show(stage);
                timerButton.setText("Cancel");
                snackButton.setVisible(false);
                timerSubmit.setOnAction(e2 -> {
                    timerPopup.hide();
                    secondsTotal = timerPopup.getSecondsTotal(inputTimerMinutes);
                    inputTimerMinutes.clear();
                    timerButton.setText("Timer");
                    studyMessage.setText("Time to Study!");
                    //updates time left every second
                    KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
                        secondsTotal--;
                        minutes = secondsTotal / 60;
                        seconds = secondsTotal % 60;
                        timerLabel.setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds));

                        //Makes alert message blink when there are 10 seconds left on the timer
                        if (secondsTotal == 10) {
                            new Thread(() -> {
                                try {
                                    while (true) {
                                        if (alertLabel.getText().trim().isEmpty())
                                            alertText = "Timer Almost Over";
                                        else
                                            alertText = "";

                                        Platform.runLater(() -> alertLabel.setText(alertText));

                                        //controls the speed of the blinking
                                        Thread.sleep(200);

                                        //stop the blinking once timer reaches 0
                                        if (secondsTotal == 0) {
                                            Thread.currentThread().interrupt();
                                            return;
                                        }
                                    }
                                }
                                catch (InterruptedException ignored) {
                                }
                            }).start();
                        }

                        if (secondsTotal == 0) {
                            timeline.stop();
                            studyMessage.setText("Take a Break!");
                            snackButton.setVisible(true);
                            alertLabel.setText("");
                        }
                    });
                    timeline.getKeyFrames().add(keyFrame);
                    timeline.setCycleCount(secondsTotal);
                    timeline.play();
                });
            }
            else {
                timerPopup.hide();
                inputTimerMinutes.clear();
                timerButton.setText("Timer");
            }
        });

        //snack popup
        SnackPopup snackPopup = new SnackPopup();
        HBox sandwichOrDessertRow = snackPopup.getSandwichOrDessert(sandwichButton, dessertButton);
        VBox snackPopupColumn = snackPopup.getSnackPopupColumn(sandwichOrDessertRow, snackLabel, drinkLabel);
        snackPopup.getContent().add(snackPopupColumn);

        //setup drink binary tree
        for (String s : drinkArray) {
            drinkTree.insertDrink(s);
        }

        //create a new snack
        this.snackButton.setOnAction((e) -> {
            if (!snackPopup.isShowing()) {
                snackPopup.show(stage);
                //create a savory sandwich
                snackButton.setText("Exit Generator");
                sandwichButton.setOnAction((event) -> {
                    sandwichOrDessertRow.setVisible(false);
                    snackLabel.setText(this.sandwich.getSnack());
                    drinkLabel.setVisible(true);
                    drinkLabel.setText("Enjoy it with some " + drinkTree.getDrink());
                });
                //create a sweet dessert
                dessertButton.setOnAction((event) -> {
                    sandwichOrDessertRow.setVisible(false);
                    snackLabel.setText(this.dessert.getSnack());
                    drinkLabel.setVisible(true);
                    drinkLabel.setText("Enjoy it with some " + drinkTree.getDrink());
                });
            }
            else {
                snackPopup.hide();
                snackPopup.reset(snackButton, sandwichOrDessertRow, snackLabel, drinkLabel);
            }
        });

        //create main scene
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Homework App");
        stage.show();
    }
}