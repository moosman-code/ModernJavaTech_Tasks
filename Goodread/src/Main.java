import bg.sofia.uni.fmi.mjt.goodreads.BookLoader;
import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.io.*;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/bg/sofia/uni/fmi/mjt/goodreads/resources/goodreads_data.csv"));
            Reader stopWordsReader = new FileReader("src/bg/sofia/uni/fmi/mjt/goodreads/resources/stopwords.txt")) {
            Set<Book> books =  BookLoader.load(reader);

            TextTokenizer tokenizer = new TextTokenizer(stopWordsReader);

            for (Book book : books) {
                System.out.println(tokenizer.tokenize(book.description()));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
