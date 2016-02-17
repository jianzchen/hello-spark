# hello-spark


./bin/spark-submit \
    --master "local[*]" \
    --driver-memory 1g --executor-memory 1g \
    --executor-cores 1 \
    --class SparkPi \
    lib/hello-spark-1.0-SNAPSHOT-jar-with-dependencies.jar

./bin/spark-submit \
    --master "local[*]" \
    --class SparkDedup \
    lib/hello-spark-1.0-SNAPSHOT-jar-with-dependencies.jar \
    /user/jianzchen/input/test.csv /user/jianzchen/output
    
spark-submit \
    --queue root.default \
    --class testDF \
    ~/cjz/lib/hello-spark-1.0-SNAPSHOT-jar-with-dependencies.jar \
    /user/mis/cjz/input/ /user/mis/cjz/output
    
create external table hdw.cjz_spark_test 
(
id int,name string,desc string
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'
LINES TERMINATED BY '\n'
STORED AS SEQUENCEFILE
LOCATION '/user/mis/cjz/output2/';
