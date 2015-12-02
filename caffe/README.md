# Compile

```
For simple app:
sbt package

To include external jars:
sbt assembly

```

# run

```
$SPARK_HOME/bin/spark-submit --class CaffeApp --master <sparkURL> target/scala-2.10/caffe-app_2.10-1.0.jar
```

