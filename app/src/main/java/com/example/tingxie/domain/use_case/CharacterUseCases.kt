package com.example.tingxie.domain.use_case

data class CharacterUseCases(
    val addCharacter: AddCharacter,
    val deleteCharacter: DeleteCharacter,
    val getCharacters: GetCharacters,
    val getCharacter: GetCharacter,
    val getNRandomCharacters: GetNRandomCharacters,
    val insertQuizResult: InsertQuizResult,
    val getCharacterResults: GetCharacterResults,
    val getQuizResult: GetQuizResult,
    val getQuizResults: GetQuizResults,
    val getQuizResultsLimitedBy: GetQuizResultsLimitedBy,
    val getQuizResultsBetween: GetQuizResultsBetween
)