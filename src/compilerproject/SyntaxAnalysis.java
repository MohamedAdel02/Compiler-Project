/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compilerproject;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 *
 * @author A
 */
public class SyntaxAnalysis {
    
    ArrayList<Token> tokens = new ArrayList<>();
    ArrayList<String> atoms = new ArrayList<>();
    int labelCount = 1;
    int tempCount = 0;

    
    SyntaxAnalysis(ArrayList<Token> tokens,  ArrayList<String> atoms) {
        this.tokens = tokens;
        this.atoms = atoms;
    }
    
    
    
    void mainSyntax() {
        
        if (!checkError()) {
            for (int i = 0; i < tokens.size(); i++) {

                if(tokens.get(i).symbol.equals("for")) {
                    i = forLoop(i);
                } else if(tokens.get(i).symbol.equals("while")) {
                    i = whileLoop(i);
                } else if(tokens.get(i).symbol.equals("if")) {
                    i = ifCondition(i);
                } else if(tokens.get(i).symbol.equals("+=")) {
                    atoms.add("(ADD, " + tokens.get(i-1).symbol +", "+ tokens.get(i+1).symbol + ")");
                } else if(tokens.get(i).symbol.equals("-=")) {
                    atoms.add("(SUB, " + tokens.get(i-1).symbol +", "+ tokens.get(i+1).symbol + ")");
                } else if(tokens.get(i).symbol.equals("*=")) {
                    atoms.add("(MUL, " + tokens.get(i-1).symbol +", "+ tokens.get(i+1).symbol + ")");
                } else if(tokens.get(i).symbol.equals("/=")) {
                    atoms.add("(DIV, " + tokens.get(i-1).symbol +", "+ tokens.get(i+1).symbol + ")");
                } else if(tokens.get(i).symbol.equals("=")) {
                    calculationAtoms(i + 1);
                }    
            }
        } else {
            atoms.add("SYNTAX ERROR");
        }
    }
    
    
    int calculationAtoms(int i) {
        
        int j;
        int lastIndex = i;
        ArrayList<String> arr = new ArrayList<>();

        for (j = i; j < tokens.size(); j++) {
            arr.add(tokens.get(j).symbol);
            if(tokens.get(j).symbol.equals(";")) {
                lastIndex = j;
                break;
            }            
        }
        
        for (int y = 0; y < arr.size(); y++) {
            if(arr.get(y).equals("(")) {
                ArrayList<String> subArr = new ArrayList<>();
                for (int x = y+1; x < arr.size(); x++) {
                    subArr.add(arr.get(x));
                    if(arr.get(x).equals(")")) {
                        subCalculationAtoms(subArr);
                        subArr.remove(subArr.size() - 1);
                        for (int q = x+1; q < arr.size(); q++) {
                            subArr.add(arr.get(q));
                        }
                        arr = subArr;
                        y = x;
                        break;
                    }
                }
            }      
        }
        
        subCalculationAtoms(arr);
        atoms.add("(MOVE, " + arr.get(0) + ", " + tokens.get(i-2).symbol + ")");
        return lastIndex;
    }
    
    void subCalculationAtoms(ArrayList<String> arr) {
        
        
        for (int  i = 0; i < arr.size(); i++) { 

            if (arr.size() <= 1) {
                break;
            }

            if(arr.get(i).equals("*")) {
                tempCount++;
                atoms.add("(MUL, " + arr.get(i-1) +", "+ arr.get(i+1) + ", temp" + tempCount + ")");
                arr.set(i, "temp" + tempCount);
                arr.remove(i+1);
                arr.remove(i-1);
            }
            if(arr.get(i).equals("/")) {
                tempCount++;
                atoms.add("(DIV, " + arr.get(i-1) +", "+ arr.get(i+1) + ", temp" + tempCount + ")");
                arr.set(i, "temp" + tempCount);
                arr.remove(i+1);
                arr.remove(i-1);
            }
        } 
        
        for (int  i =0; i < arr.size(); i++) { 
            if (arr.size() <= 1) {
                break;
            }

            if(arr.get(i).equals("+")) {
                tempCount++;
                atoms.add("(ADD, " + arr.get(i-1) +", "+ arr.get(i+1) + ", temp" + tempCount + ")");
                arr.set(i, "temp" + tempCount);
                arr.remove(i+1);
                arr.remove(i-1);
            }
            if(arr.get(i).equals("-")) {
                tempCount++;
                atoms.add("(SUB, " + tokens.get(i-1) +", "+ tokens.get(i+1) + ", temp" + tempCount + ")");
                arr.set(i, "temp" + tempCount);
                arr.remove(i+1);
                arr.remove(i-1);
            }
        } 
                     
    }
       
    boolean checkError() {
        
        for (int i = 0; i < tokens.size(); i++) {
            if(tokens.get(i).type == "Operator") {
                if(tokens.get(i+1).type == "Operator") {
                    return true;
                }
            }    
        }
        
        return false;
    }
    
