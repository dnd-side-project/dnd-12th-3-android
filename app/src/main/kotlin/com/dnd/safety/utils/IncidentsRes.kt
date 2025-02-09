package com.dnd.safety.utils

import com.dnd.safety.R
import com.dnd.safety.domain.model.IncidentCategory

val IncidentCategory.icon: Int
    get() = when (this) {
        IncidentCategory.ALL -> R.drawable.ic_location
        IncidentCategory.FIRE -> R.drawable.ic_fire
        IncidentCategory.TRAFFIC -> R.drawable.ic_traffic
        IncidentCategory.COLLAPSE -> R.drawable.ic_collapse
        IncidentCategory.EXPLOSION -> R.drawable.ic_explosion
        IncidentCategory.NATURAL -> R.drawable.ic_natural
        IncidentCategory.DUST -> R.drawable.ic_dust
        IncidentCategory.TERROR -> R.drawable.ic_terror
    }