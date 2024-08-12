package com.genesisguild.model.request

import com.genesisguild.model.Content

data class GeminiRequest(
    val contents: List<Content>
)