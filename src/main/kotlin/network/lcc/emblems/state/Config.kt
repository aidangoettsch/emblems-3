package network.lcc.emblems.state

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    var powerMax: Double = 10.0,
    var powerMin: Double = -10.0,
    var powerLossOnDeath: Double = 2.0
)