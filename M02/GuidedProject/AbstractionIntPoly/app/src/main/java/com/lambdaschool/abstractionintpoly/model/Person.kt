package com.lambdaschool.abstractionintpoly.model

import com.google.gson.annotations.SerializedName

/*
{
    name: "Chewbacca",
    height: "228",
    mass: "112",
    hair_color: "brown",
    skin_color: "unknown",
    eye_color: "blue",
    url: "https://swapi.co/api/people/13/"
}
 */

// TODO 2: S05M02-2 Add class which inherits from parent class
data class Person(val height:String,
                  val mass:String,
                  @SerializedName("hairColor")val hairColor:String,
                  @SerializedName("skinColor")val skinColor:String,
                  @SerializedName("eyesColor")val eyesColor:String)
    :SwApiObject(){
    override fun info(): String = "EYES = $eyesColor\nURL = $url"

    override fun toString(): String {
        return "$name is $height cm tall, have $skinColor skin,$hairColor hair and $eyesColor eyes"
    }
    }

