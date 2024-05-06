package com.example.cdi.Model;

import javax.persistence.*;

@Entity
@Table(name = "Book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // Id du livre
    private String title; // Titre du livre
    private String isbn; // ISBN du livre
    private String author; // Auteur du livre
    private String year; // Année de publication du livre
    private Integer pages; // Nombre de pages du livre
    private String description; // Description du livre
    private String image; // URL de l'image de la couverture du livre

    // Constructeurs
    public Book() {
    }

    public Book(String title, String isbn, String author, String year, Integer pages, String description, String image) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.year = year;
        this.pages = pages;
        this.description = description;
        this.image = image;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // Méthode pour obtenir un aperçu du livre
    public String getPreview() {
        return "Titre: " + getTitle() +
                "\nISBN: " + getIsbn() +
                "\nAuteur: " + getAuthor() +
                "\nAnnée: " + getYear() +
                "\nPages: " + getPages() +
                "\nDescription: " + getDescription() +
                "\nImage: " + getImage();
    }

    @Override
    public String toString() {
        return getTitle();
    }
}

