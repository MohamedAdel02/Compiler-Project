/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
     
package compilerproject;

import java.util.ArrayList;

/**
 *
 * @author A
 */


class Token {
    String type;
    String symbol;
    
    Token(String type, String symbol) {
        this.type = type;
        this.symbol = symbol;
    }
}


public class CompilerProject {

    /**
     * @param args the command line arguments
     */
    
    static String code = "static public int testFunc ( ) { "
            + "x = i + 2 * + ; "
            + "if ( x > 6 ) { "
            + "y = x * 7 ; "
            + "} else { "
            + "y = x / 4 ; "
            + "} "
            + "name = \"Jennifer Lawrence\" ; "
            + "for ( int i = 0 ; i < 8 ; i ++ ) "
            + "{ "
            + "/* update x */ "
            + "x = i * 6 ; "
            + "} "
            + "}";

     
    static ArrayList<Token> tokens = new ArrayList<>();
    static ArrayList<String> atoms = new ArrayList<>();
           
    public static void main(String[] args) {
        
        LexicalAnalysis lexicalAnalysis = new LexicalAnalysis(tokens);
        SyntaxAnalysis syntaxAnalysis = new SyntaxAnalysis(tokens, atoms);

       
        lexicalAnalysis.mainLexical();
        syntaxAnalysis.mainSyntax();
        
        
        System.out.println("<================ Tokens ================>");
        printTokens();
         System.out.println("\n\n<================ Atoms ================>");
        printAtoms();
  
    }
    

    static void printTokens() {
        for (int i = 0; i < tokens.size(); i++) {
            System.out.println(tokens.get(i).symbol + "  ===> " + tokens.get(i).type);
        }
    }
    
    static void printAtoms() {
        for (int i = 0; i < atoms.size(); i++) {
            System.out.println(atoms.get(i));
        }
    }
    
    
}
