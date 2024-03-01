package ru.yandex.practicum.filmorate.model;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
public class Film {
    public static final LocalDate OLDEST_FILM = LocalDate.of(1895, 12, 28);

    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration; // minutes
    @Getter
    private final Set<Long> likes = new HashSet<>();
    @NotNull
    private Mpa mpa;
    @Getter
    private final List<Genre> genres = new ArrayList<>(); // ???

//    @Getter
//    private final Set<Genre> genres = new LinkedHashSet<>(); // ???

}
