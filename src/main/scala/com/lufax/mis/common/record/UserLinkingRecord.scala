package com.lufax.mis.common.record

import org.apache.commons.lang.StringUtils

/**
  * Created by chenghan951 on 2016/2/4.
  */
class UserLinkingRecord extends Serializable {
  val delimiter: String = "\t"

  var guid: String = ""
  var userName: String = ""
  var timestamp: Long = 0l

  def setValue(context: String): Unit = {
    val cols: Array[String] = StringUtils.splitPreserveAllTokens(context, delimiter)
    if (!(cols(0) == UserLinkingRecord.dataSource)) {
      throw new InterruptedException("Expect " + UserLinkingRecord.dataSource + ", but " + cols(0))
    }

    this.guid = cols(1)
    this.userName = cols(2)
    this.timestamp = cols(3).toLong
  }

  def setValue(other: UserLinkingRecord): UserLinkingRecord = {
    if (this.timestamp < other.timestamp) {
      this.userName = other.userName
      this.timestamp = other.timestamp
    }

    return this
  }

  override def toString(): String = {
    getValue()
  }

  def getValue(): String = {
    val builder = new StringBuilder()

    builder.append(UserLinkingRecord.dataSource)
    builder.append(delimiter)

    builder.append(guid)
    builder.append(delimiter)

    builder.append(userName)
    builder.append(delimiter)

    builder.append(timestamp)

    return builder.toString()
  }

  def setValue(event: BackFillCalEventRecord): Unit = {
    this.guid = event.guidPost
    this.userName = event.userNamePost
    this.timestamp = event.timestamp
  }
}

object UserLinkingRecord extends Serializable {
  val dataSource: String = "UL"
}