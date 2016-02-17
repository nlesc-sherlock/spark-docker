
# Build

```
sbt assembly
```

Jars are build in `target/scala-2.10/`.

# Prep data

```
tar -cf images.tar images
forqlift fromarchive --file images.seq images.tar
hdfs dfs -copyFromLocal images.seq /home/shelly/images.seq
rm images.seq images.tar
```

# Run

On spark initialized prompt (spark-env.sh sourced).

```
hdfs dfs -rm -r labels.orc
spark-submit --class CaffeAppSeq --master yarn-cluster  --num-executors 15 --executor-memory 512m \
--files /usr/hdp/current/spark-client/conf/hive-site.xml \
--jars /usr/hdp/current/spark-client/lib/datanucleus-api-jdo-3.2.6.jar,\/usr/hdp/current/spark-client/lib/datanucleus-rdbms-3.2.9.jar,/usr/hdp/current/spark-client/lib/datanucleus-core-3.2.10.jar \
caffeAppSeq-assembly-1.0.2.jar images.seq labels.orc
```
