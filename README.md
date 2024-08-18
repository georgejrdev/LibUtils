<div id="title" align="center">
  <h1>Lib Utils</h1>
  <p>A lib for java that abstracts common actions across multiple projects.</p>
</div>

<div id="badges" align="center">
  
![License](https://img.shields.io/github/license/georgejrdev/Lib-Utils.svg)
![Version](https://img.shields.io/badge/version-1.0.1-53918E.svg)
![Windows](https://img.shields.io/badge/made%20for-java-AD6845.svg)
<a href="https://github.com/georgejrdev/Lib-Utils/raw/main/build/Lib-Utils-1.0.1.jar">![Windows](https://img.shields.io/badge/download-lib-AA155E.svg)</a>

</div>

<br>
<br>

## Utility Classes:

### Email:
send emails easily and quickly, with a single static class for the entire project.

#### Example: 

```java
import com.georgejrdev.auxiliar.email.Email;

public class Main {
    public static void main(String[] args) {
            Email.initialize("gmail", "email@gmail.com", "app_password");

            Email.sendEmail("Subject", "Body", "recipient@gmail.com");

        } 
}
```
#### Methods:
```java
- sendEmail("Subject", "Body", "recipient@gmail.com");
```

<br>

### Key-Value Json Manipulation:

manipulate json files in simple key-value structure.

#### Example:

```java
import com.georgejrdev.auxiliar.json.*;

KeyValueJson json = new KeyValueJsonImpl("path");

json.createRegister();
```

#### Methods:
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