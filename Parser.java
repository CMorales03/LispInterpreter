import java.lang.reflect.Array;
import java.util.*;
import java.lang.*;

public class Parser {
    public Parser() {

    }

    public String evaluate(Vector <String> tokens) throws Exception  {
        String output;

        switch(tokens.get(1)) {
            case "+":
                output = math(tokens, '+');
                break;
            case "-":
                output = math(tokens, '-');
                break;
            case "*":
                output = math(tokens, '*');
                break;
            case "/":
                output = math(tokens, '/');
                break;
            case "QUOTE":
                output = quote(tokens);
                break;
            case "IF":
                output = iff(tokens);
                break;
            case "DEFINE":
                output = define(tokens);
                break;
            case "SET!":
                output = set(tokens);
                break;
            case "PRINT":
                output = print(tokens);
                break;
            case "DEFUN":
                output = defun(tokens);
                break;
            default:
                output = proc(tokens);
                break;
        }

        // Output intermediate response
        System.out.println(output);

        // Also need to output to file
        return output;
    }

    private String proc(Vector<String> toks) throws Exception {
        // Short procs should be a print call.
        // ex: (9), or (x)
        if(toks.size() == 3)
            return print(toks);

        Integer params;
        Vector<String> body;
        Vector<String> fakeBody;
        String name = toks.get(1);
        int numparams = toks.size() - 5;
        int i = 0;
        int in = 0;

        // Function is not defined
        if(!Env.params.containsKey(name))
            throw new Exception("Function does not exist");

        params = Env.params.get(name);
        body = Env.body.get(name);

        Vector<String> parameters = new Vector<>();

        for(i = 0; i < numparams; i++) {
            parameters.add(toks.get(3 + i));
        }

        switch(name) {
            case "SQRT":
                if(params != numparams)
                    throw new Exception("Invalid number of arguments to function");
                else {
                    if(!toks.get(3).matches("[0-9]+"))
                        if(Env.variables.containsKey(toks.get(3)))
                            toks.set(3, Env.variables.get(toks.get(3)));

                    return String.valueOf(Math.sqrt(Integer.parseInt(toks.get(3))));
                }
            case "SIN":
                if(params != numparams)
                    throw new Exception("Invalid number of arguments to function");
                else {
                    if(!toks.get(3).matches("[0-9]+"))
                        if(Env.variables.containsKey(toks.get(3)))
                            toks.set(3, Env.variables.get(toks.get(3)));
                    return String.valueOf(Math.sin(Integer.parseInt(toks.get(3))));
                }
            case "COS":
                if(params != numparams)
                    throw new Exception("Invalid number of arguments to function");
                else {
                    if(!toks.get(3).matches("[0-9]+"))
                        if(Env.variables.containsKey(toks.get(3)))
                            toks.set(3, Env.variables.get(toks.get(3)));
                    return String.valueOf(Math.cos(Integer.parseInt(toks.get(3))));
                }
            case "TAN":
                if(params != numparams)
                    throw new Exception("Invalid number of arguments to function");
                else {
                    if(!toks.get(3).matches("[0-9]+"))
                        if(Env.variables.containsKey(toks.get(3)))
                            toks.set(3, Env.variables.get(toks.get(3)));
                    return String.valueOf(Math.tan(Integer.parseInt(toks.get(3))));
                }
            default:
                if(params != numparams)
                    throw new Exception("Invalid number of arguments to function");
                else {
                    // Replace all parameters with passed values
                    fakeBody = new Vector<>(body);
                    for(i = 0; i < body.size(); i++) {
                        if(!fakeBody.get(i).matches("[0-9(+-/*)]+")) {
                            fakeBody.set(i, parameters.get(in));
                            in++;
                        }
                    }
                    return math(fakeBody, fakeBody.get(1).charAt(0));
                }
        }

    }

    private String defun(Vector<String> toks) throws Exception {
        String name = toks.get(2);

        if(Env.params.containsKey(name))
            throw new Exception("Function already exists");

        int start = 3;
        int end = 0;
        int i = 0;
        Vector<String> v = new Vector<>();

        for(i = start; i < toks.size(); i++) {
            if(toks.get(i).equals(")")) {
                end = i;
                break;
            }
        }

        // Get number of params
        Integer params = end - start - 1;

        // Get body of function
        for(i = end + 1; i < toks.size() - 1; i++) {
            v.add(toks.get(i));
        }

        Env.params.put(name, params);
        Env.body.put(name, v);

        return name;
    }

    private String print(Vector<String> toks) {
        String var = toks.get(2);

        if(var.equals(")"))
            var = toks.get(1);

        if(var.matches("[0-9]+"))
            return var;
        else
            return Env.variables.getOrDefault(var, "Variable not declared");
    }

    private String set(Vector<String> toks) throws Exception {
        String varname = toks.get(2);
        String value = toks.get(3);
        int i = 0;

        if(!Env.variables.containsKey(varname))
            throw new Exception("Cannot assign an undeclared variable!");

        Vector<String> v = new Vector<>();

        // Expression given, not just number
        if(toks.get(3).charAt(0) == '(') {
            for(i = 3; i < toks.size() - 1; i++)
                v.add(toks.get(i));
            Env.variables.put(varname, math(v, v.get(1).charAt(0)));
        } else {
            v.add("(");
            v.add(value);
            v.add(")");
            Env.variables.put(varname, math(v, 'v'));
        }

        return Env.variables.get(varname);
    }

