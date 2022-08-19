package com.example.tingxie.domain.model.util

sealed class Ordering {
    object Acsending : Ordering()
    object Descending : Ordering()
}
