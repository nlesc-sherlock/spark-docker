# Compile

```
For simple app:
sbt package

To include external jars:
sbt assembly

```

# run

```
$SPARK_HOME/bin/spark-submit --class EnronDataApp --master <sparkURL> target/scala-2.10/enronData-app_2.10-1.0.jar
```

