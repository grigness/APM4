package gui;

import domain.SearchEngine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import service.Service;

import java.util.List;

public class Controller {
    private Service service;
    @FXML
    private Button bestMatchButton;

    @FXML
    private TextField bestMatchTextField;

    @FXML
    private ListView<String> listViewBestMatch;


    @FXML
    private ListView<SearchEngine> listViewPrincipal;

    @FXML
    private ListView<SearchEngine> listViewSearch;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button sortButton;

    @FXML
    private TextField textFieldContents;

    @FXML
    private TextField textFieldKeyword;

    @FXML
    private Button updateButton;

    @FXML
    void bestMatch(ActionEvent event) {

        String bestMatching=bestMatchTextField.getText().toLowerCase();
        SearchEngine bestMatch=service.findBestMatching(bestMatching);
        if (bestMatch!=null) {
            ObservableList<String> doclist=FXCollections.observableArrayList();
            String doc="Name: "+bestMatch.getName()+" / Keywords: "+String.join(bestMatch.getKeywords())+
                    "/ Content: "+bestMatch.getContents()+"/Similarity"+service.bestMatching(bestMatching,bestMatch.getName());
            doclist.add(doc);
            listViewBestMatch.setItems(doclist);
        }else{
            listViewBestMatch.setItems(FXCollections.observableArrayList());
        }

    }

    @FXML
    void mouseClick(MouseEvent event) {
        SearchEngine selectedValue=listViewPrincipal.getSelectionModel().getSelectedItem();
        if(selectedValue != null){
            textFieldKeyword.setText(selectedValue.getKeywords());
            textFieldContents.setText(selectedValue.getContents());
        }else{
            textFieldContents.clear();
            textFieldKeyword.clear();
        }
    }


    @FXML
    void update(ActionEvent event) {
        String keyword=textFieldKeyword.getText();
        String content=textFieldContents.getText();

        SearchEngine selectedValue=listViewPrincipal.getSelectionModel().getSelectedItem();
        service.updateDocument(selectedValue,keyword,content);
        populateList();

    }

    @FXML
    void sort(ActionEvent event) {
        List<SearchEngine> sortedActivities=service.getSorted();
        ObservableList<SearchEngine>observableActivities=FXCollections.observableArrayList(sortedActivities);
        listViewPrincipal.setItems(observableActivities);
    }

    @FXML
    void search(ActionEvent event) {

        String searchText=searchTextField.getText();
        if(searchText.isEmpty()){
            populateList();
        }else{
            ObservableList<SearchEngine> getDocument=FXCollections.observableArrayList(service.searchByNameOrKeywords(searchTextField.getText()));
            listViewPrincipal.setItems(getDocument);
        }


    }


    public Controller(Service service) {
        this.service = service;
    }


    public void initialize() {
        populateList();
    }

    void populateList()
    {
        List<SearchEngine> documents = service.getDocuments();
        ObservableList<SearchEngine> obsList = FXCollections.observableList(documents);
        listViewPrincipal.setItems(obsList);
    }

}
