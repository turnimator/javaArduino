void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  Serial.println("Hello");
  pinMode(13, OUTPUT);
  digitalWrite(13, 1);
  delay(1000);
  digitalWrite(13, 0);
}

char buffer[256];
void loop() {
  int i = 0;
  while (Serial.available()) {
    buffer[i++] = Serial.read();
    delay(20);
  }
  buffer[i] = 0;
  if (i == 0){
    yield();
    return;
  }
  Serial.print("Received: ");
  Serial.println(buffer);
  if (buffer[2] == 'A'){
    digitalWrite(13, 1);
  }
  if (buffer[2] == 'O'){
    digitalWrite(13, 0);
  }
}
