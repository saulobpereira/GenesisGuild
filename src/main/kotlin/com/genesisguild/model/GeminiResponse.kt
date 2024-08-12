package com.genesisguild.model

data class GeminiResponse(
    val candidates: List<Candidate>,
    val usageMetadata: UsageMetadata
)