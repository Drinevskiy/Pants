package com.example.pants.presentation.common

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pants.domain.model.ColorModel
import com.example.pants.domain.usecase.CheckBoardOrderUseCase
import com.example.pants.domain.usecase.GetColorBoardUseCase
import com.example.pants.presentation.utils.extension.other.hue
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SharedGameViewModel(
    private val getColorBoardUseCase: GetColorBoardUseCase,
    private val checkBoardOrderUseCase: CheckBoardOrderUseCase
) : ViewModel() {

    private val _colorBoard = MutableStateFlow(EMPTY_BOARD)
    val colorBoard: StateFlow<List<ColorModel>> = _colorBoard.asStateFlow()

    private val _currentColorName = MutableStateFlow<String?>(null)
    val currentColorName: StateFlow<String?> = _currentColorName.asStateFlow()

    private val _selectedColor = MutableStateFlow(Color.Red)
    val selectedColor: StateFlow<Color> = _selectedColor.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage.asSharedFlow()

    init {
        initColorBoard()
    }

    fun setColorModelByName(name: String) {
        _colorBoard.value.find { it.name == name }?.let { colorModel ->
            _currentColorName.value = colorModel.name
        }
    }

    fun saveColor() {
        if (_colorBoard.value.isEmpty()) return
        val updatedColors = _colorBoard.value.map {
            if (it.name == currentColorName.value) it.updateHue(selectedColor.value.hue) else it
        }
        _colorBoard.value = updatedColors
    }

    fun updateColorBoard(board: List<ColorModel>){
        _colorBoard.value = board
    }

    fun updateColorSettings(hue: Float) {
        _selectedColor.value = Color.hsv(hue, 1f, 1f)
    }

    fun checkColorOrder(): List<ColorModel>? {
        when {
            _colorBoard.value.isEmpty() -> {
                initColorBoard()
                return colorBoard.value
            }

            checkBoardOrderUseCase(_colorBoard.value) -> {
                initColorBoard()
                return null
            }

            else -> {
                return _colorBoard.value.sortedBy { it.realHue }
            }
        }
    }

    private fun initColorBoard() {
        viewModelScope.launch {
            getColorBoardUseCase(BOARD_SIZE).fold(
                onSuccess = { _colorBoard.value = it.toPersistentList() },
                onFailure = { _errorMessage.emit(it.message.orEmpty()) }
            )
        }
    }

    private companion object {
        const val BOARD_SIZE = 5
        val EMPTY_BOARD = emptyList<ColorModel>()
    }
}
