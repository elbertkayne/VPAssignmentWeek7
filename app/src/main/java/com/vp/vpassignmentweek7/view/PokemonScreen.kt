package com.vp.vpassignmentweek7.view

import coil.compose.AsyncImage
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vp.vpassignmentweek7.model.PokemonCard
import com.vp.vpassignmentweek7.viewmodel.PokemonViewModel
import com.vp.vpassignmentweek7.viewmodel.UiState

val DarkBackground = Color(0xFF121214)
val CardBackground = Color(0xFF1E1E24)
val PrimaryRed = Color(0xFFD32F2F)

@Composable
fun PokemonScreen(viewModel: PokemonViewModel) {
    val uiState = viewModel.uiState
    val selectedCard = viewModel.selectedCard

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = DarkBackground
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(PrimaryRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("P", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("PokéTracker", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text("Real-time TCG Card Valuation", color = Color.Gray, fontSize = 10.sp)
                        }
                    }
                    Badge(containerColor = Color(0xFF00C853)) {
                        Text("• LIVE API", color = Color.White, modifier = Modifier.padding(4.dp), fontSize = 10.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = viewModel.searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    placeholder = { Text("Charizard", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray) },
                    trailingIcon = {
                        IconButton(onClick = { viewModel.searchCards(viewModel.searchQuery) }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.Gray)
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { viewModel.searchCards(viewModel.searchQuery) }),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.DarkGray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                val chips = listOf("Charizard", "Pikachu", "Mewtwo", "Gengar")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(chips) { chip ->
                        FilterChip(
                            selected = viewModel.searchQuery.equals(chip, ignoreCase = true),
                            onClick = {
                                viewModel.updateSearchQuery(chip)
                                viewModel.searchCards(chip)
                            },
                            label = { Text(chip) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (uiState) {
                    is UiState.Idle -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Cari kartu Pokémon favoritmu", color = Color.Gray, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Ketik nama kartu atau pilih chip di atas", color = Color.DarkGray, fontSize = 12.sp)
                            }
                        }
                    }
                    is UiState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = PrimaryRed)
                        }
                    }
                    is UiState.Success -> {
                        Column {
                            Text("Market Results (${uiState.cards.size})", color = Color.Gray, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(8.dp))

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(uiState.cards) { card ->
                                    PokemonCardItem(card = card, onClick = { viewModel.selectCard(card) })
                                }
                            }
                        }
                    }
                    is UiState.Error -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Connection Failed", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(uiState.message, color = Color.Red, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { viewModel.searchCards(viewModel.searchQuery) },
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = "Retry")
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Try Again")
                                }
                            }
                        }
                    }
                }
            }

            if (selectedCard != null) {
                CardDetailView(card = selectedCard, onDismiss = { viewModel.selectCard(null) })
            }
        }
    }
}

@Composable
fun PokemonCardItem(card: PokemonCard, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // 🖼️ Menampilkan gambar kartu dari URL API menggunakan Coil AsyncImage
            AsyncImage(
                model = card.imageCdnUrl400,
                contentDescription = card.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = androidx.compose.ui.layout.ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(card.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1)
            Text(card.rarity ?: "Promo", color = Color.Gray, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("\$${card.prices?.market ?: "0.00"}", color = Color(0xFF00C853), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Badge { Text(card.rarity ?: "Promo") }
            }
        }
    }
}

@Composable
fun CardDetailView(card: PokemonCard, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }

                    AsyncImage(
                        model = card.imageCdnUrl400,
                        contentDescription = card.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = androidx.compose.ui.layout.ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(card.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(card.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C35))) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Market Price", color = Color.Gray, fontSize = 11.sp)
                                Text("\$${card.prices?.market ?: "21.78"}", color = Color(0xFF00C853), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                        Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C35))) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Lowest Price", color = Color.Gray, fontSize = 11.sp)
                                Text("\$${card.prices?.low ?: "2.84"}", color = Color(0xFFFFB300), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                    }
                }

                item {
                    Text("Market Activity", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Active Listings: ${card.prices?.listings ?: 109}", color = Color.Gray, fontSize = 12.sp)
                    Text("Verified Sellers: ${card.prices?.sellers ?: 88}", color = Color.Gray, fontSize = 12.sp)
                    Text("Primary Printing: ${card.prices?.primaryPrinting ?: "Holofoil"}", color = Color.Gray, fontSize = 12.sp)
                }

                item {
                    Text("Card Specifications", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Card Number: ${card.cardNumber ?: "XY29"}", color = Color.Gray, fontSize = 12.sp)
                    Text("Rarity: ${card.rarity ?: "Promo"}", color = Color.Gray, fontSize = 12.sp)
                    Text("Type: Fire", color = Color.Gray, fontSize = 12.sp)
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Done", color = Color.White)
                    }
                }
            }
        }
    }
}
