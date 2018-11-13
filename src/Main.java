public class Main {
    public static void main(String[] args) {
        Interpreter intepreter = new Interpreter();

        String input = "src/inputFiles/input8.txt";
        String output = "src/output.txt";

        Interpreter.interpreter(input,output);
    }
}
