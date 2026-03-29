package com.theveloper.pixelplay.data.media

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ReplayGainManagerTest {

    private val manager = ReplayGainManager()

    @Test
    fun extractReplayGainValues_readsStandardReplayGainKeys() {
        val values = manager.extractReplayGainValues(
            mapOf(
                "REPLAYGAIN_TRACK_GAIN" to arrayOf("-13.52 dB"),
                "REPLAYGAIN_ALBUM_GAIN" to arrayOf("-14.00 dB")
            )
        )

        assertThat(values).isEqualTo(
            ReplayGainManager.ReplayGainValues(
                trackGainDb = -13.52f,
                albumGainDb = -14.00f
            )
        )
    }

    @Test
    fun extractReplayGainValues_matchesNormalizedKeyVariants() {
        val values = manager.extractReplayGainValues(
            mapOf(
                "ReplayGain Track Gain" to arrayOf("-13.52 dB"),
                "replaygain-album-gain" to arrayOf("-14.00 dB")
            )
        )

        assertThat(values).isEqualTo(
            ReplayGainManager.ReplayGainValues(
                trackGainDb = -13.52f,
                albumGainDb = -14.00f
            )
        )
    }

    @Test
    fun getVolumeMultiplier_usesAlbumGainWhenRequested() {
        val values = ReplayGainManager.ReplayGainValues(
            trackGainDb = -13.52f,
            albumGainDb = -14.00f
        )

        val trackVolume = manager.getVolumeMultiplier(values, useAlbumGain = false)
        val albumVolume = manager.getVolumeMultiplier(values, useAlbumGain = true)

        assertThat(trackVolume).isWithin(0.0001f).of(0.2108f)
        assertThat(albumVolume).isWithin(0.0001f).of(0.1995f)
    }
}
