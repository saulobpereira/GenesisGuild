package com.genesisguild.model

data class UsageMetadata(
    val candidatesTokenCount: Int,
    val promptTokenCount: Int,
    val totalTokenCount: Int
)