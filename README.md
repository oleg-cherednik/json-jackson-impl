[![Maven Central](https://maven-badges.herokuapp.com/maven-central/ru.oleg-cherednik.utils/jackson-utils/badge.svg)](https://maven-badges.herokuapp.com/maven-central/ru.oleg-cherednik.utils/jackson-utils)
[![javadoc](https://javadoc.io/badge2/ru.oleg-cherednik.utils/jackson-utils/javadoc.svg)](https://javadoc.io/doc/ru.oleg-cherednik.utils/jackson-utils)
[![Build Status](https://travis-ci.org/oleg-cherednik/jackson-utils.svg?branch=dev)](https://travis-ci.org/oleg-cherednik/jackson-utils)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![codecov](https://codecov.io/gh/oleg-cherednik/jackson-utils/branch/dev/graph/badge.svg)](https://codecov.io/gh/oleg-cherednik/jackson-utils)
[![Known Vulnerabilities](https://snyk.io//test/github/oleg-cherednik/jackson-utils/badge.svg?targetFile=build.gradle)](https://snyk.io//test/github/oleg-cherednik/jackson-utils?targetFile=build.gradle)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/a2abf7ff8b1b4e82ad2cd0d039aea353)](https://www.codacy.com/gh/oleg-cherednik/jackson-utils/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=oleg-cherednik/jackson-utils&amp;utm_campaign=Badge_Grade)
[![coverity](https://scan.coverity.com/projects/22381/badge.svg)](https://scan.coverity.com/projects/oleg-cherednik-jackson-utils)
     
# jackson-utils
> a java tool to make working with Jackson Project more comfortable

## Features
  * Encapsulate all checked exceptions from Jackson with custom runtime exception;
  * A centralized configuration of `ObjectMapper`;
  * A central place of settings and all `ObjectMapper` instances;
  * Utility class to make most common operations much more comfortable to use;
  * Ability to change `Zone` to save `ZonedDateTime` independently of original zone;
  * `InputStream` support for objects, lists and maps;
  * Lazy read support for list from `InputStream`. 

## Gradle

```
compile 'ru.oleg-cherednik.utils:jackson-utils:2.12.0.2'
```

## Maven

```xml
<dependency>
    <groupId>ru.oleg-cherednik.utils</groupId>
    <artifactId>jackson-utils</artifactId>
    <version>2.12.0.2</version>
</dependency>
```                                                    

In the version, first 3 places are the version of `Jackson` that is used in this utils.
The last section is the `jackson-utils` version. This number is unique. 

## Usage 

To simplify usage of _jackson-utils_, there're following classes:
* [JsonUtils](#jsonutils) - utility class with set of method to use json transformation;

### JsonUtils

#### Read json from `String`

##### Custom object type (but not a collection)

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
```
String json;
Data data = JsonUtils.readValue(json, Data.class);
```

##### List of custom object type

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
```
String json;
List<Data> res = JsonUtils.readList(json, Data.class);
```

##### Map of custom object type

###### Map with `String` keys and `Map` or primitive types as values

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
```
String json;
Map<String, ?> map = JsonUtils.readMap(json);
```
**Note:** `map` values have either primitive type or `Map` or `List`.

###### Map with `String` keys and given type as value

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
```
String json;
Map<String, Data> map = JsonUtils.readMap(json, Data.class);
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
```
String json;
Map<Integer, Data> map = JsonUtils.readMap(json, Integer.class, Data.class);
```

#### Read json from `InputStream`

##### Custom object type (but not a collection)

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
```         
try (InputStream in = ...) {
    Data data = JsonUtils.readValue(in, Data.class);
}
```

##### List of custom object type

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
```
try (InputStream in = ...) {
    List<Data> res = JsonUtils.readList(in, Data.class);
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
```
try (InputStream in = ...) {
    Iterator<Data> it = JsonUtils.readListLazy(in, Data.class);
    
    while (it.hasNext()) {
        Data data = it.next();
    }
}
```
##### Map of custom object type

###### Map with `String` keys and `Map` or primitive types as values

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
```
try (InputStream in = ...) {
    Map<String, ?> map = JsonUtils.readMap(in);
}
```
**Note:** `map` values have either primitive type or `Map` or `List`.

###### Map with `String` keys and given type as value

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
```
try (InputStream in = ...) {
    Map<String, ?> map = JsonUtils.readMap(in, Data.class);
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
```
try (InputStream in = ...) {
    Map<Integer, Data> map = JsonUtils.readMap(in, Integer.class, Data.class);
}
```

##### Links
* Home page: https://github.com/oleg-cherednik/jackson-utils
* Maven:
  * **central:** https://mvnrepository.com/artifact/ru.oleg-cherednik.utils/jackson-utils
  * **download:** https://repo1.maven.org/maven2/ru/oleg-cherednik/utils/jackson-utils/
