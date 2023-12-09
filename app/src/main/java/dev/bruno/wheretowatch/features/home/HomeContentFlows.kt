package dev.bruno.wheretowatch.features.home

import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

class HomeContentFlows(
    val trendingContent: Flow<HomeTrending>,
    val popularContent: Flow<ImmutableList<HomeMovieItem>>,
    val upcomingContent: Flow<ImmutableList<HomeMovieItem>>,
    val topRatedContent: Flow<ImmutableList<HomeMovieItem>>,
    val actionContent: Flow<ImmutableList<HomeMovieItem>>,
    val horrorContent: Flow<ImmutableList<HomeMovieItem>>,
    val netflixContent: Flow<ImmutableList<HomeMovieItem>>,
)
