# pojo-agent

A tool which enhances your pojo, powered by java-agent.

## current supported functions

* Helps you know that a setter is invoked or not.

## how to use

Works on java 11, 17.

Add `@Pojo` annotation on you pojo:

```java
@Pojo
public class MyEntity {
  private String id;
  // getters ...
  // setters ...
}
```

Add `@PojoCaller` on the method which requires enhancement:

```java
@PojoCaller
public void updateMyEntity(MyEntity entity) {
  // ...
}
```

Use `PojoAgent` helper class in the `@PojoCaller` functions:

```java
// check whether a field is set
PojoAgent.fieldIsSet(entity.getId())

// unset the field
PojoAgent.unsetField(entity.getId())
// then you will get `false` from `fieldIsSet` call after calling `unsetField`
// However note that the value of this property will not be modified, you can still get correct result from `getId()`
```

## sample

The sample code is in `./sample/src/main/java/my/test/Main.java`

Run `./gradlew clean sample` to see the output.
