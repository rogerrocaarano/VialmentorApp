package me.rogerroca.vialmentorapp.model.entity

sealed class Identifier {
    data class IntId(val id: Int) : Identifier()
    data class StringId(val id: String) : Identifier()
}