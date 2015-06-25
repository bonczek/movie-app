package model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Movie {

    private String title;
    private float averageVote;
    private List<String> genres = new ArrayList<>();

    public Movie(String title, float averageVote) {
        this.title = title;
        this.averageVote = averageVote;
    }
}
