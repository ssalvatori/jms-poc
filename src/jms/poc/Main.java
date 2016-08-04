package jms.poc;

public class Main {

    static String serverUrl = "host";
    static Integer port = 10701;
    static String userName = "user";
    static String password = "password";
    
    private static final int MAX_MESSAGES = 10000;

    public static void main(String[] args) {
        try {
            String connectionStr = serverUrl + ":" + port;
            Producer producer = new Producer(connectionStr, userName, password);
            producer.open("factoryNAME", "QueueNAME");
            
            
            for (int i=0;i < MAX_MESSAGES; i++) {
                System.out.println("Sending message: "+String.valueOf(i));
                producer.sendTopicMessage(String.valueOf(i));
            }
                       
            producer.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }
}
