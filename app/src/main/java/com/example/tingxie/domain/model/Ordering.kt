package com.example.tingxie.domain.model

sealed class Ordering {
    object Acsending : Ordering()
    object Descending : Ordering()
}
