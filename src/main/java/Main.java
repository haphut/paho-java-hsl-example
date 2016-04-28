import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Main {
    // FIXME: Change CLIENT_ID.
    private static final String CLIENT_ID = "clientIdGoesHere";
    private static final String BROKER = "tcp://mqtt.hsl.fi:1883";
    private static final String TOPIC = "/hfp/journey/#";
    private static final int QOS = 2;

    public static void main(String[] args) {
        try {
            // FIXME: Possibly change to MqttAsyncClient.
            MqttClient client = new MqttClient(BROKER, CLIENT_ID);

            MqttCallback callback = new MqttCallback() {
                public void connectionLost(Throwable cause) {
                    // FIXME: Add logging: https://wiki.eclipse.org/Paho/Log_and_Debug_in_the_Java_client
                    // FIXME: Add reconnecting: https://stackoverflow.com/a/33735501
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Not needed as we are only receiving MQTT messages.
                }

                public void messageArrived(String topic, MqttMessage message)
                        throws Exception {
                    // FIXME: Parse message and send JMS from here.
                    System.out.println("Topic " + topic);
                    System.out.println("Message " + message.toString());
                }
            };

            client.setCallback(callback);

            // FIXME: Fine-tune connection settings, e.g. will and TLS.
            MqttConnectOptions connectOptions = new MqttConnectOptions();

            System.out.println("Connect to broker " + BROKER);
            client.connect(connectOptions);

            System.out.println("Subscribe to topic " + TOPIC);
            client.subscribe(TOPIC, QOS);

            Thread.sleep(5000);

            System.out.println("Disconnect");
            client.disconnect();
        } catch(MqttException me) {
            System.out.println("Reason " + me.getReasonCode());
            System.out.println("Message " + me.getMessage());
            System.out.println("LocalizedMessage " + me.getLocalizedMessage());
            System.out.println("Cause " + me.getCause());
            System.out.println("Exception " + me);
            me.printStackTrace();
        // Required by Thread.sleep().
        } catch(InterruptedException ie) {
            System.out.println("Exception " + ie);
            ie.printStackTrace();
        }
    }
}