    int ifCondition(int j) {
        
        int falseLabel;
        int bracketNum = 0;
        int i = j;
        
        atoms.add("(Test, " + tokens.get(i+2).symbol +", " + tokens.get(i+3).symbol +", "  +
                        tokens.get(i+4).symbol +", " + "L" + labelCount +")");
        labelCount++;
        atoms.add("(JMP, L" + labelCount+")");
        atoms.add("(LBL, L" + (labelCount-1)+")");
        falseLabel = labelCount;
        labelCount++;
        
        for (i = j; i < tokens.size(); i++) {
            
            if(tokens.get(i).symbol.equals("{")) {
                calculationAtoms(i+3);
                bracketNum++;
                continue;
            }
            
            
            if(tokens.get(i).symbol.equals("}")) {
                bracketNum--;
                break;    
            }       
        }
        atoms.add("(LBL, L" + falseLabel+")");
        return i;
    }
    
    int forLoop(int j) {
        
        int forLabel;
        int falseLabel;
        int trueLabel = 0;
        int bracketNum = 0;
        int operationIndex = 0;
        int i;
        boolean flag = false;

        atoms.add("(LBL, L" + labelCount+")");
        forLabel = labelCount;
        labelCount++;
        
        
        for (i = j; i < tokens.size(); i++) {
            
            if(tokens.get(i).symbol.equals(";")) {
                flag = true;
                continue;
            }
            
            if(tokens.get(i).type.equals("Operator") && flag) {
                atoms.add("(Test, " + tokens.get(i-1).symbol +", " + tokens.get(i).symbol +", "  +
                        tokens.get(i+1).symbol +", " + "L" + labelCount +")");
                trueLabel = labelCount;
                labelCount ++;
                operationIndex = i + 2;
                break;
            } 
             
        }
        
        atoms.add("(JMP, L" + labelCount+")");
        atoms.add("(LBL, L" + trueLabel+")");
        falseLabel = labelCount;
        labelCount++;
        
        for (i = j; i < tokens.size(); i++) {
            
            if(tokens.get(i).symbol.equals("{")) {
                calculationAtoms(i+3);
                bracketNum++;
                continue;
            }
            
            if(tokens.get(i).symbol.equals("}")) {
                bracketNum--;
                
                if(bracketNum == 0) {
                    getForOperationAtom(operationIndex);                    
                    break;
                }     
            }       
        }
        
        atoms.add("(JMP, L" + forLabel+")");
        atoms.add("(LBL, L" + falseLabel+")");
        return i;
    }
    
    
    void getForOperationAtom(int i) {
        
        for (int x = i; x < tokens.size(); x++) {
            switch (tokens.get(x).symbol) {
                case "++":
                    atoms.add("(ADD, " + tokens.get(x-1).symbol +", "+ "1" + ")");
                    return;
                case "--":
                    atoms.add("(SUB, " + tokens.get(x-1).symbol +", "+ "1" + ")");
                    return;
                case "+=":
                    atoms.add("(ADD, " + tokens.get(x-1).symbol +", "+ tokens.get(x+1).symbol + ")");  
                    return;
                case "-=":
                    atoms.add("(SUB, " + tokens.get(x-1).symbol +", "+ tokens.get(x+1).symbol + ")");  
                    return;
                case "*=":
                    atoms.add("(MUL, " + tokens.get(x-1).symbol +", "+ tokens.get(x+1).symbol + ")");  
                    return;
                case "/=":
                    atoms.add("(DIV, " + tokens.get(x-1).symbol +", "+ tokens.get(x+1).symbol + ")");  
                    return;
                case "+":
                    atoms.add("(ADD, " + tokens.get(x-1).symbol +", "+ tokens.get(x+1).symbol + ")");  
                    return;
                case "-":
                    atoms.add("(SUB, " + tokens.get(x-1).symbol +", "+ tokens.get(x+1).symbol + ")");  
                    return;
                case "*":
                    atoms.add("(MUL, " + tokens.get(x-1).symbol +", "+ tokens.get(x+1).symbol + ")");  
                    return;
                case "/":
                    atoms.add("(DIV, " + tokens.get(x-1).symbol +", "+ tokens.get(x+1).symbol + ")");  
                    return; 
                default:
                    break;
            }
        }

    }

    
    int whileLoop(int j) {
        
        int whileLabel;
        int falseLabel;
        int trueLabel = 0;
        int bracketNum = 0;
        int i;

        atoms.add("(LBL, L" + labelCount+")");
        whileLabel = labelCount;
        labelCount++;
        
        
        for (i = j; i < tokens.size(); i++) {
            
            if(tokens.get(i).type.equals("Operator")) {
                atoms.add("(Test, " + tokens.get(i-1).symbol +", " + tokens.get(i).symbol +", "  +
                        tokens.get(i+1).symbol +", " + "L" + labelCount +")");
                trueLabel = labelCount;
                labelCount ++;
                break;
            } 
             
        }
        
        atoms.add("(JMP, L" + labelCount+")");
        atoms.add("(LBL, L" + trueLabel+")");
        falseLabel = labelCount;
        labelCount++;
        
        for (i = j; i < tokens.size(); i++) {
            
            if(tokens.get(i).symbol.equals("{")) {
                calculationAtoms(i+3);
                bracketNum++;
                continue;
            }
            
            if(tokens.get(i).symbol.equals("}")) {
                bracketNum--;
                
                if(bracketNum == 0) {
                    break;
                }     
            }       
        }
        
        atoms.add("(JMP, L" + whileLabel+")");
        atoms.add("(LBL, L" + falseLabel+")");
        return i;
    }

}
