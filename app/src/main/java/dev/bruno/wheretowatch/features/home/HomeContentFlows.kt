package dev.bruno.wheretowatch.features.home

import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

class HomeContentFlows(
    val tendingContent: Flow<HomeTrending>,
    val popularContent: Flow<ImmutableList<HomeMovieItem>>,
)
