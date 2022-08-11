package com.example.tingxie.domain.use_case

data class CharacterUseCases(
    val addCharacter: AddCharacter,
    val deleteCharacter: DeleteCharacter,
    val getCharacters: GetCharacters,
    val getCharacter: GetCharacter,
    val getNRandomCharacters: GetNRandomCharacters
)