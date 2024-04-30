package airhacks.lambda.greetings.control;

public interface Log {
    static void debug(Object message){
        System.out.println(message);
    }
}
