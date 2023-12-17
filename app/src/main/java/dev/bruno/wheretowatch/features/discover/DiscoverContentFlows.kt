package dev.bruno.wheretowatch.features.discover

import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

class DiscoverContentFlows(
    val trendingContent: Flow<DiscoverTrending>,
    val popularContent: Flow<ImmutableList<DiscoverMovieItem>>,
    val upcomingContent: Flow<ImmutableList<DiscoverMovieItem>>,
    val topRatedContent: Flow<ImmutableList<DiscoverMovieItem>>,
    val actionContent: Flow<ImmutableList<DiscoverMovieItem>>,
    val horrorContent: Flow<ImmutableList<DiscoverMovieItem>>,
    val netflixContent: Flow<ImmutableList<DiscoverMovieItem>>,
    val warContent: Flow<ImmutableList<DiscoverMovieItem>>,
    val harryPotterContent: Flow<ImmutableList<DiscoverMovieItem>>,
)
