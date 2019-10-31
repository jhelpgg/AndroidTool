package jhelp.thread.permission

import jhelp.thread.promise.Promise

class BeforeAskPermissionAction(
   val permission: String,
   private val task: () -> Unit,
   private val promiseReceiveDecision: Promise<Pair<String, () -> Unit>>
                               )
{
   fun allowToAskPermission()
   {
      this.promiseReceiveDecision.result(Pair(this.permission, this.task))
   }

   fun refuseToAskPermission()
   {
      this.promiseReceiveDecision.error(Exception(this.permission))
   }
}