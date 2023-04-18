package com.example.abstractassignmentrebuild.model

abstract class Vehicle (id:String,weight:Int,favorite:Boolean) {
    abstract fun Travel(): String
}
interface AirTravel{
    fun fly():String
}

interface WaterTravel{
    fun sail():String
}

interface GroundTravel{
    fun drive():String
}



class Submarine() : WaterTravel,Vehicle("SUB",200000,false) {
    override fun sail():String {
        return "yes this thing can Sail"
    }

    override fun Travel(): String {
        return "Submarine Travel by sailin"
    }

}

class BigBike : GroundTravel,Vehicle("BB",1,false){
    override fun Travel(): String {
        return "Ride Big Bike to travel on ground"
    }

    override fun drive():String {
        return "You actually don't drive this, you ride it"
    }

}

class Truck : GroundTravel,Vehicle("T",3,true){
    override fun Travel(): String {
        return "Drive Truck to travel on ground"
    }

    override fun drive():String {
        return "You drove a truck and run over someone"
    }

}

class AttackHelicopter : AirTravel,Vehicle("AH",30000,true){
    override fun Travel(): String {
        return "Helicopter travel by flyin around"
    }

    override fun fly():String {
        return "not just fly you can obliterate people with this"
    }

}

class Titanic : WaterTravel,Vehicle("BT",52000000,false){
    override fun Travel(): String {
        return "It's a big boat it can sail"
    }

    override fun sail(): String {
        return "It spend more time sinking than sailing actually"
    }

}

class ME : GroundTravel,Vehicle(id = "HUMAN",90,true){
    override fun Travel(): String {
        return "You can ride my back to travel on foot"
    }

    override fun drive(): String {
        return "You actually don't drive this, you ride me"
    }

}

fun ListOfVehicle():ArrayList<Vehicle> {
val listOfVehicle = arrayListOf<Vehicle>()
    listOfVehicle.add(Submarine())
    listOfVehicle.add(AttackHelicopter())
    listOfVehicle.add(Truck())
    listOfVehicle.add(BigBike())
    listOfVehicle.add(Titanic())
    listOfVehicle.add(ME())

    return listOfVehicle
}