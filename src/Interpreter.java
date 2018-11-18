import java.io.*;
import java.util.ArrayList;
import java.math.*;
import java.util.Collections;
import java.util.HashMap;

// Make all operations int * int to string * string by doing the parseInt in the function

public class Interpreter {
    private static ArrayList<String> stack = new ArrayList<>();
    private static ArrayList<Boolean> isName = new ArrayList<>();
    private static HashMap<String, String> bindList = new HashMap<>();

    public static void interpreter (String inputFile, String outputFile) {
        String tempStr, push;
        String error = ":error:", line = null, letter = "[a-z A-Z].*", specialChar = "[!#$%&()*+,./:;<=>?@\\[\\]^_{|}~]";
        ArrayList<String> lol = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(inputFile);
            FileWriter fileWriter = new FileWriter(outputFile);

            BufferedReader br = new BufferedReader(fileReader);
            BufferedWriter bw = new BufferedWriter(fileWriter);

            while ((line = br.readLine()) != null) {
                boolean notIntOperational = stack.isEmpty() || stack.size() == 1 || !isInteger(stack.get(stack.size() - 1)) || !isInteger(stack.get(stack.size() - 2));

                //push functions
                if (line.contains("push")) {
                     tempStr = line.substring(4);
                     push = tempStr.trim();

                     if(isInteger(push)){
                        if (push.contains("-0")) {
                            tempStr = push.replaceAll("-","");
                            push = tempStr;
                            stack.add(push);
                            isName.add(false);
                        }
                        else {
                            stack.add(push);
                            isName.add(false);
                        }
                     } else if (push.contains("\"")) {
                         tempStr = push.replaceAll("\"","");
                         push = tempStr;
                         stack.add(push);
                         isName.add(false);
                     }
                     else if (push.matches(letter) || push.startsWith("_")) {
                         stack.add(push);
                         isName.add(true);
                     }
                     else if (push.equals(":true:")) {
                         stack.add(":true:");
                         isName.add(false);
                     }
                     else if (push.equals(":false:")) {
                         stack.add(":false:");
                         isName.add(false);
                     }
                     else if (push.equals(error)) {
                         stack.add(error);
                         isName.add(false);
                     }
                     else {
                         stack.add(error);
                         isName.add(false);
                     }
                }

                //pop function
                if (line.equals("pop")) {
                    if (stack.isEmpty()) {
                        stack.add(error);
                        isName.add(false);
                    }
                    else {
                        stack.remove(stack.size() - 1);
                        isName.remove(isName.size() - 1);
                    }
                }

                //add function
                if (line.equals("add")) {
//                    // Checks if binding values exists and general rules
                    if ((isName.get(isName.size() - 1) && bindList.containsKey(stack.get(stack.size() - 1))) && (!isName.get(isName.size() - 2) && !isInteger(stack.get(stack.size() - 2)))) {
                        add(Integer.parseInt(bindList.get(stack.get(stack.size() - 2))), Integer.parseInt(stack.get(stack.size() - 1)));
                    }
                    else if ((isName.get(isName.size() - 2) && bindList.containsKey(stack.get(stack.size() - 2))) && (!isName.get(isName.size() - 1)  && !isInteger(stack.get(stack.size() - 1)))) {
                        add(Integer.parseInt(bindList.get(stack.get(stack.size() - 2))), Integer.parseInt(stack.get(stack.size() - 1)));
                    }
                    else if ((isName.get(isName.size() - 1) && bindList.containsKey(stack.get(stack.size() - 1)) && !isInteger(stack.get(stack.size() - 1))) && (isName.get(isName.size() - 2) && bindList.containsKey(stack.get(stack.size() - 2)) && !isInteger(stack.get(stack.size() - 2)))) {
                        add(Integer.parseInt(bindList.get(stack.get(stack.size() - 2))), Integer.parseInt(bindList.get(stack.get(stack.size() - 1))));
                    }
//                    else if (bindList.containsKey(stack.get(stack.size() - 1)) && bindList.containsKey(stack.get(stack.size() - 2))) {
//                        add(Integer.parseInt(bindList.get(stack.get(stack.size() - 2))), Integer.parseInt(bindList.get(stack.get(stack.size() - 1))));
//                    }
                    else if (notIntOperational) {
                        stack.add(error);
                        isName.add(false);
                    }
                    else {
                        add(Integer.parseInt(stack.get(stack.size() - 2)), Integer.parseInt(stack.get(stack.size() - 1)));
                    }
                }

                //sub function
                if (line.equals("sub")) {
                    if ((isName.get(isName.size() - 1) && bindList.containsKey(stack.get(stack.size() - 1))) && (!isName.get(isName.size() - 2) && !isInteger(stack.get(stack.size() - 2)))) {
                        sub(Integer.parseInt(bindList.get(stack.get(stack.size() - 2))), Integer.parseInt(stack.get(stack.size() - 1)));
                    }
                    else if ((isName.get(isName.size() - 2) && bindList.containsKey(stack.get(stack.size() - 2))) && (!isName.get(isName.size() - 1)  && !isInteger(stack.get(stack.size() - 1)))) {
                        sub(Integer.parseInt(bindList.get(stack.get(stack.size() - 2))), Integer.parseInt(stack.get(stack.size() - 1)));
                    }
                    else if ((isName.get(isName.size() - 1) && bindList.containsKey(stack.get(stack.size() - 1)) && !isInteger(stack.get(stack.size() - 1))) && (isName.get(isName.size() - 2) && bindList.containsKey(stack.get(stack.size() - 2)) && !isInteger(stack.get(stack.size() - 2)))) {
                        sub(Integer.parseInt(bindList.get(stack.get(stack.size() - 2))), Integer.parseInt(bindList.get(stack.get(stack.size() - 1))));
                    }
//                    if (bindList.containsKey(stack.get(stack.size() - 1)) && bindList.containsKey(stack.get(stack.size() - 2))) {
//                        sub(Integer.parseInt(bindList.get(stack.get(stack.size() - 2))), Integer.parseInt(bindList.get(stack.get(stack.size() - 1))));
//                    }
                    else if (notIntOperational) {
                        stack.add(error);
                        isName.add(false);
                    }
                    else {
                        sub(Integer.parseInt(stack.get(stack.size() - 2)),Integer.parseInt(stack.get(stack.size() - 1)));
                    }
                }

                //mul function
                if (line.equals("mul")) {
                    if ((isName.get(isName.size() - 1) && bindList.containsKey(stack.get(stack.size() - 1))) && (!isName.get(isName.size() - 2) && !isInteger(stack.get(stack.size() - 2)))) {
                        mul(Integer.parseInt(bindList.get(stack.get(stack.size() - 2))), Integer.parseInt(stack.get(stack.size() - 1)));
                    }
                    else if ((isName.get(isName.size() - 2) && bindList.containsKey(stack.get(stack.size() - 2))) && (!isName.get(isName.size() - 1)  && !isInteger(stack.get(stack.size() - 1)))) {
                        mul(Integer.parseInt(bindList.get(stack.get(stack.size() - 2))), Integer.parseInt(stack.get(stack.size() - 1)));
                    }
                    else if ((isName.get(isName.size() - 1) && bindList.containsKey(stack.get(stack.size() - 1)) && !isInteger(stack.get(stack.size() - 1))) && (isName.get(isName.size() - 2) && bindList.containsKey(stack.get(stack.size() - 2)) && !isInteger(stack.get(stack.size() - 2)))) {
                        mul(Integer.parseInt(bindList.get(stack.get(stack.size() - 2))), Integer.parseInt(bindList.get(stack.get(stack.size() - 1))));
                    }
//                    if (bindList.containsKey(stack.get(stack.size() - 1)) && bindList.containsKey(stack.get(stack.size() - 2))) {
//                        mul(Integer.parseInt(bindList.get(stack.get(stack.size() - 2))), Integer.parseInt(bindList.get(stack.get(stack.size() - 1))));
//                    }
                    else if (notIntOperational) {
                        stack.add(error);
                        isName.add(false);
                    }
                    else {
                        mul(Integer.parseInt(stack.get(stack.size() - 2)),Integer.parseInt(stack.get(stack.size() - 1)));
                    }
                }

                //div function
                if (line.equals("div")) {
                    if ((isName.get(isName.size() - 1) && bindList.containsKey(stack.get(stack.size() - 1))) && (!isName.get(isName.size() - 2) && !isInteger(stack.get(stack.size() - 2)))) {
                        div(Integer.parseInt(bindList.get(stack.get(stack.size() - 2))), Integer.parseInt(stack.get(stack.size() - 1)));
                    }
                    else if ((isName.get(isName.size() - 2) && bindList.containsKey(stack.get(stack.size() - 2))) && (!isName.get(isName.size() - 1)  && !isInteger(stack.get(stack.size() - 1)))) {
                        div(Integer.parseInt(bindList.get(stack.get(stack.size() - 2))), Integer.parseInt(stack.get(stack.size() - 1)));
                    }
                    else if ((isName.get(isName.size() - 1) && bindList.containsKey(stack.get(stack.size() - 1)) && !isInteger(stack.get(stack.size() - 1))) && (isName.get(isName.size() - 2) && bindList.containsKey(stack.get(stack.size() - 2)) && !isInteger(stack.get(stack.size() - 2)))) {
                        div(Integer.parseInt(bindList.get(stack.get(stack.size() - 2))), Integer.parseInt(bindList.get(stack.get(stack.size() - 1))));
                    }
//                    if (bindList.containsKey(stack.get(stack.size() - 1)) && bindList.containsKey(stack.get(stack.size() - 2))) {
//                        div(Integer.parseInt(bindList.get(stack.get(stack.size() - 2))), Integer.parseInt(bindList.get(stack.get(stack.size() - 1))));
//                    }
                    else if (notIntOperational) {
                        stack.add(error);
                        isName.add(false);
                    }
                    else {
                        div(Integer.parseInt(stack.get(stack.size() - 2)),Integer.parseInt(stack.get(stack.size() - 1)));
                    }
                }

                //rem function
                if (line.equals("rem")) {
                    if ((isName.get(isName.size() - 1) && bindList.containsKey(stack.get(stack.size() - 1))) && (!isName.get(isName.size() - 2) && !isInteger(stack.get(stack.size() - 2)))) {
                        rem(Integer.parseInt(bindList.get(stack.get(stack.size() - 2))), Integer.parseInt(stack.get(stack.size() - 1)));
                    }
                    else if ((isName.get(isName.size() - 2) && bindList.containsKey(stack.get(stack.size() - 2))) && (!isName.get(isName.size() - 1)  && !isInteger(stack.get(stack.size() - 1)))) {
                        rem(Integer.parseInt(bindList.get(stack.get(stack.size() - 2))), Integer.parseInt(stack.get(stack.size() - 1)));
                    }
                    else if ((isName.get(isName.size() - 1) && bindList.containsKey(stack.get(stack.size() - 1)) && !isInteger(stack.get(stack.size() - 1))) && (isName.get(isName.size() - 2) && bindList.containsKey(stack.get(stack.size() - 2)) && !isInteger(stack.get(stack.size() - 2)))) {
                        rem(Integer.parseInt(bindList.get(stack.get(stack.size() - 2))), Integer.parseInt(bindList.get(stack.get(stack.size() - 1))));
                    }
//                    if (bindList.containsKey(stack.get(stack.size() - 1)) && bindList.containsKey(stack.get(stack.size() - 2))) {
//                        rem(Integer.parseInt(bindList.get(stack.get(stack.size() - 2))), Integer.parseInt(bindList.get(stack.get(stack.size() - 1))));
//                    }
                    else if (notIntOperational) {
                        stack.add(error);
                        isName.add(false);
                    }
                    else {
                        rem(Integer.parseInt(stack.get(stack.size() - 2)),Integer.parseInt(stack.get(stack.size() - 1)));
                    }
                }

                //neg function
                if (line.equals("neg")) {
                    if ((isName.get(isName.size() - 1) && bindList.containsKey(stack.get(stack.size() - 1)) && isInteger(bindList.get(stack.get(stack.size() - 1))))) {
                        neg(Integer.parseInt(bindList.get(stack.get(stack.size() - 1))));
                    }
                    else if (stack.isEmpty() || !isInteger(stack.get(stack.size() - 1))) {
                        stack.add(error);
                        isName.add(false);
                    }
                    else {
                        neg(Integer.parseInt(stack.get(stack.size() - 1)));
                    }
                }

                //swap function
                if (line.equals("swap")) {
                    if (stack.isEmpty() || stack.size() == 1) {
                        stack.add(error);
                        isName.add(false);
                    }
                    else {
                        swap(stack.get(stack.size() - 1), stack.get(stack.size() - 2));
                    }
                }

                //cat function
                if (line.equals("cat")) {
                    // Checks if binding values exists and general rules
                    if ((isName.get(isName.size() - 2) && bindList.containsKey(stack.get(stack.size() - 2))) && (!isName.get(isName.size() - 1) && !isInteger(stack.get(stack.size() - 1)))) {
                        cat(bindList.get(stack.get(stack.size() - 2)), stack.get(stack.size() - 1));
                    }
                    else if ((isName.get(isName.size() - 2) && bindList.containsKey(stack.get(stack.size() - 2))) && (!isName.get(isName.size() - 1)  && !isInteger(stack.get(stack.size() - 1)))) {
                        cat(bindList.get(stack.get(stack.size() - 2)), stack.get(stack.size() - 1));
                    }
                    else if ((isName.get(isName.size() - 1) && bindList.containsKey(stack.get(stack.size() - 1)) && !isInteger(stack.get(stack.size() - 1))) && (isName.get(isName.size() - 2) && bindList.containsKey(stack.get(stack.size() - 2)) && !isInteger(stack.get(stack.size() - 2)))) {
                        cat(bindList.get(stack.get(stack.size() - 2)), bindList.get(stack.get(stack.size() - 1)));
                    }
                    else if (stack.isEmpty() || stack.size() == 1 || isInteger(stack.get(stack.size() - 1)) || isInteger(stack.get(stack.size() - 2)) || isName.get(isName.size() - 1) || isName.get(isName.size() - 2)) {
                        stack.add(error);
                        isName.add(false);
                    }
                    else {
                        cat(stack.get(stack.size() - 2), stack.get(stack.size() - 1));
                    }
                }

                //and function
                if (line.equals("and")) {
                    // Checks if binding values exists and general rules
                    if ((isName.get(isName.size() - 1) && bindList.containsKey(stack.get(stack.size() - 1)) && isBoolean(bindList.get(stack.get(stack.size() - 1)))) && (!isName.get(isName.size() - 2)) && isBoolean(stack.get(stack.size() - 2))) {
                        and(bindList.get(stack.get(stack.size() - 1)), stack.get(stack.size() - 2));
                    }
                    else if ((isName.get(isName.size() - 2) && bindList.containsKey(stack.get(stack.size() - 2)) && isBoolean(bindList.get(stack.get(stack.size() - 2)))) && (!isName.get(isName.size() - 1)  && isBoolean(stack.get(stack.size() - 1)))) {
                        and(bindList.get(stack.get(stack.size() - 2)), stack.get(stack.size() - 1));
                    }
                    else if ((isName.get(isName.size() - 1) && bindList.containsKey(stack.get(stack.size() - 1)) && isBoolean(bindList.get(stack.get(stack.size() - 1)))) && (isName.get(isName.size() - 2) && bindList.containsKey(stack.get(stack.size() - 2)) && isBoolean(bindList.get(stack.get(stack.size() - 2))))) {
                        and(bindList.get(stack.get(stack.size() - 1)), bindList.get(stack.get(stack.size() - 2)));
                    }
                    else if (stack.isEmpty() || stack.size() == 1 || !isBoolean(stack.get(stack.size() - 1)) || !isBoolean(stack.get(stack.size() - 2))) {
                        stack.add(error);
                        isName.add(false);
                    }
                    else {
                        and(stack.get(stack.size() - 1), stack.get(stack.size() - 2));
                    }
                }

                //or function
                if (line.equals("or")) {
                    // Checks if binding values exists and general rules
                    if ((isName.get(isName.size() - 1) && bindList.containsKey(stack.get(stack.size() - 1)) && isBoolean(bindList.get(stack.get(stack.size() - 1)))) && (!isName.get(isName.size() - 2)) && isBoolean(stack.get(stack.size() - 2))) {
                        or(bindList.get(stack.get(stack.size() - 1)), stack.get(stack.size() - 2));
                    }
                    else if ((isName.get(isName.size() - 2) && bindList.containsKey(stack.get(stack.size() - 2)) && isBoolean(bindList.get(stack.get(stack.size() - 2)))) && (!isName.get(isName.size() - 1)  && isBoolean(stack.get(stack.size() - 1)))) {
                        or(bindList.get(stack.get(stack.size() - 2)), stack.get(stack.size() - 1));
                    }
                    else if ((isName.get(isName.size() - 1) && bindList.containsKey(stack.get(stack.size() - 1)) && isBoolean(bindList.get(stack.get(stack.size() - 1)))) && (isName.get(isName.size() - 2) && bindList.containsKey(stack.get(stack.size() - 2)) && isBoolean(bindList.get(stack.get(stack.size() - 2))))) {
                        or(bindList.get(stack.get(stack.size() - 1)), bindList.get(stack.get(stack.size() - 2)));
                    }
                    else if (stack.isEmpty() || stack.size() == 1 || !isBoolean(stack.get(stack.size() - 1)) || !isBoolean(stack.get(stack.size() - 2))) {
                        stack.add(error);
                        isName.add(false);
                    }
                    else {
                        or(stack.get(stack.size() - 1), stack.get(stack.size() - 2));
                    }
                }

                //not function
                if (line.equals("not")) {
                    // Checks if binding values exists and general rules
                    if ((isName.get(isName.size() - 1) && bindList.containsKey(stack.get(stack.size() - 1)) && isBoolean(bindList.get(stack.get(stack.size() - 1))))) {
                        not(bindList.get(stack.get(stack.size() - 1)));
                    }
                    else if (stack.isEmpty() ||!isBoolean(stack.get(stack.size() - 1))) {
                        stack.add(error);
                        isName.add(false);
                    }
                    else {
                        not(stack.get(stack.size() - 1));
                    }
                }

                //equal function
                if (line.equals("equal")) {
                    if ((isName.get(isName.size() - 1) && bindList.containsKey(stack.get(stack.size() - 1))) && (!isName.get(isName.size() - 2) && !isInteger(stack.get(stack.size() - 2)))) {
                        equal(bindList.get(stack.get(stack.size() - 2)), stack.get(stack.size() - 1));
                    }
                    else if ((isName.get(isName.size() - 2) && bindList.containsKey(stack.get(stack.size() - 2))) && (!isName.get(isName.size() - 1)  && !isInteger(stack.get(stack.size() - 1)))) {
                        equal(bindList.get(stack.get(stack.size() - 2)), stack.get(stack.size() - 1));
                    }
                    else if ((isName.get(isName.size() - 1) && bindList.containsKey(stack.get(stack.size() - 1)) && !isInteger(stack.get(stack.size() - 1))) && (isName.get(isName.size() - 2) && bindList.containsKey(stack.get(stack.size() - 2)) && !isInteger(stack.get(stack.size() - 2)))) {
                        equal(bindList.get(stack.get(stack.size() - 2)), bindList.get(stack.get(stack.size() - 1)));
                    }
                    if (notIntOperational) {
                        stack.add(error);
                        isName.add(false);
                    }
                    else {
                        equal(stack.get(stack.size() - 1), stack.get(stack.size() - 2));
                    }
                }

                //lessThan function
                if (line.equals("lessThan")) {
                    if ((isName.get(isName.size() - 1) && bindList.containsKey(stack.get(stack.size() - 1))) && (!isName.get(isName.size() - 2) && !isInteger(stack.get(stack.size() - 2)))) {
                        lessThan(bindList.get(stack.get(stack.size() - 2)), stack.get(stack.size() - 1));
                    }
                    else if ((isName.get(isName.size() - 2) && bindList.containsKey(stack.get(stack.size() - 2))) && (!isName.get(isName.size() - 1)  && !isInteger(stack.get(stack.size() - 1)))) {
                        lessThan(bindList.get(stack.get(stack.size() - 2)), stack.get(stack.size() - 1));
                    }
                    else if ((isName.get(isName.size() - 1) && bindList.containsKey(stack.get(stack.size() - 1)) && !isInteger(stack.get(stack.size() - 1))) && (isName.get(isName.size() - 2) && bindList.containsKey(stack.get(stack.size() - 2)) && !isInteger(stack.get(stack.size() - 2)))) {
                        lessThan(bindList.get(stack.get(stack.size() - 2)), bindList.get(stack.get(stack.size() - 1)));
                    }
                    else if (stack.isEmpty() || stack.size() == 1 || !isInteger(stack.get(stack.size() - 1)) || !isInteger(stack.get(stack.size() - 2)) ) {
                        stack.add(error);
                        isName.add(false);
                    }
                    else {
                        lessThan(stack.get(stack.size() - 1), stack.get(stack.size() - 2));
                    }
                }

                //bind function
                if (line.equals("bind")) {
                    if (stack.isEmpty() || !isName.get(isName.size() - 2) || stack.get(stack.size() - 1).equals(":error:") || (isName.get(isName.size() - 1) && !bindList.containsKey(stack.get(stack.size() - 1)))) {
                        stack.add(error);
                        isName.add(false);
                    }
                    else {
                        bind(stack.get(stack.size() - 2), stack.get(stack.size() - 1));
                    }
                }

                //let function
                if (line.equals("let")) {
                }

                //end function

                //if function
                if (line.equals("if")) {
                    if ((isName.get(isName.size() - 1) && bindList.containsKey(stack.get(stack.size() - 1)) && isBoolean(bindList.get(stack.get(stack.size() - 1))))) {
                        not(bindList.get(stack.get(stack.size() - 1)));
                    }
                    else if (stack.isEmpty() || stack.size() < 3 || !isBoolean(stack.get(stack.size() - 3))) {
                        stack.add(error);
                        isName.add(false);
                    }
                    else {
                        if_fun(stack.get(stack.size() - 3));
                    }
                }

                //quit function
                if (line.contains("quit")){
                    Collections.reverse(stack);
                    for (String str: stack) {
                        bw.write(str);
                        bw.newLine();
                    }

                    Collections.reverse(isName);
                    for (boolean b: isName) {
                        System.out.print(b + ", ");
                    }

                    System.out.println(bindList);
                }
            }
            br.close();
            bw.close();
        } catch (FileNotFoundException e) {
            System.out.println("No files detected");

        } catch (IOException e) {
            System.out.println("IO Exception");
        }

    }

    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private static boolean isBoolean(String str) {
        if (str.equals(":true:") || str.equals(":false:")){
            return true;
        }
        return false;
    }

    private static void add(int a, int b) {
        String res;
        int tempInt;
        tempInt = (a) + (b);
        res = Integer.toString(tempInt);
        stack.set(stack.size() - 2, res);
        stack.remove(stack.size() - 1);
        isName.set(isName.size() - 2, false);
        isName.remove(isName.size() - 1);
    }

    private static void sub(int a, int b) {
        int tempInt;
        String res;
        tempInt = (a) - (b);
        res = Integer.toString(tempInt);
        stack.set(stack.size() - 2, res);
        stack.remove(stack.size() - 1);
        isName.set(isName.size() - 2, false);
        isName.remove(isName.size() - 1);
    }

    private static void mul(int a, int b){
        int tempInt;
        String res;
        tempInt = (a) * (b);
        res = Integer.toString(tempInt);
        stack.set(stack.size() - 2, res);
        stack.remove(stack.size() - 1);
        isName.set(isName.size() -2, false);
        isName.remove(isName.size() - 1);
    }

    private static void div(int a, int b) {
        try {
            if (b == 0) {
                stack.add(":error:");
            }
            else {
                int tempInt;
                String res;
                tempInt = (a) / (b);
                res = Integer.toString(tempInt);
                stack.set(stack.size() - 2, res);
                stack.remove(stack.size() - 1);
                isName.set(isName.size() -2, false);
                isName.remove(isName.size() - 1);
            }
        } catch (ArithmeticException e){
            System.out.println("Cannot divide by 0");
        }
    }

    private static void rem(int a, int b) {
        int tempInt;
        String res;

        try {
            if (b == 0) {
                stack.add(":error:");
                isName.add(false);
            }
            else {
                tempInt = a % b;
                res = Integer.toString(tempInt);
                stack.set(stack.size() - 2, res);
                stack.remove(stack.size() - 1);
                isName.set(isName.size() -2, false);
                isName.remove(isName.size() - 1);
            }
        } catch (ArithmeticException e){
            System.out.println("Modulus cannot be applied by 0");
        }
    }

    private static void neg(int a) {
        String res;
        int b;
        if (a != 0) {
            BigDecimal negInt = new BigDecimal(a);
            b = negInt.negate().intValue();
            res = Integer.toString(b);
            stack.set(stack.size() - 1, res);
        }
    }

    private static void swap(String a, String b) {
        String tempStr;
        tempStr = a;
        a = b;
        b = tempStr;
        stack.set(stack.size() - 1, a);
        stack.set(stack.size() - 2, b);

        boolean x, y, tempBool;
        x = isName.get(isName.size() - 1);
        y = isName.get(isName.size() - 2);
        tempBool = x;
        x = y;
        y = tempBool;
        isName.set(isName.size() - 1, x);
        isName.set(isName.size() - 2, y);
    }

    private static void cat(String a, String b) {
        String res;
        res = a + b;
        stack.set(stack.size() - 2, res);
        stack.remove(stack.size() - 1);
        isName.set(isName.size() -2, false);
        isName.remove(isName.size() - 1);
    }

    private static void and(String a, String b) {
        if (((a.equals(":true:") && b.equals(":true:")) || ((a.equals(":false:") && b.equals(":false:"))))) {
            stack.set(stack.size() - 2, ":true:");
            stack.remove(stack.size() - 1);
            isName.set(isName.size() -2, false);
            isName.remove(isName.size() - 1);
        }
        else {
            stack.set(stack.size() - 2, ":false:");
            stack.remove(stack.size() - 1);
            isName.set(isName.size() -2, false);
            isName.remove(isName.size() - 1);
        }
    }

    private static void or(String a, String b) {
        if (((a.equals(":true:") || b.equals(":true:")))) {
            stack.set(stack.size() - 2, ":true:");
            stack.remove(stack.size() - 1);
            isName.set(isName.size() -2, false);
            isName.remove(isName.size() - 1);
        }
        else {
            stack.set(stack.size() - 2, ":false:");
            stack.remove(stack.size() - 1);
            isName.set(isName.size() -2, false);
            isName.remove(isName.size() - 1);
        }
    }

    private static void not(String str) {
        if (str.equals(":true:")) {
            stack.set(stack.size() - 1, ":false:");
        }
        else {
            stack.set(stack.size() - 1, ":true:");
        }
    }

    private static void equal(String a, String b) {
        int x, y;
        x = Integer.parseInt(a);
        y = Integer.parseInt(b);
        if (x == y) {
            stack.set(stack.size() - 2, ":true:");
            stack.remove(stack.size() - 1);
            isName.set(isName.size() -2, false);
            isName.remove(isName.size() - 1);
        }
        else {
            stack.set(stack.size() - 2, ":false:");
            stack.remove(stack.size() - 1);
            isName.set(isName.size() -2, false);
            isName.remove(isName.size() - 1);
        }
    }

    private static void lessThan(String a, String b) {
        int x, y;
        x = Integer.parseInt(a);
        y = Integer.parseInt(b);
        if (x > y) {
            stack.set(stack.size() - 2, ":true:");
            stack.remove(stack.size() - 1);
            isName.set(isName.size() -2, false);
            isName.remove(isName.size() - 1);
        }
        else {
            stack.set(stack.size() - 2, ":false:");
            stack.remove(stack.size() - 1);
            isName.set(isName.size() -2, false);
            isName.remove(isName.size() - 1);
        }
    }

    private static void bind(String key, String value) {
        if (bindList.containsKey(value)) {
            bindList.put(key, bindList.get(value));
            stack.remove(stack.size() - 1);
            stack.remove(stack.size() - 1);
            stack.add(":unit:");
            isName.remove(isName.size() - 1);
            isName.remove(isName.size() - 1);
            isName.add(false);
        }
        else {
            bindList.put(key, value);
            stack.remove(stack.size() - 1);
            stack.remove(stack.size() - 1);
            stack.add(":unit:");
            isName.remove(isName.size() - 1);
            isName.remove(isName.size() - 1);
            isName.add(false);
        }
    }

    private static void if_fun(String str) {
        if (str.equals(":true:")) {
            stack.remove(stack.size() - 2);
            stack.remove(stack.size() - 2);
            isName.remove(isName.size() - 2);
            isName.remove(isName.size() - 2);
        }
        else if (str.equals(":false:")) {
            stack.remove(stack.size() - 1);
            stack.remove(stack.size() - 2);
            isName.remove(isName.size() - 1);
            isName.remove(isName.size() - 2);
        }
    }
}
