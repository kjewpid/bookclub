package se.yrgo.bookclub.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se.yrgo.bookclub.domain.Book;
import se.yrgo.bookclub.domain.Genres;
import se.yrgo.bookclub.repositories.BookRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class BookController {
    private BookRepository repository;

    public BookController(BookRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("message", "Welcome to the Book Club!");

        LocalDateTime dateAndTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
        model.addAttribute("dateAndTime", dateAndTime.format(formatter));

        return "home";
    }

    @GetMapping("/books")
    public String books(Model model) {
        model.addAttribute("books", repository.findAll());
        return "booklist";
    }

    @GetMapping("/filter")
    public String showGenreForm() {
        return "filter";
    }

    @GetMapping("/filter/genre")
    public String filterByGenre(@RequestParam("genre") String genre,
            Model model) {
        List<Book> books = repository.findByGenre(genre);

        model.addAttribute("books", books);

        if (books.isEmpty()) {
            model.addAttribute("error",
                    "No books found in genre '" + genre + "'");
        } else {
            model.addAttribute("selectedGenre", genre);
        }

        return "filter";
    }

    @GetMapping("/filter/title")
    public String filterByTitle(@RequestParam("title") String title,
            Model model) {
        List<Book> books = repository.findByTitleContainingIgnoreCase(title);

        model.addAttribute("books", books);

        if (books.isEmpty()) {
            model.addAttribute("error",
                    "No books found with title: " + title);
        } else {
            model.addAttribute("searchedTitle", title);
        }

        return "filter";
    }

    @GetMapping("/books/new")
    public String addBookForm() {
        return "newBook";
    }

    @PostMapping("/books/new")
    public String newBook(@RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("genre") String genre) {
        repository.save(new Book(title, author, genre));
        return "redirect:/books";
    }

    @ModelAttribute("genres")
    public List<String> availableGenres() {
        return Genres.ALL;
    }

    @GetMapping("/books/delete")
    public String showDeleteForm(Model model) {
        model.addAttribute("books", repository.findAll());
        return "deleteBook";
    }

    @PostMapping("/books/delete")
    public String deleteBook(@RequestParam("id") Long id) {
        repository.deleteById(id);
        return "redirect:/books";
    }
}
