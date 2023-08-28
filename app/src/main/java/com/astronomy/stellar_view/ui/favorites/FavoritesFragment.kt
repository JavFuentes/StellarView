package com.astronomy.stellar_view.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.astronomy.stellar_view.R
import com.astronomy.stellar_view.ui.base.BaseFragment
import com.astronomy.stellar_view.databinding.FragmentFavoritesBinding
import com.astronomy.stellar_view.domain.model.Photo
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>() {

    // Inicializa el viewModel con la delegación de viewModels, proporcionado por Hilt
    private val viewModel: FavoritesViewModel by viewModels()

    // Declaración de la variable adapter, inicialmente nula
    private var adapter: FavoritesAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuración del adaptador
        setupAdapter()

        // Observando los cambios en las fotos y eventos de la UI desde el ViewModel
        observePhotos()
        observeUiEvent()

        // Asignación del adaptador a la vista de RecyclerView
        binding.rvFavorites.adapter = adapter
    }

    // Observa los eventos de la interfaz de usuario desde el ViewModel
    private fun observeUiEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.favoritesEvent.collect { event ->
                when (event) {
                    is FavoritesViewModel.UiFavoritesEvent.ShowUndoDeletePhotoMessage -> {
                        showUndoSnackbar(event.photo)
                    }
                }
            }
        }
    }

    // Muestra una Snackbar con la opción de deshacer cuando se elimina una foto
    private fun showUndoSnackbar(photo: Photo) {
        Snackbar.make(requireView(), getString(R.string.photo_deleted_alert), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.undo_action)) {
                viewModel.onEvent(FavoritesEvent.DeletedPhotoRestored(photo))
            }
            .show()
    }

    // Observa los cambios en la lista de fotos y actualiza el adaptador
    private fun observePhotos() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.photos.collect { data ->
                adapter?.submitList(data)
            }
        }
    }

    // Configura el adaptador de la lista de favoritos
    private fun setupAdapter() {
        adapter = FavoritesAdapter(
            onDelete = { photo ->
                viewModel.onEvent(FavoritesEvent.DeleteButtonClicked(photo))
            }
        )
    }

    // Infla y establece el enlace de la vista del fragmento
    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentFavoritesBinding.inflate(inflater, container, false)
}
