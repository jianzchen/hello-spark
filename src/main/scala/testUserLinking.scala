import org.apache.hadoop.io.Text
import org.apache.hadoop.io.compress.SnappyCodec

/**
  * Created by chenjianzhou622 on 2016/2/19.
  */
class testUserLinking {

  //TODO 这里还需要一步更新user_linking的步骤,需要考虑user_linking以怎样的方式更新比较合理
  val userLinkingTempPath: String = "/user/mis/user_linking/temp"
  //TODO 配置化
  val userLinkingLatestPath: String = "/user/mis/user_linking/latest"
  //TODO 配置化
  val userLinkingPreviousPath: String = "/user/mis/user_linking/previous"
  //TODO 配置化
  val userLinkingPartitionNum = 10
  //TODO 配置化
  val hadoopConf = new org.apache.hadoop.conf.Configuration()
  val hdfs = org.apache.hadoop.fs.FileSystem.get(hadoopConf)
  try {
    hdfs.delete(new org.apache.hadoop.fs.Path(userLinkingPreviousPath), true)
    hdfs.rename(new org.apache.hadoop.fs.Path(userLinkingLatestPath), new org.apache.hadoop.fs.Path(userLinkingPreviousPath))
  } catch {
    case _: Throwable => {}
  }
  val userLinkingOutputRdd = backFillUserNameRdd
    .flatMap(value => UserLinkingAction.getLatestUserLinking(value))

  userLinkingOutputRdd.map(x => (new Text(x._1), new Text(x._2.getValue))).saveAsSequenceFile(userLinkingTempPath, Some(classOf[SnappyCodec]))

  userLinkingOutputRdd.map(x => (new Text(x._1), x._2))
    .union(sparkContext.sequenceFile(userLinkingPreviousPath, classOf[Text], classOf[Text]).map(x => (x._1, UserLinkingAction.getUserLinkingRecord(x.toString()))))
    .reduceByKey({ (x, y) => (x._1, x._2.setValue(y._2)) }, userLinkingPartitionNum)
    .map(x => (x._1, new Text(UserLinkingAction.updateUserLinking(x._2).getValue())))
    .saveAsSequenceFile(userLinkingLatestPath, Some(classOf[SnappyCodec]))


}
