package dds.perf


import dds.Topic
import dds.pub._
import dds.sub._
import dds.qos._
import org.opensplice.perf.SeqPayload
import dds.event.DataAvailable
import swing.event.Event

import org.opensplice.perf.SeqPayload
import reflect.ValDef

object LatencySub {
  def main(args: Array[String]) {
    val wtopic = Topic[SeqPayload]("RSeqPayload")
    val dwqos = DataWriterQos() <= Reliable() <= TransientDurability()
    val writer = DataWriter[SeqPayload](wtopic, dwqos)

    val rtopic = Topic[SeqPayload]("WSeqPayload")
    val drqos = DataReaderQos() <= Reliable() <= TransientDurability()
    val reader = DataReader[SeqPayload](rtopic, drqos)

    reader.reactions += {
      case e: DataAvailable[_] => {
        val data: Array[SeqPayload] = e[SeqPayload].reader.read
        writer ! data(0)
      }
    }
  }

}