package com.example.tfm.model.giphy


import com.google.gson.annotations.SerializedName

data class Images(
    @SerializedName("downsized")
    val downsized: Downsized,
    @SerializedName("downsized_large")
    val downsizedLarge: DownsizedLarge,
    @SerializedName("downsized_medium")
    val downsizedMedium: DownsizedMedium,
    @SerializedName("downsized_small")
    val downsizedSmall: DownsizedSmall,
    @SerializedName("downsized_still")
    val downsizedStill: DownsizedStill,
    @SerializedName("fixed_height")
    val fixedHeight: FixedHeight,
    @SerializedName("fixed_height_downsampled")
    val fixedHeightDownsampled: FixedHeightDownsampled,
    @SerializedName("fixed_height_small")
    val fixedHeightSmall: FixedHeightSmall,
    @SerializedName("fixed_height_small_still")
    val fixedHeightSmallStill: FixedHeightSmallStill,
    @SerializedName("fixed_height_still")
    val fixedHeightStill: FixedHeightStill,
    @SerializedName("fixed_width")
    val fixedWidth: FixedWidth,
    @SerializedName("fixed_width_downsampled")
    val fixedWidthDownsampled: FixedWidthDownsampled,
    @SerializedName("fixed_width_small")
    val fixedWidthSmall: FixedWidthSmall,
    @SerializedName("fixed_width_small_still")
    val fixedWidthSmallStill: FixedWidthSmallStill,
    @SerializedName("fixed_width_still")
    val fixedWidthStill: FixedWidthStill,
    @SerializedName("looping")
    val looping: Looping,
    @SerializedName("original")
    val original: Original,
    @SerializedName("original_mp4")
    val originalMp4: OriginalMp4,
    @SerializedName("original_still")
    val originalStill: OriginalStill,
    @SerializedName("preview")
    val preview: Preview,
    @SerializedName("preview_gif")
    val previewGif: PreviewGif,
    @SerializedName("preview_webp")
    val previewWebp: PreviewWebp,
    @SerializedName("480w_still")
    val wStill: WStill
)