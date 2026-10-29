package se.yrgo.bookclub.domain;

import java.util.List;

public final class Genres {
    private Genres() {
    }

    public static List<String> ALL = List.of(
            "Fantasy",
            "Romance",
            "Horror",
            "Science Fiction",
            "Lifestyle"
    );
}
