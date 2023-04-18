package com.lambdaschool.httpoperations.model

import android.util.Log

data class EmployeeJson (val name:String, val id: Int, val age:Int, val title: String)

open class Employee (val name:String, val id: Int, val age:Int, val title: String){
    val email:String = "$title$name@Kekw.lol"
    open fun signature():String{
        return "dear ,\n$name, $title\n$email\n"
    }
}

// TODO 5: Try to make some subclasses of this class. Is there an issue?
class Engineer(name: String, id:Int, age:Int, title:String):Employee(name, id, age, title){
    fun doEngineering(){
        Log.i("Employee","Doing engineer stuff")
    }

    /*override fun signature(): String {
        return "${super.signature()},\n$email"
    }*/
}
class Officer(name: String, id:Int, age:Int, title:String):Employee(name, id, age, title){
    fun goToMeeting(){
        Log.i("Employee", "going to sleep in the meeting room")
    }
}
class Tester(name: String, id:Int, age:Int, title:String):Employee(name, id, age, title){
    fun approve(){
        Log.i("Employee","Approved the product")
    }
}
class Designer(name: String, id:Int, age:Int, title:String):Employee(name, id, age, title){
    fun designerDesigning(){
        Log.i("Employee","Designer do designing")
    }
}
// TODO 6: Make the Employee class open and start to derive some subclasses (CLevelEmployee, Engineer, etc.)

/* TODO 7: Make a function in the Employee class to return an email address as a String.
    Use the format "$name@bigcorp.com"
 */

/* TODO 8: Add a function to the Employee class to generate an email signature.
    The signature should start "Best regards,\n$name, $title\n"
    For non-C-level employees, add the email address on the next line
    For C-level employees, don't include the email address
 */

// TODO 9: Make up some data to use
object employeesFakeData : ArrayList<Employee>(
    arrayListOf(
    Engineer(name = "ChuChu", id = 1, age = 16, title = "Engineer")
,Engineer(name = "Clark", id = 2, age = 30, title = "Engineer")
,Officer(name = "Dan", id = 3, age = 26, title = "CTO")
,Officer(name = "Ben", id = 4, age = 20, title = "CEO")
,Tester(name = "Kevin", id = 5, age = 21, title = "Tester")
,Designer(name = "Gwen", id = 6, age = 20, title = "Designer")
)
)