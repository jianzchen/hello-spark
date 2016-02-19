package com.lufax.mis.common.record

/**
  * Created by chenjianzhou622 on 2016/2/16.
  */
object UserLinkingAction {
  def updateUserLinking(value: Iterable[UserLinkingRecord]): UserLinkingRecord = {
    value.toArray.sortWith(_.timestamp < _.timestamp).last
  }

  def main(args: Array[String]) {
    val backFillCalEventRecord1 = new BackFillCalEventRecord
    backFillCalEventRecord1.setValue("BF_CAL\tlog_id\tguid_post\tuser_name_post\tremote_addr\tuser_agent\t0\t0\tsession_id\t1\t2\t3\t1435358549226")
    val backFillCalEventRecord2 = new BackFillCalEventRecord
    backFillCalEventRecord2.setValue("BF_CAL\tlog_id2\tguid_post\tuser_name_post2\tremote_addr2\tuser_agent2\t0\t0\tsession_id\t1\t2\t3\t1435358549224")
    val backFillCalEventRecord3 = new BackFillCalEventRecord
    backFillCalEventRecord3.setValue("BF_CAL\tlog_id2\tguid_post\tuser_name_post2\tremote_addr2\tuser_agent2\t0\t0\tsession_id\t1\t2\t3\t1435358549225")
    val iter = Iterable[BackFillCalEventRecord](backFillCalEventRecord1, backFillCalEventRecord2, backFillCalEventRecord3)
    val result = getLatestUserLinking(("guid_post", iter))
    for (x <- result) println(x._1 + "|" + x._2.getValue)
  }

  def getLatestUserLinking(value: (String, Iterable[BackFillCalEventRecord])): Array[(String, UserLinkingRecord)] = {
    val userLinkingRecord = new UserLinkingRecord
    val delimiter = userLinkingRecord.delimiter
    val guidPost = value._1
    val backFillCalEvent = value._2

    //backFillCalEvent.map(x => (x.userNamePost,x.timestamp)).filter(! _._1.isEmpty).filter(! _._1.equals("")).groupBy(_._1).mapValues(_.max).map(x => (guidPost,x._2._1,x._2._2)).toArray
    val record = backFillCalEvent.map(x => (x.guidPost, x.userNamePost, x.timestamp)).filter(x => filterNotNull(x))
      /*.map(x => getUserLinkingRecord(UserLinkingRecord.dataSource+delimiter+x.productIterator.mkString(delimiter)))*/
      .groupBy(x => x._1 + delimiter + x._2)
      .map(x => (x._1, getUserLinkingRecord(UserLinkingRecord.dataSource + delimiter + x._1 + delimiter + getMaxTimestamp(x._2))))
      .toArray
    record
    /*    val record = backFillCalEvent.map(x => (x.guidPost,x.userNamePost,x.timestamp)).filter(x => filterNotNull(x))
        if (!record.isEmpty) {
          userLinkingRecord.setValue(UserLinkingRecord.dataSource + delimiter + record.toArray.sortWith(_._3 < _._3).last.productIterator.mkString(delimiter))
        }
        userLinkingRecord*/
  }

  def filterNotNull(value: (String, String, Long)): Boolean = {
    if ((value._1 isEmpty) || (value._2.isEmpty) || (value._1 equals "") || (value._2 equals "")) false
    else true
  }

  def getMaxTimestamp(value: Iterable[(String, String, Long)]): Long = {
    value.map(_._3).max
  }

  def getUserLinkingRecord(value: String): UserLinkingRecord = {
    val userLinkingRecord = new UserLinkingRecord
    userLinkingRecord.setValue(value)
    userLinkingRecord
  }

}
