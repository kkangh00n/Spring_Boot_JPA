package jpabook.jpashop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jpabook.jpashop.controller.BookForm;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("B")
public class Book extends Item {
    private String author;
    private String isbn;

    public static Book createBook(BookForm bookForm){
        Book book = new Book();
        book.setName(book.getName());
        book.setPrice(book.getPrice());
        book.setStockQuantity(book.getStockQuantity());
        book.setAuthor(book.getAuthor());
        book.setIsbn(book.getIsbn());

        return book;
    }
}
