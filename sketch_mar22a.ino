#include <ESP8266WiFi.h>

const char* ssid     = "Zairza Net";
const char* password = "notyourwifi";

const char* host = "studentsforhire.in";
int var1 = D2;
int i =0;
void setup() {
  Serial.begin(115200);
  delay(10);

  // We start by connecting to a WiFi network
  pinMode(var1,OUTPUT); 
  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  
  /* Explicitly set the ESP8266 to be a WiFi-client, otherwise, it by default,
     would try to act as both a client and an access-point and could cause
     network-issues with your other WiFi-devices on your WiFi-network. */
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");  
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

int value = 0;

void loop() {
  delay(5000);
  ++value;

  Serial.print("connecting to ");
  Serial.println(host);
  
  // Use WiFiClient class to create TCP connections
  WiFiClient client;
  const int httpPort = 80;
  if (!client.connect(host, httpPort)) {
    Serial.println("connection failed");
    return;
  }
  
  // We now create a URI for the request
  String url = "/test1";
  //url += streamId;
  //url += "?private_key=";
  //url += privateKey;
  //url += "&value=";
  //url += value;
  
  Serial.print("Requesting URL: ");
  Serial.println(url);
  
  // This will send the request to the server
  client.print(String("GET ") + url + " HTTP/1.1\r\n" +
               "Host: " + host + "\r\n" + 
               "Connection: close\r\n\r\n");
  unsigned long timeout = millis();
  while (client.available() == 0) {
    if (millis() - timeout > 5000) {
      Serial.println(">>> Client Timeout !");
      client.stop();
      return;
    }
  }
  
  // Read all the lines of the reply from server and print them to Serial
  while(client.available()){
    String line = client.readStringUntil('\r');
    Serial.print(line);
    String tm,st;
    tm = line.substring(0,3);
    st = line.substring(4,5);
    Serial.println(tm);
    Serial.println(st);
    if(st.equals("1")){
    digitalWrite(var1,HIGH);
    }
    if(st.equals("0")){
      digitalWrite(var1,LOW);
    }
  }
  
  Serial.println();
  Serial.println("closing connection");
}

