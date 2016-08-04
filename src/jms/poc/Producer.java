package jms.poc;

import java.util.Properties;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Producer {

    private Connection connection;
    private Session session;
    private Destination destination;
    private ConnectionFactory factory;
    private MessageProducer messageProducer;

    private final String connectionString, userName, password;

    private static final int DELIVERY_MODE = DeliveryMode.NON_PERSISTENT;

    Producer(String connectionString, String userName, String password) {

        System.out.println("**  Creating Producer ");
        System.out.println("connectionString: " + connectionString);
        System.out.println("username: " + userName);
        System.out.println("password: " + password);

        this.connectionString = connectionString;
        this.userName = userName;
        this.password = password;
    }

    public void open(String factoryName, String queueName) throws Exception {

        try {

            Properties props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.tibco.tibjms.naming.TibjmsInitialContextFactory");
            props.setProperty(Context.PROVIDER_URL, connectionString);
            InitialContext context = new InitialContext(props);

            factory = (ConnectionFactory) context.lookup(factoryName);
            destination = (Destination) context.lookup(queueName);
            connection = factory.createConnection(userName, password);
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            messageProducer = session.createProducer(destination);

        } catch (NamingException | JMSException e) {
            throw new Exception(e);
        }

    }

    public void sendTopicMessage(String messageStr) throws Exception {

        try {
            TextMessage message = session.createTextMessage(messageStr);
            messageProducer.send(message, DELIVERY_MODE, Message.DEFAULT_PRIORITY, Message.DEFAULT_TIME_TO_LIVE);
            System.out.println("Published message: " + messageStr);

        } catch (JMSException e) {
            throw new Exception(e);
        }
    }

    public void close() throws Exception {
        try {
            session.close();
            connection.close();
        } catch (JMSException e) {
            throw new Exception(e);
        }
    }

}
