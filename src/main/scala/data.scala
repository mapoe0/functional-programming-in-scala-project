case class Data(info:List[Int], addressing:List[Int]){

  /**
   * 
   * @param info
   * @param newInfo
   * @param infoCursor
   * @param newInfoCursor
   * @return 
   * Return a new version of the List[Int] with the new info added 
   */
  def addNewInfo(info:List[Int],newInfo:List[Int], infoCursor:Int=0,newInfoCursor:Int=0):List[Int]={
    if((infoCursor < info.length) & (newInfoCursor<newInfo.length)){
      if(info(infoCursor) !=0){
        addNewInfo(info,newInfo,infoCursor+1,newInfoCursor)
      }
      else{
        addNewInfo(info.updated(infoCursor,newInfo(newInfoCursor)),newInfo, infoCursor+1, newInfoCursor+1)
      }
    }
    else{
      info
    }
  }

  /**
   *
   * @param addressing
   * @param newAddressing [n° info, taille]
   * @param addressingCursor
   * @param newAddressingCursor
   * @return Return a new version of the List[Int] with the new addressing added
   */
  def addNewAddressing(addressing:List[Int], newAddressing:List[Int], addressingCursor:Int=0, newAddressingCursor:Int=0): List[Int] ={
    if((addressingCursor<addressing.length) & (newAddressingCursor< newAddressing.length)){
      if(addressing(addressingCursor)!=0){
        addNewAddressing(addressing,newAddressing, addressingCursor+1, newAddressingCursor)
      }
      else{
        addNewAddressing(addressing.updated(addressingCursor,newAddressing(newAddressingCursor)), newAddressing, addressingCursor+1, newAddressingCursor+1)
      }
    }
    else{
      addressing
    }
  }

  // replace an info by 0
  def deleteInfo(info:List[Int],infoId:Int,infoCursor:Int=0,infoDelete:List[Int]=List[Int]()): List[Int] ={
    // step 1 get the sum of sizes of the info before the one to delete
    val indexStart = getIndexStart(infoId)
    val sizeIndex = addressing.indexOf(infoId)+1
    val rangeToDelete = addressing(sizeIndex)
    if(infoCursor<info.length){
      // before the section to Delete
      if(infoCursor<indexStart){
        deleteInfo(info, infoId, infoCursor+1,infoDelete.appended(info(infoCursor)))
      }
        // range of the section: recall the function without append the newInfo Lst
      else if(infoCursor<(indexStart+rangeToDelete)){
        deleteInfo(info, infoId, infoCursor+1, infoDelete)
      }
        // after the section to delete
      else{
        deleteInfo(info, infoId, infoCursor+1,infoDelete.appended(info(infoCursor)))
      }
    }
    else{
      val newInfo = addZero(infoDelete,rangeToDelete)
      newInfo
    }
  }
  // replace an addressing by 0
  def deleteAddressing(addressing:List[Int],index:Int,cursor:Int=0):List[Int]={
    if(cursor == 0 | cursor==1){
      deleteAddressing(addressing.patch(index,Nil,1), index, cursor+1)
    }
    else if (cursor == 2 | cursor == 3){
      deleteAddressing(addressing.appended(0),index,cursor+1)
    }
    else{
      addressing
    }
  }
  //  get the sum of sizes of the info before the one to delete
  def getIndexStart(infoId:Int,cursor:Int=0,sum:Int=0): Int ={
    // remind: List(info0,size0, info1,size1, info2,size2)
    val position = addressing.indexOf(infoId)
    if(cursor<=position){
      // we know that the size is always on an odd index
      if(cursor%2 == 0){
        getIndexStart(infoId, cursor+1, sum)
      }
      else{
        getIndexStart(infoId, cursor+1, sum+addressing(cursor))
      }
    }
    else{
      sum
    }
  }
  // replace the deleted elem by 0 at the bottom
  def addZero(lst:List[Int],nbOfZero:Int,cursor:Int=0):List[Int]={
      if(cursor < nbOfZero){
        addZero(lst.appended(0),nbOfZero,cursor+1)
      }
    else{
        lst
      }
  }

  def displayData(cursor:Int=0):Unit={
    if(cursor < addressing.length & addressing(cursor)!=0){
      val currentSeq = getIntSeqFromAddressing(addressing(cursor))
      println("Information "+addressing(cursor) +" (taille "+addressing(cursor+1)+")")
      currentSeq.foreach((c)=>print(c.toChar))
      println("\n")
      displayData(cursor+2)
    }
  }

  def getAvailableSpaceInInfo(cursor:Int=0, cpt:Int=0):Int={
    if(cursor < info.length){
      if(info(cursor)!=0){
        getAvailableSpaceInInfo(cursor+1,cpt)
      }
      else{
        getAvailableSpaceInInfo(cursor+1,cpt+1)
      }
    }
    else {
      cpt
    }
  }

  def getAvailableSpaceInAddressing(cursor:Int=0, cpt:Int=0):Int={
    if(cursor<addressing.length){
      if(addressing(cursor) !=0){
        getAvailableSpaceInAddressing(cursor+1,cpt)
      }
      else{
        getAvailableSpaceInAddressing(cursor+1,cpt+1)
      }
    }
    else{
      cpt
    }
  }

  /**
   * @param id id of the seq to return
   */
  def getIntSeqFromAddressing(id:Int): List[Int] ={
      val start = getIndexStart(id)
      val end = addressing(addressing.indexOf(id)+1)+start
      info.slice(start,end)
    }

  def checkInInfo(newInfo:List[Int],cursor:Int=0):Boolean={
    if(cursor<addressing.length & addressing(cursor)!=0){
      val currentSeq =  getIntSeqFromAddressing(addressing(cursor))
      if(currentSeq == newInfo) {
        println("Cette information existe déjà")
        true
      }
      else {
        checkInInfo(newInfo,cursor+2)
      }
    }
    else{
      false
    }
  }

  def checkInAddressing(id:Int,cursor:Int=0):Boolean={
    if(cursor < addressing.length){
      if(cursor%2 == 0 & addressing(cursor) == id){
        println("Ce numéro d'addressing existe déjà")
        true
      }
      else{
        checkInAddressing(id, cursor+1)
      }
    }
    else{
      false
    }
  }

}
