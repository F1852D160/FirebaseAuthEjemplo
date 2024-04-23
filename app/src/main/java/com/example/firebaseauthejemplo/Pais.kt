package com.example.firebaseauthejemplo

data class Pais(val id:String,val nombre:String,val id_estado:String){
    constructor():this("","","")

    override fun toString(): String {
        return nombre
    }
}
