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
    
    