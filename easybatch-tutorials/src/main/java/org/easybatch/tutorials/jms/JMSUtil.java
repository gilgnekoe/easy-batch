/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.easybatch.tutorials.jms;

import org.apache.activemq.broker.BrokerService;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

/**
 * JMS Utilities class.
 */
public class JMSUtil {

    private static QueueSender queueSender;

    private static QueueSession queueSession;

    private static QueueConnection queueConnection;

    private static BrokerService broker;

    public static void initJMSFactory() throws Exception {

        Properties p = new Properties();
        p.load(JMSUtil.class.getResourceAsStream(("/jndi.properties")));
        Context jndiContext = new InitialContext(p);

        QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) jndiContext.lookup("QueueConnectionFactory");
        Queue queue = (Queue) jndiContext.lookup("q");

        queueConnection = queueConnectionFactory.createQueueConnection();
        queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        queueSender = queueSession.createSender(queue);
        queueConnection.start();

    }

    public static void startBroker() throws Exception {
        broker = new BrokerService();
        broker.addConnector("tcp://localhost:61616");
        broker.start();
        initJMSFactory();
    }

    public static void sendJmsMessage(String jmsMessage) throws JMSException {
        TextMessage message = queueSession.createTextMessage();
        message.setText(jmsMessage);
        queueSender.send(message);
        System.out.println("Message '" + jmsMessage + "' sent to JMS queue");
    }

    public static void closeJMSSession() throws JMSException {
        queueConnection.close();
        queueSender.close();
        queueSession.close();
    }

    public static void stopBroker() throws Exception {
        queueConnection.close();
        queueSender.close();
        queueSession.close();
        broker.stop();
    }

}
