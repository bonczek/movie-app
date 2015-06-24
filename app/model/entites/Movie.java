package model.entites;

import com.avaje.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Id;

public class Movie extends Model {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "original_language")
    private String orginalLanguage;

    @Column(name = "original_title")
    private String originalTitle;

}
