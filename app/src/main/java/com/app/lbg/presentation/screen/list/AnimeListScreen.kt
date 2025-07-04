package com.app.lbg.presentation.screen.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import androidx.navigation.NavController
import com.app.lbg.presentation.viewmodel.AnimeListViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.common.Resource
import com.app.lbg.R

@Composable
fun AnimeListScreen(
    navController: NavController,
    viewModel: AnimeListViewModel = hiltViewModel()
) {
    val animeListState = viewModel.animeList.collectAsState()
    when (val result = animeListState.value) {
        is Resource.Loading -> LoadingScreen()
        is Resource.Success -> Column {
            Text(
                text = stringResource(id = R.string.top_anime_header),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Ensure this takes remaining space
            ) {
                val animeList = result.data.orEmpty()
                items(animeList.size) { index ->
                    val item = animeList[index]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.setSelectedAnime(item)
                                navController.navigate("detail")
                            }
                            .padding(8.dp)
                    ) {
                        Image(
                            painter = rememberImagePainter(item.imageUrl),
                            contentDescription = item.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(100.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = item.title, style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }

        is Resource.Error -> ErrorScreen(result.message)
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(message: String?) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message ?: stringResource(id = R.string.something_went_wrong),
            color = Color.Red,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}