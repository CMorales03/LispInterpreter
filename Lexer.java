import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Lexer {
    private Vector<String> tokens = new Vector<String>();

    public Lexer() {
        String programString = "";
        Scanner s = new Scanner(System.in);
        Parser p = new Parser();

        Vector<String> outputs = new Vector<>();
        Path file = Paths.get("results.txt");

        System.out.print("$ ");

        while(s.hasNextLine()) {
            programString = s.nextLine().trim().toUpperCase();

            if(programString.equals("(QUIT)")) {
                System.out.print("Bye.\n");
                break;
            }

            try {
                checkMatchingParen(programString);
                tokens = tokenize(programString);
                outputs.add(p.evaluate(tokens));
            } catch(Exception e) {
                System.out.println(e);
            }

            System.out.print("$ ");
        }

        try {
            Files.write(file, outputs, Charset.forName("UTF-8"));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void checkMatchingParen(String s) throws Exception {
        int open = 0;
        int close = 0;

        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == '(')
                open++;
            else if(s.charAt(i) == ')')
                close++;
        }

        if(open != close)
            throw new Exception("Parentheses don't match!");
    }


    private Vector <String> tokenize(String s){
        int i = 0;
        Vector <String> tokens = new Vector<>();
        if ( s.length() == 1 ){
            tokens.add(s);
            return tokens;
        }
        while (  i < s.length() ){
            int j = i + 1;
            if ( s.substring(i, j).matches(Helper.LETTER) || s.substring(i, j).matches(Helper.NUMERIC_ATOM) ){
                while ( s.substring(i,j + 1).matches(Helper.LITERAL) || s.substring(i, j + 1).matches(Helper.NUMERIC_ATOM) ){
                    j++;
                }
                tokens.add(s.substring(i,j));
            } else if ( s.substring(i, j).matches(Helper.SYMBOL) ){
                tokens.add(s.substring(i,j));
            }
            i = j;
        }
        return tokens;
    }
}
