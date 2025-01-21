import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) throws Exception {
        System.out.print("$ ");
        Scanner scanner = new Scanner(System.in);
        String cwd = Path.of("").toAbsolutePath().toString();
        do{
            String input = scanner.nextLine();
            String[] inputArr = {};
            if(input.contains("exe ")){
                inputArr = input.split("/",2);
                catOperations("\""+"/"+inputArr[1]+"\"");
            }
            else{
                inputArr = input.split(" ",2);
                if("exit".equals(inputArr[0])){
                    System.exit(Integer.parseInt(inputArr[1]));
                }else if("echo".equals(inputArr[0])){
                    echoMethod(input,inputArr);
                }else if("type".equals(inputArr[0])){
                    getType(inputArr[1]);
                }else if ("cd".equals(inputArr[0])) {
                    cwd = cdTypeOperations(inputArr[1],cwd);
                }else if ("cat".equals(inputArr[0])) {
                    catOperations(inputArr[1]);
                }else{
                    execPath(input,inputArr,cwd);
                }
            }
            System.out.print("$ ");
        }while(scanner.hasNext());
        scanner.close();
    }

    private static void echoMethod(String input, String[] inputArr) throws InterruptedException, IOException{
        if(input.contains("1>")){
            echocat(input);
        }else if(inputArr[1].startsWith("\"") && !inputArr[1].contains("\\")){
            doubleQuotes(inputArr[1]);
        }else if(inputArr[1].startsWith("'")){
            String temp = inputArr[1].substring(1, inputArr[1].length()-1);
            System.out.println(temp.replace(String.valueOf("'"),""));
        }else if(!inputArr[1].startsWith("'") || !inputArr[1].startsWith("\"")){
            specialChar(inputArr);
        }else{
            String[] splited = inputArr[1].split(" ");
            String res = splited[0];
            for(int i=1;i<splited.length;i++){
                if(splited[i] != ""){
                    res = res + " "+splited[i];
                }
            }
            System.out.println(res);
        }
    }

    private static void echocat(String input) throws InterruptedException, IOException{
        String[] split = input.split(" 1> ");
            // System.out.println(split[0]+"---"+split[1]);
        File outputFile = new File(split[1]);
        File outputDir = outputFile.getParentFile();
        if (!outputDir.exists()) {
            if (!outputDir.mkdirs()) {
                System.err.println("Failed to create output directory: " + outputDir.getAbsolutePath());
                return;
            }
        }    
        String[] commandParts = split[0].split(" ",2);
        commandParts[1] = commandParts[1].trim();
        commandParts[1] = commandParts[1].substring(1,commandParts[1].length()-1);
        ProcessBuilder processBuilder = new ProcessBuilder(commandParts);
        processBuilder.redirectOutput(outputFile);
        Process process = processBuilder.start();
        process.waitFor();
    }

    private static void specialChar(String[] inputArr){
        if(inputArr[1].contains("\\")){
            if(!inputArr[1].startsWith("'") && !inputArr[1].startsWith("\"")){
                String temp = inputArr[1].replace(String.valueOf("\\"),"");
                System.out.println(temp);
            }else{
            String result = parseQuotedString(inputArr[1]);
            System.out.println(result);
            }
            
        }else{
            String[] splited = inputArr[1].split(" ",2);
            String res = splited[0].trim() + " " + splited[1].trim();
            System.out.println(res);
        }
    }

    private static String getPath(String parameter) {
        for (String path : System.getenv("PATH").split(":")) {
          Path fullPath = Path.of(path, parameter);
          if (Files.isRegularFile(fullPath)) {
            return fullPath.toString();
          }
        }
        return null;
    }

    private static void getType(String param){
        String[] builtIn = {"echo", "exit", "type", "pwd"};
        if(Arrays.asList(builtIn).contains(param)){
            System.out.println(param+" is a shell builtin");
        }else{
            String path = getPath(param);
            if (path != null) {
                System.out.println(param + " is " + path);
            } else {
                System.out.println(param + ": not found");
            }
        }
    }

    private static void execPath(String input, String[] inputArr, String cwd) throws IOException, InterruptedException{
        String param = inputArr[0];
        String path = getPath(param);
        if("pwd".equals(param)){
            System.out.println(cwd);
        }else if (path == null) {
            System.out.println(param+": command not found");
        } else if (input.contains(">")) {
            String[] split = input.split(" > ");
            // System.out.println(split[0]+"---"+split[1]);
            File outputFile = new File(split[1]);
            File outputDir = outputFile.getParentFile();
            if (!outputDir.exists()) {
                if (!outputDir.mkdirs()) {
                    System.err.println("Failed to create output directory: " + outputDir.getAbsolutePath());
                    return;
                }
            }    
            String[] commandParts = split[0].split(" ");
            ProcessBuilder processBuilder = new ProcessBuilder(commandParts);
            processBuilder.redirectOutput(outputFile);
            Process process = processBuilder.start();
            process.waitFor();
            
        }else {
            Process p = Runtime.getRuntime().exec(input.split(" "));
            p.getInputStream().transferTo(System.out);
        }
    }

    private static void doubleQuotes(String input){
        String temp = input.replace(String.valueOf("\" "),"\"");
        String[] splited = temp.split("\"");
        String res = "";
        for (String splited1 : splited) {
            if (splited1 != " ") {
                res += splited1;
            }
        }
        System.out.println(res);
    }

    private static void catOperations(String inputArr) throws IOException, InterruptedException{
        String[] splitted = {};
        if(inputArr.startsWith("'")){
            String temp = inputArr.substring(1, inputArr.length()-1);
            splitted = temp.split("' '");
            catprint(splitted);
        }else if(inputArr.startsWith("\"")){
            String temp = inputArr.substring(1, inputArr.length()-1);
            splitted = temp.split("\" \"");
            catprint(splitted);
        }else if(inputArr.contains("1>")){
            String[] temp = inputArr.split(" 1> ");
            String[] tempT = temp[0].split(" ");
            if(!Files.isRegularFile(Path.of(tempT[0]))){
                System.out.println("cat: " +tempT[0]+": No such file or directory");
            }else{
                Path path = Paths.get(tempT[0]);
                String content = Files.readString(path);
                echocat("echo \""+content+"\" 1> "+temp[1]);
            }
        
            if(!Files.isRegularFile(Path.of(tempT[1]))){
                    System.out.println("cat: " +tempT[1]+": No such file or directory");
            }else{
                Path path = Paths.get(tempT[1]);
                String content = Files.readString(path);
                echocat("echo \""+content+"\" 1> "+temp[1]);  
            }
        }else{
            catprint(inputArr.split("' '"));
        }
    }

    private static void catprint(String[] splitted) throws IOException{
        for(int i=0;i<splitted.length;i++){
            Path path = Paths.get(splitted[i].trim());
            // System.out.println(Files.readAllLines(path));
            String content = Files.readString(path);
            System.out.print(content);
        }
        
    }

    private static String cdTypeOperations(String inputArr,String cwd){
        if(inputArr.startsWith("./")){
            cwd = cwd + inputArr.substring(1);
        }else if (inputArr.startsWith("../")) {
            int count = (int) inputArr.length()/3;
            String[] temp = cwd.substring(1).split("/");
            String res = "/";
            if(count>=temp.length){
                cwd = res;
            }else{
                for(int i=0;i<temp.length-count;i++){
                    res = res + temp[i];
                }
                cwd = res;
            }

        }else if (inputArr.startsWith("~")) {
            cwd = System.getenv("HOME");
        }else if (Files.isDirectory(Path.of(inputArr))) {
            cwd = inputArr;
        }else{
            System.out.println("cd: " + inputArr+": No such file or directory");
        }
        return cwd;
    }

    private static String parseQuotedString(String inputString) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        int length = inputString.length();
        while (i < length) {
            char currentChar = inputString.charAt(i);
            if (currentChar == '\\' && i + 1 < length) {
                char nextChar = inputString.charAt(i + 1);
                if (nextChar == '\\' || nextChar == '$' || nextChar == '"' || nextChar == '\n') {
                    result.append(nextChar);
                    i += 2;
                    continue;
                } else {
                    result.append('\\');
                }
            } else if (currentChar == '$' || currentChar == '"'){
                i+=1;
                continue;
            }
            else {
                result.append(currentChar);
            }
            i++;
        }
        return result.toString();
    }
}
