//package lk.ijse.dep11.app;
//
//import java.util.Map;
//import java.util.Set;
//
///**
// * A class to demonstrate how to access and display operating system environment variables.
// */
//public class OSEnv {
//
//    /**
//     * The main method of the program.
//     *
//     * @param args Command-line arguments (not used in this example).
//     */
//    public static void main(String[] args) {
//        // Get the operating system environment variables
//        Map<String, String> osEnv = System.getenv();
//
//        // Get and print the value of the "PATH" environment variable
//        String path = osEnv.get("PATH");
//        System.out.println("PATH: " + path);
//
//        // Get the set of environment variable names
//        Set<String> names = osEnv.keySet();
//
//        // Iterate through each environment variable and print its name and value
//        for (String name : names) {
//            System.out.printf("%s: %s \n", name, osEnv.get(name));
//        }
//    }
//}
