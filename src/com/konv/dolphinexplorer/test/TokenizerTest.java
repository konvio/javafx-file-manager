package com.konv.dolphinexplorer.test;

import com.konv.dolphinexplorer.spreadsheet.Tokenizer;
import org.junit.Test;

import java.util.List;

import static com.konv.dolphinexplorer.spreadsheet.Tokenizer.Token;
import static com.konv.dolphinexplorer.spreadsheet.Tokenizer.TokenType.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class TokenizerTest {

    private static final Tokenizer mTokenizer = new Tokenizer();

    @Test
    public void simpleAddition() {
        String input = "2 + 3";
        List<Token> response = mTokenizer.tokenize(input);
        assertThat(response, contains(new Token(NUMBER, "2"), new Token(BINARYOP, "+"), new Token(NUMBER, "3")));
    }

    @Test
    public void simpleAdditionWithArbitraryWhitespaces() {
        String input = "     2   +3   ";
        List<Token> response = mTokenizer.tokenize(input);
        assertThat(response, contains(new Token(NUMBER, "2"), new Token(BINARYOP, "+"), new Token(NUMBER, "3")));
    }

    @Test
    public void binaryOperatorsSequence() {
        String input = "+-*/23*";
        List<Token> response = mTokenizer.tokenize(input);
        assertThat(response, contains(new Token(BINARYOP, "+"), new Token(BINARYOP, "-"), new Token(BINARYOP, "*"),
                new Token(BINARYOP, "/"), new Token(NUMBER, "23"), new Token(BINARYOP, "*")));
    }

    @Test
    public void bracesSequence() {
        String input = "))-(45()";
        List<Token> response = mTokenizer.tokenize(input);
        assertThat(response, contains(new Token(BRACECLOSE, ")"), new Token(BRACECLOSE, ")"), new Token(BINARYOP, "-"),
                new Token(BRACEOPEN, "("), new Token(NUMBER, "45"), new Token(BRACEOPEN, "("), new Token(BRACECLOSE, ")")));
    }

    @Test
    public void referencesSequence() {
        String input = "A2 + h56";
        List<Token> response = mTokenizer.tokenize(input);
        assertThat(response, contains(new Token(REFERENCE, "A2"), new Token(BINARYOP, "+"), new Token(REFERENCE, "H56")));
    }
}
