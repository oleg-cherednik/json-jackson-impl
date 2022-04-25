[![Maven Central](https://maven-badges.herokuapp.com/maven-central/ru.oleg-cherednik.utils/jackson-utils/badge.svg)](https://maven-badges.herokuapp.com/maven-central/ru.oleg-cherednik.jackson/jackson-utils)
[![javadoc](https://javadoc.io/badge2/ru.oleg-cherednik.utils/jackson-utils/javadoc.svg)](https://javadoc.io/doc/ru.oleg-cherednik.jackson/jackson-utils)
[![java1.8](https://badgen.net/badge/java/1.8/blue)](https://badgen.net/)
[![circle-ci](https://circleci.com/gh/oleg-cherednik/jackson-utils/tree/dev.svg?style=svg)](https://circleci.com/gh/oleg-cherednik/jackson-utils/tree/dev)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![quality](https://app.codacy.com/project/badge/Grade/a2abf7ff8b1b4e82ad2cd0d039aea353)](https://www.codacy.com/gh/oleg-cherednik/jackson-utils/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=oleg-cherednik/jackson-utils&amp;utm_campaign=Badge_Grade)
[![coverage](https://app.codacy.com/project/badge/Coverage/a2abf7ff8b1b4e82ad2cd0d039aea353)](https://www.codacy.com/gh/oleg-cherednik/jackson-utils/dashboard?utm_source=github.com&utm_medium=referral&utm_content=oleg-cherednik/jackson-utils&utm_campaign=Badge_Coverage)
[![vulnerabilities](https://snyk.io//test/github/oleg-cherednik/JacksonUtils/badge.svg?targetFile=build.gradle)](https://snyk.io//test/github/oleg-cherednik/JacksonUtils?targetFile=build.gradle)
[![license-scan](https://app.fossa.com/projects/git%2Bgithub.com%2Foleg-cherednik%2Fjackson-utils?utm_source=share_link)

     
# jackson-utils
> [Jackson Project](https://github.com/FasterXML/jackson) usability utilities.
> It's designed to add additional features like easy and centralized configuration,
> builder or static method set. Artifact does not include direct `Jackson Project`.
> It is up to you to add them into your project.

## Features
*   Encapsulate all checked exceptions from Jackson with custom runtime exception;
*   A central place for configuration;
*   A central place for holding `ObjectMapper` instances;
*   Utility class to make most common operations much more comfortable to use;
*   Ability to change `Zone` to save `ZonedDateTime` independently of original zone;
*   `ByteBuffer`/`InputStream` support for objects, lists and maps;
*   Lazy read support for list from `Writer`;
*   Read numeric as `Integer`, `Long`, `BigInteger` or `Double` (but not only as `Double`);
*   Advanced `Reader`/`Writer` support for `enum`. 

## Gradle

```groovy
compile 'ru.oleg-cherednik.jackson:jackson-utils:2.4'
```

## Maven

```xml
<dependency>
    <groupId>ru.oleg-cherednik.jackson</groupId>
    <artifactId>jackson-utils</artifactId>
    <version>2.4</version>
</dependency>
```                                                    

**Note:** `jackson-utils` does not contain dependency to the specific `Jackson Project`
version, so you have to add it additionally:

## Usage 

To simplify usage of _jackson-utils_, there're following classes:
*   [JacksonUtils](#jacksonutils-class) - utility class with set of methods to use json transformation;
*   [EnumId](#work-with-enum) - advanced enum serialization support.

### JacksonUtils class

#### Read json from `String`

##### `String` to a custom object type (but not a collection)

```java
class Data {
    int intVal;
    String strVal;
}
```
```java
String json = """
              {
                  "intVal" : 666,
                  "strVal" : "omen"
              }
              """;
Data data = JacksonUtils.readValue(json, Data.class);
```

##### `String` to a list of custom object type

```java
class Data {
    int intVal;
    String strVal;
}
```
```java
String json = """
              [
                  {
                      "intVal" : 555,
                      "strVal" : "victory"
                  },
                  {
                      "intVal" : 666,
                      "strVal" : "omen"
                  }
              ]
              """;
List<Data> res = JacksonUtils.readList(json, Data.class);
```

##### `String` to a map of custom object type

###### Map with `String` keys and `Map` or primitive types as values

```java
String json = """
              {
                  "victory" : {
                      "intVal" : 555,
                      "strVal" : "victory"
                  },
                  "omen" : {
                      "intVal" : 666,
                      "strVal" : "omen"
                  }
              }
              """;
Map<String, ?> map = JacksonUtils.readMap(json);
```
**Note:** `map` values have either primitive type or `Map` or `List`.

###### `String` to a map with `String` keys and given type as value

```java
class Data {
    int intVal;
    String strVal;
}
```
```java
String json = """
              {
                  "victory" : {
                      "intVal" : 555,
                      "strVal" : "victory"
                  },
                  "omen" : {
                      "intVal" : 666,
                      "strVal" : "omen"
                  }
              }
              """;
Map<String, Data> map = JacksonUtils.readMap(json, Data.class);
```

###### `String` to a map with `Integer` keys and given type as value

```java
class Data {
    int intVal;
    String strVal;
}
```
```java
String json = """
              {
                  "1" : {
                      "intVal" : 555,
                      "strVal" : "victory"
                  },
                  "2" : {
                      "intVal" : 666,
                      "strVal" : "omen"
                  }
              }
              """;
Map<Integer, Data> map = JacksonUtils.readMap(json, Integer.class, Data.class);
```

#### Read json from `InputStream`

##### `InputStream` to a custom object type (but not a collection)

```java
class Data {
    int intVal;
    String strVal;
}
```
```json                        
{
    "intVal" : 666,
    "strVal" : "omen"
}
```
```java         
try (InputStream in = ...) {
    Data data = JacksonUtils.readValue(in, Data.class);
}
```

##### `InputStream` to a list of custom object type

##### Read eager
```java
class Data {
    int intVal;
    String strVal;
}
```
```json                        
[
    {
        "intVal" : 555,
        "strVal" : "victory"
    },
    {
        "intVal" : 666,
        "strVal" : "omen"
    }
]
```
```java
try (InputStream in = ...) {
    List<Data> res = JacksonUtils.readList(in, Data.class);
}
```

##### Read lazy

```java
class Data {
    int intVal;
    String strVal;
}
```
```json                        
[
    {
        "intVal" : 555,
        "strVal" : "victory"
    },
    {
        "intVal" : 666,
        "strVal" : "omen"
    }
]
```
```java
try (InputStream in = ...) {
    Iterator<Data> it = JacksonUtils.readListLazy(in, Data.class);
    
    while (it.hasNext()) {
        Data data = it.next();
    }
}
```
##### `InputStream` to a map of custom object type

###### `InputStream` to a map with `String` keys and `Map` or primitive types as values

```json                        
{
    "victory" : {
        "intVal" : 555,
        "strVal" : "victory"
    },
    "omen" : {
        "intVal" : 666,
        "strVal" : "omen"
    }
}
```
```java
try (InputStream in = ...) {
    Map<String, ?> map = JacksonUtils.readMap(in);
}
```
**Note:** `map` values have either primitive type or `Map` or `List`.

###### `InputStream` to a map with `String` keys and given type as value

```java
class Data {
    int intVal;
    String strVal;
}
```
```json                        
{
    "victory" : {
        "intVal" : 555,
        "strVal" : "victory"
    },
    "omen" : {
        "intVal" : 666,
        "strVal" : "omen"
    }
}
```
```java
try (InputStream in = ...) {
    Map<String, ?> map = JacksonUtils.readMap(in, Data.class);
}
```

###### Map with `Integer` keys and given type as value

```java
class Data {
    int intVal;
    String strVal;
}
```
```json                        
{
    "1" : {
        "intVal" : 555,
        "strVal" : "victory"
    },
    "2" : {
        "intVal" : 666,
        "strVal" : "omen"
    }
}
```
```java
try (InputStream in = ...) {
    Map<Integer, Data> map = JacksonUtils.readMap(in, Integer.class, Data.class);
}
```

##### Links

*   Home page: https://github.com/oleg-cherednik/jackson-utils

*   Maven:
    *   **central:** https://mvnrepository.com/artifact/ru.oleg-cherednik.jackson/jackson-utils
    *   **download:** https://repo1.maven.org/maven2/ru/oleg-cherednik/jackson/jackson-utils
