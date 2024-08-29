<div id="title" align="center">
  <h1>Lib Utils</h1>
  <p>A lib for java that abstracts common actions across multiple projects.</p>
</div>

<div id="badges" align="center">
  
![License](https://img.shields.io/github/license/georgejrdev/Lib-Utils.svg)
![Version](https://img.shields.io/badge/version-1.1.2-53918E.svg)
![Windows](https://img.shields.io/badge/made%20for-java-AD6845.svg)
<a href="https://github.com/georgejrdev/Lib-Utils/raw/main/build/Lib-Utils-1.1.2.jar">![Windows](https://img.shields.io/badge/download-lib-AA155E.svg)</a>

</div>

<br>
<br>

## Utility Classes

### Hot Reload
a hot reload for files.

#### Example

```java
import com.georgejrdev.lib.reload.HotReload;

String fileToWatch = "file/to/watch.file";
String fileToUpdate = "file/to/update.file";
int httpPort = 8080;
int webSocketPort = 8081;

HotReload hotReload = new HotReload(fileToWatch, fileToUpdate, httpPort, webSocketPort);

hotReload.start();
```

you can also pass a parser, implemented following the Parser interface, defined in the library. This will convert from the source file, to the destination html file.

#### Example

##### ParserImpl.java
```java
import com.georgejrdev.lib.reload.Parser;

public class ParseImpl implements Parser{

    public void parse() {  
        // Write a new file with all changes
    }
```

##### Main.java

```java
import com.georgejrdev.lib.reload.HotReload;

ParserImpl parser = new ParserImpl(); // PARSER

String fileToWatch = "file/to/watch.file";
String fileToUpdate = "file/to/update.file";
int httpPort = 8080;
int webSocketPort = 8081;

HotReload hotReload = new HotReload(fileToWatch, fileToUpdate, httpPort, webSocketPort, parser); // <- PARSER

hotReload.start();
```

#### Methods
```java
- start();
```

<br>

### Email
send emails easily and quickly, with a single static class for the entire project.

#### Example 

```java
import com.georgejrdev.lib.email.Email;

public class Main {
    public static void main(String[] args) {
            Email emailSender = new Email("emailServer","email","password");
            emailSEnder.sendEmail("subject","body","to");
        } 
}
```
#### Methods
```java
- sendEmail("Subject", "Body", "recipient@gmail.com");
```

<br>

### Key-Value Json Manipulation

manipulate json files in simple key-value structure.

#### Example

```java
import com.georgejrdev.lib.json.*;

KeyValueJson json = new KeyValueJsonImpl("path");

json.createRegister();
```

#### Methods
```java
- createRegister();
```
```java
- updateRegister();
```
```java
- deleteRegister();
```
```java
- getContent();
```

<br>

### File Watcher

detect changes to files and take action based on that

for this, you need to implement a class from the FileWatcherCallback interface.

#### Example

##### FileWatcherAction.java

```java
import com.georgejrdev.lib.watcher.FileWacherCallback;

public class FileWatcherAction {
    public void action(){
        // action for when a change is detected
    }
}
```

now, you can instantiate the File Watcher class. It must be started next to a new Thread, to keep it running in the background, listening for changes while other things happen.


##### Main.java
```java
import com.georgejrdev.lib.watcher.FileWatcher;

public class Main{
    public static void main(String[] args){
    
        FileWatcherAction action = new FileWatcherAction();

        FileWatcher fileWatcher = new FileWatcher("./watch.file", action);

        new Thread(fileWatcher).start();

    }
}
```

#### Methods
```java
- start() 
```

```java
- stop()
```

<br>

### Simple Http Server

a simple http server

#### Example

```java
import com.georgejrdev.lib.htpp.SimpleHttpServer;

public class Main{

    public static void main(String[] args){

        SimpleHttpServer server = new SimpleHttpServer();
        server.start(8080,"file/to/expose.file");
    }
}
```

#### Methods

```java
- start(PORT,"File/To/Expose.file")
```

```java
- stop()
```

<br>

### Simple WebSocket Server

a simple websocket server

#### Example

```java
import com.georgejrdev.lib.websocket.SimpleWebSocketServer;

public class Main{

    public static void main(String[] args){

        SimpleWebSocketServer server = new SimpleWebSocketServer(8080);
        server.start();
        server.notifyClients("message to all clients");
    }
}
```

#### Methods

```java
- start()
```

```java
- notifyClients("Message")
```

```java
- stop()
```