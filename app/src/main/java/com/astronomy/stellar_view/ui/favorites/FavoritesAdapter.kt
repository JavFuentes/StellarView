package com.astronomy.stellar_view.ui.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.astronomy.stellar_view.databinding.AdapterFavoritesItemBinding
import com.astronomy.stellar_view.domain.model.Photo

class FavoritesAdapter(
    private val onDelete: (Photo) -> Unit
) : ListAdapter<Photo, FavoritesAdapter.FavoritesViewHolder>(FavoritesDiffUtil) {

    class FavoritesViewHolder(
        private val binding: AdapterFavoritesItemBinding,
        private val onDelete: (Photo) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        // Método para enlazar los datos del modelo de Photo con los elementos de la vista
        fun bind(photo: Photo) {
            // Cargar la imagen utilizando la biblioteca Coil
            binding.ivPhoto.load(photo.url)
            // Establecer el título
            binding.tvTitle.text = photo.title
            // Establecer la fecha
            binding.tvDate.text = photo.date
            // Establecer la explicación
            binding.tvContent.text = photo.explanation
            // Configurar el evento de eliminación al hacer clic en el botón de eliminar
            binding.btnRemoveFavorites.setOnClickListener { onDelete(photo) }
            binding.btnDetails.setOnClickListener {

                // Alternar la visibilidad del LinearLayout extendido al hacer clic en el botón de detalles
                if (binding.extendedLinearLayout.visibility == View.GONE)
                    binding.extendedLinearLayout.visibility = View.VISIBLE
                else
                    binding.extendedLinearLayout.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        // Inflar el diseño del elemento de la lista utilizando el ViewBinding
        return FavoritesViewHolder(
            AdapterFavoritesItemBinding.inflate(layoutInflater, parent, false),
            onDelete
        )
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {

        // Vincular los datos del modelo al ViewHolder
        holder.bind(getItem(position))
    }

    // Utilidad de diferencia para comparar elementos de la lista
    companion object FavoritesDiffUtil : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {

            // Comprobar si los elementos son los mismos
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {

            // Comprobar si los contenidos de los elementos son los mismos
            return oldItem.date == newItem.date
        }
    }
}
