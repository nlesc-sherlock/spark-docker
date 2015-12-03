# Compile

```
For simple app:
sbt package

To include external jars:
sbt assembly

```

# run

```
$SPARK_HOME/bin/spark-submit --class CaffeApp --master yarn-cluster  --num-executors 15 --executor-memory 2G `pwd`/target/scala-2.10/caffeApp-assembly-1.0.jar /user/sherlock/lda/caffe/images/ /user/sherlock/lda/caffe/out
```

