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

# Upload cleaned emails

Download forqlift from http://qethanm.cc/projects/forqlift
```
./bin/forqlift fromarchive --date-type text --file enron_mail_clean.seq enron_mail_clean.tar.gz
hdfs dfs -put enron_mail_clean.seq enron_mail/
```

Process cleaned mails in Spark using
```
val emailsList = sc.sequenceFile[String, String]("/user/sherlock/enron_mail/enron_mail_clean.seq")
```

