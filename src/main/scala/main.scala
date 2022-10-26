import java.io.{File, PrintWriter}
import scala.io.Source._
import scala.io.StdIn._

object main extends App{


  def start():Unit={
    // on charge les données au démarage
    val data = loadData()
    println("Bonjour et bienvenu")
    askTask(data)

  }


  def askTask(data: Data):Unit={
    println("Merci de saisir une action: ")
    println(" 1. Lecture\t2. Ajout\t3. Suppression\t4. A propos\t5. Quitter")
    try{
      val action = readInt()
      action match {
        // lecture
        case 1 => read(data)
        // ajout
        case 2 => add(data)
        // suppresion
        case 3 => delete(data)
        // a propos
        case 4 => about(data)
        // quitter
        case 5 => quit()
        // saisie n'imp
        case _ => askTask(data)

      }
    }catch {
      case _:Throwable => {
        println("!!! Merci de saisir un entier entre 1 et 5 !!!\n")
        askTask(data)
      }
    }



  }

  def read(data: Data): Unit = {
    data.displayData()
    askTask(data)
  }

  /**
   *
   * @param data
   * 1 readUser input & convert into List[Int]
   * 2 check if can be add (enough space and not already present)
   */
  def add(data: Data):Unit ={

    val availableDataSpace = data.getAvailableSpaceInInfo()
    val availableAddressingSpace = data.getAvailableSpaceInAddressing()

    println("Ajout d'une information: \n\n"+availableDataSpace+" caractères d'information disponibles \n"+availableAddressingSpace/2+ " places disponible d'addressage\n")
    // 1
    try {
      val newInfoStr = readLine() // adding space for readability
      println("quel numero pour cette nouvelle info ?")
      val newId = readInt()
      val newInfoIntLst = convertString2LstInt(newInfoStr.toList)
      val newAddressingIntLst = createAddressing(newId, newInfoIntLst.length)
      // 2 verification
      if ((newInfoIntLst.length < availableDataSpace) & (availableAddressingSpace > 2) & (!data.checkInInfo(newInfoIntLst)) & (!data.checkInAddressing(newId)) & (newInfoStr != "0")) {
        val newInfo = data.addNewInfo(data.info, newInfoIntLst)
        val newAddressing = data.addNewAddressing(data.addressing, newAddressingIntLst)
        println("Etes vous-sur de vouloir ajouter ? Y/n")
        val confirm = readLine()
        if (confirm == "Y") {
          println("Ajouté ! ")
          val dataConvertToSave = convertListInt2Lines(newInfo ++ newAddressing)
          saveData(dataConvertToSave)
          askTask(Data(newInfo, newAddressing))
        }
        else {
          askTask(data)
        }

      }
      else {
        println("Pas possible d'ajouter")
        askTask(data)
      }
    }catch {
      case _:Throwable=>{
        println("!!! Erreur de saisie !!! ")
        askTask(data)
      }
    }

  }

  def delete(data:Data):Unit ={
    println("Veuillez saisir le numéro à supprimer: ")
    try {
      val index = readInt()
      if (data.checkInAddressing(index)) {
        val newInfo = data.deleteInfo(data.info, index)
        val newAddressing = data.deleteAddressing(data.addressing, data.addressing.indexOf(index))
        val dataConvertToSave = convertListInt2Lines(newInfo ++ newAddressing)
        println("Etes vous-sur de vouloir supprimer ? Y/n")
        val confirm = readLine()
        if(confirm =="Y"){
          saveData(dataConvertToSave)
          askTask(Data(newInfo, newAddressing))
        }
        else{
          askTask(data)
        }
      }
      else {
        println("Le n° saisie ne correspond à rien ")
        askTask(data)
      }
    }
    catch {
      case _:Throwable =>{
        println("!!! Erreur de saisie !!! ")
        askTask(data)
      }
    }
  }

  def about(data: Data):Unit ={
    println("Ce programme a été réaliser par Aurélien Pollet")
    println("@2022 Junia Hei, All right reserved")
    println("Scala code runner version 3.2.0 -- Copyright 2002-2022, LAMP/EPFL\n")
    askTask(data)
  }

  def quit():Unit={
    println("A bientôt")
  }

  /**  LOAD THE DATA
   * 1 Load lines from file
   * 2 call function to convert it into intList
   * 3 call function to convert intList into data object class
   * return the data
   */
  def loadData():Data= {
    // load and convert only if passed data if not null

      // 1
      val source = fromFile("src/main/scala/sample_cassette.txt")
      val lines = try source.getLines.toList finally source.close()
      // 2
      val intLst = convertLines2LstInt(lines)
      // 3
      val convertData = convertIntLst2Data(intLst)
      // 4
      convertData

  }

  def saveData(lines:List[String]):Unit={
    val writer = new PrintWriter(new File("src/main/scala/sample_cassette.txt"))
    lines.foreach((line)=>writer.write(line))
    writer.close()
  }

  // CONVERSION SECTION

  def convertLines2LstInt(lines:List[String],intLst: List[Int]=List[Int](), cursor:Int=0):List[Int]={
    if(cursor < lines.length){
      val int_line = lines(cursor).split(',').map(_.trim.toInt).toList
      convertLines2LstInt(lines, intLst++int_line, cursor+1)
    }
    else {
      intLst
    }
  }

  def convertIntLst2Data(intLst:List[Int]):Data={
    val splitLst = intLst.splitAt(800)
    Data(splitLst._1,splitLst._2)
  }

  def convertString2LstInt(line:List[Char], intLst:List[Int]=List[Int](),cursor:Int=0):List[Int]={
    if(cursor<line.length){
      val intFromChar = line(cursor).toInt
      convertString2LstInt(line, intLst.appended(intFromChar), cursor+1)
    }
    else {
      intLst
    }
  }

  def createAddressing(id:Int,size:Int):List[Int]={
    List(id,size)
  }
  // espect to get info + addressing already concat
  def convertListInt2Lines(intList:List[Int],lines:List[String]=List[String](),cursor:Int=0):List[String]={
    if(cursor < intList.length){
      convertListInt2Lines(intList, lines.appended(intList(cursor).toString+","), cursor+1)
    }
    else{
      lines
    }
  }
  start()
}
