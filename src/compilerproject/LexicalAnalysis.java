package compilerproject;

import static compilerproject.CompilerProject.code;
import static compilerproject.CompilerProject.tokens;
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author A
 */
public class LexicalAnalysis {
    
    
    ArrayList<Token> tokens = new ArrayList<>();

    
    LexicalAnalysis(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }
    
    
    void mainLexical() {
        
        String subStr = "";
    
        for(int i = 0; i < code.length(); i++) {

            String strChar = String.valueOf(code.charAt(i));

            if (strChar.equals(" ")) {
                i = checkString(subStr, i);
                subStr = "";
            } else {
                subStr += strChar;
            }         
        }
    }
    
    
    int checkString(String subStr, int i) {
        
        if (subStr.equals("/*")) {
           i = ignoreComment(i, code);
           return i;
        }

        String subFirst = String.valueOf(subStr.charAt(0));
        if(subFirst.equals("\"")) {
            i = getCharConst(subStr, i, code, tokens);
            return i;
        }

        if(isKeyword(subStr)) {
            tokens.add(new Token("Keyword", subStr));
        } else if(isSpecialChar(subStr)) {
            tokens.add(new Token("SpecialChar", subStr));
        } else if(isNumericConst(subStr)) {
            tokens.add(new Token("NumericConst", subStr));
        } else if(isValidIdentifier(subFirst)) {
            tokens.add(new Token("Identifier", subStr));
        } else if(isOperator(subStr)){
            tokens.add(new Token("Operator", subStr));
        } 

        return i;   
    }
    
    int getCharConst(String subStr, int i, String code, ArrayList<Token> tokens) {
        
        int subLength = subStr.length();
               
        String lastStr = String.valueOf(subStr.charAt(subLength-1));
        
        if (subLength != 1 && lastStr.equals("\"")) {
            tokens.add(new Token("CharConst", subStr));
            return i;
        } else {
                    
            String str = subStr;
            
            for(int j = i; j < code.length(); j++) {
                
                String ch = String.valueOf(code.charAt(j));
                if(ch.equals("\"")) {
                    str += "\"";
                    tokens.add(new Token("CharConst", str));
                    return j+1; 
                } else {
                    str += ch;
                }

            }
        }
        
        return i;
    }
    
    int ignoreComment(int i, String code) {
        String subStr = "";
        for(int j = i; j < code.length(); j++) {
            String ch = String.valueOf(code.charAt(j));
            if (ch.equals(" ")) {
                
                if(subStr.equals("*/")) {
                    return j;
                }
                
                subStr = "";
            } else {
                subStr += ch;
            }
        }
        
        return i;
    }
    
    boolean isKeyword(String str) {
        return  str.equals("if")         || str.equals("else")      || str.equals("for")          || 
                str.equals("while")      || str.equals("do")        || str.equals("try")          ||
                str.equals("throw")      || str.equals("throws")    || str.equals("final")        || 
                str.equals("finally")    || str.equals("boolean")   || str.equals("int")          || 
                str.equals("long")       || str.equals("float")     || str.equals("double")       || 
                str.equals("void")       || str.equals("short")     || str.equals("null")         || 
                str.equals("new")        || str.equals("static")    || str.equals("public")       || 
                str.equals("private")    || str.equals("protected") || str.equals("static")       ||
                str.equals("super")      || str.equals("return")    || str.equals("char")         || 
                str.equals("catch")      || str.equals("default")   || str.equals("continue")     || 
                str.equals("enum")       || str.equals("class")     || str.equals("case")         || 
                str.equals("abstract")   || str.equals("this")      || str.equals("transient")    || 
                str.equals("volatile")   || str.equals("assert")    || str.equals("break")        || 
                str.equals("byte")       || str.equals("extends")   || str.equals("implements")   || 
                str.equals("import")     || str.equals("native")    || str.equals("import")       ||
                str.equals("instanceof") || str.equals("interface") || str.equals("package")      || 
                str.equals("strictfp")   || str.equals("switch")    || str.equals("synchronized") ||
                str.equals("this")       || str.equals("transient") || str.equals("volatile");
    }
    
    boolean isOperator(String str) {
        return  str.equals("+")  || str.equals("-")   || str.equals("*")   || 
                str.equals("/")  || str.equals("%")   || str.equals("++")  ||
                str.equals("--") || str.equals("=")   || str.equals("+=")  || 
                str.equals("-=") || str.equals("*=")  || str.equals("/=")  || 
                str.equals("%=") || str.equals("&=")  || str.equals("|=")  || 
                str.equals("^=") || str.equals(">>=") || str.equals("<<=") || 
                str.equals("==") || str.equals("!=")  || str.equals(">")   || 
                str.equals("<")  || str.equals(">=")  || str.equals("<=")  ||
                str.equals("&&") || str.equals("||")  || str.equals("!"); 
    }
    
    boolean isNumericConst(String str) {
            
        for (int i = 0; i < str.length(); i++) {
 
            String ch = String.valueOf(str.charAt(i));
            
            if( !ch.equals("0") && !ch.equals("1") && !ch.equals("2") && 
                !ch.equals("3") && !ch.equals("4") && !ch.equals("5") &&
                !ch.equals("6") && !ch.equals("7") && !ch.equals("8") && 
                !ch.equals("9")) {
                return false;
            }     
        }

        return true;
    }
    
    boolean isSpecialChar(String str) {
        return  str.equals("{")   || str.equals("}")   || str.equals("(")   || 
                str.equals(")")   || str.equals("[")   || str.equals("]")   ||
                str.equals(",")   || str.equals(":")   || str.equals(";")   || 
                str.equals("\\t") || str.equals("\\b") || str.equals("\\n") || 
                str.equals("\\r") || str.equals("\\f") || str.equals("\'")  || 
                str.equals("\"")  || str.equals("\\");
    }
    
    
    boolean isValidIdentifier(String ch) {
        return  !ch.equals("0") && !ch.equals("1")    && !ch.equals("2") && 
                !ch.equals("3") && !ch.equals("4")    && !ch.equals("5") &&
                !ch.equals("6") && !ch.equals("7")    && !ch.equals("8") && 
                !ch.equals("9") && !isSpecialChar(ch) && !isOperator(ch);
    }
    
}
