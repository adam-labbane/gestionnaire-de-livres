package com.example.cdi;

import com.example.cdi.Model.Book;
import com.example.cdi.Repository.BookRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class HelloController {

    @FXML
    private Label status;

    @FXML
    private Button delete;

    @FXML
    private Button add;

    @FXML
    private Button edit;

    @FXML
    private ListView bookList;

    @FXML
    private TextField title;

    @FXML
    private TextField isbn;

    @FXML
    private TextField author;

    @FXML
    private TextField year;

    @FXML
    private TextField pages;

    @FXML
    private TextArea description;

    @FXML
    private TextArea preview;

    @FXML
    private TextField searchField;

    @FXML
    private ImageView coverImage;

    @FXML
    private TextField imageUrl;
    private EmailNotificationService emailNotificationService;

    public HelloController() {
        emailNotificationService = new EmailNotificationService();
    }

    @FXML
    public void initialize() {

        List<Book> liste = new BookRepository().findAll();
        ObservableList items = FXCollections.observableArrayList();

        for (Book book : liste){
            items.add(book);
        }

        bookList.setItems(items);
        edit.setDisable(true);
        delete.setDisable(true);

        bookList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue instanceof Book) {
                Book selectedBook = (Book) newValue;
                System.out.println("Recette selectionnée : " + newValue);
                title.setText(selectedBook.getTitle());
                isbn.setText(selectedBook.getIsbn());
                author.setText(selectedBook.getAuthor());
                year.setText(String.valueOf(selectedBook.getYear()));
                pages.setText(String.valueOf(selectedBook.getPages()));
                description.setText(selectedBook.getDescription());
                preview.setText(selectedBook.getPreview());
                status.setText("Recette " + selectedBook.getTitle() + " affichée");

                String imgUrl = selectedBook.getImage();

                if (imgUrl != null && !imgUrl.isEmpty()) {
                    Image image = new Image(imgUrl);
                    coverImage.setImage(image);
                } else {
                    // Effacer l'ImageView si aucune image n'est disponible
                    coverImage.setImage(null);
                }
                imageUrl.setText(imgUrl);

                edit.setDisable(false);
                delete.setDisable(false);
            } else {
                // Effacer les détails si aucun livre n'est sélectionné
                title.clear();
                isbn.clear();
                author.clear();
                year.clear();
                pages.clear();
                description.clear();
                preview.clear();
                status.setText("Aucun livre sélectionné");
                coverImage.setImage(null);
                edit.setDisable(true);
                delete.setDisable(true);
            }
        });
    }

    @FXML
    protected void onButtonAddClick() {
        String titleText = title.getText();
        String isbnText = isbn.getText();
        boolean bookExists = false;

        // Vérifier si le livre existe déjà dans la liste
        for (Object item : bookList.getItems()) {
            if (item instanceof Book) {
                Book book = (Book) item;
                if (book.getIsbn().equals(isbnText)) {
                    bookExists = true;
                    break;
                }
            }
        }

        // Si le livre existe déjà, demander une confirmation
        if (bookExists) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("A book with the same ISBN already exists. Do you want to add it anyway?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                addBook(titleText, isbnText);
            }
        } else {
            addBook(titleText, isbnText);
        }
    }

    private void addBook(String titleText, String isbnText) {
        String authorText = author.getText();
        String yearText = year.getText();
        int pagesInt = Integer.parseInt(pages.getText());
        String descriptionText = description.getText();
        String imageUrl = coverImage.getImage() != null ? coverImage.getImage().getUrl() : "";

        Book newBook = new Book(titleText, isbnText, authorText, yearText, pagesInt, descriptionText, imageUrl);
        bookList.getItems().add(newBook);
        new BookRepository().save(newBook);
        bookList.refresh();
        System.out.println("New book added: " + newBook);
        status.setText("New book added: " + newBook);
        emailNotificationService.sendEmailNotification("add", newBook);
    }


    @FXML
    protected  void onButtonDeleteClick() {
        Book selectedBook = (Book) bookList.getSelectionModel().getSelectedItem();

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation suppression");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer le livre '" + selectedBook.getTitle() + "' ?");

        ButtonType confirmationButton = new ButtonType("Spprimer", ButtonBar.ButtonData.OK_DONE);
        confirmationAlert.getButtonTypes().setAll(confirmationButton, ButtonType.CANCEL);
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == confirmationButton) {
            // supprimer de la session local
            bookList.getItems().remove(selectedBook);

            // supprimer de la base de donnée
            new BookRepository().delete(selectedBook);

            status.setText("Livre supprimé");
            emailNotificationService.sendEmailNotification("suppression", selectedBook);
        }
    }

    @FXML
    protected void onButtonEditClick() {
        Book selectedBook = (Book) bookList.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            String titleText = title.getText();
            String isbnText = isbn.getText();
            String authorText = author.getText();
            String yearText = year.getText();
            String url = imageUrl.getText();
            int pagesInt = Integer.parseInt(pages.getText());
            String descriptionText = description.getText();

            selectedBook.setTitle(titleText);
            selectedBook.setIsbn(isbnText);
            selectedBook.setAuthor(authorText);
            selectedBook.setYear(yearText);
            selectedBook.setPages(pagesInt);
            selectedBook.setDescription(descriptionText);
            selectedBook.setImage(url);

            preview.setText(selectedBook.getPreview());
            new BookRepository().update(selectedBook);
            status.setText("Book modified");
            emailNotificationService.sendEmailNotification("modification", selectedBook);

            // Mettre à jour l'image de l'ImageView avec la nouvelle URL d'image
            if (!"Image not available".equals(url)) {
                Image cover = new Image(url);
                coverImage.setImage(cover);
            } else {
                coverImage.setImage(null); // Si l'URL de l'image est indisponible, afficher une image par défaut ou rien du tout
            }
        }
    }



    @FXML
    protected void onSearchButtonClick(){
        String isbnn = searchField.getText();
        System.out.println("isbn : " + isbnn);
        try {
            JSONObject bookData = OpenLibraryAPI.searchBookByISBN(isbnn);

            // Déclaration des variables à l'extérieur de la boucle
            String titre = null;
            String auteur = null;
            String publicationYear = null;
            int numberOfPages = 0;
            String resume = null;
            String image = null;

            // Vérification de la présence de la clé "records" dans l'objet JSON
            if (bookData.has(isbnn)) {
                JSONObject bookRecord = bookData.getJSONObject(isbnn);

                // Extraction des informations du livre
                try {
                    titre = bookRecord.getJSONObject("details").getString("title");
                } catch (JSONException e) {
                    titre = "Book not found";
                }

                try {
                    JSONArray authorsArray = bookRecord.getJSONObject("details").getJSONArray("authors");
                    if (authorsArray.length() > 0) {
                        auteur = authorsArray.getJSONObject(0).getString("name");
                    }
                } catch (JSONException e) {
                    auteur = "Book not found";
                }

                try {
                    publicationYear = bookRecord.getJSONObject("details").getString("publish_date");
                } catch (JSONException e) {
                    publicationYear = "Book not found";
                }

                numberOfPages = bookRecord.getJSONObject("details").optInt("number_of_pages", 0); // Utilisation de optInt pour gérer le cas où le nombre de pages n'est pas disponible

                try {
                    resume = bookRecord.getJSONObject("details").getString("description");
                } catch (JSONException e) {
                    resume = "Book not found";
                }

                try {
                    image = bookRecord.getString("thumbnail_url");
                } catch (JSONException e) {
                    image = "Image not available";
                }

                // Affichage des informations du livre
                System.out.println("Title: " + titre);
                System.out.println("Author: " + auteur);
                System.out.println("Publication Year: " + publicationYear);
                System.out.println("Number of Pages: " + numberOfPages);
                System.out.println("Description: " + resume);
                System.out.println("Thumbnail URL: " + image);

                // Création de l'objet Book à partir des informations obtenues
                Book newBook = new Book(titre, isbnn, auteur, publicationYear, numberOfPages, resume, image);
                preview.setText(newBook.getPreview());

                isbn.setText(isbnn);
                title.setText(titre);
                author.setText(auteur);
                year.setText(publicationYear);
                pages.setText(String.valueOf(numberOfPages));
                description.setText(resume);

                // Afficher l'image du livre
                if (!"Image not available".equals(image)) {
                    Image cover = new Image(image);
                    coverImage.setImage(cover);
                } else {
                    coverImage.setImage(null);
                }
            } else {
                System.out.println("No book found for the provided ISBN.");
                // Afficher "Book not found" dans tous les champs
                titre = "Book not found";
                auteur = "Book not found";
                publicationYear = "Book not found";
                resume = "Book not found";
                isbn.setText(isbnn);
                title.setText(titre);
                author.setText(auteur);
                year.setText(publicationYear);
                pages.setText("0");
                description.setText(resume);
                // Afficher "Book not found" dans l'aperçu
                preview.setText("Book not found");
                // Afficher l'image par défaut
                coverImage.setImage(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            status.setText("Books not found");
            // Afficher "Book not found" dans l'aperçu
            preview.setText("Book not found");
            // Afficher l'image par défaut
        }
    }









}
