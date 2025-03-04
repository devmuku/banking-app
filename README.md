# Banking App

Prototyp eines Backend-Dienstes für eine Banking-App


## Voraussetzung

Auf dem Entwicklungsrechner soll Java 17 oder neuer und maven 3.6 oder neuer installiert werden, damit man in der Lage ist den Code zu kompilieren und die Anwendung auszuführen.  

Java version prüfen 
```shell
java -version
```
Maven version prüfen
```shell
mvn -v
```


## Verwendung in IDE
In der IDE oder auf Eingabeaufforderung kan man die Anwendung, mit Hilfe von spring-boot-maven-plugin starten und stoppen.

Die Anwendung zu starten
```shell
mvn spring-boot:run
```
Die Anwendung zu stoppen
```shell
mvn spring-boot:stop
```

## Installation

Um die Anwendung zu installieren braucht man eine JAR-Datei, diese wird mit Hilfe von spring-boot-maven-plugin gebaut.
```shell
mvn clean package spring-boot:repackage
```
Die gebaute Jar-Datei ist unter **'banking-app/target'** zu finden. Die Banking App kan mann auf einem Rechner installieren, indem man diese Datei darauf kopiert. Auf dem Rechner soll JRE vorhanden sein, damit die Anwendung laufen kann. Sie kann dem Befehl **java -jar** ausführt werden, wie folgt.
```shell
java -jar <Pfad zur JAR-datei>
```
Nachdem die Anwendung gestartet wurde, läuft sie auf der Port 8080. 

## Endpunkte

Folgende tabelle stellt eine grobe Beschreibung aller implementierten Endpunkten dar. Eine ausführliche Dokumentation aller Endpunkten kann, nach dem Starten der Anwendung, unter [api-doc](http://localhost:8080/swagger-ui/index.html) eingesehen werden. Auf dieser Seite kann man die einzelnen Endpunkten aufrufen, um deren Funktionsweise zu prüfen.

| Endpunkt                      | Methode | Beschreibung                                                                 |
|-------------------------------|---------|------------------------------------------------------------------------------|
| /accounts/deposit             | PUT     | Einzahlen von Geld auf ein Konto                                             |
| /accounts/withdraw            | PUT     | Abheben von Geld von einem Konto                                             |
| /accounts/{accountNr}/balance | GET     | Anzeigen des aktuellen Kontostands eines Kontos                              |
| /accounts/{accountNr}/history | GET     | Anzeigen von Kontohistorie von Abhebungen und Einzahlungen im Laufe der Zeit |
| /accounts/transfer            | PUT     | Überweisen von Geld auf ein anderes Konto                                    |

Endpunkte sind, wie folgt, erreichbar.
```
http://<Rechner IP>:8080/<Endpunkt>
```
Um sie aufzurufen, kann man das Kommandozeilentool curl oder postman verwenden.