    private String define(Vector<String> toks) throws Exception {
        String varname = toks.get(2);
        String value = toks.get(3);
        int i = 0;

        if(Env.variables.containsKey(varname))
            throw new Exception("Variable name already declared!");

        Vector<String> v = new Vector<>();

        // Expression given, not just number
        if(toks.get(3).charAt(0) == '(') {
            for(i = 3; i < toks.size() - 1; i++)
                v.add(toks.get(i));
            Env.variables.put(varname, math(v, v.get(1).charAt(0)));
        } else {
            v.add("(");
            v.add(value);
            v.add(")");
            Env.variables.put(varname, math(v, 'v'));
        }

        return varname;
    }

    private String iff(Vector<String> toks) throws Exception {
        int i = 0;
        String comparison = toks.get(3);
        String operator1 = toks.get(8);
        String operator2 = toks.get(13);
        Boolean flag = false;

        if(!toks.get(4).matches("[0-9]+")) {
            if(Env.variables.containsKey(toks.get(4))) {
                toks.set(4, Env.variables.get(toks.get(4)));
            }
        }

        if(!toks.get(5).matches("[0-9]+")) {
            if(Env.variables.containsKey(toks.get(5))) {
                toks.set(5, Env.variables.get(toks.get(5)));
            }
        }

        switch(comparison) {
            case "<":
                if(Integer.parseInt(toks.get(4)) < Integer.parseInt(toks.get(5)))
                    flag = true;
                break;
            case ">":
                if(Integer.parseInt(toks.get(4)) > Integer.parseInt(toks.get(5)))
                    flag = true;
                break;
            default:
                throw new Exception("Unknown comparison operator in if statement!");
        }

        if(flag) {
            Vector<String> thenBlock = new Vector<String>(toks);

            // Then block -- remove first 6 tokens, then everything after
            for(i = 0; i < 7; i++) {
                thenBlock.remove(0);
            }

            for(i = 0; i < thenBlock.size(); i++) {
                thenBlock.remove(5);
            }
            return math(thenBlock, operator1.charAt(0));
        } else {
            Vector<String> elseBlock = new Vector<String>(toks);

            // Else block -- remove first 11 tokens and final token
            for(i = 0; i < 12; i++) {
                elseBlock.remove(0);
            }

            elseBlock.remove(5);
            return math(elseBlock, operator2.charAt(0));
        }

    }


    private String quote(Vector<String> toks) {
        int i = 0;
        String s = "";

        // Return everything after (QUOTE except the final parentheses
        for(i = 2; i < toks.size() - 1; i++) {
            s = s.concat(toks.get(i));
            s = s.concat(" ");
        }

        return s;
    }

    private String math(Vector<String> toks, char type) {
        int i = 0;
        int start = 0;
        int end = 0;

        for(i = 0; i < toks.size(); i++) {
            // Found end of expression
            if(toks.get(i).equals(")")) {
                end = i;
                break;
            }
        }

        for(i = end; i >= 0; i--) {
            // Found start of expression
            if(toks.get(i).equals("(")) {
                start = i;
                break;
            }
        }

        // There is no sub expression, base case for recursion
        if(end == toks.size() - 1) {
            // The first input is not a number
            // Replace the variable with its value
            if(!toks.get(2).matches("[0-9]+")) {
                if(Env.variables.containsKey(toks.get(2))) {
                    toks.set(2, Env.variables.get(toks.get(2)));
                }
            }

            try {
                toks.get(3);
                // The second input is not a number
                // Replace the variable with its value
                if(!toks.get(3).matches("[0-9]+")) {
                    if(Env.variables.containsKey(toks.get(3))) {
                        toks.set(3, Env.variables.get(toks.get(3)));
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e) {}

            switch(type) {
                case '+':
                    return Functions.add(toks.get(2), toks.get(3));
                case '-':
                    return Functions.subtract(toks.get(2), toks.get(3));
                case '*':
                    return Functions.multiply(toks.get(2), toks.get(3));
                case '/':
                    return Functions.divide(toks.get(2), toks.get(3));
                case 'v':
                    // Should only hit when math is called with (v) where v is a variable
                    if(!toks.get(1).matches("[0-9]+")) {
                        if(Env.variables.containsKey(toks.get(1))) {
                            toks.set(1, Env.variables.get(toks.get(1)));
                        }
                    }
                    return toks.get(1);

                default:
                    return "Unknown math operator.";
            }
        } else {
            // New string with the inner expression
            Vector<String> subTokens = new Vector<>();

            for(i = start; i < end + 1; i++)
                subTokens.add(toks.get(i));

            try {
                // Evaluate the inner expression
                String inner = math(subTokens, type);

                // Substitute inner into outer expression
                subTokens = new Vector<>(toks);

                for(i = start; i < end; i++)
                    subTokens.remove(start);

                subTokens.set(start, inner);

                return math(subTokens, type);
            } catch(Exception e) {
                System.out.println(e);
            }

            return "Parentheses error!";
        }
    }
}
