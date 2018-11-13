import java.io.*;
import java.util.ArrayList;
import java.math.*;
import java.util.Collections;

public class Interpreter {
    private static ArrayList<String> stack = new ArrayList<>();

    public static void interpreter (String inputFile, String outputFile) {
        String tempStr, push;
        String error = ":error:", line = null, letter = ".*[a-zA-Z].*", specialChar = "[!#$%&()*+,./:;<=>?@\\[\\]^_{|}~]";
        boolean notOperational = stack.isEmpty() || stack.size() == 1 || !isInteger(stack.get(stack.size() - 1)) || !isInteger(stack.get(stack.size() - 2));

        try {
            FileReader fileReader = new FileReader(inputFile);
            FileWriter fileWriter = new FileWriter(outputFile);

            BufferedReader br = new BufferedReader(fileReader);
            BufferedWriter bw = new BufferedWriter(fileWriter);

            while ((line = br.readLine()) != null) {

                // push functions
                if (line.contains("push")) {
                     tempStr = line.substring(4);
                     push = tempStr.trim();

                    if(isInteger(push)){
                        if (push.contains("-0")) {
                            tempStr = push.replaceAll("-","");
                            push = tempStr;
                            stack.add(push);
                        }
                        else {
                            stack.add(push);
                        }
                    }
                    else if (push.contains("")){
                        tempStr = push.replaceAll("\"","");
                        push = tempStr;
                        stack.add(push);
                    }
                    else if (push.matches((letter)) || push.matches(specialChar))  {
                        stack.add(push);
                    }
                    else if (line.equals(":true:")) {
                        stack.add(":true:");
                    }
                    else if (line.equals(":false:")) {
                        stack.add(":false:");
                    }
                    else if (line.equals(error)) {
                        stack.add(error);
                    }
                    else {
                        stack.add(error);
                    }
                }

                // pop function
                if (line.equals("pop")) {
                    if (stack.isEmpty()) {
                        stack.add(error);
                    }
                    else {
                        stack.remove(stack.size() - 1);
                    }
                }

                // add function
                if (line.equals("add")) {
                    if (notOperational) {
                        stack.add(error);
                    }
                    else {
                        add(Integer.parseInt(stack.get(stack.size() - 1)), Integer.parseInt(stack.get(stack.size() - 2)));
                    }
                }

                // sub function
                if (line.equals("sub")) {
                    if (notOperational) {
                        stack.add(error);
                    }
                    else {
                        sub(Integer.parseInt(stack.get(stack.size() - 2)),Integer.parseInt(stack.get(stack.size() - 1)));
                    }
                }

                // mul function
                if (line.equals("mul")) {
                    if (notOperational) {
                        stack.add(error);
                    }
                    else {
                        mul(Integer.parseInt(stack.get(stack.size() - 2)),Integer.parseInt(stack.get(stack.size() - 1)));
                    }
                }

                // div function
                if (line.equals("div")) {
                    if (notOperational) {
                        stack.add(error);
                    }
                    else {
                        div(Integer.parseInt(stack.get(stack.size() - 2)),Integer.parseInt(stack.get(stack.size() - 1)));
                    }
                }

                // rem function
                if (line.equals("rem")) {
                    if (notOperational) {
                        stack.add(error);
                    }
                    else {
                        rem(Integer.parseInt(stack.get(stack.size() - 2)),Integer.parseInt(stack.get(stack.size() - 1)));
                    }
                }

                // neg function
                if (line.equals("neg")) {
                    if (stack.isEmpty() || !isInteger(stack.get(stack.size() - 1))) {
                        stack.add(error);
                    }
                    else {
                        neg(Integer.parseInt(stack.get(stack.size() - 1)));
                    }
                }

                // swap function
                if (line.equals("swap")) {
                    if (stack.isEmpty() || stack.size() == 1) {
                        stack.add(error);
                    }
                    else {
                        swap(stack.get(stack.size() - 1), stack.get(stack.size() - 2));
                    }
                }

                if (line.equals("cat")) {

                }

                // quit function
                if (line.contains("quit")){
                    Collections.reverse(stack);
                    for (String str: stack) {
                        bw.write(str);
                        bw.newLine();
                    }
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

    private static void add(int a, int b) {
        String res;
        int tempInt;
        tempInt = (a) + (b);
        res = Integer.toString(tempInt);
        stack.set(stack.size() - 2, res);
        stack.remove(stack.size() - 1);
    }

    private static void sub(int a, int b) {
        int tempInt;
        String res;
        tempInt = (a) - (b);
        res = Integer.toString(tempInt);
        stack.set(stack.size() - 2, res);
        stack.remove(stack.size() - 1);
    }

    private static void mul(int a, int b){
        int tempInt;
        String res;
        tempInt = (a) * (b);
        res = Integer.toString(tempInt);
        stack.set(stack.size() - 2, res);
        stack.remove(stack.size() - 1);
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
            }
            else {
                tempInt = a % b;
                res = Integer.toString(tempInt);
                stack.set(stack.size() - 2, res);
                stack.remove(stack.size() - 1);
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

    private static void swap(String x, String y) {
        String tempStr;
        tempStr = x;
        x = y;
        y = tempStr;
        stack.set(stack.size() - 1, x);
        stack.set(stack.size() - 2, y);
    }
}
